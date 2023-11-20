package org.hyperskill.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.hyperskill.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var count = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.startButton.setOnClickListener {
            handler.removeCallbacks(startCounting)
            handler.postDelayed(startCounting, 1000)
        }
        binding.resetButton.setOnClickListener {
            handler.removeCallbacks(startCounting)
            count = 0
            binding.textView.text = "00:00"
        }
    }

    private val startCounting = object : Runnable {
        override fun run() {
            count++
            binding.textView.text = "${String.format("%02d", count / 60)}:${String.format("%02d", count % 60)}"
            handler.postDelayed(this, 1000)
        }
    }
}