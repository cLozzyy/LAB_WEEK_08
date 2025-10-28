package com.example.lab_week_08

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.map_mid_term.worker.FirstWorker
import com.example.map_mid_term.worker.SecondWorker

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStart = findViewById<Button>(R.id.btnStartWork)

        btnStart.setOnClickListener {
            startWorkChain()
        }
    }

    private fun startWorkChain() {
        // Kirim data awal
        val inputData = Data.Builder()
            .putString(FirstWorker.INPUT_DATA_ID, "Job_001")
            .build()

        // Worker pertama
        val firstWork = OneTimeWorkRequestBuilder<FirstWorker>()
            .setInputData(inputData)
            .build()

        // Worker kedua
        val secondWork = OneTimeWorkRequestBuilder<SecondWorker>().build()

        // Jalankan secara berurutan
        WorkManager.getInstance(this)
            .beginWith(firstWork)
            .then(secondWork)
            .enqueue()

        Toast.makeText(this, "WorkManager started!", Toast.LENGTH_SHORT).show()
    }
}