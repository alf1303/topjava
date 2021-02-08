package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealRepositoryMemoryImpl implements MealRepository{
    private static Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private static AtomicInteger id = new AtomicInteger(0);
    @Override
    public List<Meal> getAllMeals() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal deleteMeal(int id) {
        return meals.remove(id);
    }

    @Override
    public void updateMeal(Meal meal) {
        meals.put(meal.getId(), meal);
    }

    @Override
    public void addMeal(Meal meal) {
        int mealId = getNextId();
        meal.setId(mealId);
        meals.put(mealId, meal);
    }

    @Override
    public Meal getById(int id) {
        return meals.get(id);
    }

    private int getNextId() {
        return id.incrementAndGet();
    }
}
