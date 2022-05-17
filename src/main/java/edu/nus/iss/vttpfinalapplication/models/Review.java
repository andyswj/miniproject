package edu.nus.iss.vttpfinalapplication.models;

import java.util.Date;

public class Review {
    private String moduleName;
    private int rating;
    private String comment;
    private int userId;
    private Date reviewDate;

    public Review() {
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    @Override
    public String toString() {
        return "Review [comment=" + comment + ", moduleName=" + moduleName + ", rating=" + rating + ", userId=" + userId
                + "]";
    }
    
}
