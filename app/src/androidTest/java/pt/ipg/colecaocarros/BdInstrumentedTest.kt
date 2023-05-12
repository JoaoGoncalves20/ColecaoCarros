package pt.ipg.colecaocarros

import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class BdInstrumentedTest {

    private fun getAppContext() =
        InstrumentationRegistry.getInstrumentation().targetContext


    private fun getWritableDataBase(): SQLiteDatabase {
        val openHelper = BDcolecaocarrosOpenHelper(getAppContext())
        val bd = openHelper.writableDatabase
        return bd
    }

    private fun insereDetalhes(
        bd: SQLiteDatabase,
        detalhes: Detalhes
    ) {
        detalhes.id = TabelaDetalhes(bd).insere(detalhes.toContetValues())
        assertNotEquals(-1, detalhes.id)
    }

    @Before
    fun apagaBaseDados(){
        getAppContext().deleteDatabase(BDcolecaocarrosOpenHelper.NOME_BASE_DADOS)
    }

    @Test
    fun consegueAbrirBaseDados() {
        val openHelper = BDcolecaocarrosOpenHelper(getAppContext())
        val bd = openHelper.readableDatabase
        assert(bd.isOpen)

        val appContext = getAppContext()
        assertEquals("pt.ipg.colecaocarros", appContext.packageName)
    }

    @Test
    fun consegueInserirDetalhes(){
        val bd = getWritableDataBase()

        val detalhes = Detalhes("Ford",10356.0,15823.7)
        insereDetalhes(bd, detalhes)

        //TabelaDetalhes(bd).insere()
    }

    @Test
    fun consegueInserirCarros(){
        val bd = getWritableDataBase()

        val detalhes = Detalhes("Toyota",14567.0,123789.3)
        insereDetalhes(bd, detalhes)

        val carro = Carros("Ford","Focus","18/8/99", detalhes.id)

    }

    @Test
    fun consegueLerDetalhes(){
        val bd = getWritableDataBase()

        val detalhes1 = Detalhes("Novo",27989.90,0.0)
        insereDetalhes(bd, detalhes1)

        val detalhes2 = Detalhes("usado",2989.90,157876.0)
        insereDetalhes(bd, detalhes2)

        val tabelaCategorias = TabelaDetalhes(bd)
        val cursor = tabelaCategorias.consulta(
            TabelaDetalhes.CAMPOS,
            "${BaseColumns._ID}=?",
            arrayOf(detalhes1.id.toString()),
            null,
            null,
            null
            )

        assert(cursor.moveToNext())

        val detalhesBD = Detalhes.fromCursor(cursor)

        assertEquals(detalhes1, detalhesBD)

        val cursorTodasCategorias = tabelaCategorias.consulta(TabelaDetalhes.CAMPOS, null, null, null, null, TabelaDetalhes.CAMPO_ESTADO)

        assert(cursorTodasCategorias.count > 1)

    }
}