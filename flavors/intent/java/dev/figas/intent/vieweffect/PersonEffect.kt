package dev.figas.intent.vieweffect

import dev.figas.model.Person

sealed class PersonEffect {

    data class OnPersonSaved(val person: Person) : PersonEffect()

}