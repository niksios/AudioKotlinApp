package com.niks.audiokotlinapp

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var seekbar: SeekBar

    private lateinit var tvPlayed: TextView

    private lateinit var tvDue: TextView

    private var mediaPlayer: MediaPlayer? = null

    private lateinit var runnable: Runnable

    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handler = Handler(Looper.getMainLooper())

        seekbar = findViewById(R.id.sbClapping)

        tvPlayed = findViewById(R.id.tvPlayed)

        tvDue = findViewById(R.id.tvDue)

        //val btnClap = findViewById<Button>(R.id.btnClap)

        /*btnClap.setOnClickListener {
            mediaPlayer.start()
        }*/

        val fabPlay = findViewById<FloatingActionButton>(R.id.fabPlay)
        val fabPause = findViewById<FloatingActionButton>(R.id.fabPause)
        val fabStop = findViewById<FloatingActionButton>(R.id.fabStop)

        fabPlay.setOnClickListener {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.applauding)
                initializeSeekBar()
            }
            mediaPlayer?.start()

        }

        fabPause.setOnClickListener {
            mediaPlayer?.pause()
        }

        fabStop.setOnClickListener {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
            handler.removeCallbacks(runnable)

            seekbar.progress = 0
            tvPlayed.text = "-"
            tvDue.text = "-"
        }
    }

    private fun initializeSeekBar() {

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        //val tvPlayed = findViewById<TextView>(R.id.tvPlayed)
        //val tvDue = findViewById<TextView>(R.id.tvDue)

        seekbar.max = mediaPlayer!!.duration
        runnable = Runnable {
            seekbar.progress = mediaPlayer!!.currentPosition
            //played time
            val playedTime = mediaPlayer!!.currentPosition / 1000
            tvPlayed.text = "$playedTime sec"

            //remaining/due time
            val duration = mediaPlayer!!.duration / 1000
            val dueTime = duration - playedTime
            tvDue.text = "$dueTime sec"

            handler.postDelayed(runnable, 1000)
        }

        handler.postDelayed(runnable, 1000)

    }

}