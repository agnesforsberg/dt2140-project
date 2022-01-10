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
        return listOf("Ready","I'm ready", "lets go", "continue", "Done", "finished")
    }
}

class Repeat : Intent(){
    override fun getExamples(lang: Language): List<String> {
        return listOf("Repeat","One more time", "Can you please take it again", "Again", "What options do I have?")
    }
}

class DontKnow : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("I don't know", "Maybe", "Possibly", "I'm not sure", "What does it mean?")
    }
}

class Shock : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("shock", "my patient has shock", "they are shocked")
    }
}

class Wound : Intent() {
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

class OpenWounds(var wounds: Woundtypes? = null) : Intent(){
    override fun getExamples(lang: Language): List<String> {
        return listOf("@wounds", "they have @wounds", "they have a @wounds")
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