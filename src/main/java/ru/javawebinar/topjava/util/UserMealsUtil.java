package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(22, 0), 2000);
        mealsTo.forEach(System.out::println);

        List<UserMealWithExcess> mealsToOneCycle = filteredByOneCycleRecursed(meals, LocalTime.of(7, 0), LocalTime.of(22, 0), 2000);
        mealsToOneCycle.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(22, 0), 2000));

        System.out.println(filteredByOneStream(meals, LocalTime.of(7, 0), LocalTime.of(22, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> map = new HashMap<>();
        meals.forEach(meal -> map.merge(meal.getDate(), meal.getCalories(), Integer::sum));
        List<UserMealWithExcess> result = new LinkedList<>();
        meals.forEach(meal -> {
            if(TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                boolean excess = map.get(meal.getDate()) > caloriesPerDay;
                UserMealWithExcess userMealWithExcess = new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
                result.add(0, userMealWithExcess);
            }
        });
        return result;
    }

    public static List<UserMealWithExcess> filteredByOneCycleRecursed(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> map = new HashMap<>();
        List<UserMealWithExcess> result = new LinkedList<>();
        if (meals != null && !meals.isEmpty()) {
            recurse(meals, map, result, 0, startTime, endTime, caloriesPerDay);
        }
        return result;
    }

    public static void recurse(List<UserMeal> meals, Map<LocalDate, Integer> map, List<UserMealWithExcess> result, int index, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        UserMeal currentUserMeal = meals.get(index);
        map.merge(currentUserMeal.getDate(), currentUserMeal.getCalories(), Integer::sum);
        if(index < meals.size() - 1) recurse(meals, map, result, index+1, startTime, endTime, caloriesPerDay);
        if(TimeUtil.isBetweenHalfOpen(currentUserMeal.getTime(), startTime, endTime)) {
            boolean excess = map.get(currentUserMeal.getDate()) > caloriesPerDay;
            UserMealWithExcess userMealWithExcess = new UserMealWithExcess(currentUserMeal.getDateTime(), currentUserMeal.getDescription(), currentUserMeal.getCalories(), excess);
            result.add(userMealWithExcess);
        }
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> map = meals.stream().collect(Collectors.groupingBy((UserMeal::getDate), Collectors.summingInt(UserMeal::getCalories)));
        return meals.stream().filter(m -> TimeUtil.isBetweenHalfOpen(m.getTime(), startTime, endTime))
                .map(m -> {
                    boolean excess = map.get(m.getDate()) > caloriesPerDay;
                    return new UserMealWithExcess(m.getDateTime(), m.getDescription(), m.getCalories(), excess);
        }).collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByOneStream(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream().collect(new MyCollector(caloriesPerDay, startTime, endTime));
    }
}

class MyCollector implements Collector<UserMeal, ArrayList<UserMeal>, List<UserMealWithExcess>> {
    private final int maxCalories;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final Map<LocalDate, Integer> map;

    public MyCollector(int maxCalories, LocalTime startTime, LocalTime endTime) {
        this.maxCalories = maxCalories;
        this.startTime = startTime;
        this.endTime = endTime;
        map = new HashMap<>();
    }

    private boolean isExceess(LocalDate date) {
        return map.get(date) > maxCalories;
    }

    @Override
    public Supplier<ArrayList<UserMeal>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<ArrayList<UserMeal>, UserMeal> accumulator() {
        return ((userMeals, userMeal) -> {
            map.merge(userMeal.getDate(), userMeal.getCalories(), Integer::sum);
            if (TimeUtil.isBetweenHalfOpen(userMeal.getTime(), startTime, endTime)) {
                userMeals.add(userMeal);
            }
        });
    }

    @Override
    public BinaryOperator<ArrayList<UserMeal>> combiner() {
        return (g, otherG) -> {
            throw new UnsupportedOperationException("Parallel stream not supported");
        };
    }

    @Override
    public Function<ArrayList<UserMeal>, List<UserMealWithExcess>> finisher() {
        return in -> in.stream().map(e-> new UserMealWithExcess(e.getDateTime(), e.getDescription(), e.getCalories(), isExceess(e.getDate()))).collect(Collectors.toList());
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }
}
