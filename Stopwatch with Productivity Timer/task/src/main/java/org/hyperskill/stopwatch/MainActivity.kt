package org.hyperskill.stopwatch

import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
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
import androidx.core.app.NotificationCompat
import org.hyperskill.stopwatch.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var count = 0
    private val handler = Handler(Looper.getMainLooper())
    private var limit: Int? = null

    private val channelId = "org.hyperskill"
    private val name = "Stopwatch"

    @RequiresApi(Build.VERSION_CODES.N)
    private val importance = NotificationManager.IMPORTANCE_HIGH

    @RequiresApi(Build.VERSION_CODES.O)
    private val channel = NotificationChannel(channelId, name, importance)

    private val notificationBuilder = NotificationCompat.Builder(this, channelId)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Stopwatch")
        .setContentText("Time exceeded")
        .setOnlyAlertOnce(true)


    private lateinit var notificationManager: NotificationManager
    private lateinit var notification: Notification

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Register the channel with the system
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        notification = notificationBuilder.build().apply {
            flags = flags or Notification.FLAG_INSISTENT
        }

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
            limit?.let {
                if (it in 1 until count) {
                    binding.textView.setTextColor(Color.RED)
                    notificationManager.notify(
                        393939,
                        notification
                    )
                }
            }
            val color = Random.nextInt()
            binding.progressBar.indeterminateTintList = ColorStateList.valueOf(color)
            binding.textView.text =
                "${String.format("%02d", count / 60)}:${String.format("%02d", count % 60)}"

            handler.postDelayed(this, 1000)
        }
    }
}
