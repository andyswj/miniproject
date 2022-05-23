package edu.nus.iss.vttpfinalapplication.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.nus.iss.vttpfinalapplication.models.Review;
import edu.nus.iss.vttpfinalapplication.services.LoginService;
import edu.nus.iss.vttpfinalapplication.services.ModuleService;
import edu.nus.iss.vttpfinalapplication.services.ReviewService;

@Controller
public class LoginController {

    @Autowired
    private ModuleService moduleSvc;

    @Autowired
    LoginService loginSvc;

    @Autowired
    ReviewService reviewSvc;

    @RequestMapping("/")
    public String accessToModule(Model model) {

        List<String> mods = moduleSvc.getISSModule();

        model.addAttribute("mods", mods);
        model.addAttribute("status0", 0);
        model.addAttribute("status", 0);
        model.addAttribute("status1", 0);
        return "index";
    }

    @GetMapping(path="/create")
    public String create() {
        return "index";
    }

    @GetMapping(path="/login")
    public String login(Model model) {
        model.addAttribute("status0", 1);
        model.addAttribute("status", 0);
        model.addAttribute("status1", 0);
        return "index";
    }

    @GetMapping(path="/view")
    public String view(Model model) {
        List<String> mods = moduleSvc.getISSModule();

        model.addAttribute("mods", mods);
        model.addAttribute("status", 1);
        model.addAttribute("status0", 0);
        model.addAttribute("status1", 0);
        return "index";
    }

    @PostMapping(path="/createaccount")
    public ModelAndView createAccount(@RequestBody MultiValueMap<String, String> payload, HttpSession session) {

        String username = payload.getFirst("username").trim().toLowerCase();
        String password = payload.getFirst("password");

        String hashPassword = loginSvc.hashingPassword(password);

        int result = loginSvc.createUserWithDB(username, hashPassword);

        if (result == 1) {
            session.setAttribute("username", username); //from controller to controller to pass data
            final ModelAndView mav = new ModelAndView("redirect:/protected/review");       
            // mav.addObject("username", username);
            // mav.setStatus(HttpStatus.OK);
            return mav;
        } else{
            final ModelAndView mav1 = new ModelAndView("error");
            mav1.addObject("errorMsg", "Username is not available, please try with other username. Thank you");
            mav1.setStatus(HttpStatus.BAD_REQUEST);
            return mav1;
        }

    }

    @PostMapping(path="/autheticate")
    public ModelAndView loginAuthetication(@RequestBody MultiValueMap<String, String> payload, HttpSession session) {
        
        String username = payload.getFirst("username").trim().toLowerCase();
        String password = payload.getFirst("password");

        String hashPassword = loginSvc.hashingPassword(password);

        Boolean result = loginSvc.autheticationWithDB(username, hashPassword);

        if (result) {
            session.setAttribute("username", username); //from controller to controller to pass data
            final ModelAndView mav = new ModelAndView("redirect:/protected/review");
            // mav.addObject("username", username);
            // mav.setStatus(HttpStatus.OK);
            return mav;
        } else{
            final ModelAndView mav1 = new ModelAndView("error");
            mav1.addObject("errorMsg", "Wrong username or password. Please try again. Thank you.");
            mav1.setStatus(HttpStatus.BAD_REQUEST);
            return mav1;
        }
    }

    @GetMapping("/list")
    public String listAllReview(@RequestParam("module") String moduleName, @RequestParam("page") Optional<Integer> page, Model model) {

        String search = moduleName.replace("+", " ");
        System.out.println("Module name: " + search);

        int currentPage = page.orElse(1);

        Page<Review> reviewPage = reviewSvc.findPaginated(PageRequest.of(currentPage - 1, 1), moduleName);

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
        model.addAttribute("moduleName", moduleName);
        model.addAttribute("status1", 1);
        model.addAttribute("status", 1);

        return "index";
    }

    @GetMapping("/logout")
    public String getLogout(HttpSession session, HttpServletRequest req, HttpServletResponse resp) {
        session.invalidate();

        Cookie[] cookies = req.getCookies();

        if (cookies != null)
        for (Cookie cookie : cookies) {
            // System.out.println("cookie value: " + cookie.getValue());
            cookie.setValue("");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            resp.addCookie(cookie);
        }

        return "index";
    }
    
}
