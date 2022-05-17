package edu.nus.iss.vttpfinalapplication.repositories;

public interface Queries {
    
    public static final String SQL_CREATE_USER = "insert into user (username, password) values (?, ?)";
    public static final String SQL_GET_USER = "select * from user where username = ? and password = ?";
    public static final String SQL_GET_USER_BY_USERNAME = "select * from user where username = ?";
    public static final String SQL_GET_USERID_BY_USERNAME = "select user_id from user where username = ?";
    public static final String SQL_CREATE_REVIEW = "insert into module_review (module_name, comment, rating, user_id) values (?, ?, ?, ?)";
    public static final String SQL_GET_REVIEW_BY_MODNAME = "select * from module_review where module_name = ?";
    public static final String SQL_GET_REVIEW_BY_USERID = "select * from module_review where user_id = ?";
}
