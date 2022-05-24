package edu.nus.iss.vttpfinalapplication.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String home(Model model, HttpSession session, @RequestParam("page") Optional<Integer> page) {

        String username = (String) session.getAttribute("username");

        int currentPage = page.orElse(1);

        Page<Review> reviewPage = reviewSvc.findPaginatedByUserId(PageRequest.of(currentPage - 1, 2), username);

        model.addAttribute("reviewPage", reviewPage);

        for(Review review : reviewPage.getContent()) {
            System.out.println(review.getComment());
        }

        int totalPages = reviewPage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        List<String> mods = moduleSvc.getISSModule();

        model.addAttribute("mods", mods);
        model.addAttribute("username", username);

        return "review";
    }

    @PostMapping("/protected/add")
    public String createReview(@RequestBody MultiValueMap<String, String> payload, HttpSession session, Model model, 
        @RequestParam("page") Optional<Integer> page) {

        String username = (String) session.getAttribute("username");

        Review review = new Review();
        review.setModuleName(payload.getFirst("module"));
        review.setComment(payload.getFirst("comment"));
        review.setRating(Integer.parseInt(payload.getFirst("rating")));

        reviewSvc.addReview(review, username);

        int currentPage = page.orElse(1);

        Page<Review> reviewPage = reviewSvc.findPaginatedByUserId(PageRequest.of(currentPage - 1, 2), username);

        model.addAttribute("reviewPage", reviewPage);

        // for(Review review1 : reviewPage.getContent()) {
        //     System.out.println(review1.getComment());
        // }

        int totalPages = reviewPage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        
        List<String> mods = moduleSvc.getISSModule();

        model.addAttribute("mods", mods);
        model.addAttribute("username", session.getAttribute("username"));

        return "review";
    }

    @GetMapping("/protected/delete")
    public String deleteReview(@RequestParam("reviewId") int reviewId, HttpSession session, Model model, 
    @RequestParam("page") Optional<Integer> page) {

        String username = (String) session.getAttribute("username");

        Page<Review> reviewPage = reviewSvc.deleteReview(reviewId, username);

        model.addAttribute("reviewPage", reviewPage);

        // for(Review review1 : reviewPage.getContent()) {
        //     System.out.println(review1.getComment());
        // }

        int totalPages = reviewPage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        
        List<String> mods = moduleSvc.getISSModule();

        model.addAttribute("mods", mods);
        model.addAttribute("username", session.getAttribute("username"));

        return "review";

    }
}
