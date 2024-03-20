package fr.louis.gsbkotlin

import fr.louis.gsbkotlin.*

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class BdAdapter(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 4
        private const val DATABASE_NAME = "GsbBd"

        // Noms de table et de colonnes pour la table des échantillons
        private const val TABLE_ECHANTILLON = "echantillon"
        const val KEY_CODE = "code"
        const val KEY_LIBELLE = "libelle"
        const val KEY_QUANTITE = "quantite"

        // Noms de table et de colonnes pour la table des journaux
        private const val TABLE_JOURNAL = "journal"

        const val KEY_ACTION = "action"
        const val KEY_DESCRIPTION = "description"
        const val KEY_TIMESTAMP = "timestamp"
    }

    // Création des tables
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_ECHANTILLON = ("CREATE TABLE $TABLE_ECHANTILLON("
                + "$KEY_CODE TEXT PRIMARY KEY,"
                + "$KEY_LIBELLE TEXT,"
                + "$KEY_QUANTITE INTEGER" + ")")
        db?.execSQL(CREATE_TABLE_ECHANTILLON)

        val CREATE_TABLE_JOURNAL = ("CREATE TABLE $TABLE_JOURNAL("
                + "$KEY_CODE TEXT PRIMARY KEY,"
                + "$KEY_LIBELLE TEXT,"
                + "$KEY_QUANTITE INTEGER,"
                + "$KEY_TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "$KEY_ACTION TEXT,"
                + "$KEY_DESCRIPTION TEXT" + ")")
        db?.execSQL(CREATE_TABLE_JOURNAL)
    }


    // Mise à niveau de la base de données
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ECHANTILLON")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_JOURNAL")
        onCreate(db)
    }

    // Ajouter un nouvel échantillon avec journalisation
    fun ajouterEchantillon(echantillon: Echantillon) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(KEY_CODE, echantillon.code)
            put(KEY_LIBELLE, echantillon.libelle)
            put(KEY_QUANTITE, echantillon.quantite.toInt()) // Convertir la quantité en Int si nécessaire
        }
        db.insert(TABLE_ECHANTILLON, null, values)
        db.close()

        // Journaliser l'ajout de l'échantillon
        journaliserAction("Ajout d'échantillon", "Code: ${echantillon.code}, Libellé: ${echantillon.libelle}, Quantité: ${echantillon.quantite}")
    }

    // Réduire la quantité d'un échantillon avec journalisation
    fun reduireEchantillon(code: String, quantiteARetirer: Int): Boolean {
        val db = this.writableDatabase
        val echantillon = getEchantillonWithCode(code)

        if (echantillon != null) {
            val quantiteActuelle = echantillon.quantite.toIntOrNull() ?: 0
            val nouvelleQuantite = quantiteActuelle - quantiteARetirer

            if (nouvelleQuantite >= 0) {
                val values = ContentValues().apply {
                    put(KEY_QUANTITE, nouvelleQuantite)
                }
                db.update(TABLE_ECHANTILLON, values, "$KEY_CODE = ?", arrayOf(code))
                db.close()
                // Journaliser la réduction de quantité
                journaliserAction("Réduction de quantité", "Code: $code, Quantité retirée: $quantiteARetirer")
                return true // Retourne true si la quantité a été retirée avec succès
            } else {
                // Si la nouvelle quantité serait négative, ne faites rien et retournez false pour indiquer une opération non réussie
                db.close()
                return false
            }
        } else {
            // Gérer le cas où l'échantillon n'est pas trouvé (peut-être une erreur)
            // Vous pouvez choisir de gérer cela en lançant une exception ou en retournant false, par exemple.
            // Ici, je choisis simplement de retourner false
            db.close()
            return false
        }
    }

    // Augmenter la quantité d'un échantillon avec journalisation
    fun augmenterEchantillon(code: String, quantite: Int): Int {
        val db = this.writableDatabase
        val echantillon = getEchantillonWithCode(code)
        val affectedRows: Int

        affectedRows = if (echantillon != null) {
            val nouvelleQuantite = echantillon.quantite.toInt() + quantite // Ajouter la quantité fournie à la quantité existante
            val values = ContentValues().apply {
                put(KEY_QUANTITE, nouvelleQuantite)
            }
            db.update(TABLE_ECHANTILLON, values, "$KEY_CODE = ?", arrayOf(code))
        } else {
            // Si l'échantillon n'existe pas, l'ajouter avec la quantité spécifiée
            val values = ContentValues().apply {
                put(KEY_CODE, code)
                put(KEY_QUANTITE, quantite)
            }
            db.insert(TABLE_ECHANTILLON, null, values)
            1 // Retourne 1 pour indiquer une insertion réussie
        }

        db.close()

        // Journaliser l'augmentation de quantité
        journaliserAction("Augmentation de quantité", "Code: $code, Quantité ajoutée: $quantite")

        return affectedRows
    }

    // Mettre à jour un échantillon avec journalisation
    fun majEchantillon(code: String, libelle: String, quantite: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_LIBELLE, libelle)
            put(KEY_QUANTITE, quantite)
        }
        db.update(TABLE_ECHANTILLON, values, "$KEY_CODE = ?", arrayOf(code))
        db.close()

        // Journaliser la mise à jour de l'échantillon
        journaliserAction("Mise à jour d'échantillon", "Code: $code, Libellé: $libelle, Quantité: $quantite")
    }

    // Obtenir tous les échantillons
    fun obtenirTousLesEchantillons(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_ECHANTILLON", null)
    }

    // Obtenir un échantillon par son libellé
    fun getEchantillonWithLib(libelle: String): Echantillon? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_ECHANTILLON,
            arrayOf(KEY_CODE, KEY_LIBELLE, KEY_QUANTITE),
            "$KEY_LIBELLE = ?",
            arrayOf(libelle),
            null,
            null,
            null
        )
        var echantillon: Echantillon? = null

        if (cursor.moveToFirst()) {
            val codeIndex = cursor.getColumnIndex(KEY_CODE)
            val libelleIndex = cursor.getColumnIndex(KEY_LIBELLE)
            val quantiteIndex = cursor.getColumnIndex(KEY_QUANTITE)

            if (codeIndex != -1 && libelleIndex != -1 && quantiteIndex != -1) {
                echantillon = Echantillon(
                    cursor.getString(codeIndex),
                    cursor.getString(libelleIndex),
                    cursor.getString(quantiteIndex)
                )
            }
        }
        cursor.close()
        return echantillon
    }

    // Obtenir un échantillon par son code
    fun getEchantillonWithCode(code: String): Echantillon? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_ECHANTILLON,
            arrayOf(KEY_CODE, KEY_LIBELLE, KEY_QUANTITE),
            "$KEY_CODE = ?",
            arrayOf(code),
            null,
            null,
            null
        )
        var echantillon: Echantillon? = null

        if (cursor.moveToFirst()) {
            val codeIndex = cursor.getColumnIndex(KEY_CODE)
            val libelleIndex = cursor.getColumnIndex(KEY_LIBELLE)
            val quantiteIndex = cursor.getColumnIndex(KEY_QUANTITE)

            if (codeIndex != -1 && libelleIndex != -1 && quantiteIndex != -1) {
                echantillon = Echantillon(
                    cursor.getString(codeIndex),
                    cursor.getString(libelleIndex),
                    cursor.getString(quantiteIndex)
                )
            }
        }
        cursor.close()
        return echantillon
    }

    // Journaliser une action
    private fun journaliserAction(action: String, description: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(KEY_ACTION, action)
            put(KEY_DESCRIPTION, description)
            put(KEY_TIMESTAMP, obtenirDateTime())
        }
        db.insert(TABLE_JOURNAL, null, values)
        db.close()
    }

    // Obtenir la date et l'heure actuelles
    private fun obtenirDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    // Ouvrir la base de données
    fun open() {
        writableDatabase
    }

    // Fermer la base de données
    override fun close() {
        writableDatabase.close()
    }

    // Récupérer toutes les données de la table des échantillons
    fun getData(): Cursor {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_ECHANTILLON"
        return db.rawQuery(query, null)
    }

    // Ajouter une méthode pour récupérer tous les journaux dans la base de données
    fun getAllLogs(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_JOURNAL", null)
    }

}

