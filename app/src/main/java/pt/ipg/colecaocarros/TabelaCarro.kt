package pt.ipg.colecaocarros

import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class TabelaCarro(db: SQLiteDatabase):TabelaBD(db,NOME_TABELA) {
    override fun cria() {
        db.execSQL("CREATE TABLE $NOME_TABELA ($CHAVE_TABELA, $CAMPO_MARCA TEXT NOT NULL, $CAMPO_MODELO TEXT NOT NULL, $CAMPO_DATA TEXT NOT NULL)")
    }

    companion object{
        const val NOME_TABELA = "carros"
        const val CAMPO_MARCA = "marca"
        const val CAMPO_MODELO = "modelo"
        const val CAMPO_DATA = "data"

    }
}