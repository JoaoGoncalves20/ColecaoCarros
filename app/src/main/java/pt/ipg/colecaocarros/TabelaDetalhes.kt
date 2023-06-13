package pt.ipg.colecaocarros

import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class TabelaDetalhes(db: SQLiteDatabase):TabelaBD(db, NOME_TABELA) {
    override fun cria() {
        db.execSQL("CREATE TABLE $NOME_TABELA ($CHAVE_TABELA, $CAMPO_ESTADO TEXT NOT NULL)")
    }

    companion object{
        const val NOME_TABELA = "detalhes"

        const val CAMPO_ID = "$NOME_TABELA.${BaseColumns._ID}"
        const val CAMPO_ESTADO = "estado"

        val CAMPOS = arrayOf(BaseColumns._ID,CAMPO_ESTADO)

    }

}
