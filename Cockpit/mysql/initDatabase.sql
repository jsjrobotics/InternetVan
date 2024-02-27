SET GLOBAL time_zone = '+4:00';
DROP USER IF EXISTS board_database;
CREATE USER IF NOT EXISTS 'board_database'@'localhost' IDENTIFIED BY 'board_database';
GRANT CREATE, ALTER, DROP, INSERT, UPDATE, EXECUTE, INDEX, DELETE, SELECT, REFERENCES, RELOAD on *.* TO 'board_database'@'localhost';
FLUSH PRIVILEGES;

DROP DATABASE IF EXISTS board;
CREATE DATABASE board;


USE board;	
CREATE TABLE IF NOT EXISTS `players` (
	`user_id` INT AUTO_INCREMENT,
	`username` VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL,
	`email` VARCHAR(255) CHARACTER SET utf8mb4 NULL,
	`password` VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL,
	`create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`user_id`),
	UNIQUE (`username`)
);

CREATE TABLE IF NOT EXISTS `participants` (
  `participants_id` INT NOT NULL AUTO_INCREMENT,
  `list_user_ids` VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL,
  PRIMARY KEY (`participants_id`)
);

CREATE TABLE IF NOT EXISTS `game_rooms` (
	`game_room_id` INT AUTO_INCREMENT,
	`host_user_id` INT,
	`participants` INT,
	`rules` VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL,
	FOREIGN key (`host_user_id`) REFERENCES `players` (`user_id`),
	FOREIGN key (`participants`) REFERENCES `participants` (`participants_id`),
	PRIMARY KEY (`game_room_id`)
);

	
CREATE TABLE IF NOT EXISTS `game_state` (
  `game_state_id` INT NOT NULL AUTO_INCREMENT,
  `game_room_id` INT,
  `state` VARCHAR(255) CHARACTER SET utf8mb4 NULL,
  FOREIGN key (`game_room_id`) REFERENCES `game_rooms` (`game_room_id`),
  PRIMARY KEY (`game_state_id`)
);

CREATE TABLE IF NOT EXISTS `apks` (
  `apk_id` INT NOT NULL AUTO_INCREMENT,
  `apk_name` VARCHAR(255)  CHARACTER SET utf8mb4,
  `filepath` VARCHAR(255) CHARACTER SET utf8mb4 NULL,
  PRIMARY KEY (`apk_id`),
  UNIQUE(`apk_name`)
);

DELIMITER //
CREATE PROCEDURE createPlayer (IN create_username CHAR(255), IN create_email CHAR(255), IN create_password CHAR(255), OUT created_id INT) READS SQL DATA
BEGIN
	INSERT INTO players (username, email, password) VALUES (create_username, create_email, create_password); 
	SELECT user_id FROM players WHERE username=create_username INTO created_id;
END//

CREATE PROCEDURE createParticipants (IN user_id INT, OUT created_participants_id INT) READS SQL DATA
BEGIN
	INSERT INTO participants (list_user_ids) VALUES (user_id); 
	SELECT participants_id FROM participants WHERE list_user_ids=user_id INTO created_participants_id;
END//

CREATE PROCEDURE createGameRoom (IN user_id INT, IN participants_id INT, IN create_rules CHAR(255), OUT created_room_id INT) READS SQL DATA
BEGIN
	INSERT INTO game_rooms (host_user_id, participants, rules) VALUES (user_id, participants_id, create_rules);
	SELECT game_room_id FROM game_rooms WHERE host_user_id=user_id INTO created_room_id;
END//

CREATE PROCEDURE createGameRoomState (IN room_id INT, IN game_state CHAR(255), OUT created_state_id INT) READS SQL DATA
BEGIN
	INSERT INTO game_state (game_room_id, state) VALUES (room_id, game_state); 
	SELECT game_state_id FROM game_state WHERE game_room_id=room_id INTO created_state_id;
END//

CREATE PROCEDURE getUserIdFromUserName (IN client_username CHAR(255), OUT created_id INT) READS SQL DATA
BEGIN
	SELECT user_id FROM players WHERE username=client_username INTO created_id;
END//

CREATE PROCEDURE getParticipantIdFromHostId (IN host_id INT, OUT created_participants_id INT) READS SQL DATA
BEGIN
	SELECT participants_id FROM participants WHERE list_user_ids=host_id INTO created_participants_id;
END//

CREATE PROCEDURE getGameRoomIdFromHostId (IN host_id INT, OUT created_room_id INT) READS SQL DATA
BEGIN
	SELECT game_room_id FROM game_rooms WHERE host_user_id=host_id INTO created_room_id;
END//

CREATE PROCEDURE deleteGameRoom (IN room_id INT, OUT rows_affected INT) READS SQL DATA
BEGIN
	DELETE FROM game_state WHERE game_room_id=room_id;
	DELETE FROM game_rooms WHERE game_room_id=room_id;
	SELECT ROW_COUNT() INTO rows_affected;
END//

CREATE PROCEDURE deleteParticipants (IN created_participants_id INT, OUT rows_affected INT) READS SQL DATA
BEGIN
	DELETE FROM participants WHERE participants_id=created_participants_id;
	SELECT ROW_COUNT() INTO rows_affected;
END//

CREATE PROCEDURE deletePlayer (IN user_id INT, OUT rows_affected INT) READS SQL DATA
BEGIN
	DELETE FROM players WHERE user_id=user_id;
	SELECT ROW_COUNT() INTO rows_affected;
END//

CREATE PROCEDURE getApks () READS SQL DATA
BEGIN
	SELECT apk_id,apk_name,filepath FROM apks;
END//

CREATE PROCEDURE addApk (IN name CHAR(255), IN path CHAR(255), OUT created_id INT) READS SQL DATA
BEGIN
	INSERT INTO apks (apk_name, filepath) VALUES (name, path); 
	SELECT apk_id FROM apks WHERE apk_name=name INTO created_id;
END//
CREATE PROCEDURE deleteApk (IN name CHAR(255), OUT rows_affected INT) READS SQL DATA
BEGIN
	DELETE FROM apks WHERE apk_name=name;
	SELECT ROW_COUNT() INTO rows_affected;
END//
CREATE PROCEDURE getApkPath (IN id INT, OUT path CHAR(255)) READS SQL DATA
BEGIN
	SELECT CONCAT(filepath,apk_name) FROM apks where apk_id=id into path;
END//
DELIMITER ;
