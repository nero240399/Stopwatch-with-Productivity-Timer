package org.hyperskill.stopwatch

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.hyperskill.stopwatch.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var count = 0
    private val handler = Handler(Looper.getMainLooper())
    private var limit: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.startButton.setOnClickListener {
            handler.removeCallbacks(startCounting)
            binding.progressBar.visibility = View.VISIBLE
            handler.postDelayed(startCounting, 1000)
            binding.settingsButton.isEnabled = false
        }
        binding.resetButton.setOnClickListener {
            handler.removeCallbacks(startCounting)
            count = 0
            binding.textView.text = "00:00"
            binding.progressBar.visibility = View.INVISIBLE
            binding.settingsButton.isEnabled = true
            binding.textView.setTextColor(Color.BLACK)
        }
        binding.settingsButton.setOnClickListener {
            val contentView =
                layoutInflater.inflate(R.layout.dialog_edit_text, binding.mainViewGroup, false)
            AlertDialog.Builder(this)
                .setTitle(R.string.dialog_title)
                .setView(contentView)
                .setPositiveButton(R.string.ok) { _, _ ->
                    val editText = contentView.findViewById<EditText>(R.id.upperLimitEditText)
                    limit = editText.text.toString().toIntOrNull()
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
        }
    }

    private val startCounting = object : Runnable {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun run() {
            count++
            val color = Random.nextInt()
            binding.progressBar.indeterminateTintList = ColorStateList.valueOf(color)
            binding.textView.text =
                "${String.format("%02d", count / 60)}:${String.format("%02d", count % 60)}"
            handler.postDelayed(this, 1000)
            limit?.let {
                if (count > it) {
                    binding.textView.setTextColor(Color.RED)
                }
            }
        }
    }
}