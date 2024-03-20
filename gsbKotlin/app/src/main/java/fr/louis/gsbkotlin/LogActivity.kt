package fr.louis.gsbkotlin

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

class LogActivity : AppCompatActivity() {
    private lateinit var recyclerViewLogs: RecyclerView
    private lateinit var logAdapter: LogAdapter
    private lateinit var bdAdapter: BdAdapter
    private lateinit var btnQuitter: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.log_activity)

        recyclerViewLogs = findViewById(R.id.recyclerViewLogs)
        btnQuitter = findViewById(R.id.btnQuitterLog)
        bdAdapter = BdAdapter(this)

        // Ouvrir la base de données et obtenir les journaux
        bdAdapter.open()
        val cursor = bdAdapter.getAllLogs()

        // Initialisez et configurez votre adaptateur personnalisé
        logAdapter = LogAdapter(cursor)

        // Configurez votre RecyclerView avec l'adaptateur personnalisé et un LayoutManager
        recyclerViewLogs.adapter = logAdapter
        recyclerViewLogs.layoutManager = LinearLayoutManager(this)

        btnQuitter.setOnClickListener {
            finish() // Fermer l'activité lorsque le bouton "Quitter" est cliqué
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bdAdapter.close()
    }

    inner class LogAdapter(private val cursor: Cursor) :
        RecyclerView.Adapter<LogAdapter.LogViewHolder>() {

        inner class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textViewCode: TextView = itemView.findViewById(R.id.textViewCode)
            val textViewLibelle: TextView = itemView.findViewById(R.id.textViewLibelle)
            val textViewQuantite: TextView = itemView.findViewById(R.id.textViewQuantite)
            val textViewTimestamp: TextView = itemView.findViewById(R.id.textViewTimestamp)
            val textViewAction: TextView = itemView.findViewById(R.id.textViewAction)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.list_item_log, parent, false)
            return LogViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
            cursor.moveToPosition(position)
            val code = cursor.getString(cursor.getColumnIndexOrThrow(BdAdapter.KEY_CODE))
            val libelle =
                cursor.getString(cursor.getColumnIndexOrThrow(BdAdapter.KEY_LIBELLE))
            val quantite =
                cursor.getString(cursor.getColumnIndexOrThrow(BdAdapter.KEY_QUANTITE))
            val timestamp =
                cursor.getString(cursor.getColumnIndexOrThrow(BdAdapter.KEY_TIMESTAMP))
            val action = cursor.getString(cursor.getColumnIndexOrThrow(BdAdapter.KEY_ACTION))

            // Mettre à jour les TextView avec les données du curseur
            holder.textViewCode.text = "Code: $code"
            holder.textViewLibelle.text = "Libellé: $libelle"
            holder.textViewQuantite.text = "Quantité: $quantite"
            holder.textViewTimestamp.text = "Timestamp: $timestamp"
            holder.textViewAction.text = "Action: $action"
        }

        override fun getItemCount(): Int {
            return cursor.count
        }
    }
}
