package dev.figas.intent.event

sealed class PersonEvent {

    object OnLoad : PersonEvent()

    object OnRelease : PersonEvent()

    data class OnSubmitClicked(val name : String) : PersonEvent()

}