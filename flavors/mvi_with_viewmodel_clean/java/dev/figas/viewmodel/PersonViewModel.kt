package dev.figas.viewmodel

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

import dev.figas.domain.models.Person
import dev.figas.domain.usecases.GetPersonUseCase
import dev.figas.domain.usecases.UpdatePersonUseCase
import dev.figas.intent.event.PersonEvent
import dev.figas.intent.vieweffect.PersonEffect
import dev.figas.intent.viewstate.PersonState

class PersonViewModel(private val getPersonUseCase: GetPersonUseCase,
                      private val updatePersonUseCase: UpdatePersonUseCase
) : ViewModel() {

    private val _uiState : MutableLiveData<PersonState> = MutableLiveData(PersonState.Idle)
    val uiState : LiveData<PersonState> = _uiState

    private val _events : MutableLiveData<PersonEvent> = MutableLiveData()

    private val _effect : MutableLiveData<PersonEffect> = MutableLiveData()
    val effect : LiveData<PersonEffect> = _effect

    private val eventObserver : Observer<PersonEvent> by lazy {
        Observer<PersonEvent> { event -> handleEvent(event) }
    }

    private val requests = mutableListOf<AsyncTask<*, *, *>>()

    init {
        subscribeEvents()
    }


    fun sendIntent(event : PersonEvent) {
        _events.value = event
    }

    private fun subscribeEvents() {
        _events.observeForever(eventObserver)
    }

    private fun handleEvent(event: PersonEvent) {
        when (event) {
           is PersonEvent.OnSubmitClicked -> {
               injectPerson(event.name)
           }

            is PersonEvent.OnLoad -> {
                fetchPerson()
            }
        }
    }

    private fun injectPerson(name: String) {
        requests.add(
            updatePersonUseCase.execute(Person(name), onPreExecute = {
                _uiState.value = PersonState.Loading
            }, onPostExecute = { person ->
                _effect.value = PersonEffect.OnPersonSaved(person)
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

        _events.removeObserver(eventObserver)
    }

}
