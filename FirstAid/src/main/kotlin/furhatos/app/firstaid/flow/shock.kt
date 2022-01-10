package furhatos.app.firstaid.flow

import furhatos.app.firstaid.nlu.Ready
import furhatos.app.firstaid.nlu.Repeat
import furhatos.flow.kotlin.*
import furhatos.nlu.common.*

/** Should maybe be changed so that furhat asks in each step if the person has that symptom,
 * and in that case tells the person what to do.
 * Then we can also use gaze/attending checks for the eye thing. */

val ExplainShock1 : State = state(Interaction) {

    onEntry {
        furhat.say("Some of the symptoms of shock are: cool and clammy skin, pale or ashen skin, bluish lips or fingers.")
        furhat.ask("Do you want to hear more symptoms?")
    }

    onResponse<Yes> {
        goto(ExplainShock2)
    }

    onResponse<No> {
        val shock = furhat.askYN("Do you think your patient is experiencing shock?")
        if(shock == true){
            goto(ShockStartState)
        }else {
            goto(WoundStartState)
        }
    }
}

val ExplainShock2 : State = state(Interaction) {
    onEntry {
        furhat.say("The symptoms are: rapid pulse, rapid breathing, and nausea or vomiting.")
        furhat.ask("Do you want to hear the last symptoms as well?")
    }

    onResponse<Yes> {
        goto(ExplainShock3)
    }

    onResponse<No> {
        val shock = furhat.askYN("Do you think your patient is experiencing shock?")
        if(shock == true){
            goto(ShockStartState)
        }else {
            goto(WoundStartState)
        }
    }
}

val ExplainShock3 : State = state(Interaction) {
    onEntry {
        furhat.say("The symptoms are: enlarged pupils, weakness or fatigue, dizziness or fainting, anxiousness or agitation.")
        val shock = furhat.askYN("Do you think your patient is experiencing shock?")
        if(shock == true){
            goto(ShockStartState)
        }else {
            goto(WoundStartState)
        }
    }
}

val ShockStartState : State = state(Interaction) {

    onEntry {
        furhat.say("I will guide you through the steps of treating shock. You need enough space that the patient can lie down.")
        furhat.say("If you are worried for your patient's life - call 112 immediately!")
        furhat.ask("Are you ready to begin?")
    }

    onResponse<Yes> {
        goto(ShockPart1)
    }

    onResponse<Ready> {
        goto(ShockPart1)
    }

    onResponse<No> {
        furhat.say("I will wait 10 seconds, say Ready when you are ready.")
        furhat.listen(timeout = 10000)
    }
}

val ShockPart : State = state(Interaction) {
    onResponse<Repeat> {
        reentry()
    }

    onNoResponse {
        furhat.say("Let me know when you are ready for the next step.")
        furhat.listen()
    }
}

val ShockPart1 : State = state(parent= ShockPart) {
    onEntry {
        furhat.say("Lay the person down and elevate the legs and feet slightly, unless you think this may cause pain or further injury.")
        furhat.say("Let me know when you are ready for the next step.")
        furhat.listen()
    }

    onResponse<Ready> {
        goto(ShockPart2)
    }
}

val ShockPart2: State = state(parent= ShockPart) {
    onEntry {
        furhat.say("Keep the person still and don't move him or her unless necessary.")
        furhat.say("Begin CPR if the person shows no signs of life, such as not breathing, coughing or moving. In this case, call 112!")
        furhat.listen()
    }

    onResponse<Ready> {
        goto(ShockPart3)
    }
}

val ShockPart3: State = state(parent= ShockPart) {
    onEntry {
        furhat.ask("Do you suspect that the person is having an allergic reaction?")
    }

    onResponse<Yes> {
        val injector = furhat.askYN("Do you have access to an epinephrine auto injector?")
        if(injector == true){
            furhat.say("Use it according to the instructions.")
        } else {
            furhat.say("Check if someone in your surrounding has one. Otherwise call 112!")
        }
        furhat.listen()
    }

    onResponse<No> {
        goto(ShockPart4)
    }

    onResponse<Ready> {
        goto(ShockPart4)
    }
}

val ShockPart4: State = state(parent= ShockPart) {
    onEntry {
        val bleeding = furhat.askYN("Is the person bleeding?")
        if(bleeding == true) {
            furhat.say("Hold pressure over the bleeding area. You can use a towel, sheet or clothing.")
            furhat.listen()
        }else {
            goto(ShockPart5)
        }
    }

    onResponse<Ready> {
        goto(ShockPart5)
    }
}

val ShockPart5: State = state(parent= ShockPart) {
    onEntry {
        val vomiting = furhat.askYN("Is the person vomiting or bleeding from the mouth?")
        if(vomiting == true ){
            furhat.say("If no spinal injury is suspected, turn them onto a side to prevent choking.")
            furhat.say("If you suspect spinal injury do not move them as that can worsen the injury.")
            furhat.listen()
        }else {
            goto(ShockEnding)
        }
    }

    onResponse<Ready> {
        goto(ShockEnding)
    }
}

val ShockEnding: State = state(Interaction) {
    onEntry {
        furhat.say("Those are all the steps.")
        furhat.say("If the person is still showing symptoms, call 112.")
        val again = furhat.askYN("Do you want to hear the steps again?")
        if(again == true){
            goto(ShockPart1)
        }else {
            goto(MoreHelp)
        }
    }
}