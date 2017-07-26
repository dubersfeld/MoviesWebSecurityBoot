DROP DATABASE IF EXISTS moviesBootDB;

CREATE DATABASE moviesBootDB DEFAULT CHARACTER SET 'utf8'
  DEFAULT COLLATE 'utf8_unicode_ci';

USE moviesBootDB;

CREATE TABLE actor(
    actorId   BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    firstName VARCHAR(20) NOT NULL,
    lastName  VARCHAR(20) NOT NULL,
    birthDate DATE NOT NULL, 	
    CONSTRAINT actor_unique UNIQUE (firstName, lastName),
    INDEX actor_name  (firstName, lastName)
) ENGINE = InnoDB;

CREATE TABLE director(
    directorId BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    firstName  VARCHAR(20) NOT NULL,
    lastName   VARCHAR(20) NOT NULL,
    birthDate  DATE NOT NULL,
    CONSTRAINT director_unique UNIQUE (firstName, lastName),
    INDEX director_name  (firstName, lastName)	
) ENGINE = InnoDB;

CREATE TABLE movie(    
    movieId     BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    title       VARCHAR(50) NOT NULL,
    releaseDate DATE NOT NULL,
    directorId  BIGINT UNSIGNED NOT NULL, 
    runningTime INT,
    CONSTRAINT  movie_unique UNIQUE (title, releaseDate),
    FOREIGN KEY (directorId) REFERENCES director (directorId) ON DELETE CASCADE,
    INDEX movie_index (title, releaseDate) 
) ENGINE = InnoDB;

CREATE TABLE actorFilm(
    actorId BIGINT UNSIGNED NOT NULL,
    filmId  BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (filmId) REFERENCES movie (movieId) ON DELETE CASCADE,
    FOREIGN KEY (actorId) REFERENCES actor (actorId) ON DELETE CASCADE,
    CONSTRAINT actorFilm_unique UNIQUE (actorId, filmId) 
) ENGINE = InnoDB; 

CREATE TABLE actorImages(
    imageId BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    data blob NOT NULL,
    actorId BIGINT UNSIGNED NOT NULL, 
    FOREIGN KEY (actorId) REFERENCES actor (actorId) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE user (
  userId BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(30) NOT NULL,
  hashedPassword BINARY(60) NOT NULL,
  accountNonExpired BOOLEAN NOT NULL,
  accountNonLocked BOOLEAN NOT NULL,
  credentialsNonExpired BOOLEAN NOT NULL,
  enabled BOOLEAN NOT NULL,
  CONSTRAINT user_unique UNIQUE (username)
) ENGINE = InnoDB;

CREATE TABLE user_Authority (
  userId BIGINT UNSIGNED NOT NULL,
  authority VARCHAR(100) NOT NULL,
  UNIQUE KEY user_Authority_User_Authority (userId, authority),
  CONSTRAINT user_Authority_UserId FOREIGN KEY (userId)
    REFERENCES user (userId) ON DELETE CASCADE
) ENGINE = InnoDB;


INSERT INTO actor VALUES (
  '100', 'George', 'Clooney', '1961-05-06'
);

INSERT INTO actor VALUES (
  '101', 'Brad', 'Pitt', '1963-12-18'
);

INSERT INTO actor VALUES (
  '102', 'Tom', 'Cruise', '1962-07-03'
);

INSERT INTO actor VALUES (
  '103', 'Matt', 'Damon', '1970-10-08'
);

INSERT INTO actor VALUES (
  '104', 'James', 'Dean', '1931-02-08'
);

INSERT INTO actor VALUES (
  '105', 'Sean', 'Penn', '1960-08-17'
);

INSERT INTO actor VALUES (
  '106', 'Sean', 'Connery', '1930-08-25'
);

INSERT INTO actor VALUES (
  '107', 'Ben', 'Affleck', '1972-08-15'
);

INSERT INTO actor VALUES (
  '108', 'Ben', 'Kingsley', '1943-12-31'
);

INSERT INTO actor VALUES (
  '109', 'Orlando', 'Bloom', '1977-01-13'
);

INSERT INTO actor VALUES (
  '110', 'Angelina', 'Jolie', '1975-06-04'
);

INSERT INTO actor VALUES (
  '111', 'Will', 'Smith', '1968-09-25'
);

INSERT INTO actor VALUES (
  '112', 'Bill', 'Pullmann', '1953-12-16'
);

INSERT INTO actor VALUES (
  '113', 'Channing', 'Tatum', '1980-04-26'
);

INSERT INTO actor VALUES (
  '114', 'Jamie', 'Foxx', '1967-12-13'
);

INSERT INTO actor VALUES (
  '115', 'Halle', 'Berry', '1966-08-14'
);

INSERT INTO actor VALUES (
  '116', 'Daniel', 'Craig', '1968-03-02'
);


INSERT INTO director VALUES (
  '1', 'Brian', 'De Palma', '1940-09-11'
);

INSERT INTO director VALUES (
  '2', 'Marc', 'Forster', '1969-11-30'
);

INSERT INTO director VALUES (
  '3', 'Wolfgang', 'Petersen', '1941-03-14'
);

INSERT INTO director VALUES (
  '4', 'Roland', 'Emmerich', '1955-11-10'
);

INSERT INTO director VALUES (
  '5', 'Josh', 'Trank', '1984-02-19'
);

INSERT INTO director VALUES (
  '6', 'Tim', 'Story', '1970-03-13'
);

INSERT INTO director VALUES (
  '7', 'David', 'Fincher', '1962-08-18'
);

INSERT INTO director VALUES (
  '8', 'Doug', 'Liman', '1965-07-24'
);




INSERT INTO movie VALUES (
  '1', 'Mission: impossible', '1996-05-22', '1', '110' 
);

INSERT INTO movie VALUES (
  '2', 'Quantum of Solace', '2008-10-29', '2', '106' 
);

INSERT INTO movie VALUES (
  '3', 'Troy', '2004-05-09', '3', '196' 
);

INSERT INTO movie VALUES (
  '4', 'Fantastic Four', '2005-07-08', '6', '106' 
);

INSERT INTO movie VALUES (
  '5', 'Fantastic Four', '2015-08-04', '5', '100' 
);

INSERT INTO movie VALUES (
  '6', 'Fight Club', '1999-11-10', '7', '151' 
);

INSERT INTO movie VALUES (
  '7', 'Mr & Mrs Smith', '2005-06-10', '8', '122' 
);

INSERT INTO movie VALUES (
  '8', 'Independence Day', '1996-09-27', '4', '153' 
);

INSERT INTO movie VALUES (
  '9', 'White House Down', '2013-06-28', '4', '131' 
);

INSERT INTO movie VALUES (
  '10', 'Monster Ball', '2002-03-07', '2', '111' 
);



INSERT INTO actorFilm VALUES (
  '101', '3' 
);

INSERT INTO actorFilm VALUES (
  '102', '6'
);

INSERT INTO actorFilm VALUES (
  '101', '7'
);

INSERT INTO actorFilm VALUES (
  '110', '7'
);

INSERT INTO actorFilm VALUES (
  '111', '8' 
);

INSERT INTO actorFilm VALUES (
  '112', '8' 
);

INSERT INTO actorFilm VALUES (
  '113', '9'
);

INSERT INTO actorFilm VALUES (
  '114', '9' 
);

INSERT INTO actorFilm VALUES (
  '115', '10' 
);


INSERT INTO user (username, hashedPassword, accountNonExpired,
                           accountNonLocked, credentialsNonExpired, enabled)
VALUES ( -- s1a2t3o4r
  'Carol', '$2a$10$.6kYphQ8VqJ9NPKtMje.JeWt1aoX4/ZRzVFGiO7Cen.rk88laGTCi',
  TRUE, TRUE, TRUE, TRUE
);

INSERT INTO user (username, hashedPassword, accountNonExpired,
                           accountNonLocked, credentialsNonExpired, enabled)
VALUES ( -- a5r6e7p8o
  'Albert', '$2a$10$CXNlyhzVkeHbqNpIJNBTl.WXP9WVouQwqh7M7IkI/WTXywakU5kha',
  TRUE, TRUE, TRUE, TRUE
);

INSERT INTO user (username, hashedPassword, accountNonExpired,
                           accountNonLocked, credentialsNonExpired, enabled)
VALUES ( -- t4e3n2e1t
  'Werner', '$2a$10$BKSkkYAh5COX/vvQrwPwuucL77Ydf61EEd97kwaBndKtxHJktQX/S',
  TRUE, TRUE, TRUE, TRUE
);

INSERT INTO user (username, hashedPassword, accountNonExpired,
                           accountNonLocked, credentialsNonExpired, enabled)
VALUES ( -- o8p7e6r5a
  'Alice', '$2a$10$fDcpl4fYkqyaKwVfBOFwTu5igi7yXzCw2AWp0oSkZ0iwMXzZsZ2t.',
  TRUE, TRUE, TRUE, TRUE
);

INSERT INTO user (username, hashedPassword, accountNonExpired,
                           accountNonLocked, credentialsNonExpired, enabled)
VALUES ( -- r1o2t3a4s
  'Richard', '$2a$10$ej/Cmw3p0b8UbWOQAhiEW.2LW03nspV2yLFaoli0CdxK8./miktoW',
  TRUE, TRUE, TRUE, TRUE
);



INSERT INTO user_Authority (UserId, Authority)
  VALUES (1, 'VIEW');

INSERT INTO user_Authority (UserId, Authority)
  VALUES (2, 'VIEW');

INSERT INTO user_Authority (UserId, Authority)
  VALUES (3, 'VIEW');

INSERT INTO user_Authority (UserId, Authority)
  VALUES (4, 'VIEW'), (4, 'CREATE'), (4, 'UPDATE');

INSERT INTO user_Authority (UserId, Authority)
  VALUES (5, 'VIEW'), (5, 'CREATE'), (5, 'UPDATE'), (5, 'DELETE');

   
