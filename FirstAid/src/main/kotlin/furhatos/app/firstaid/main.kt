package furhatos.app.firstaid

import furhatos.app.firstaid.flow.*
import furhatos.skills.Skill
import furhatos.flow.kotlin.*

class FirstaidSkill : Skill() {
    override fun start() {
        Flow().run(Idle)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}
