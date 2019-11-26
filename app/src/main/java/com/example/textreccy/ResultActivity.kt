package com.example.textreccy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class ResultActivity : AppCompatActivity() {

    lateinit var backButton : Button
    lateinit var resultTextView : TextView
    var resultText : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        resultTextView = findViewById(R.id.result_textview)
        backButton = findViewById(R.id.back_button)
        resultText = this.intent.getStringExtra(LCOTextRecognition.RESULT_TEXT)

        resultTextView.text = (resultText)

        backButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v : View) {
                finish()
            }
        })
    }
}
