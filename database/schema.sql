DROP SCHEMA IF EXISTS miniproject;

CREATE SCHEMA miniproject;

USE miniproject;


CREATE TABLE user(
    user_id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL,
    password VARCHAR(256) NOT NULL,

    PRIMARY KEY (user_id)
);

CREATE TABLE module_review(
    review_id INT NOT NULL AUTO_INCREMENT,
    module_name VARCHAR(256) NOT NULL,
    rating INT,
    comment VARCHAR(512),
    review_date date default (current_date),
    user_id INT NOT NULL,

    PRIMARY KEY (review_id),
    CONSTRAINT fk_user_id FOREIGN KEY(user_id) REFERENCES user(user_id)
);