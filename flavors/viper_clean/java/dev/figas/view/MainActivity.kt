package dev.figas.view

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import dev.figas.R
import dev.figas.data.mappers.PersonMapper
import dev.figas.data.repositories.PersonRepository
import dev.figas.domain.repositories.PersonRepoContract
import dev.figas.domain.usecases.GetPersonUseCase
import dev.figas.domain.usecases.UpdatePersonUseCase
import dev.figas.interactor.MainInteractor
import dev.figas.presenter.PersonPresenter
import dev.figas.presenter.PersonPresenterContract
import dev.figas.route.MainRouter

class MainActivity : AppCompatActivity(), PersonView {

    private lateinit var presenter: PersonPresenterContract
    private lateinit var nameEt: EditText
    private lateinit var progressPb: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repo : PersonRepoContract = PersonRepository(this, PersonMapper())
        presenter = PersonPresenter(this,
            MainRouter(this),
            MainInteractor(GetPersonUseCase(repo),
                UpdatePersonUseCase(repo))
        )

        setContentView(R.layout.activity_main)
        setupUI()

        presenter.fetchPerson()
    }

    private fun setupUI() {
        nameEt = findViewById(R.id.name_et)
        progressPb = findViewById(R.id.progress_pb)
        findViewById<View>(R.id.submit_bt).setOnClickListener {
            presenter.injectPerson(nameEt.text.toString())
        }
    }

    override fun showPersonName(name: String) {
        nameEt.setText(name)
    }

    override fun showLoading() {
        progressPb.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressPb.visibility = View.INVISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.release()
    }
}

interface PersonView {
    fun hideLoading()
    fun showLoading()
    fun showPersonName(name: String)

}
