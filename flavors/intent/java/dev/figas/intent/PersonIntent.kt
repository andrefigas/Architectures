package dev.figas.intent

sealed class PersonIntent {

    object OnLoad : PersonIntent()

    object OnRelease : PersonIntent()

    data class OnSubmitClicked(val name : String) : PersonIntent()

}