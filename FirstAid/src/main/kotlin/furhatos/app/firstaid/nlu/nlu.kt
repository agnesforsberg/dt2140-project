package furhatos.app.firstaid.nlu

import furhatos.nlu.ComplexEnumEntity
import furhatos.nlu.EnumEntity
import furhatos.nlu.Intent
import furhatos.nlu.ListEntity
import furhatos.nlu.common.Number
import furhatos.util.Language

class Emergencies : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("shock", "bleeding", "wound")
    }
}

class ExplainEmergency(var emergeny : Emergencies? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@emergency", "I have @emergencies", "I am suffering from @emergencies",
                "My friend has @emergencies", "My friend is suffering from @emergencies")
    }
}

class Ready : Intent(){
    override fun getExamples(lang: Language): List<String> {
        return listOf("Ready","I'm ready", "let's go", "continue", "Done", "finished", "next", "next step",
        "ready for the next step", "okay", "and then", "then what?")
    }
}

class Repeat : Intent(){
    override fun getExamples(lang: Language): List<String> {
        return listOf("Repeat","One more time", "Can you please take it again", "Again", "What options do I have?")
    }
}

class DontKnow : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("don't know", "I don't know", "Maybe", "Possibly", "I'm not sure", "What does it mean?")
    }
}

class Shock : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("shock", "my patient has shock", "they are shocked", "they have shock", "shocked")
    }
}

class Wound(var woundTypes: Woundtypes? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("wound", "bleeding", "they are bleeding", "they have a wound", "my patient has a wound",
                "my patient is bleeding", "the wound is @woundTypes")
    }
}

class Graze : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("graze", "guys", "gays", "grace", "gaze", "gace")
    }
}

class Bite : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("bite", "byte", "B")
    }
}

class OpenWounds : Intent(){
    override fun getExamples(lang: Language): List<String> {
        return listOf("open", "open wound", "it's open", "they have an open wound")
    }
}

class ClosedWounds : Intent(){
    override fun getExamples(lang: Language): List<String> {
        return listOf("closed", "closed wound", "it's closed", "they have a closed wound")
    }
}

// wound entity
class Woundtypes : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("infected", "leaking", "bite", "laceration", "graze")
    }
}

class WoundList : ListEntity<QuantifiedWounds>()

class QuantifiedWounds(
    val count : Number? = Number(1),
    val wounds : Woundtypes? = null) : ComplexEnumEntity() {

    override fun getEnum(lang: Language): List<String> {
        return listOf("@count @wounds", "@wounds")
    }

    override fun toText(): String {
        return generate("$count $wounds")
    }
}