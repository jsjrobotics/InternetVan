package nyc.spookyrobotics.piserver.database

import java.sql.Connection
import java.sql.PreparedStatement

class StatementBuilder(private val databaseConnection: Connection, private val queryProvider: QueryProvider) {
    private val statementCreators: Map<DatabaseFunctionId, String> = mutableMapOf<DatabaseFunctionId, String>().apply {
        DatabaseFunctionId.entries.forEach { id ->
            val query = queryProvider.getQuery(id)
            put(id, query)
        }
    }

    val preparedStatements: Map< DatabaseFunctionId, PreparedStatement> = mutableMapOf<DatabaseFunctionId, PreparedStatement>().apply {
        statementCreators.forEach { (id, sql) ->
            val statement = databaseConnection.prepareCall(sql)
            put(id, statement)
        }
    }

}
