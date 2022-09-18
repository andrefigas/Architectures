package dev.figas.viewmodel

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.figas.model.Person
import dev.figas.model.PersonModelContract

class PersonViewModel(val model: PersonModelContract) : ViewModel() {

    private val requests = mutableListOf<AsyncTask<*, *, *>>()
    private val _data: MutableLiveData<Person> = MutableLiveData<Person>()
    val data: LiveData<Person> = _data

    private val _insert: MutableLiveData<Person> = MutableLiveData<Person>()
    val insert: LiveData<Person> = _insert

    @SuppressLint("StaticFieldLeak")
    fun injectPerson(name: String) {
        requests.add(
            model.injectPerson(Person(name), onPreExecute = {
                //do nothing
            }, onPostExecute = { person ->
                _insert.value = person
            })
        )

    }

    @SuppressLint("StaticFieldLeak")
    fun fetchPerson() {
        requests.add(
            model.providePerson(onPreExecute = {
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
