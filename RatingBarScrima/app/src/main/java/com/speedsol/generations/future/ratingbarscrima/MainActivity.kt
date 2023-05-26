package com.speedsol.generations.future.ratingbarscrima

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    internal lateinit var textView: TextView
    internal lateinit var button: Button
    internal lateinit var ratingBar: RatingBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById<View>(R.id.tvtext) as TextView
        button = findViewById<View>(R.id.btnsubmit) as Button
        ratingBar = findViewById<View>(R.id.ratingbar) as RatingBar


    }

    fun onSubmit(view: View) {
        val ratingValue = ratingBar.rating
        if (ratingValue < 2) {
            textView.text = "Rating : $ratingValue\n Very Poor"
        } else if (ratingValue >= 2 && ratingValue <= 3) {
            textView.text = "Rating : $ratingValue\n Poor"
        } else if (ratingValue >= 3 && ratingValue <= 4) {
            textView.text = "Rating : $ratingValue\n Good"
        } else if (ratingValue >= 4 && ratingValue <= 5) {
            textView.text = "Rating : $ratingValue\n Excellent"
        } else {
            textView.text = "Please Rate Our App"
        }
    }
}
