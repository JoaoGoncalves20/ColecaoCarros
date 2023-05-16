package pt.ipg.colecaocarros

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

data class Detalhes(var estado: String, var preco: Double, var kilometragem: Double, var id: Long = -1) {
    fun toContetValues(): ContentValues {
        val valores = ContentValues()

        valores.put(TabelaDetalhes.CAMPO_ESTADO, estado)
        valores.put(TabelaDetalhes.CAMPO_PRECO, preco)
        valores.put(TabelaDetalhes.CAMPO_KILOMETRAGEM, kilometragem)
        return valores
    }

    companion object{
        fun fromCursor(cursor: Cursor): Detalhes{
            val posID = cursor.getColumnIndex(BaseColumns._ID)
            val posEstado = cursor.getColumnIndex(TabelaDetalhes.CAMPO_ESTADO)
            val posPreco = cursor.getColumnIndex(TabelaDetalhes.CAMPO_PRECO)
            val posKilometragem = cursor.getColumnIndex(TabelaDetalhes.CAMPO_KILOMETRAGEM)

            val id = cursor.getLong(posID)
            val estado = cursor.getString(posEstado)
            val preco = cursor.getDouble(posPreco)
            val kilometragem = cursor.getDouble(posKilometragem)

            return Detalhes(estado,preco,kilometragem,id)

        }
    }
}