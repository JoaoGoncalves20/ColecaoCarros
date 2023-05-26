package pt.ipg.colecaocarros

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns
import java.util.Calendar
import java.util.Date

data class Carros(var marca: String, var modelo: String, var data: Calendar, var id_detalhes: Long, var id: Long=-1) {
    fun toContentValues():ContentValues{
        val valores = ContentValues()

        valores.put(TabelaCarro.CAMPO_MARCA, marca)
        valores.put(TabelaCarro.CAMPO_MODELO, modelo)
        valores.put(TabelaCarro.CAMPO_DATA, data.timeInMillis)
        valores.put(TabelaCarro.CAMPO_FK_DETALHES, id_detalhes)

        return valores
    }
    companion object{
        fun fromCursor(cursor: Cursor): Carros{
            val posID = cursor.getColumnIndex(BaseColumns._ID)
            val posMarca = cursor.getColumnIndex(TabelaCarro.CAMPO_MARCA)
            val posModelo = cursor.getColumnIndex(TabelaCarro.CAMPO_MODELO)
            val posData = cursor.getColumnIndex(TabelaCarro.CAMPO_DATA)
            val posDetalhesFK = cursor.getColumnIndex(TabelaCarro.CAMPO_FK_DETALHES)

            val id = cursor.getLong(posID)
            val marca = cursor.getString(posMarca)
            val modelo = cursor.getString(posModelo)
            val data = Calendar.getInstance()
            data.timeInMillis = cursor.getLong(posData)
            val detalhesFK = cursor.getLong(posDetalhesFK)


            return Carros(marca,modelo,data,detalhesFK,id)

        }
    }
}