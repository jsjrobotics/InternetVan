package nyc.spookyrobotics.piserver

import nyc.spookyrobotics.piserver.datastructures.Param

abstract class Endpoint (val path: String,
    val method: String,
    val paramList: List<Param> = emptyList()
){
}