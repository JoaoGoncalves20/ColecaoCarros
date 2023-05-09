package pt.ipg.colecaocarros

import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class TabelaCarro(db: SQLiteDatabase):TabelaBD(db,NOME_TABELA) {
    override fun cria() {
        db.execSQL("CREATE TABLE $NOME_TABELA ($CHAVE_TABELA, marca TEXT NOT NULL, modelo TEXT NOT NULL, data TEXT NOT NULL)")
    }

    companion object{
        const val NOME_TABELA = "carros"

    }
}