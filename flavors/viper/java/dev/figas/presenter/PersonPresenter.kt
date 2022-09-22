package dev.figas.presenter

import dev.figas.interactor.MainInteractorContract
import dev.figas.route.MainRouterContract
import dev.figas.view.PersonView

class PersonPresenter(private val view : PersonView,
                      private val router: MainRouterContract,
                      private val interactor: MainInteractorContract) : PersonPresenterContract {

    override fun injectPerson(name : String) {
        interactor.injectPerson(name,
            onPreExecute = {
                view.showLoading()
            },
            onPostExecute = { person->
                view.hideLoading()
                router.goToSuccess(person.name)
            }
        )

    }

    override fun fetchPerson() {
        interactor.fetchPerson(
            onPreExecute = {
                view.showLoading()
            },
            onPostExecute = { person->
                view.hideLoading()
                view.showPersonName(person.name)
            })
    }

    override fun release(){
        interactor.release()
    }

}

interface PersonPresenterContract{
    fun injectPerson(name: String)
    fun fetchPerson()
    fun release()
}
