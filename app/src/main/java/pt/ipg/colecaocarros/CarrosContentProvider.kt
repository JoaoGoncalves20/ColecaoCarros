package pt.ipg.colecaocarros

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns
import kotlin.contracts.contract

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

        val endereco = uriMatcher().match(p0)

        val tabela = when (endereco){
            URI_DETALHES, URI_DETALHES_ID -> TabelaDetalhes(bd)
            URI_CARROS, URI_CARROS_ID -> TabelaCarro(bd)

            else -> null
        }

        val id = p0.lastPathSegment

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
        val endereco = uriMatcher().match(p0)
        return  when(endereco){
            URI_DETALHES -> "vnd.android.cursor.dir/$DETALHES"
            URI_CARROS -> "vnd.android.cursor.dir/$CARROS"
            URI_DETALHES_ID -> "vnd.android.cursor.item/$DETALHES"
            URI_CARROS_ID -> "vnd.android.cursor.item/$CARROS"
            else -> null
        }
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        val bd = bdOpenHelper!!.writableDatabase

        val endereco = uriMatcher().match(p0)

        val tabela = when (endereco){
            URI_DETALHES -> TabelaDetalhes(bd)
            URI_CARROS -> TabelaCarro(bd)
            else -> return null
        }

        val id = tabela.insere(p1!!)
        if(id == -1L) {
            return null
        }

        return Uri.withAppendedPath(p0, id.toString())
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        val bd = bdOpenHelper!!.writableDatabase

        val endereco = uriMatcher().match(p0)

        val tabela = when (endereco){
            URI_DETALHES_ID -> TabelaDetalhes(bd)
            URI_CARROS_ID -> TabelaCarro(bd)

            else -> return 0
        }

        val id = p0.lastPathSegment!!
        return tabela.elemina("${BaseColumns._ID}=?", arrayOf(id))
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        val bd = bdOpenHelper!!.writableDatabase

        val endereco = uriMatcher().match(p0)

        val tabela = when (endereco){
            URI_DETALHES_ID -> TabelaDetalhes(bd)
            URI_CARROS_ID -> TabelaCarro(bd)

            else -> return 0
        }

        val id = p0.lastPathSegment!!
        return tabela.altera(p1!!,"${BaseColumns._ID}=?", arrayOf(id))

    }

    companion object{
        private  const val AUTORIDADE = "pt.ipg.ColecaoCarros"
        private const val DETALHES = "detalhes"
        private const val CARROS = "carros"


        private const val URI_DETALHES = 100
        private const val URI_DETALHES_ID = 101
        private const val URI_CARROS = 200
        private const val URI_CARROS_ID = 201

        private val ENDERECO_BASE = Uri.parse("content://$AUTORIDADE")

        val ENDERECO_DETALHES = Uri.withAppendedPath(ENDERECO_BASE, DETALHES)
        val ENDERECO_CARROS = Uri.withAppendedPath(ENDERECO_BASE, CARROS)

        fun uriMatcher() = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTORIDADE, DETALHES,URI_DETALHES) /* Content://pt.ipg.ColecaoCarros/detalhes */
            addURI(AUTORIDADE,"$DETALHES/#", URI_DETALHES_ID) /* Content://pt.ipg.ColecaoCarros/detalhes/(um numero) */
            addURI(AUTORIDADE, CARROS,URI_CARROS)
            addURI(AUTORIDADE,"$CARROS/#", URI_CARROS_ID)


        }

    }

}