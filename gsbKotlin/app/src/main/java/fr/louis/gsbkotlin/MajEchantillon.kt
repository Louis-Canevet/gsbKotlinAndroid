package fr.louis.gsbkotlin

import fr.louis.gsbkotlin.*
import android.content.Intent
import android.database.SQLException
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MajEchantillon : AppCompatActivity(), View.OnClickListener {
    private lateinit var editTextCode: EditText
    private lateinit var editTextQuantite: EditText
    private lateinit var btnQuitterMaj: Button
    private lateinit var btnMajAjout: Button
    private lateinit var btnMajSuppr: Button
    private lateinit var bdAdapter: BdAdapter // Déclaration de la variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.maj_echantillon)
        editTextCode = findViewById(R.id.editTextCode)
        editTextQuantite = findViewById(R.id.editTextQuantite)
        btnMajAjout = findViewById(R.id.btnMajAjout)
        btnMajSuppr = findViewById(R.id.btnMajSuppr)
        btnQuitterMaj = findViewById(R.id.btnQuitter)

        btnMajAjout.setOnClickListener(this)
        btnMajSuppr.setOnClickListener(this)
        btnQuitterMaj.setOnClickListener {
            val quitterMaj = Intent(applicationContext, MainActivity::class.java)
            startActivity(quitterMaj)
            finish()
        }

        // Initialisation de bdAdapter
        bdAdapter = BdAdapter(this) // Assurez-vous que cela est correct
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnMajAjout -> {
                ajouterEchantillon()
            }
            R.id.btnMajSuppr -> {
                supprimerEchantillon()
            }
        }
    }

    private fun ajouterEchantillon() {
        val code = editTextCode.text.toString()
        val quantiteStr = editTextQuantite.text.toString()
        if (code.isEmpty() || quantiteStr.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
        } else {
            val quantite = quantiteStr.toIntOrNull() // Convertir la quantité de String à Int
            if (quantite != null) {
                try {
                    bdAdapter.open() // Ouvrir la base de données
                    val echantillon = bdAdapter.getEchantillonWithCode(code)
                    if (echantillon != null) {
                        val success = bdAdapter.augmenterEchantillon(code, quantite)
                        if (success > 0) {
                            Toast.makeText(this, "Mise à jour réussie", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Échec de la mise à jour", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Échec de la mise à jour", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: SQLException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Erreur lors de l'ouverture de la base de données", Toast.LENGTH_SHORT).show()
                } finally {
                    bdAdapter.close() // Fermer la base de données dans le bloc finally
                }
            } else {
                Toast.makeText(this, "La quantité doit être un nombre valide", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun supprimerEchantillon() {
        val code = editTextCode.text.toString()
        val quantiteStr = editTextQuantite.text.toString()
        if (code.isEmpty() || quantiteStr.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
        } else {
            val quantite = quantiteStr.toIntOrNull() // Convertir la quantité de String à Int
            if (quantite != null) {
                try {
                    bdAdapter.open() // Ouvrir la base de données
                    val echantillon = bdAdapter.getEchantillonWithCode(code)
                    if (echantillon != null) {
                        val currentStock = echantillon.quantite.toIntOrNull() ?: 0
                        if (currentStock >= quantite) {
                            val newStock = currentStock - quantite
                            bdAdapter.reduireEchantillon(code, quantite)
                            Toast.makeText(this, "Stock mis à jour", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Stock insuffisant", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Échantillon non trouvé dans la base de données", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: SQLException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Erreur lors de l'ouverture de la base de données", Toast.LENGTH_SHORT).show()
                } finally {
                    bdAdapter.close() // Fermer la base de données dans le bloc finally
                }
            } else {
                Toast.makeText(this, "La quantité doit être un nombre valide", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
