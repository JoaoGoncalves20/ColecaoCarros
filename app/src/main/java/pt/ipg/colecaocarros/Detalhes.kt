package pt.ipg.colecaocarros

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

data class Detalhes(var estado: String, var id: Long = -1) {
    fun toContentValues(): ContentValues {
        val valores = ContentValues()

        valores.put(TabelaDetalhes.CAMPO_ESTADO, estado)
        return valores
    }

    companion object{
        fun fromCursor(cursor: Cursor): Detalhes{
            val posID = cursor.getColumnIndex(BaseColumns._ID)
            val posEstado = cursor.getColumnIndex(TabelaDetalhes.CAMPO_ESTADO)

            val id = cursor.getLong(posID)
            val estado = cursor.getString(posEstado)
            return Detalhes(estado, id)

        }
    }
}