package com.crowd.mapper.controller;

import com.crowd.mapper.model.Problem;
import com.crowd.mapper.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private LocaleResolver localeResolver;

    private void addLocaleToModel(Model model, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);
        String lang = locale.getLanguage();
        if (locale.toString().equals("hi_IN") || lang.equals("hi")) {
            model.addAttribute("currentLang", "hi");
        } else if (locale.toString().equals("mr_IN") || lang.equals("mr")) {
            model.addAttribute("currentLang", "mr");
        } else {
            model.addAttribute("currentLang", "en");
        }
        // Pass logged-in user info to ALL templates
        var principal = request.getUserPrincipal();
        if (principal != null) {
            model.addAttribute("loggedInUser", principal.getName());
            model.addAttribute("isAdmin", request.isUserInRole("ADMIN"));
            model.addAttribute("isStaff", request.isUserInRole("STAFF"));
        }
        model.addAttribute("isLoggedIn", principal != null);
    }

    // 1. Home — PUBLIC
    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) {
        addLocaleToModel(model, request);
        model.addAttribute("problems", problemService.getAllProblemsSorted());
        model.addAttribute("totalProblems", problemService.getTotalProblems());
        model.addAttribute("highPriorityCount", problemService.getCountBySeverity("High"));
        model.addAttribute("totalVerifications", problemService.getTotalVerifications());
        model.addAttribute("activeCities", problemService.getTop5Problems().size());
        return "index";
    }

    // 2. Report Problem — requires login
    @GetMapping("/report")
    public String reportPage(Model model, HttpServletRequest request) {
        addLocaleToModel(model, request);
        model.addAttribute("problem", new Problem());
        return "report";
    }

    @PostMapping("/report")
    public String submitProblem(@ModelAttribute Problem problem) {
        problemService.saveProblem(problem);
        return "redirect:/";
    }

    // 3. Verify
    @GetMapping("/verify")
    public String verifyPage(Model model, HttpServletRequest request) {
        addLocaleToModel(model, request);
        model.addAttribute("problems", problemService.getAllProblemsSorted());
        return "verify";
    }

    @PostMapping("/verify/{id}")
    public String verifyProblem(@PathVariable Long id, @RequestParam String action) {
        boolean isConfirm = "confirm".equals(action);
        problemService.voteProblem(id, isConfirm);
        return "redirect:/verify";
    }

    // 4. Dashboard — requires login
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpServletRequest request) {
        addLocaleToModel(model, request);
        model.addAttribute("totalProblems", problemService.getTotalProblems());
        model.addAttribute("topProblems", problemService.getTop5Problems());
        model.addAttribute("highPriorityCount", problemService.getCountBySeverity("High"));
        model.addAttribute("mediumPriorityCount", problemService.getCountBySeverity("Medium"));
        model.addAttribute("lowPriorityCount", problemService.getCountBySeverity("Low"));
        model.addAttribute("resolvedCount", problemService.getCountByStatus("Resolved"));
        model.addAttribute("totalVerifications", problemService.getTotalVerifications());
        return "dashboard";
    }

    // 5. Admin — requires ADMIN role
    @GetMapping("/admin")
    public String adminPage(Model model, HttpServletRequest request) {
        addLocaleToModel(model, request);
        model.addAttribute("problems", problemService.getAllProblemsSorted());
        model.addAttribute("totalProblems", problemService.getTotalProblems());
        model.addAttribute("resolvedCount", problemService.getCountByStatus("Resolved"));
        model.addAttribute("highPriorityCount", problemService.getCountBySeverity("High"));
        return "admin";
    }

    // 6. Login — PUBLIC
    @GetMapping("/login")
    public String loginPage(Model model, HttpServletRequest request) {
        addLocaleToModel(model, request);
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        if ("admin".equals(username) && "admin123".equals(password)) {
            return "redirect:/admin";
        }
        return "redirect:/login?error";
    }
    
    @GetMapping("/register")
    public String registerPage(Model model, HttpServletRequest request) {
        addLocaleToModel(model, request);
        return "register";
    }

    @PostMapping("/admin/updateStatus/{id}")
    public String updateStatus(@PathVariable Long id, @RequestParam String status) {
        problemService.updateStatus(id, status);
        return "redirect:/admin";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteProblem(@PathVariable Long id) {
        problemService.deleteProblem(id);
        return "redirect:/admin";
    }

    // 6. About Page
    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    // 7. Contact Page
    @GetMapping("/contact")
    public String contactPage() {
        return "contact";
    }

    // 8. Notifications Page
    @GetMapping("/notifications")
    public String notificationsPage(Model model, HttpServletRequest request) {
        addLocaleToModel(model, request);
        return "notifications";
    }

    // 9. My Complaints Page
    @GetMapping("/my-complaints")
    public String myComplaintsPage(Model model, HttpServletRequest request) {
        addLocaleToModel(model, request);
        model.addAttribute("problems", problemService.getAllProblemsSorted());
        return "my-complaints";
    }

    // 10. Profile Page
    @GetMapping("/profile")
    public String profilePage(Model model, HttpServletRequest request) {
        addLocaleToModel(model, request);
        return "profile";
    }
}
