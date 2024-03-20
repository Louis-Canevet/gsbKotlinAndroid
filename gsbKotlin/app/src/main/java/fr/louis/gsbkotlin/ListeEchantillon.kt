package fr.louis.gsbkotlin

import fr.louis.gsbkotlin.*
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListeEchantillon : AppCompatActivity() {
    private lateinit var recyclerViewEchantillons: RecyclerView
    private lateinit var echantBdd: BdAdapter
    private lateinit var echantAdapter: EchantillonAdapter
    private lateinit var cursor: Cursor // Déclarez le curseur en tant que variable de classe
    private lateinit var btnQuitterListe : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.liste_echantillon)

        btnQuitterListe = findViewById(R.id.btnQuitterLog)

        recyclerViewEchantillons = findViewById(R.id.recyclerViewEchantillons)
        echantBdd = BdAdapter(this)

        // Ouvrez la base de données et obtenez le curseur
        echantBdd.open()
        cursor = echantBdd.getData()

        // Initialisez et configurez votre adaptateur personnalisé
        echantAdapter = EchantillonAdapter(cursor)

        // Configurez votre RecyclerView avec l'adaptateur personnalisé et un LayoutManager
        recyclerViewEchantillons.adapter = echantAdapter
        recyclerViewEchantillons.layoutManager = LinearLayoutManager(this)

        btnQuitterListe.setOnClickListener {
            val MainActivity = Intent(this, MainActivity::class.java)
            startActivity(MainActivity)
        }
    }

    // fermer le curseur et la connexion à la base de données lorsque l'activité est détruite
    override fun onDestroy() {
        super.onDestroy()
        cursor.close()
        echantBdd.close()
    }
}


// EchantillonAdapter.kt
class EchantillonAdapter(private val cursor: Cursor) : RecyclerView.Adapter<EchantillonAdapter.ViewHolder>() {

    // ViewHolder pour contenir les vues
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val codeTextView: TextView = itemView.findViewById(R.id.tvCodeEchantillon)
        val libelleTextView: TextView = itemView.findViewById(R.id.tvLibelleEchantillon)
        val stockTextView: TextView = itemView.findViewById(R.id.tvStockEchantillon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.echant_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        cursor.moveToPosition(position)
        holder.codeTextView.text = "Code: " + cursor.getString(cursor.getColumnIndexOrThrow(BdAdapter.KEY_CODE))
        holder.libelleTextView.text = "Libellé: " + cursor.getString(cursor.getColumnIndexOrThrow(BdAdapter.KEY_LIBELLE))
        holder.stockTextView.text = "Quantité: " + cursor.getString(cursor.getColumnIndexOrThrow(BdAdapter.KEY_QUANTITE))
    }

    override fun getItemCount(): Int {
        return cursor.count
    }

}

