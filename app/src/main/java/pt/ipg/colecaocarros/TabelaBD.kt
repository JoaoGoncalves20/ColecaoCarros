package pt.ipg.colecaocarros

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

abstract class TabelaBD(val db: SQLiteDatabase, val nome: String) {
    abstract fun cria()

    fun insere(valores: ContentValues) =
        db.insert(nome,null,valores)


    fun consulta(
        colunas: Array<String>,
        selacao: String?,
        argsSelecao: Array<String>?,
        orderby: String?,
        having: String?,
        groupby: String?
    ) =
        db.query(nome,colunas,selacao,argsSelecao,groupby,having,orderby)


    fun altera(valores: ContentValues, where: String, argsWhere: Array<String>) =
        db.update(nome,valores,where,argsWhere)

    fun elemina(where: String, argsWhere: Array<String>) =
        db.delete(nome, where, argsWhere)

}