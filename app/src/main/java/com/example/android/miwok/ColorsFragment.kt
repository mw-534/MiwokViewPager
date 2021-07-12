package com.example.android.miwok

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.annotation.RequiresApi

/**
 * A simple [Fragment] subclass.
 */
class ColorsFragment : Fragment() {

    /** Handles playback of all the sound files. */
    private var mMediaPlayer: MediaPlayer? = null

    /** Handles audio focus when playing a sound file. */
    private var mAudioManager : AudioManager? = null

    /**
     * This listener gets triggered whenever the audio focus changes.
     * (i.e. we gain or lose the audio focus because of another app or device).
     */
    private val mOnAudioFocusChangeListener = AudioManager.OnAudioFocusChangeListener{ focusChange ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT,
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file.
                // That way, we can play the word from the beginning when we resume playback.
                mMediaPlayer?.pause()
                mMediaPlayer?.seekTo(0)
            }
            AudioManager.AUDIOFOCUS_GAIN -> {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mMediaPlayer?.start()
            }
            AudioManager.AUDIOFOCUS_LOSS -> {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer()
            }
        }
    }

    /**
     * This listener gets triggered when the [MediaPlayer] has completed playing the audio file.
     * We use a function type instead of an anonymous class because the listener will be
     * assigned multiple times and hence we save memory using this approach.
     */
    private val mCompletionListener = MediaPlayer.OnCompletionListener { mp -> releaseMediaPlayer() }

    // Usage of run extension method is not necessary here because Builder supports the builder pattern
    // itself but it might be good practice because run extension method can be used in general for
    // this type of pattern. run turns extended object in its block to this instead of passing it as
    // an input argument because it is an extension method and these always have this as the object
    // they are called on. run has a generic return type so it will return the result of the last
    // call in its block.
    @RequiresApi(26)
    private val mAudioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT).run {
        setAudioAttributes(AudioAttributes.Builder().run {
            setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            build()
        })
        setWillPauseWhenDucked(true)
        setAcceptsDelayedFocusGain(false)
        setOnAudioFocusChangeListener(mOnAudioFocusChangeListener)
        build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.word_list, container, false)

        // Create and setup the [AudioManager] to request audio focus.
        mAudioManager = activity?.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val words = ArrayList<Word>()
        words.add(Word("red", "weṭeṭṭi", R.raw.color_red, R.drawable.color_red))
        words.add(Word("mustard yellow", "chiwiiṭә", R.raw.color_mustard_yellow, R.drawable.color_mustard_yellow))
        words.add(Word("dusty yellow", "ṭopiisә", R.raw.color_dusty_yellow, R.drawable.color_dusty_yellow))
        words.add(Word("green", "chokokki", R.raw.color_green, R.drawable.color_green))
        words.add(Word("brown", "ṭakaakki", R.raw.color_brown, R.drawable.color_brown))
        words.add(Word("gray", "ṭopoppi", R.raw.color_gray, R.drawable.color_gray))
        words.add(Word("black", "kululli", R.raw.color_black, R.drawable.color_black))
        words.add(Word("white", "kelelli", R.raw.color_white, R.drawable.color_white))

        // Create an [WordAdapter], whose data source is a list of [Word]s. The
        // adapter knows how to create list items for each item in the list.
        val adapter = WordAdapter(activity!!, words, R.color.category_colors)

        // Find the [ListView] object in the view hierarchy of the [Activity].
        // There should be a [ListView] with the view ID called list, which is declared in the
        // activity_numbers.xml layout file.
        val listView = rootView.findViewById<ListView>(R.id.list)

        // Make the [ListView] use the [WordAdapter] we created above, so that the
        // [ListView] will display list items for each [Word] in the list.
        listView.adapter = adapter

        // Set a click listener to play the audio file when the item is clicked on.
        listView.setOnItemClickListener { parent, view, position, id ->
            // Release MediaPlayer if it currently exists because we are about to play
            // a different sound file.
            releaseMediaPlayer()

            // Get the [Word] object at the given position the user clicked on.
            val word = parent.getItemAtPosition(position) as Word

            Log.v("NumbersActivity", "Current word: $word")

            // Request audio focus in order to play the sound file. The app needs to play a
            // short sound file, so we will request audio focus with a short amount of time
            // with AUDIO_FOCUS_GAIN_TRANSIENT.
            val res : Int?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                res = mAudioManager?.requestAudioFocus(mAudioFocusRequest)
            } else {
                res = mAudioManager?.requestAudioFocus(mOnAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
            }

            if (res == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                // We have audio focus now.

                // Create and setup the [MediaPlayer] for the audio resource associated with the current word.
                mMediaPlayer = MediaPlayer.create(activity, word.audioResourceId)

                // Start the audio file.
                mMediaPlayer?.start()

                // Setup a listener on the media player, so that we can stop and release the
                // media player once the sound has finished playing.
                mMediaPlayer?.setOnCompletionListener(mCompletionListener)
            }
        }

        return rootView
    }

    override fun onStop() {
        super.onStop()

        // When the activity is stopped release the MediaPlayer resource because
        // we won't be playing any more sounds.
        releaseMediaPlayer()
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private fun releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer?.release()

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the OnAudioFocusChangeListener so we don't get anymore callbacks.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mAudioManager?.abandonAudioFocusRequest(mAudioFocusRequest)
            } else {
                mAudioManager?.abandonAudioFocus(mOnAudioFocusChangeListener)
            }
        }
    }

}