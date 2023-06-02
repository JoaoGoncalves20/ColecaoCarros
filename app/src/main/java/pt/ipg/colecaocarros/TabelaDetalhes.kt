package pt.ipg.colecaocarros

import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class TabelaDetalhes(db: SQLiteDatabase):TabelaBD(db, NOME_TABELA) {
    override fun cria() {
        db.execSQL("CREATE TABLE $NOME_TABELA ($CHAVE_TABELA, $CAMPO_ESTADO TEXT NOT NULL, $CAMPO_PRECO REAL, $CAMPO_KILOMETRAGEM REAL)")
    }

    companion object{
        const val NOME_TABELA = "detalhes"

        const val CAMPO_ID = "$NOME_TABELA.${BaseColumns._ID}"
        const val CAMPO_ESTADO = "estado"
        const val CAMPO_PRECO = "preco"
        const val CAMPO_KILOMETRAGEM = "kilometragem"

        val CAMPOS = arrayOf(BaseColumns._ID,CAMPO_ESTADO,CAMPO_PRECO,CAMPO_KILOMETRAGEM)

    }

}
