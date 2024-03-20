package fr.louis.gsbkotlin
/*
import fr.louis.gsbkotlin.BdAdapter.Companion.KEY_ACTION
import fr.louis.gsbkotlin.BdAdapter.Companion.KEY_CODE
import fr.louis.gsbkotlin.BdAdapter.Companion.KEY_LIBELLE
import fr.louis.gsbkotlin.BdAdapter.Companion.KEY_QUANTITE
import fr.louis.gsbkotlin.BdAdapter.Companion.KEY_TIMESTAMP
import android.database.Cursor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LogAdapter(cursor1: LogActivity, private val cursor: Cursor) : RecyclerView.Adapter<LogAdapter.LogViewHolder>() {

    class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewCode: TextView = itemView.findViewById(R.id.textViewCode)
        val textViewLibelle: TextView = itemView.findViewById(R.id.textViewLibelle)
        val textViewQuantite: TextView = itemView.findViewById(R.id.textViewQuantite)
        val textViewTimestamp: TextView = itemView.findViewById(R.id.textViewTimestamp)
        val textViewAction: TextView = itemView.findViewById(R.id.textViewAction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_log, parent, false)
        return LogViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        cursor?.let {
            if (it.moveToPosition(position)) {
                val code = it.getString(it.getColumnIndexOrThrow(BdAdapter.KEY_CODE))
                val libelle = it.getString(it.getColumnIndexOrThrow(BdAdapter.KEY_LIBELLE))
                val quantite = it.getString(it.getColumnIndexOrThrow(BdAdapter.KEY_QUANTITE))
                val timestamp = it.getString(it.getColumnIndexOrThrow(BdAdapter.KEY_TIMESTAMP))
                val action = it.getString(it.getColumnIndexOrThrow(BdAdapter.KEY_ACTION))

                // Mettre à jour les TextView avec les données du curseur
                holder.textViewCode.text = "Code: $code"
                holder.textViewLibelle.text = "Libellé: $libelle"
                holder.textViewQuantite.text = "Quantité: $quantite"
                holder.textViewTimestamp.text = "Timestamp: $timestamp"
                holder.textViewAction.text = "Action: $action"
            }
        }
    }


    override fun getItemCount(): Int {
        return cursor.count
    }
}



*/