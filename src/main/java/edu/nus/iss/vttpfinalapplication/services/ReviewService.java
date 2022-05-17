package edu.nus.iss.vttpfinalapplication.services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import edu.nus.iss.vttpfinalapplication.models.Review;
import edu.nus.iss.vttpfinalapplication.repositories.ReviewRepository;
import edu.nus.iss.vttpfinalapplication.repositories.UserRepository;

@Service
public class ReviewService {

    @Autowired
    ReviewRepository reviewRepo;

    @Autowired
    UserRepository userRepo;

    public void addReview(Review review, String username) {

        // System.out.println("username: " + username);
        review.setUserId(userRepo.selectUserIdByUsername(username));

        reviewRepo.addReview(review);

    }

    public Page<Review> findPaginated(PageRequest pageRequest, String moduleName) {
        
        List<Review> reviews = reviewRepo.getReviewByModuleName(moduleName);

        int currentPage = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();

        int startItem = currentPage * pageSize;

        List<Review> list;

        if(reviews.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, reviews.size());
            list = reviews.subList(startItem, toIndex);
        }

        Page<Review> reviewPage = new PageImpl<Review>(list, PageRequest.of(currentPage, pageSize), reviews.size());

        return reviewPage;
    }
    
}
