package pt.ipg.colecaocarros

import android.database.sqlite.SQLiteDatabase
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



}