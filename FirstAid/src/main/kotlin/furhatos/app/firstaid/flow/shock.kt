package furhatos.app.firstaid.flow

import furhatos.app.firstaid.nlu.DontKnow
import furhatos.app.firstaid.nlu.Ready
import furhatos.app.firstaid.nlu.Repeat
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.common.*
import furhatos.skills.emotions.UserGestures

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
        //goto(CheckAttentionStart)
    }

    onResponse<Ready> {
        goto(ShockPart1)
    }

    onResponse<No> {
        furhat.say("I will wait 10 seconds, tell me when you are ready.")
        furhat.listen(timeout = 10000)
    }
}

val ShockPart : State = state(Interaction) {
    onResponse<Repeat> {
        reentry()
    }

    onNoResponse {
        furhat.say("Let me know when you are ready for the next step.")
        furhat.listen(5000)
    }
}

val ShockPart1 : State = state(parent= ShockPart) {
    onEntry {
        furhat.say("Lay the person down and elevate the legs and feet slightly, " +
                "unless you think this may cause pain or further injury.")
        furhat.say("Do not let the person eat or drink anything.")
        furhat.say("Let me know when you are ready for the next step.")
        furhat.listen(5000)
    }

    onResponse<Ready> {
        goto(ShockPart2)
    }
}

val ShockPart2: State = state(parent= ShockPart) {
    onEntry {
        furhat.say("Keep the person still and don't move them unless necessary.")
        furhat.say("Begin CPR if the person shows no signs of life, such as not breathing, coughing or moving. " +
                "In this case, call 112!")
        furhat.say("Loosen tight clothing and, if needed, cover the person with a blanket to prevent chilling.")
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

    onResponse<DontKnow> {
        furhat.say("Some symptoms are: swollen tongue or face, difficulty breathing, confusion, " +
                "blue skin or lips, and lightheadedness.")
        furhat.ask("Does your patient have these symptoms?")
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
            goto(ShockFurhatChecks)
        }
    }

    onResponse<Ready> {
        goto(ShockFurhatChecks)
    }
}

val ShockFurhatChecks: State = state(Interaction) {
    onEntry {
        furhat.say("I will now perform some checks on the patient's level of consciousness.")
        goto(CheckAttentionStart)
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

val CheckAttentionStart: State = state(Interaction) {
    /** This state checks if the patient is conscious enough to look at the furhat, as not having a steady gaze
     * can be a sign of severe shock. To do this the attention is moved from the helper, talking user,
     * to another person. This assumes that there is another person, so if the helper is helping itself it will
     * not work. */
    onEntry {
        furhat.say("I will check if the patient can direct their gaze to me.")
        furhat.ask("Make sure only you and the patient are close to me. Are you ready?")
    }

    onResponse<Yes> {
        goto(CheckAttention)
    }

    onResponse<Ready> {
        goto(CheckAttention)
    }

    onResponse<No> {
        furhat.say("Move the other people so that only you and the patient is close to me.")
        reentry()
    }

    onReentry {
        furhat.ask("Are you ready?")
    }
}

val CheckAttention: State = state(Interaction) {
    onEntry {
        furhat.attend(users.other)
        furhat.ask("Can you look at me?")
        //furhat.glance(users.other, 3000)
    }

    onResponse<Yes> {
        if(users.current.isAttendingFurhat() ){
            furhat.say("That is a good sign. I will now perform the next check.")
            goto(CheckCanSmile)
        }else {
            furhat.ask("Can you look at me?")
        }
    }

    onResponse<No> {
        furhat.say("Your patient can not look at me. That is not a good sign.")
        furhat.attend(users.other)
        goto(ShockEnding)
    }

    onUserAttend(instant = true) {user ->
        if (user.isAttendingFurhat() && (user.id == users.current.id)) {
            furhat.say("That is a good sign. I will now perform the next check.")
            goto(CheckCanSmile)
        } else {
            print("not looking")
        }
    }

    onNoResponse {
        furhat.attend(users.other)
        val wait = furhat.askYN("Do you want me to wait longer?")
        if(wait == true){
            reentry()
        } else {
            goto(ShockEnding)
        }
    }

    onReentry {
        furhat.say("Please look at me.")
    }
}

val CheckCanSmile: State = state(Interaction) {
    /** This state checks if the patient can smile. A smile is a simple gesture that every person
     * can do. Therefor this check can indicate a lack in muscle control, hearing or a lower level of consciousness.
     * If the patient can not smile it is good to perform more checks to know which is the case. */

    onEntry {
        furhat.say("It's a good sign to be able to smile. Can you smile?")
    }

    onUserGesture(UserGestures.Smile) {
        if(it.userID == users.current.id){
            furhat.say("Good job!")
            furhat.attend(users.other)
            goto(ShockEnding)
        }else {
            furhat.say("The patient should be the one to smile.")
            furhat.ask("Can the patient smile?")
        }
    }

    onResponse<No> {
        furhat.attend(users.other)
        goto(ShockEnding)
    }

    onReentry {
        furhat.glance(users.other, 2000)
        furhat.say("Can you show me a smile?")
    }
}
