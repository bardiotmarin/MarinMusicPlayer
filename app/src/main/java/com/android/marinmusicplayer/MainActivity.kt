package com.android.marinmusicplayer
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {
    private lateinit var lvSongs:ListView

    var songsArrayList = ArrayList<Song>()


    companion object {
        const val REQUEST_PERMISSION = 99
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lvSongs = findViewById(R.id.lvSongs)
        lvSongs.adapter = SongAdapter(this , songsArrayList)
// quand l'aplication se lance ajouter un pop up ,
// verification des permision d'utilisation des données dans le telephone
// le premier c'est pour verifier si oui ou non l'user a accepter la permission
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
           // ici c'est pour faire la demande a l'user : on va lui donner la liste de permission a passer , il n/'y a pas vraiment de liste mais request permission a besoin d'une liste pour fonctionne donc je lui passe une liste , je cree aussi une costante ligne 23 avec le REQUEST_PERMISSION qui et une constante que j'ai crée avec le compagnon object
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION)
            return
        }
        else{
            getSongs()
        }
    }

    //onRequestPermissionsResult utilise la liste des permition  et il utilise  grantResults pour verifier les permission accepter ou non
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION){
            //ici on va verifier si mon request code et egal a ma REQUEST_PERMISSION , si la permition et aussi accorder
            //on va appeler getsongs
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getSongs()
            }
        }

    }

    //
    private fun getSongs() {

    }
}