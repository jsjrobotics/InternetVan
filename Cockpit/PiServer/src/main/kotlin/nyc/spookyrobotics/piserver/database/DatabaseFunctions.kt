package nyc.spookyrobotics.piserver.database

import nyc.spookyrobotics.piserver.datastructures.ApkFile
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.Types

class DatabaseFunctions(private val statementBuilderProvider: () -> StatementBuilder?) {
    private val statementBuilder : StatementBuilder?
        get() = statementBuilderProvider.invoke()

    private fun getStatement(id: DatabaseFunctionId): PreparedStatement? {
        return statementBuilder?.preparedStatements?.get(id) ?: run {
            println("No statement found for $id")
            return null
        }
    }
    private fun parseArgs(statement: PreparedStatement, arguments: List<Any>) {
        arguments.forEachIndexed { index, arg ->
            val parameterIndex = index+1
            when (arg) {
                is String -> statement.setString(parameterIndex, arg)
                is Int -> statement.setInt(parameterIndex, arg)
                else -> throw IllegalArgumentException("Unhandled argument type ${arg.javaClass}")
            }
        }
    }
    private fun getCallable(functionId: DatabaseFunctionId): CallableStatement? {
        return getStatement(functionId) as? CallableStatement?
    }
    fun createPlayer (username: String, email: String, password: String): Long? {
        val sql = getCallable(DatabaseFunctionId.createPlayer) ?: run {
            println("No callable statement found for createPlayer")
            return null
        }
        parseArgs(sql, listOf(username, email, password))
        sql.registerOutParameter(4, Types.CHAR)
        sql.execute()
        val id = sql.getLong(4)
        println("playerId: $id")
        return id
    }

    fun deletePlayer (userId: Int ): Int? {
        val sql = getCallable(DatabaseFunctionId.deletePlayer) ?: run {
            println("No callable statement found for deletePlayer")
            return null
        }
        parseArgs(sql, listOf(userId))
        sql.registerOutParameter(2, Types.INTEGER)
        sql.execute()
        val rows_affected = sql.getInt(2)
        println("deletePlayer: $rows_affected")
        return rows_affected
    }

    fun getApks(): List<ApkFile>? {
        val sql = getCallable(DatabaseFunctionId.getApks) ?: run {
            println("No callable statement found for getApks")
            return null
        }
        val result = sql.executeQuery()
        val apkList = mutableListOf<ApkFile>()
        while (result.next()) {
            val name = result.getString("apk_name")
            val id = result.getInt("apk_id")
            val path = result.getString("filepath")
            val apk = ApkFile(name, path, id)
            apkList.add(apk)
        }
        return apkList
    }
}