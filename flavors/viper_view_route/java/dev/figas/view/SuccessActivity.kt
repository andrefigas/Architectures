package dev.figas.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import dev.figas.R

class SuccessActivity : AppCompatActivity(){

    companion object {
        private const val KEY_NAME = "name"

        fun launch(context: Context, name: String) {
            val intent = Intent(context, SuccessActivity::class.java)
            intent.putExtra(KEY_NAME, name)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)
        findViewById<TextView>(R.id.success_message).text =
            getString(R.string.submit_success_message, intent.getStringExtra(KEY_NAME))
    }

}