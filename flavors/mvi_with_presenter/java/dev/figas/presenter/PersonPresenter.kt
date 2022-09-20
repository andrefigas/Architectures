package dev.figas.presenter

import android.os.AsyncTask
import dev.figas.intent.event.PersonEvent
import dev.figas.intent.vieweffect.PersonEffect
import dev.figas.intent.viewstate.PersonState
import dev.figas.model.Person
import dev.figas.model.PersonModelContract
import dev.figas.view.PersonView

class PersonPresenter(private val view : PersonView, private val model: PersonModelContract) : PersonPresenterContract {

    private val requests = mutableListOf<AsyncTask<*, *, *>>()

     override fun processIntent(personEvent: PersonEvent){
        when(personEvent){
            is PersonEvent.OnSubmitClicked -> {
                injectPerson(personEvent.name)
            }

            is PersonEvent.OnLoad -> {
                fetchPerson()
            }

            is PersonEvent.OnRelease -> {
                release()
            }
        }
    }

    private fun injectPerson(name : String) {
        requests.add(
            model.injectPerson(
                Person(name),
                onPreExecute = {
                    view.processPageState(PersonState.Loading)
                },
                onPostExecute = { person->
                    view.processEffect(PersonEffect.OnPersonSaved(person))
                }
            )

        )

    }

    private fun fetchPerson() {
        requests.add(
            model.providePerson(
                onPreExecute = {
                    view.processPageState(PersonState.Loading)
                },
                onPostExecute = { person->
                    view.processPageState(PersonState.Data(person))
                })
        )
    }

    private fun release(){
        requests.forEach {
            it.cancel(true)
        }
    }

}

interface PersonPresenterContract{
    fun processIntent(personIntent: PersonEvent)
}
