package pt.ipg.colecaocarros

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.provider.BaseColumns

class TabelaCarro(db: SQLiteDatabase):TabelaBD(db,NOME_TABELA) {
    override fun cria() {
        db.execSQL("CREATE TABLE $NOME_TABELA ($CHAVE_TABELA, $CAMPO_MARCA TEXT NOT NULL, $CAMPO_MODELO TEXT NOT NULL, $CAMPO_DATA INTEGER NOT NULL, $CAMPO_FK_DETALHES INTEGER NOT NULL, FOREIGN KEY($CAMPO_FK_DETALHES) REFERENCES ${TabelaCarro.NOME_TABELA}(${BaseColumns._ID}) ON DELETE RESTRICT)")
    }

    override fun consulta(
        colunas: Array<String>,
        selecao: String?,
        argsSelecao: Array<String>?,
        groupby: String?,
        having: String?,
        orderby: String?
    ): Cursor {
        val sql = SQLiteQueryBuilder()
        sql.tables = "$NOME_TABELA INNER JOIN ${TabelaDetalhes.NOME_TABELA} ON ${TabelaDetalhes.CAMPO_ID}=$CAMPO_FK_DETALHES"

        return sql.query(db, colunas, selecao, argsSelecao, groupby, having, orderby)
    }

    companion object{
        const val NOME_TABELA = "carros"

        const val CAMPO_ID = "$NOME_TABELA.${BaseColumns._ID}"
        const val CAMPO_MARCA = "marca"
        const val CAMPO_MODELO = "modelo"
        const val CAMPO_DATA = "data"
        const val CAMPO_FK_DETALHES = "id_detalhes"
        const val CAMPO_ESTADO_DETALHES = TabelaDetalhes.CAMPO_ESTADO
        const val CAMPO_PRECO_DETALHES = TabelaDetalhes.CAMPO_PRECO
        const val CAMPO_KILOMETRAGEM_DETALHES = TabelaDetalhes.CAMPO_KILOMETRAGEM

        val CAMPOS = arrayOf(CAMPO_ID, CAMPO_MARCA, CAMPO_MODELO, CAMPO_DATA, CAMPO_FK_DETALHES,CAMPO_ESTADO_DETALHES,CAMPO_PRECO_DETALHES,CAMPO_KILOMETRAGEM_DETALHES)

    }
}