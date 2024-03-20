package fr.louis.gsbkotlin

import fr.louis.gsbkotlin.*
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import fr.louis.gsbkotlin.R

class MainActivity : AppCompatActivity() {
    private lateinit var txtPresentation: TextView
    private lateinit var btnAddEchantillon: Button
    private lateinit var btnListeEchantillon: Button
    private lateinit var btnMajEchantillon: Button
    private lateinit var btnLog: Button
    private lateinit var echantBdd: BdAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //txtPresentation = findViewById(R.id.txtPresentation)
        btnAddEchantillon = findViewById(R.id.btnAjoutEchantillon)
        btnListeEchantillon = findViewById(R.id.btnListeEchantillon)
        btnMajEchantillon = findViewById(R.id.btnMajEchantillon)
        btnLog = findViewById(R.id.btnLog)
        echantBdd = BdAdapter(this)

        btnAddEchantillon.setOnClickListener {
            val ajoutEchantillonIntent = Intent(this, AjoutEchantillon::class.java)
            startActivity(ajoutEchantillonIntent)
        }

        btnListeEchantillon.setOnClickListener {
            val listeEchantillonsIntent = Intent(this, ListeEchantillon::class.java)
            startActivity(listeEchantillonsIntent)
        }

        btnMajEchantillon.setOnClickListener {
            val majEchantillonIntent = Intent(this, MajEchantillon::class.java)
            startActivity(majEchantillonIntent)
        }

        btnLog.setOnClickListener {
            val intent = Intent(this, LogActivity::class.java)
            startActivity(intent)
        }



        testBd1()
    }

    private fun testBd1() {
        echantBdd.open()

        // Exemple de manipulation de la base de données...
        val unEchantFromBdd = echantBdd.getEchantillonWithLib("lib1")
        if (unEchantFromBdd != null) {
            Toast.makeText(this, unEchantFromBdd.libelle, Toast.LENGTH_LONG).show()
            unEchantFromBdd.libelle = "lib2"
            echantBdd.majEchantillon(unEchantFromBdd.code, unEchantFromBdd.libelle, unEchantFromBdd.quantite)
        } else {
            Toast.makeText(this, "Échantillon non trouvé", Toast.LENGTH_LONG).show()
        }

        echantBdd.close()
    }
}
