package com.android.marinmusicplayer

import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import java.io.IOException

class MarinMusicPLayerActivity : AppCompatActivity() {

    private lateinit var tvTime: TextView
    private lateinit var tvDuration: TextView
    private lateinit var musicName: TextView
    private lateinit var artistName: TextView
    private lateinit var seekBarTime: SeekBar
    private lateinit var seekBarVolume: SeekBar
    private lateinit var btnPlay: Button

    var musicPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marin_music_player)

        val song = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("song", Song::class.java)
        } else {
            intent.getSerializableExtra("song") as Song?
        }

        tvTime = findViewById(R.id.tvTime)
        tvDuration = findViewById(R.id.tvDuration)
        musicName = findViewById(R.id.musicName)
        artistName = findViewById(R.id.artistName)
        seekBarTime = findViewById(R.id.seekBarTime)
        seekBarVolume = findViewById(R.id.seekBarVolume)
        btnPlay = findViewById(R.id.btnPlay)

        musicName.text = song!!.title
        artistName.text = song.artist
        musicPlayer = MediaPlayer()
        try {
            musicPlayer!!.setDataSource(song.path)
            musicPlayer!!.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        musicPlayer!!.isLooping = true
//        sa permet a l'utilisateur de bouger la position de la barre d'ecoute
        musicPlayer!!.seekTo(0)
//        pour regler le volume : on le fix a 50%
        musicPlayer!!.setVolume(0.5f, 0.5f)
        // la on calcule la duration du titre
        val duration = millisecondsToString(musicPlayer!!.duration)
// ici on affecte
        tvDuration.text = duration
        seekBarVolume.progress = 50
        seekBarVolume.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // quand l'utilisateur change la position du volume , on le recupere et on le convertit en volume , on prend le progresse et on le divise par :
                // 100 car le 100% du volume est a  1 , et le 100% du progresse c'est 100 ,je divise donc le total du progress et je le divise par 100 pour etre compatible avec le 1 du volume
                val volume = progress / 100f
                musicPlayer!!.setVolume(volume, volume)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        seekBarTime.max = musicPlayer!!.duration
        seekBarTime.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // si le deplacement et fait par l'user
                if (fromUser) {
                    musicPlayer!!.seekTo(progress)
                    seekBar?.progress = progress
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        btnPlay.setOnClickListener {
            if (musicPlayer!!.isPlaying) {
                musicPlayer!!.pause()
                btnPlay.setBackgroundResource(R.drawable.ic_play_btn)

            } else {
                musicPlayer!!.start()
                btnPlay.setBackgroundResource(R.drawable.ic_pause)
            }
        }
        // je vais faire un procesuse qui fonctionnera automatique pour avancer les minutes
        Thread {
          while  (musicPlayer != null){
              if (musicPlayer!!.isPlaying){
                  try {
                      val current = musicPlayer!!.currentPosition.toDouble()
                      val elapsedTime = millisecondsToString(current.toInt())
                      runOnUiThread{
                          tvTime.text = elapsedTime
                          seekBarTime.progress = current.toInt()
                      }
                      Thread.sleep(1000)
                  }catch (e: InterruptedException){
                      break
                  }
              }
          }
        }.start()
    }


    override fun onPause(){
        super.onPause()
        musicPlayer!!.stop()
    }
    // la duration veut des string , moi j'ai besoin de minutage je fait donc une fonction pour modifier les parrament que duration attemp

    private fun millisecondsToString(time: Int): String{
//      var elapsedTime: String? = ""   c'est le temps passer donc zero au debut donc retourne une chaine vide
        var elapsedTime: String? = ""
        // on divise les minutes en secondes
        val minutes = time / 1000 / 60
        //on divise le temps en minutes et apres on prend le reste de la division avec le modulo
        val seconds = time / 1000 % 60
//        pour avoir le format 00:00 donc minute secondes je concaténe la variable minute avec les seconde
        elapsedTime = "$minutes:"
        // si le nombre de seconde et inferrireure a 10 on ajoute zero avant , pour avoir un format correcte si c'est deux minutes et trente seconde le format est 02:30 et pas 22:30
        if (seconds < 10){
            elapsedTime += "0"
        }
        // et apres je concatene en rajoutant le nombre de seconde pour garder la méme logique si une musique a une minutes et 7 secondes nous voulons 01:07 et pas 01:7
        elapsedTime += seconds
        // et a la fin on retourne le elapsedTime
        return elapsedTime
    }
}