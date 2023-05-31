package com.android.marinmusicplayer
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.AdapterView
import android.widget.ListView
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    private lateinit var lvSongs:ListView

    private var songsArrayList = ArrayList<Song>()
    private var songsAdapter: SongsAdapter? = null

    companion object {
        const val REQUEST_PERMISSION = 99
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        lvSongs = findViewById(R.id.lvSongs)
        // j'initialise songadapteur
        songsAdapter = SongsAdapter(this, songsArrayList)
        //j'affecte song adapteur a ma listeView
        lvSongs.adapter = songsAdapter
// quand l'aplication se lance ajouter un pop up ,
// verification des permision d'utilisation des données dans le telephone
// le premier c'est pour verifier si oui ou non l'user a accepter la permission
        //  Si des que j'ai besoin d'acceder a un contenue de l'application qui demande des permition ->> this, android.Manifest.permission
        // je verifi si la permition n'a pas été encore accorder -->> != PackageManager.PERMISSION_GRANTED
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
        // SI la permition na pas encore été accorder je demande la permition avec ----> requestPermissions
            //Et je lui passe la liste des permition que je veux avec ---> (this, arrayOf(android.Manifest.permission
            //MAIS j'ajoute aussi un code vérification de réponse avec ----> REQUEST_PERMISSION  <--- pour savoir si il a bien répondu
            // ici c'est pour faire la demande a l'user : on va lui donner la liste de permission a passer , il n/'y a pas vraiment de liste mais request permission a besoin d'une liste pour fonctionne donc je lui passe une liste , je cree aussi une costante ligne 23 avec le REQUEST_PERMISSION qui et une constante que j'ai crée avec le compagnon object
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION)
            return
        }
        else{
            getSongs()
        }
        //   pour swithcher de la liste des musique au player je rajoute le onItemClickListener , je recupere la position du clique , et l'id de l'llement sur lequelle l'utilisateur clique
        lvSongs.onItemClickListener =AdapterView.OnItemClickListener { adapterView, view, position, id ->
            Intent(this@MainActivity, MarinMusicPLayerActivity::class.java).also {
               // quand l'utilisateur clique je recupere l'endroit cliquer dans un item
                // je vais recupere l'item
                // le son et un object donc il ne passera pas par intent car intent attent ( int , char , parcebale , seralisebale )
                // donc je vais modifier ma data classe en serialasible
                val song =songsArrayList[position]
                it.putExtra("song", song)
                startActivity(it)
            }

        }

    }

    //onRequestPermissionsResult utilise la liste des permition  et il utilise  grantResults pour verifier les permission accepter ou non
    //DONC onRequestPermissionsResult je vais vérifier si c'est bien ma REQUEST_PERMISSION
    // Et ensuite on vérifie si sa été accorder avec  if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
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
    private fun getSongs() {
        val songCursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null)

        if(songCursor != null && songCursor.moveToFirst()){

                val indexTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val indexArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                    // on fait aussi cela pour la data ( path/URI) du fichier
                val indexData = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA)
                // On fait cela pour definir le type de musique car MediaStore propose les son d'alarme , de sonnerie et autres
                // donc on lui précise que l'on veu seuelemnt de la musique
                val indexIsMusic = songCursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)


                //je vais récuperer le titre , l'artist , le path et la verification du type de fichier
                // Puis
                do {
                    val title = songCursor.getString(indexTitle)
                    val artist = songCursor.getString(indexArtist)
                    val path = songCursor.getString(indexData)
                    val isMusic = songCursor.getInt(indexIsMusic)
    // si c'est bien une musique  , je remplis ma liste avec l'object (Song)
                    if (isMusic == 1){
                        songsArrayList.add(Song(title, artist, path))
                    }
                } // je boucle pour
                while (songCursor.moveToNext())
                // ici je ferme mon curceur
                songCursor.close()

            }
            // ici je vais notifier l'adapteur que la data a changer , le ? s'assure que la data récuperer n'est pas nul
            songsAdapter?.notifyDataSetChanged()
    }
}