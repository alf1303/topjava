package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealRepository {
    List<Meal> getAllMeals();
    Meal deleteMeal(int id);
    void updateMeal(Meal meal);
    void addMeal(Meal meal);
    Meal getById(int id);
}
