package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.MealsService;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.InitializatorUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import static org.slf4j.LoggerFactory.getLogger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MealServlet  extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealRepository mealRepository;
    private DateTimeFormatter ddtf = DateTimeFormatter.ofPattern("mm/dd/yyyy hh:mm a");

    public MealServlet() {
        this.mealRepository = MealsService.getRepository();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int mealId = 0;
        try {
            mealId = Integer.parseInt(req.getParameter("mealId"));
        } catch (NumberFormatException e) {
        }
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        String date = req.getParameter("date");
        LocalDateTime dateTime = LocalDateTime.parse(date);

        Meal meal = new Meal(dateTime, description, calories);
        meal.setId(mealId);
        if(mealId == 0) {
            mealRepository.addMeal(meal);
        } else {
            mealRepository.updateMeal(meal);
        }
        resp.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to meals");
        String action = req.getParameter("action");
        if(action != null) {
            if(action.equals("delete")) {
                deleteMeal(req, resp);
            }
            if(action.equals("edit")) {
                editMeal(req, resp);
            }
            if(action.equals("addMeal")) {
                addMeal(req, resp);
            }
        } else {
            viewList(req, resp);
        }
    }

    private void addMeal(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Meal meal = new Meal(LocalDateTime.now(), "", 0);
        req.setAttribute("meal", meal);
        req.getRequestDispatcher("edit.jsp").forward(req, resp);
    }

    private void editMeal(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int mealId = Integer.parseInt(req.getParameter("id"));
        Meal meal = mealRepository.getById(mealId);
        req.setAttribute("meal", meal);
        req.getRequestDispatcher("edit.jsp").forward(req, resp);
    }


    private void deleteMeal(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String mealId = req.getParameter("id");
        int id = Integer.parseInt(mealId);
        mealRepository.deleteMeal(id);
        resp.sendRedirect("meals");
    }

    private void viewList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<MealTo> mealsToList = MealsUtil.filteredByStreams(mealRepository.getAllMeals(), LocalTime.MIN, LocalTime.MAX, InitializatorUtil.maxCalories);
        req.setAttribute("mealsToList", mealsToList);
        req.getRequestDispatcher("meals.jsp").forward(req, resp);
    }

}
