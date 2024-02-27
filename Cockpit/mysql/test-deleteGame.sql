# Create a player
USE board;

SET @create_username = "test_username";
SET @create_email = "test_email";
SET @create_password = "test_password";
SET @create_state = "test_state";
SET @create_rules = "no rules";
SELECT getParticipantIdFromHostId(@create_id) INTO @created_participants_id;
SELECT getUserIdFromUserName(@create_username) INTO @created_id;
SELECT getGameRoomIdFromHostId(@created_id) INTO @created_room_id;

SELECT deleteGameRoom(@created_room_id);
SELECT deleteParticipants(@created_participants_id);
SELECT deletePlayer(@created_id);
