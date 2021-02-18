package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.InitializatorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealRepositoryMemoryImpl implements MealRepository{
    private static MealRepositoryMemoryImpl instance;
    private static final AtomicInteger idGenerator = new AtomicInteger(0);
    private static final Map<Integer, Meal> meals = new ConcurrentHashMap<>();;

    private MealRepositoryMemoryImpl() {
        InitializatorUtil.meals.forEach(m -> {
            m.setId(getNextId());
            meals.put(m.getId(), m);
        });
    }

    public static synchronized MealRepositoryMemoryImpl getInstance() {
        if(instance == null) {
            instance = new MealRepositoryMemoryImpl();
        }
        return instance;
    }

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

    private static int getNextId() {
        return idGenerator.incrementAndGet();
    }
}
