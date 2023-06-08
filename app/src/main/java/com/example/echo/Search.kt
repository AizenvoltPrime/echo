package com.example.echo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import android.widget.EditText
import android.view.View

class Search : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        val searchButton = findViewById<MaterialButton>(R.id.search_btn)
        val searchBox = findViewById<EditText>(R.id.search_song)

        searchButton.setOnClickListener {
            searchButton.visibility = View.GONE
            searchBox.visibility = View.VISIBLE
        }
    }
}

