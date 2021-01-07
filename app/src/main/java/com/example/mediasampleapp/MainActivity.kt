package com.example.mediasampleapp

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.mediasampleapp.databinding.ActivityMainBinding
import java.io.IOException

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = DataBindingUtil.setContentView(this,R.layout.activity_main)
        player = MediaPlayer()
        val mediaFileUriStr = "android.resource://${packageName}/${R.raw.dust}"
        val mediaFileUri: Uri = Uri.parse(mediaFileUriStr)

        try {
            player?.apply{
                setDataSource(applicationContext,mediaFileUri)
                setOnPreparedListener(PlayerPreparedListener())
                setOnCompletionListener(PlayerCompletionListener())
                prepareAsync()
            }
        }catch (ex: IllegalArgumentException){
            Log.d("メディアプレーヤー準備時の例外発生", ex.toString())
        }
        catch (ex: IOException){
            Log.d("メディアプレーヤー準備時の例外発生",ex.toString())
        }

        onPlayButtonClick()
    }

    private inner class PlayerPreparedListener : MediaPlayer.OnPreparedListener{
        override fun onPrepared(mp: MediaPlayer?) {
            binding.apply {
                btPlay.isEnabled = true
                btBack.isEnabled = true
                btForward.isEnabled = true
            }
        }
    }

    private inner class PlayerCompletionListener : MediaPlayer.OnCompletionListener{
        override fun onCompletion(mp: MediaPlayer?) {
            binding.btPlay.setText(R.string.bt_play_play)
        }
    }

    fun onPlayButtonClick(){
        binding.btPlay.setOnClickListener{
            player?.let {
                if (it.isPlaying) {
                    it.pause()
                binding.btPlay.setText(R.string.bt_play_play)
                } else {
                    it.start()
                    binding.btPlay.setText(R.string.bt_play_pause)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        player?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
            player = null
            }
    }
}