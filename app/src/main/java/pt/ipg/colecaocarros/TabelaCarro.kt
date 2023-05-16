package pt.ipg.colecaocarros

import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class TabelaCarro(db: SQLiteDatabase):TabelaBD(db,NOME_TABELA) {
    override fun cria() {
        db.execSQL("CREATE TABLE $NOME_TABELA ($CHAVE_TABELA, $CAMPO_MARCA TEXT NOT NULL, $CAMPO_MODELO TEXT NOT NULL, $CAMPO_DATA INTEGER NOT NULL, $CAMPO_FK_DETALHES INTEGER NOT NULL, FOREIGN KEY($CAMPO_FK_DETALHES) REFERENCES ${TabelaCarro.NOME_TABELA}(${BaseColumns._ID}) ON DELETE RESTRICT)")
    }

    companion object{
        const val NOME_TABELA = "carros"
        const val CAMPO_MARCA = "marca"
        const val CAMPO_MODELO = "modelo"
        const val CAMPO_DATA = "data"
        const val CAMPO_FK_DETALHES = "id_detalhes"

        val CAMPOS = arrayOf(BaseColumns._ID, CAMPO_MARCA, CAMPO_MODELO, CAMPO_DATA, CAMPO_FK_DETALHES)

    }
}