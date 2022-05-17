package edu.nus.iss.vttpfinalapplication.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import edu.nus.iss.vttpfinalapplication.models.Review;
import edu.nus.iss.vttpfinalapplication.services.ModuleService;
import edu.nus.iss.vttpfinalapplication.services.ReviewService;

@Controller
public class ProtectedController {
    
    @Autowired
    ModuleService moduleSvc;

    @Autowired
    ReviewService reviewSvc;
    
    @GetMapping("/protected/review")
    public String home(Model model, HttpSession session) {

        List<String> mods = moduleSvc.getISSModule();

        model.addAttribute("mods", mods);
        model.addAttribute("username", session.getAttribute("username"));

        return "review";
    }

    @PostMapping("/protected/add")
    public String createReview(@RequestBody MultiValueMap<String, String> payload, HttpSession session, Model model) {

        String username = (String) session.getAttribute("username");

        Review review = new Review();
        review.setModuleName(payload.getFirst("module"));
        review.setComment(payload.getFirst("comment"));
        review.setRating(Integer.parseInt(payload.getFirst("rating")));

        reviewSvc.addReview(review, username);
        
        List<String> mods = moduleSvc.getISSModule();

        model.addAttribute("mods", mods);
        model.addAttribute("username", session.getAttribute("username"));


        return "review";
    }
}
