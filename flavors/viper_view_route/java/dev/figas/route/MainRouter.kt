package dev.figas.route

import dev.figas.view.MainActivity
import dev.figas.view.SuccessActivity

class MainRouter(private val activity: MainActivity) : MainRouterContract {

    override fun goToSuccess(name: String) {
        SuccessActivity.launch(activity, name)
    }

}

interface MainRouterContract{

    fun goToSuccess(name : String)

}