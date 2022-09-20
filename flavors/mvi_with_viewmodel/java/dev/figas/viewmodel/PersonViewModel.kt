package dev.figas.viewmodel

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.figas.intent.event.PersonEvent
import dev.figas.model.Person
import dev.figas.model.PersonModelContract
import dev.figas.intent.vieweffect.PersonEffect
import dev.figas.intent.viewstate.PersonState
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject


class PersonViewModel(val model: PersonModelContract) : ViewModel() {

    private val _uiState : MutableLiveData<PersonState> = MutableLiveData(PersonState.Idle)
    val uiState = _uiState

    private val _events = PublishSubject.create<PersonEvent>()
    val events : Observable<PersonEvent> = _events

    private val _effect = PublishSubject.create<PersonEffect>()
    val effect : Observable<PersonEffect> = _effect

    private val requests = mutableListOf<AsyncTask<*, *, *>>()

    init {
        subscribeEvents()
    }

    fun sendIntent(event : PersonEvent) {
        _events.onNext(event)
    }

    private fun subscribeEvents() {
        events.subscribe { event->
            handleEvent(event)
        }

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
            model.injectPerson(Person(name), onPreExecute = {
                _uiState.value = PersonState.Loading
            }, onPostExecute = { person ->
                _effect.onNext(PersonEffect.OnPersonSaved(person))
            })
        )

    }

    private fun fetchPerson() {
        requests.add(
            model.providePerson(onPreExecute = {
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
