package dev.figas.controller

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import dev.figas.R
import dev.figas.data.mappers.PersonMapper
import dev.figas.data.repositories.PersonRepository
import dev.figas.domain.models.Person
import dev.figas.domain.repositories.PersonRepoContract
import dev.figas.domain.usecases.GetPersonUseCase
import dev.figas.domain.usecases.UpdatePersonUseCase

class MainActivity : AppCompatActivity() {

    private lateinit var getPersonUseCase: GetPersonUseCase
    private lateinit var updatePersonUseCase: UpdatePersonUseCase
    private lateinit var nameEt: EditText
    private lateinit var progressPb: ProgressBar

    private val requests = mutableListOf<AsyncTask<*, *, *>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repo : PersonRepoContract = PersonRepository(this, PersonMapper())
        getPersonUseCase = GetPersonUseCase(repo)
        updatePersonUseCase = UpdatePersonUseCase(repo)

        setContentView(R.layout.activity_main)
        setupUI()
        fetchPerson()
    }

    private fun setupUI() {
        nameEt = findViewById(R.id.name_et)
        progressPb = findViewById(R.id.progress_pb)
        findViewById<View>(R.id.submit_bt).setOnClickListener {
            injectPerson()
        }
    }

    @SuppressLint("StaticFieldLeak")
    private fun injectPerson() {
        requests.add(
            updatePersonUseCase.execute(
                Person(nameEt.text.toString()),
                onPreExecute = {
                    showLoading()
                },
                onPostExecute = { person ->
                    hideLoading()
                    onSavePerson(person)
                }
            )

        )

    }

    @SuppressLint("StaticFieldLeak")
    private fun fetchPerson() {
        requests.add(
            getPersonUseCase.execute(
                onPreExecute = {
                    showLoading()
                },
                onPostExecute = { person ->
                    hideLoading()
                    onReceivePerson(person)
                }
            )
        )
    }

    private fun onReceivePerson(person: Person) {
        nameEt.setText(person.name)
    }

    private fun onSavePerson(person: Person) {
        Toast.makeText(
            this,
            getString(R.string.submit_success_message, person.name),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showLoading() {
        progressPb.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressPb.visibility = View.INVISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        requests.forEach {
            it.cancel(true)
        }
    }
}
