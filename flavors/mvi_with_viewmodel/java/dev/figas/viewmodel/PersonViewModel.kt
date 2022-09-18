package dev.figas.viewmodel

import android.os.AsyncTask
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.figas.domain.models.Person
import dev.figas.domain.usecases.GetPersonUseCase
import dev.figas.domain.usecases.UpdatePersonUseCase
import dev.figas.intent.PersonIntent
import dev.figas.vieweffect.PersonEffect
import dev.figas.viewstate.PersonState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PersonViewModel(val getPersonUseCase: GetPersonUseCase,
                      val updatePersonUseCase: UpdatePersonUseCase) : ViewModel() {

    private val _uiState : MutableStateFlow<PersonState> = MutableStateFlow(PersonState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _intent : MutableSharedFlow<PersonIntent> = MutableSharedFlow()
    val intent = _intent.asSharedFlow()

    private val _effect : Channel<PersonEffect> = Channel()
    val effect = _effect.receiveAsFlow()

    private val requests = mutableListOf<AsyncTask<*, *, *>>()

    init {
        subscribeEvents()
    }

    fun sendIntent(event : PersonIntent) {
        viewModelScope.launch { _intent.emit(event) }
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            intent.collect {
                handleEvent(it)
            }
        }
    }

    private fun handleEvent(event: PersonIntent) {
        when (event) {
           is PersonIntent.OnSubmitClicked -> {
               injectPerson(event.name)
           }

            is PersonIntent.OnLoad -> {
                fetchPerson()
            }
        }
    }

    private fun injectPerson(name: String) {
        requests.add(
            updatePersonUseCase.execute(Person(name), onPreExecute = {
                _uiState.value = PersonState.Loading
            }, onPostExecute = { person ->
                viewModelScope.launch {
                    _effect.send(PersonEffect.OnPersonSaved(person))
                }
            })
        )

    }

    private fun fetchPerson() {
        requests.add(
            getPersonUseCase.execute(onPreExecute = {
                _uiState.value = PersonState.Loading
            }, onPostExecute = { person ->
                _uiState.value = PersonState.Data(person)
            })
        )

    }

    override fun onCleared() {
        super.onCleared()
        requests.forEach {
            it.cancel(true)
        }
    }

}
