package furhatos.app.firstaid.flow

import furhatos.app.firstaid.nlu.DontKnow
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.onResponse
import furhatos.flow.kotlin.state
import furhatos.nlu.common.*

val WoundStartState : State = state(Interaction) {
    onEntry {
        furhat.say("Start with washing your hands with water and soap if possible.")
        furhat.say("Avoid touching the wound with your fingers while" +
                "treating the wound. If possible, use disposable gloves.")
        furhat.ask("Say ready when you are ready.")
    }

    onResponse("Ready") {
        goto(OpenOrClosed)
    }
}

val OpenOrClosed : State = state(Interaction) {
    onEntry {
        furhat.ask("Is the wound open or closed?")
    }

    onResponse<DontKnow> {
        furhat.ask("If the wound has an opening where you can see into the skin it is open.")
        reentry()
    }

    onResponse("Open") {
        goto(OpenWound)
    }

    onResponse("Closed") {
        goto(ClosedWound)
    }

    onResponse {
        furhat.say("Just say the work open or closed.")
        furhat.listen()
    }
}

val OpenWound : State = state(Interaction) {

    onEntry {
        furhat.say("Remove obstructive jewelry and clothing from the injured body part.")
        furhat.ask("Tell me when you are ready for the next step.")
    }

    onResponse("Ready") {
        var dirty = furhat.askYN("Is the wound dirty?")
        if(dirty == true ){
            furhat.say("Gently flood the wound with bottled water or clean running water " +
                    "(if available, saline solution is preferred).")
        }
        furhat.say("Gently clean around the wound with soap and clean water.")
        goto(KindOfWound)
    }

}

val KindOfWound : State = state(Interaction) {
    onEntry {
        furhat.say("There are different kinds of wounds: Infected, leaking, bite, graze or lazeration.")
        furhat.ask("What type of wound is this?")
    }

    onResponse("Repeat") {
        reentry()
    }

    // TODO treat the different kinds of wounds

}

val ClosedWound : State = state(Interaction) {

}
