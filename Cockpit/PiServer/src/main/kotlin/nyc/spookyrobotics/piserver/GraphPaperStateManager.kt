package nyc.spookyrobotics.piserver

class GraphPaperStateManager {
    fun updateState(textPayload: String?) {
        currentState = textPayload
    }

    fun reset() {
        currentState = DEFAULT_STATE
    }

    var currentState: String? = DEFAULT_STATE
        private set

    companion object {
        private val DEFAULT_STATE = "{\"channelId\":\"ChillQueens\",\"gameState\":{\"cell1\":0,\"cell2\":0,\"cell3\":0,\"cell4\":1,\"cell5\":0,\"cell6\":0,\"currentPlayerTurn\":0}}"
    }
}
