# Create a player
USE board;

SET @create_username = "test_username";
SET @create_email = "test_email";
SET @create_password = "test_password";
SELECT createPlayer(@create_username, @create_email, @create_password); 
SELECT getUserIdFromUserName(@create_username) INTO @created_id;

# Player creates a list of participants
SELECT createParticipants(@created_id); 
SELECT getParticipantIdFromHostId(@create_id) INTO @created_participants_id;

# Player creates a game room
SET @create_rules = "no rules";
SELECT createGameRoom(@created_id, @created_participants_id, @create_rules);
SELECT getGameRoomIdFromHostId(@created_id) INTO @created_room_id;
 
# Create game room state
SET @create_state = "test_state";
SELECT createGameRoomState(@created_room_id, @create_state); 
