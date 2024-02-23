package nyc.spookyrobotics.piserver.database

interface QueryProvider {
    fun getQuery(id: DatabaseFunctionId): String
}