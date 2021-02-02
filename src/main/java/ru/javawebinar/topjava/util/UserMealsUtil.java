package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

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

        System.out.println();

        List<UserMealWithExcess> mealsToOneCycle = filteredByOneCycle(meals, LocalTime.of(7, 0), LocalTime.of(22, 0), 2000);
        mealsToOneCycle.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> result = new LinkedList<>();
        Map<LocalDate, Integer> map = new HashMap<>();
        meals.forEach(meal -> map.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum));
        meals.forEach(meal -> {
            if(TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                boolean excess = map.get(meal.getDateTime().toLocalDate()) > caloriesPerDay;
                UserMealWithExcess userMealWithExcess = new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
                result.add(0, userMealWithExcess);
            }
        });
        return result;
    }

    public static List<UserMealWithExcess> filteredByOneCycle(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> result = new LinkedList<>();
        Map<LocalDate, Integer> map = new HashMap<>();
        if (meals != null && !meals.isEmpty()) {
            recursOneCycle(meals, map, result, 0, startTime, endTime, caloriesPerDay);
        }
        return result;
    }

    public static void recursOneCycle(List<UserMeal> meals, Map<LocalDate, Integer> map, List<UserMealWithExcess> result, int index, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        UserMeal currentUserMeal = meals.get(index);
        map.merge(currentUserMeal.getDateTime().toLocalDate(), currentUserMeal.getCalories(), Integer::sum);
        if(index < meals.size() - 1) recursOneCycle(meals, map, result, index+1, startTime, endTime, caloriesPerDay);
        if(TimeUtil.isBetweenHalfOpen(currentUserMeal.getDateTime().toLocalTime(), startTime, endTime)) {
            boolean excess = map.get(currentUserMeal.getDateTime().toLocalDate()) > caloriesPerDay;
            UserMealWithExcess userMealWithExcess = new UserMealWithExcess(currentUserMeal.getDateTime(), currentUserMeal.getDescription(), currentUserMeal.getCalories(), excess);
            result.add(userMealWithExcess);
        }
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        return null;
    }
}
