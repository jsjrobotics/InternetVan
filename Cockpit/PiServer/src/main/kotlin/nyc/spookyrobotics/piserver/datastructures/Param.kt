package nyc.spookyrobotics.piserver.datastructures

abstract class Param(val id: String) {
    fun queryString(value: String) = "$id=$value"
}