package com.example.lab_week_08

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.lab_week_08.worker.FirstWorker
import com.example.lab_week_08.worker.SecondWorker
import com.example.lab_week_08.worker.ThirdWorker

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ✅ Minta izin notifikasi untuk Android 13+
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }


        val btnStart = findViewById<Button>(R.id.btnStartWork)

        btnStart.setOnClickListener {
            startWorkChain()
        }
    }

    private fun startWorkChain() {
        // Input untuk worker pertama
        val inputData = Data.Builder()
            .putString(FirstWorker.INPUT_DATA_ID, "001")
            .build()

        // Worker pertama
        val firstWorker = OneTimeWorkRequestBuilder<FirstWorker>()
            .setInputData(inputData)
            .build()

        // Worker kedua
        val secondWorker = OneTimeWorkRequestBuilder<SecondWorker>()
            .build()

        // Worker ketiga
        val thirdWorker = OneTimeWorkRequestBuilder<ThirdWorker>()
            .build()

        // Jalankan berantai: 1 → 2 → 3
        WorkManager.getInstance(this)
            .beginWith(firstWorker)
            .then(secondWorker)
            .then(thirdWorker)
            .enqueue()

        Toast.makeText(this, "Work chain started!", Toast.LENGTH_SHORT).show()
    }
}
