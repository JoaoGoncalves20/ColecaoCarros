package pt.ipg.colecaocarros

import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class TabelaDetalhes(db: SQLiteDatabase):TabelaBD(db, NOME_TABELA) {
    override fun cria() {
        db.execSQL("CREATE TABLE ${TabelaCarro.NOME_TABELA} ($CHAVE_TABELA, estado TEXT NOT NULL, preco REAL, kilometragem REAL, id_carros INTEGER NOT NULL, FOREIGN KEY(id_carros) REFERENCES ${TabelaCarro.NOME_TABELA}(${BaseColumns._ID}) ON DELETE RESTRICT)")
    }

    companion object{
        const val NOME_TABELA = "detalhes"
    }

}
