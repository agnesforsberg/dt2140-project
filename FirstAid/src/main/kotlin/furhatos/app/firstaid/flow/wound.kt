package furhatos.app.firstaid.flow

import furhatos.app.firstaid.nlu.*
import furhatos.app.firstaid.nlu.DontKnow
import furhatos.flow.kotlin.*
import furhatos.nlu.common.*

val WoundStartState : State = state(Interaction) {
    onEntry {
        furhat.say("I will walk you through the steps of treating wounds.")
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

    onResponse<OpenWounds> {
        goto(OpenWound)
    }

    onResponse<ClosedWounds> {
        goto(ClosedWound)
    }

    onResponse {
        furhat.say("Just say the word open or closed.")
        furhat.listen()
    }
}



fun ready() : State = state(Interaction){
    onEntry {
        furhat.ask ( "Tell me when you are ready for the next step" )
    }
    onResponse<Ready> {
        terminate()
    }
}


val ClosedWound : State = state(Interaction) {
    onEntry{
        furhat.ask("Does you patient experience leaking from the wound?")
    }
    onReentry {
        furhat.ask("Can you see any signs of leakage?")
    }

    onResponse<Yes> {
        furhat.say("Clean the leaking area with clean water and gently apply bandage " +
                "to prevent further infections.")
        call(ready())
        goto(ClosedWound2)
    }
    onResponse<No> {
        goto(ClosedWound2)
    }
    onResponse<DontKnow> {
        furhat.say("Check for any liquids emerging from the wound")
        reentry()
    }
}

val ClosedWound2 : State = state(Interaction){
    onEntry {
        furhat.ask("Does the wound show signs damage to internal organs?")
    }
    onReentry {
        furhat.ask("Can you see any signs of organ damage?")
    }

    onResponse<Yes> {
        furhat.say("If your patient experiences intense pain and decreased movement " +
                "in the body part. Get additional help.")
        furhat.say("To decrease swelling and or pain apply cold such as ice to the " +
                "wound. Increasing elevation of the body part may also be of help.")
        call(ready())
        goto(EndClosed)
    }
    onResponse<DontKnow> {
        furhat.say("Check for any swelling or bruises.")
        reentry()
    }
    onResponse<No> {
        furhat.say("To decrease swelling and or bruising apply cold ice to the " +
                "wound. Increasing elevation of the body part may also be of help.")
        call(ready())
        furhat.say("If you see signs off internal damage call for further medical service.")
        goto(EndClosed)
    }
}

val EndClosed : State = state(Interaction){
    onEntry{
        furhat.ask("Does you patient experience other wounds in need of treatment?")
    }
    onResponse<Yes>{
        goto(OpenWound)
    }
    onResponse<No>{
        furhat.say("Let me know if I can help you with anything else.")
        goto(MoreHelp)
    }
}
