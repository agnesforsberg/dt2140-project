package furhatos.app.firstaid.flow

import furhatos.app.firstaid.nlu.*
import furhatos.app.firstaid.nlu.DontKnow
import furhatos.flow.kotlin.*
import furhatos.nlu.common.*
import furhatos.util.Language

val WoundStartState : State = state(Interaction) {
    onEntry {
        furhat.say("Start with washing your hands with water and soap if possible.")
        furhat.say("Avoid touching the wound with your fingers while" +
                " treating the wound. If possible, use disposable gloves.")
        furhat.ask("Say ready when you are ready.")
    }

    onResponse<Ready> {
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
        furhat.say("Just say the word open or closed.")
        furhat.listen()
    }
}

val OpenWound : State = state(Interaction) {

    onEntry {
        furhat.say("Remove obstructive jewelry and clothing from the injured body part.")
        furhat.ask("Tell me when you are ready for the next step.")
    }

    onResponse<Ready> {
        var dirty = furhat.askYN("Is the wound dirty?")
        if(dirty == true ){
            furhat.say("Gently flood the wound with bottled water or clean running water " +
                    "(if available, saline solution is preferred).")
        }else {
            furhat.say("Even if the wound is not dirty gently clean around the wound with soap" +
                    " and clean water. It will prevent possible infections.")
        }
        goto(KindOfWound)
    }

}

fun Ready() : State = state(Interaction){
    onEntry {
        furhat.ask ( "Tell me when you are ready for the next step" )
    }
    onResponse<Ready> {
        terminate()
    }
}

fun Infected() : State = state(Interaction){
    onEntry {
        furhat.say("Then gently apply antiseptic liquid to clean off the wound.")
        call(Ready())
        furhat.say("Once the wound is clean carefully apply an adhesive bandage or a dry clean cloth.")
        call(Ready())
        val wound = furhat.askYN ( "Does your patient have a fever and or intense pain?" )
        if(wound == true){
            furhat.say("Call 112 and make sure your patient receives more help.")
        }
        terminate()
    }
}

val Options = state(Interaction){
    onResponse<Repeat> {
        goto(KindOfWound)
    }

    onResponse("Infected"){
        call(Infected())
        reentry()
    }

    onResponse("leaking"){
        furhat.say("Gently clean the wound with a dry and clean cloth to clean any remaining fluids.")
        call(Ready())
        furhat.say("Apply an absorbing dressing such as foam or hydrofiber to prevent further leakage.")
        call(Ready())
        val wound = furhat.askYN ( "Does the wound still show signs of leakage?" )
        if(wound == true){
            furhat.say("Repeat the steps and apply pressure over the wound with a bandage.")
            call(Ready())
            furhat.say("If the leaking doesn't decrease, call 112 and make sure " +
                    "your patient receives more help.")
        }
        furhat.say("Watch for signs of possible infections and if they " +
                "appear call a doctor immediately.")
        reentry()
    }

    onResponse<Bite> {
        val bite = furhat.askYN("Has the bite broken the skin?")
        if(bite == true){
            furhat.say("Call 112 and immediately seek more help.")
            furhat.say("Run the wound under water and remove any objects in the wound " +
                    "while waiting for further assistance.")
        }else{
            val infec = furhat.askYN("Is the bite wound infected?")
            if(infec == true){
                call(Infected())
            }else{
                furhat.say("Gently clean and dry the wound and cover it with " +
                        "a clean dressing or plaster")
                call(Ready())
                furhat.say("Seek further medical advice unless the bite is very minor.")
            }
        }
        reentry()
    }

    onResponse<Graze> {
        val infec = furhat.askYN("Is the graze wound infected?")
        if(infec == true){
            call(Infected())
        }else{
            furhat.say("Clean the wound gently with antiseptic to avoid infection." +
                    " Apply a paraffin gaze or dressing to stop bleeding.")
            call(Ready())
            if(furhat.askYN("Is bleeding still persistent?") == true) {
                furhat.say("Increase elevation on the wounded body part to decrease flow. " +
                        "Or increase pressure around the wound by preventing the blood to circulate.")
                call(Ready())
                furhat.say("If the bleeding becomes significantly worse call for medical help.")
            }
            furhat.say("Watch for signs of possible infections and if they " +
                    "appear call a doctor immediately.")
        }
        reentry()
    }

    onResponse("laceration") {
        val infec = furhat.askYN("Is the laceration wound infected?")
        if(infec == true){
            call(Infected())
        }else{
            furhat.say("Start by trying to prevent blood loss by elevating the wounded area. " +
                    "It can take several minutes for flow to decrease.")
            furhat.say("If you suspect a main artery has been cut, immediately call 112 and get help.")
            call(Ready())
            if(furhat.askYN("Is bleeding still persistent?") == true) {
                furhat.say("Call for medical help.")
                furhat.say("Try to decrease bleeding via applying pressure, such as tourniquets.")
                call(Ready())
            }else{
                furhat.say("Gently clean the wound with antiseptics or clean water.")
                call(Ready())
                furhat.say("Examine the laceration.")
                val stitch = furhat.askYN("Is the wound wider or deeper than half an inch " +
                        "or does it expose bone or tissue?")
                if(stitch == true){
                    furhat.say("Patient may require stitches, call medical services to help.")
                    furhat.say("While awaiting further help patch the wound up.")
                    call(Ready())
                }else{
                    furhat.say("Apply a layer of antiseptic ointment and an cover with adhesive bandage.")
                    call(Ready())
                    furhat.say("Cover the wound with a sterile gauze.")
                    call(Ready())
                    furhat.say("Watch for signs of possible infections and if they " +
                            "appear call a doctor immediately.")
                }
            }
        }
        reentry()
    }

    onResponse<DontKnow> {
        furhat.say("Examine the open wounds of your patient.")
        call(Ready())
        goto(KindOfWound)
    }
}

val KindOfWound : State = state(parent = Options) {
    onEntry {
        furhat.say("There are different kinds of wounds: ${Woundtypes().getEnum(Language.ENGLISH_US).joinToString(", " )} wounds.")
        furhat.ask("What type of wound does your patient have?")
    }
    onReentry {
        var wounds = furhat.askYN ( "Does you patient experience any other open wounds in need of treatment?" )
        if (wounds == true){
            furhat.ask("What other types of wounds does you patient experience?")
        }else{
            furhat.say("Happy to be of service! Let me know if I can help you with anything else.")
        }
    }

}



val ClosedWound : State = state(Interaction) {

}
