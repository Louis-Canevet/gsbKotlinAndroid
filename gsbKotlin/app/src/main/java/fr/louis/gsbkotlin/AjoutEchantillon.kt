package fr.louis.gsbkotlin

import fr.louis.gsbkotlin.*
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AjoutEchantillon : AppCompatActivity(), View.OnClickListener {
    private lateinit var editTextCode: EditText
    private lateinit var editTextLibelle: EditText
    private lateinit var editTextQuantite: EditText
    private lateinit var btnAjouter: Button
    private lateinit var btnQuitter: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ajout_echantillon)
        editTextCode = findViewById(R.id.editTextCode)
        editTextLibelle = findViewById(R.id.editTextLibelle)
        editTextQuantite = findViewById(R.id.editTextQuantite)
        btnAjouter = findViewById(R.id.btnAjouter)
        btnQuitter = findViewById(R.id.btnQuitter)
        btnAjouter.setOnClickListener(this)
        btnQuitter.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view === btnAjouter) {
            val code = editTextCode.text.toString()
            val label = editTextLibelle.text.toString()
            val quantiteStock = editTextQuantite.text.toString()
            if (code.isEmpty() || label.isEmpty() || quantiteStock.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            } else {
                val echantillonAjout = Echantillon(code, label, quantiteStock)
                val bdAdapter = BdAdapter(this)
                bdAdapter.open()
                bdAdapter.ajouterEchantillon(echantillonAjout)
                bdAdapter.close()
                Toast.makeText(this, "Echantillon ajouté dans le stock", Toast.LENGTH_SHORT).show()
                editTextCode.setText("")
                editTextLibelle.setText("")
                editTextQuantite.setText("")
            }
        } else if (view === btnQuitter) {
            val quitterAjoutEchantillon = Intent(applicationContext, MainActivity::class.java)
            startActivity(quitterAjoutEchantillon)
            finish()
        }
    }
}
