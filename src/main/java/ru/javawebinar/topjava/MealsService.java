package ru.javawebinar.topjava;

import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.MealRepositoryMemoryImpl;

public class MealsService {
    public static MealRepository getRepository() {
        return MealRepositoryMemoryImpl.getInstance();
    }
}