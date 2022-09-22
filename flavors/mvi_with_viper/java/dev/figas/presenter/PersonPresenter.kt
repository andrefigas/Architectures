package dev.figas.presenter

import dev.figas.intent.event.PersonEvent
import dev.figas.intent.vieweffect.PersonEffect
import dev.figas.intent.viewstate.PersonState
import dev.figas.interactor.MainInteractorContract
import dev.figas.route.MainRouterContract
import dev.figas.view.PersonView

class PersonPresenter(private val view : PersonView,
                      private val router: MainRouterContract,
                      private val interactor: MainInteractorContract
) : PersonPresenterContract {

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
        interactor.injectPerson(
            name,
            onPreExecute = {
                view.processPageState(PersonState.Loading)
            },
            onPostExecute = { person->
                view.processEffect(PersonEffect.OnPersonSaved(person))
                router.goToSuccess(person.name)
            }
        )

    }

    private fun fetchPerson() {
        interactor.fetchPerson(
            onPreExecute = {
                view.processPageState(PersonState.Loading)
            },
            onPostExecute = { person->
                view.processPageState(PersonState.Data(person))
            })
    }

    private fun release(){
        interactor.release()
    }

}

interface PersonPresenterContract{
    fun processIntent(personIntent: PersonEvent)
}
