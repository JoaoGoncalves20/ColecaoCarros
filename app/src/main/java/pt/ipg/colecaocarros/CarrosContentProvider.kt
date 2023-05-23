package pt.ipg.colecaocarros

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns

class CarrosContentProvider : ContentProvider() {
    private var bdOpenHelper : BDcolecaocarrosOpenHelper? = null

    override fun onCreate(): Boolean {
        bdOpenHelper = BDcolecaocarrosOpenHelper(context)
        return true
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        val bd = bdOpenHelper!!.readableDatabase
        val id = p0.lastPathSegment

        val endereco = uriMatcher().match(p0)

        val tabela = when (endereco){
            URI_DETALHES, URI_DETALHES_ID -> TabelaDetalhes(bd)
            URI_CARROS, URI_CARROS_ID -> TabelaCarro(bd)

            else -> null
        }
        val (selecao, argsSel) = when(endereco){
            URI_DETALHES_ID,URI_CARROS_ID -> Pair("${BaseColumns._ID}=?", arrayOf(id))
            else -> Pair(p2,p3)
        }

        return tabela?.consulta(
            p1 as Array<String>,
            selecao,
            argsSel as Array<String>?,
            null,
            null,
            p4
        )


    }

    override fun getType(p0: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    companion object{
        private  const val AUTORIDADE = "pt.ipg.ColecaoCarros"
        const val DETALHES = "detalhes"
        const val CARROS = "carros"


        private const val URI_DETALHES = 100
        private const val URI_DETALHES_ID = 101
        private const val URI_CARROS = 200
        private const val URI_CARROS_ID = 201

        fun uriMatcher() = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTORIDADE, DETALHES,URI_DETALHES) /* Content://pt.ipg.ColecaoCarros/detalhes */
            addURI(AUTORIDADE,"$DETALHES/#", URI_DETALHES_ID) /* Content://pt.ipg.ColecaoCarros/detalhes/(um numero) */
            addURI(AUTORIDADE, CARROS,URI_CARROS)
            addURI(AUTORIDADE,"$CARROS/#", URI_CARROS_ID)


        }

    }

}