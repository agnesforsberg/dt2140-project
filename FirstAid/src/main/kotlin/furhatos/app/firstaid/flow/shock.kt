package furhatos.app.firstaid.flow

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
        var shock = furhat.askYN("Do you think your patient is experiencing shock?")
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
        var shock = furhat.askYN("Do you think your patient is experiencing shock?")
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
        var shock = furhat.askYN("Do you think your patient is experiencing shock?")
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

    onResponse("Ready") {
        goto(ShockPart1)
    }

    onResponse<No> {
        furhat.say("I will wait 10 seconds, say Ready when you are ready.")
        furhat.listen(timeout = 10000)
    }
}

val ShockPart1 : State = state(Interaction) {
    onEntry {
        furhat.say("Lay the person down and elevate the legs and feet slightly, unless you think this may cause pain or further injury.")
        furhat.say("Let me know when you are ready for the next step.")
        furhat.listen()
    }

    onResponse("Ready", "Yes", "Next") {
        goto(ShockPart2)
    }

    onResponse("Repeat") {
        reentry()
    }

    onNoResponse {
        furhat.say("Let me know when you are ready for the next step.")
    }
}

val ShockPart2: State = state(Interaction) {
    onEntry {
        furhat.say("Keep the person still and don't move him or her unless necessary.")
        furhat.say("Begin CPR if the person shows no signs of life, such as not breathing, coughing or moving. In this case, call 112!")
        furhat.listen()
    }

    onResponse("Ready", "Yes", "Next") {
        goto(ShockPart3)
    }

    onResponse("Repeat") {
        reentry()
    }

    onNoResponse {
        furhat.say("Let me know when you are ready for the next step.")
    }
}

val ShockPart3: State = state(Interaction) {
    onEntry {
        furhat.say("If you suspect that the person is having an allergic reaction, and you have access to an " +
                "epinephrine autoinjector, use it according to its instructions.")
        furhat.listen()
    }

    onResponse("Ready", "Yes", "Next") {
        goto(ShockPart4)
    }

    onResponse("Repeat") {
        reentry()
    }

    onNoResponse {
        furhat.say("Let me know when you are ready for the next step.")
    }
}

val ShockPart4: State = state(Interaction) {
    onEntry {
        furhat.say("If the person is bleeding, hold pressure over the bleeding area, using a towel or sheet.")
        furhat.listen()
    }

    onResponse("Ready", "Yes", "Next") {
        goto(ShockPart5)
    }

    onResponse("Repeat") {
        reentry()
    }

    onNoResponse {
        furhat.say("Let me know when you are ready for the next step.")
    }
}

val ShockPart5: State = state(Interaction) {
    onEntry {
        furhat.say("If the person vomits or begins bleeding from the mouth, and no spinal injury is suspected, " +
                "turn him or her onto a side to prevent choking.")
        furhat.listen()
    }

    onResponse("Ready", "Yes", "Next") {
        furhat.say("These are all the steps.")
        var again = furhat.askYN("Do you want to hear the steps again?")
        if(again == true){
            goto(ShockPart1)
        }else {
            goto(MoreHelp)
        }
    }

    onResponse("Repeat") {
        reentry()
    }

    onNoResponse {
        furhat.say("Let me know when you are ready for the next step.")
    }
}