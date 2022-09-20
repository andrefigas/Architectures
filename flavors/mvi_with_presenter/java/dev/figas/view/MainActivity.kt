package dev.figas.view

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.figas.R
import dev.figas.intent.PersonIntent
import dev.figas.model.PersonModel
import dev.figas.presenter.PersonPresenter
import dev.figas.presenter.PersonPresenterContract
import dev.figas.vieweffect.PersonEffect
import dev.figas.viewstate.PersonState

class MainActivity : AppCompatActivity(), PersonView {

    private lateinit var presenter: PersonPresenterContract
    private lateinit var nameEt: EditText
    private lateinit var progressPb: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = PersonPresenter(this, PersonModel(this))

        setContentView(R.layout.activity_main)
        setupUI()

        presenter.processIntent(PersonIntent.OnLoad)
    }

    private fun setupUI() {
        nameEt = findViewById(R.id.name_et)
        progressPb = findViewById(R.id.progress_pb)
        findViewById<View>(R.id.submit_bt).setOnClickListener {
            presenter.processIntent(PersonIntent.OnSubmitClicked(nameEt.text.toString()))
        }
    }

    override fun processEffect(personEffect: PersonEffect){
        when(personEffect){
            is PersonEffect.OnPersonSaved ->{
                hideLoading()
                showSavedPerson(personEffect.person.name)
            }
        }
    }

    override fun processPageState(personState: PersonState){
        when(personState){
            is PersonState.Data ->{
                hideLoading()
                showPersonName(personState.user.name)
            }

            is PersonState.Loading -> {
                showLoading()
            }
        }
    }

    private fun showPersonName(name: String) {
        nameEt.setText(name)
    }

    private fun showSavedPerson(name: String) {
        Toast.makeText(
            this,
            getString(R.string.submit_success_message, name),
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
        presenter.processIntent(PersonIntent.OnRelease)
    }
}

interface PersonView {
    fun processEffect(personEffect: PersonEffect)
    fun processPageState(personState: PersonState)
}
