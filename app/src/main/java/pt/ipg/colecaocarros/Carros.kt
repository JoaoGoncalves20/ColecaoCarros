package pt.ipg.colecaocarros

import android.content.ContentValues

data class Carros(var marca: String,var modelo: String, var data: String, var idDetalhes: Long, var id: Long=-1) {
    fun toContentValues():ContentValues{
        val valores = ContentValues()

        valores.put(TabelaCarro.CAMPO_MARCA, marca)
        valores.put(TabelaCarro.CAMPO_MODELO, modelo)
        valores.put(TabelaCarro.CAMPO_DATA, data)

        return valores
    }
}