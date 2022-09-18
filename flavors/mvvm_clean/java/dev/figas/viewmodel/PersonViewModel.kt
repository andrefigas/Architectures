package dev.figas.viewmodel

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.figas.domain.models.Person
import dev.figas.domain.usecases.GetPersonUseCase
import dev.figas.domain.usecases.UpdatePersonUseCase

class PersonViewModel(private val getPersonUseCase: GetPersonUseCase,
                      private val updatePersonUseCase: UpdatePersonUseCase) : ViewModel() {

    private val requests = mutableListOf<AsyncTask<*, *, *>>()
    private val _data: MutableLiveData<Person> = MutableLiveData<Person>()
    val data: LiveData<Person> = _data

    private val _insert: MutableLiveData<Person> = MutableLiveData<Person>()
    val insert: LiveData<Person> = _insert

    @SuppressLint("StaticFieldLeak")
    fun injectPerson(name: String) {
        requests.add(
            updatePersonUseCase.execute(Person(name), onPreExecute = {
                //do nothing
            }, onPostExecute = { person ->
                _insert.value = person
            })
        )

    }

    @SuppressLint("StaticFieldLeak")
    fun fetchPerson() {
        requests.add(
            getPersonUseCase.execute(onPreExecute = {
                //do nothing
            }, onPostExecute = { person ->
                _data.value = person
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
