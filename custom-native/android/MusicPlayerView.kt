package com.anonymous.podcastapp

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import java.io.IOException

class MusicPlayerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var playPauseButton: ImageButton
    private lateinit var rewindButton: ImageButton
    private lateinit var forwardButton: ImageButton
    private lateinit var playbackSeekBar: SeekBar
    private lateinit var volumeSeekBar: SeekBar
    private lateinit var currentTimeText: TextView
    private lateinit var remainingTimeText: TextView

    private val handler = Handler(Looper.getMainLooper())
    private var currentSourceUrl: String? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.music_player_view, this, true)
        // Temporary background for debugging visibility
        // setBackgroundColor(Color.MAGENTA)
        Log.d("MusicPlayerView", "Inflated layout, should be visible.")

        // Initialize UI components
        playPauseButton = findViewById(R.id.playPauseButton)
        rewindButton = findViewById(R.id.rewindButton)
        forwardButton = findViewById(R.id.forwardButton)
        playbackSeekBar = findViewById(R.id.seekBar)
        volumeSeekBar = findViewById(R.id.volumeBar)
        currentTimeText = findViewById(R.id.currentTimeText)
        remainingTimeText = findViewById(R.id.remainingTimeText)

        setupListeners()
        initializeMediaPlayer()
    }

    private fun initializeMediaPlayer() {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setOnPreparedListener {
                Log.d("MusicPlayerView", "MediaPlayer prepared. Duration: ${it.duration}")
                playbackSeekBar.max = it.duration
                updateProgress()
                playPauseButton.setImageResource(android.R.drawable.ic_media_play) // Set to play initially
                playPauseButton.isEnabled = true
            }
            setOnCompletionListener {
                Log.d("MusicPlayerView", "MediaPlayer playback completed.")
                playPauseButton.setImageResource(android.R.drawable.ic_media_play)
                playbackSeekBar.progress = 0
                // Optionally reset to start or allow re-play
            }
            setOnErrorListener { mp, what, extra ->
                Log.e("MusicPlayerView", "MediaPlayer Error: what: $what, extra: $extra")
                // Handle errors, e.g., by resetting the player or showing a message
                playPauseButton.setImageResource(android.R.drawable.ic_media_play)
                playPauseButton.isEnabled = false // Disable if source is bad
                true // Error was handled
            }
        }
        Log.d("MusicPlayerView", "MediaPlayer initialized.")
    }

    fun setSourceUrl(url: String?) {
        if (url == null || url == currentSourceUrl) {
            Log.d("MusicPlayerView", "setSourceUrl: URL is null or unchanged: $url")
            return
        }
        Log.d("MusicPlayerView", "setSourceUrl called with: $url")
        currentSourceUrl = url
        mediaPlayer?.apply {
            try {
                reset() // Reset if already used
                setDataSource(context, Uri.parse(url))
                prepareAsync() // Prepare asynchronously
                playPauseButton.isEnabled = false // Disable until prepared
                playPauseButton.setImageResource(android.R.drawable.ic_media_play) // Show play icon
                Log.d("MusicPlayerView", "MediaPlayer data source set and preparing: $url")
            } catch (e: IOException) {
                Log.e("MusicPlayerView", "Error setting data source", e)
                // Handle error, e.g., show a toast or update UI
            } catch (e: IllegalStateException) {
                Log.e("MusicPlayerView", "Error setting data source - illegal state", e)
            }
        }
    }


    private fun setupListeners() {
        playPauseButton.setOnClickListener {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.pause()
                    playPauseButton.setImageResource(android.R.drawable.ic_media_play)
                    Log.d("MusicPlayerView", "MediaPlayer paused.")
                } else {
                    it.start()
                    playPauseButton.setImageResource(android.R.drawable.ic_media_pause)
                    Log.d("MusicPlayerView", "MediaPlayer started.")
                    updateProgress() // Start updating progress
                }
            }
        }

        rewindButton.setOnClickListener {
            mediaPlayer?.let {
                val newPosition = Math.max(0, it.currentPosition - 10000) // Rewind 10 seconds
                it.seekTo(newPosition)
                playbackSeekBar.progress = newPosition
                Log.d("MusicPlayerView", "MediaPlayer rewind to: $newPosition")
            }
        }

        forwardButton.setOnClickListener {
            mediaPlayer?.let {
                val newPosition = Math.min(it.duration, it.currentPosition + 10000) // Forward 10 seconds
                it.seekTo(newPosition)
                playbackSeekBar.progress = newPosition
                Log.d("MusicPlayerView", "MediaPlayer forward to: $newPosition")
            }
        }

        playbackSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                    updateTimestamps(progress, mediaPlayer?.duration ?: 0)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val volume = progress / 100f // SeekBar max is usually 100
                    mediaPlayer?.setVolume(volume, volume)
                    Log.d("MusicPlayerView", "Volume set to: $volume")
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        // Initialize volume bar to a default value (e.g., 50%)
        volumeSeekBar.progress = 50
        mediaPlayer?.setVolume(0.5f, 0.5f)
    }

    private fun updateProgress() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                val currentPosition = it.currentPosition
                playbackSeekBar.progress = currentPosition
                updateTimestamps(currentPosition, it.duration)
                handler.postDelayed(::updateProgress, 1000) // Update every second
            }
        }
    }

    private fun updateTimestamps(currentMs: Int, totalMs: Int) {
        val currentSeconds = (currentMs / 1000) % 60
        val currentMinutes = (currentMs / (1000 * 60)) % 60
        currentTimeText.text = String.format("%02d:%02d", currentMinutes, currentSeconds)

        val remainingMs = totalMs - currentMs
        val remainingSeconds = (remainingMs / 1000) % 60
        val remainingMinutes = (remainingMs / (1000 * 60)) % 60
        remainingTimeText.text = String.format("%02d:%02d", remainingMinutes, remainingSeconds)
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.d("MusicPlayerView", "onDetachedFromWindow: Releasing MediaPlayer.")
        mediaPlayer?.release()
        mediaPlayer = null
        handler.removeCallbacksAndMessages(null) // Remove any pending updates
    }

    // Call this method when the view is about to be destroyed, e.g. from the manager
    fun onCleanup() {
        Log.d("MusicPlayerView", "onCleanup: Releasing MediaPlayer.")
        mediaPlayer?.release()
        mediaPlayer = null
        handler.removeCallbacksAndMessages(null)
    }
}
