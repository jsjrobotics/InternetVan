package nyc.spookyrobotics.piserver.database

enum class DatabaseFunctionId {
        createPlayer,
        createParticipants,
        createGameRoom,
        createGameRoomState,
        getUserIdFromUserName,
        getParticipantIdFromHostId,
        getGameRoomIdFromHostId,
        deleteGameRoom,
        deleteParticipants,
        deletePlayer,
        getApks,
}
