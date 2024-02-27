# Test Pi server by creating a game 
create_username="test_username";
create_email="test_email";
create_password="test_password";
create_rules="test_rules";
create_state="test_state";
authority='http://localhost:11136'
urlCreatePlayer="${authority}/create_player"
urlDeletePlayer="${authority}/delete_player"

# Test create player and get player id from user name
userId=`curl -X POST "$urlCreatePlayer?username=$create_username&email=$create_email&password=$create_password"`
test_userId=`curl "$urlGetUserIdFromUserName?username=$create_username"`
echo "Created UserId $userId  Retrieved UserId $test_userId"
if [ $userId = $test_userId ]; then
   echo "Pass"
else 
   echo "Fail"
fi

#SELECT createPlayer(@create_username, @create_email, @create_password);
# Save value from create player. Compare it to value from getUserIdFromUserName. both must be equal
#SELECT getUserIdFromUserName(@create_username) INTO @created_id;

# Player creates a list of participants
#curl -d "user_id=$created_id" -X POST "$urlCreateParticipants"
# Save value from create Participants. Compare it to value from getParticipantIdFromHostId. Both must be equal
#SELECT createParticipants(@created_id);
#SELECT getParticipantIdFromHostId(@create_id) INTO @created_participants_id;

# Player creates a game room
#curl -d "user_id=$created_id&participants_id=$created_participants_id&rules=$create_rules" -X POST "$urlCreateGameRoom"
# Save value from create gameroom. Compare it to value from getGameRoomIdFromHostId. Both must be equal
#SELECT createGameRoom(@created_id, @created_participants_id, @create_rules);
#SELECT getGameRoomIdFromHostId(@created_id) INTO @created_room_id;

# Create game room state
#curl -d "room_id=$created_room_id&game_state=$create_state" -X POST "$urlCreateGameRoomState"
#SELECT createGameRoomState(@created_room_id, @create_state);


#SELECT getParticipantIdFromHostId(@create_id) INTO @created_participants_id;
#SELECT getUserIdFromUserName(@create_username) INTO @created_id;
#SELECT getGameRoomIdFromHostId(@created_id) INTO @created_room_id;

#SELECT deleteGameRoom(@created_room_id);
#SELECT deleteParticipants(@created_participants_id);
#SELECT deletePlayer(@created_id);

