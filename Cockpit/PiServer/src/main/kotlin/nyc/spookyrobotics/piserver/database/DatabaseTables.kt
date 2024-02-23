package nyc.spookyrobotics.piserver.database

object DatabaseTables {
    object Players {
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_CREATE_TIME = "create_time"
    }

    object Participants {
        const val COLUMN_PARTICIPANTS_ID = "participants_id"
        const val COLUMN_LIST_USER_IDS = "list_user_ids"
    }

    object GameRooms {
        const val COLUMN_GAME_ROOM_ID = "game_room_id"
        const val COLUMN_HOST_USER_ID = "host_user_id"
        const val COLUMN_PARTICIPANTS = "participants"
        const val COLUMN_RULES = "rules"
    }


    object GameState {
        const val COLUMN_GAME_STATE_ID = "game_state_id"
        const val COLUMN_GAME_ROOM_ID = "game_room_id"
        const val COLUMN_STATE = "state"
    }
}