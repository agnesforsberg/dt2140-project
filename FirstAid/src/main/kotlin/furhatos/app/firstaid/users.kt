package furhatos.app.firstaid.flow

import furhatos.app.firstaid.nlu.WoundList
import furhatos.records.User

class WoundsData(
    var wounds : WoundList = WoundList()
)

val User.woundlist : WoundsData
    get() = data.getOrPut(WoundsData::class.qualifiedName, WoundsData())