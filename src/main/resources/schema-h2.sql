DROP TABLE IF EXISTS USER;
CREATE TABLE USER(
  user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_name VARCHAR(255),
  role VARCHAR(255)
);