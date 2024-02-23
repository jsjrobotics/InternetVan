package nyc.spookyrobotics.piserver.database

class DatabaseQueries : QueryProvider {

    override fun getQuery(id: DatabaseFunctionId): String {
        return when (id) {
            DatabaseFunctionId.createPlayer -> "CALL createPlayer(?, ?, ?,?);"
            DatabaseFunctionId.createParticipants -> "CALL createParticipants(?,?);"
            DatabaseFunctionId.createGameRoom -> "CALL createGameRoom(?,?,?,?);"
            DatabaseFunctionId.createGameRoomState -> "CALL createGameRoomState(?,?,?); "
            DatabaseFunctionId.getUserIdFromUserName -> "CALL getUserIdFromUserName(?,?);"
            DatabaseFunctionId.getParticipantIdFromHostId -> "CALL getParticipantIdFromHostId(?,?);"
            DatabaseFunctionId.getGameRoomIdFromHostId -> "CALL getGameRoomIdFromHostId(?,?);"
            DatabaseFunctionId.deleteGameRoom -> "CALL deleteGameRoom(?,?);"
            DatabaseFunctionId.deleteParticipants -> "CALL deleteParticipants(?,?);"
            DatabaseFunctionId.deletePlayer -> "CALL deletePlayer(?,?);"
            DatabaseFunctionId.getApks -> "CALL getApks();"
        }
    }
}
