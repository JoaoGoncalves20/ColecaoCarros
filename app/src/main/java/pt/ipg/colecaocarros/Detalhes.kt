package pt.ipg.colecaocarros

import android.content.ContentValues

data class Detalhes(var estado: String, var preco: Double, var kilometragem: Double, var id: Long = -1) {
    fun toContetValues(): ContentValues {
        val valores = ContentValues()

        valores.put(TabelaDetalhes.CAMPO_ESTADO, estado)
        valores.put(TabelaDetalhes.CAMPO_ESTADO, estado)
        valores.put(TabelaDetalhes.CAMPO_ESTADO, estado)
        return valores
    }
}