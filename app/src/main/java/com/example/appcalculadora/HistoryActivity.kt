package com.example.appcalculadora

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class HistoryActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)


        val historyList = intent.getStringArrayListExtra("history") ?: arrayListOf()
        Log.d("HistoryActivity", "Historial recibido: $historyList")

        val listView = findViewById<ListView>(R.id.historyListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, historyList)
        listView.adapter = adapter


        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedOperation = adapter.getItem(position)
            val resultIntent = Intent()
            resultIntent.putExtra("selectedOperation", selectedOperation)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }


    fun volverCalculadora(view: View) {
        finish()
    }
}