package dev.figas.data.repositories

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import dev.figas.data.mappers.PersonMapper
import dev.figas.data.models.PersonDataModel
import dev.figas.domain.models.Person
import dev.figas.domain.repositories.PersonRepoContract

class PersonRepository( context: Context, private val mapper: PersonMapper) : PersonRepoContract{

    companion object{
        const val PREFS = "prefs"
        const val PREF_KEY_NAME = "name"
        const val DELAY = 3000L
    }

    private val preferences : SharedPreferences

    init{
        preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    }

    override fun providePerson(onPreExecute : ()->Unit, onPostExecute : (Person)->Unit) : AsyncTask<Void, Void, Person> {
        return BaseAsyncTask<Void, Person>(onPreExecute,{
            Thread.sleep(DELAY)
            mapper.toPerson(PersonDataModel(preferences.getString(PREF_KEY_NAME,"") as String))
        }, onPostExecute).execute()

    }

    override fun injectPerson(person : Person, onPreExecute : ()->Unit, onPostExecute : (Person)->Unit): AsyncTask<String, Void, Person> {
        return BaseAsyncTask<String , Person>(onPreExecute,{ name ->
            Thread.sleep(DELAY)
            val person = PersonDataModel(name as String)
            preferences.edit().putString(PREF_KEY_NAME, person.name).commit()
            mapper.toPerson(person)
        }, onPostExecute).execute(person.name)
    }
}

class BaseAsyncTask<Params,T>(private val onPreExecute : ()->Unit?,
                              private val doInBackground : (Params?)-> T,
                              private val onPostExecute : (T)->Unit) :
    AsyncTask<Params, Void, T>() {
    override fun onPreExecute() {
        super.onPreExecute()
        onPreExecute.invoke()
    }

    override fun doInBackground(vararg params: Params): T =
        doInBackground.invoke(if(params.isEmpty()) null else params[0])

    override fun onPostExecute(result: T) {
        super.onPostExecute(result)
        onPostExecute.invoke(result)
    }

}