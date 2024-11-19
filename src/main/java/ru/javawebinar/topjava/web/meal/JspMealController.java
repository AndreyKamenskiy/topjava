package ru.javawebinar.topjava.web.meal;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController extends AbstractMealController {

    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping()
    public String meals(Model model, HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        if (startDate != null || endDate != null || startTime != null || endTime != null) {
            model.addAttribute("meals", getBetween(startDate, startTime, endDate, endTime));
        } else {
            model.addAttribute("meals", getAll());
        }
        return "meals";
    }

    @GetMapping(value = "/delete/{id}")
    public String doDelete(@PathVariable int id) {
        delete(id);
        return "redirect:/meals";
    }

    @GetMapping(value = "/update/{id}")
    public String getUpdate(@PathVariable int id, Model model) {
        model.addAttribute("meal", get(id));
        return "mealForm";
    }

    @PostMapping(value = "/update/{id}")
    public String postUpdate(HttpServletRequest request, @PathVariable String id) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        update(meal, Integer.parseInt(id));
        return "redirect:/meals";
    }

    @GetMapping(value = "/create")
    public String getCreate(Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "mealForm";
    }

    @PostMapping(value = "/create")
    public String postCreate(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        create(meal);
        return "redirect:/meals";
    }

}
