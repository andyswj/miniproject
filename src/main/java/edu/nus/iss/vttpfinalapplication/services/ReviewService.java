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

    public int addReview(Review review, String username) {

        // System.out.println("username: " + username);
        review.setUserId(userRepo.selectUserIdByUsername(username));

        reviewRepo.addReview(review);

        return 1;

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

    public Page<Review> findPaginatedByUserId(PageRequest pageRequest, String username) {

        int userId = (Integer) userRepo.selectUserIdByUsername(username);
        
        List<Review> reviews = reviewRepo.getReviewByUserId(userId);

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

    public Page<Review> deleteReview(int reviewId, String username) {
        int userId = (Integer) userRepo.selectUserIdByUsername(username);

        reviewRepo.deleteReviewByUserIdAndReviewId(userId, reviewId);

        Page<Review> reviewPage = findPaginatedByUserId(PageRequest.of(1 - 1, 2), username);

        return reviewPage;
    }
    
}
