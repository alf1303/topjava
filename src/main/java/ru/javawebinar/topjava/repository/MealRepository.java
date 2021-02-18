package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealRepository {
    Collection<Meal> getAll();
    void delete(int id);
    Meal save(Meal meal);
    Meal get(int id);
}
