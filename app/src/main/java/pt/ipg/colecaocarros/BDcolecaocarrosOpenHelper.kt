package pt.ipg.colecaocarros

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val VERSAO_BASE_DADOS = 1

class BDcolecaocarrosOpenHelper(
    context: Context?
) : SQLiteOpenHelper(context, NOME_BASE_DADOS, null, VERSAO_BASE_DADOS) {
    override fun onCreate(db: SQLiteDatabase?) {
        requireNotNull(db)
        TabelaCarro(db).cria()
        TabelaDetalhes(db).cria()
    }

    override fun onUpgrade(p0: SQLiteDatabase?, oldVersion: Int, p2: Int) {
    }

    companion object{
        const val NOME_BASE_DADOS = "carros.db"
    }
}