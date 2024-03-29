package furhatos.app.firstaid.flow

import furhatos.app.firstaid.nlu.DontKnow
import furhatos.app.firstaid.nlu.Shock
import furhatos.app.firstaid.nlu.Wound
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.skills.emotions.UserGestures

val Start : State = state(Interaction) {

    onEntry {
        furhat.ask("Hello, I can help you with treating shock or wounds. Is your patient experiencing shock?")
    }

    onReentry {
        furhat.ask("I can help with shock or wounds. Please repeat what you need help with.")
    }

    onResponse<Yes>{
        goto(ShockStartState)
    }

    onResponse<No>{
        var wound = furhat.askYN("Does your patient have a wound?")
        if(wound == true){
            goto(WoundStartState)
        }else {
            furhat.ask("I can only help with shock or wounds. Please repeat what you need help with.")
        }
    }

    onResponse<DontKnow>{
        var explainShock = furhat.askYN("Do you want me to explain the symptoms of shock?")
        if(explainShock == true){
            goto(ExplainShock1)
        }else {
            reentry()
        }
    }

    onResponse<Wound> {
        goto(WoundStartState)
    }

    onResponse<Shock> {
        goto(ShockStartState)
    }

    onResponse("Goodbye") {
        furhat.say("Come back if you need help.")
        goto(Idle)
    }

    onResponse {
        furhat.say("I didn't understand. I can only help with wounds or shock.")
        furhat.ask("Which do you need help with?")
    }

    onNoResponse {
        reentry()
    }
}

val MoreHelp : State = state(Interaction) {

    onEntry{
        furhat.ask("Does your patient need help with wounds or shock?")
    }

    onResponse<Shock> {
        goto(ShockStartState)
    }

    onResponse<Wound> {
        goto(WoundStartState)
    }

    onResponse<No> {
        furhat.say("Come back if you need more help. Good bye!")
        goto(Idle)
    }

    onResponse("Nothing", "I don't need help") {
        furhat.say("Come back if you need more help. Good bye!")
        goto(Idle)
    }

    onResponse {
        furhat.say("I didn't understand. I can only help with shock or wounds.")
        furhat.ask("Which one do you need help with?")
    }

    onNoResponse {
        goto(Idle)
    }
}