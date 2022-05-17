package edu.nus.iss.vttpfinalapplication.repositories;

import static edu.nus.iss.vttpfinalapplication.repositories.Queries.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import edu.nus.iss.vttpfinalapplication.models.Review;

@Repository
public class ReviewRepository {

    @Autowired
    JdbcTemplate template;

    public int addReview(Review review) {
        int count = template.update(
            SQL_CREATE_REVIEW,
            review.getModuleName(),
            review.getComment(),
            review.getRating(),
            review.getUserId()
        );

        return count;
    }

    public List<Review> getReviewByModuleName(String moduleName) {
        
        List<Review> reviews = new LinkedList<>();
        
        final SqlRowSet rs = template.queryForRowSet(
            SQL_GET_REVIEW_BY_MODNAME,
            moduleName
        );

        while(rs.next()) {
            Review review = new Review();
            review.setModuleName(moduleName);
            review.setComment(rs.getString("comment"));
            review.setRating(rs.getInt("rating"));
            review.setReviewDate(rs.getDate("review_date"));

            reviews.add(review);
        }

        return reviews;
    }
}

