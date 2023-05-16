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
        return openHelper.writableDatabase

    }

    private fun insereDetalhes(
        bd: SQLiteDatabase,
        detalhes: Detalhes
    ) {
        detalhes.id = TabelaDetalhes(bd).insere(detalhes.toContetValues())
        assertNotEquals(-1, detalhes.id)
    }

    private fun insereCarro(bd: SQLiteDatabase, carros: Carros) {
        carros.id = TabelaCarro(bd).insere(carros.toContentValues())
        assertNotEquals(-1, carros.id)
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

        val detalhes = Detalhes("Usado",14567.0,123789.3)
        insereDetalhes(bd, detalhes)

        val carro = Carros("Ford","Focus","18/8/99", detalhes.id)
        insereCarro(bd,carro)

        val carro2= Carros("Toyota","Prius","12/3/06", detalhes.id)
        insereCarro(bd,carro2)

    }

    @Test
    fun consegueLerDetalhes() {
        val bd = getWritableDataBase()

        val detalhes1 = Detalhes("novo", 15000.0, 0.0)
        insereDetalhes(bd, detalhes1)

        val detalhes2 = Detalhes("usado", 2989.90, 157876.0)
        insereDetalhes(bd, detalhes2)

        val tabelaDetalhes = TabelaDetalhes(bd)
        val cursor = tabelaDetalhes.consulta(
            TabelaDetalhes.CAMPOS,
            "${BaseColumns._ID}=?",
            arrayOf(detalhes2.id.toString()),
            null,
            null,
            null
        )

        assert(cursor.moveToNext())

        val detalhesBD = Detalhes.fromCursor(cursor)

        assertEquals(detalhes1, detalhesBD)

        val cursorTodasDetalhes = tabelaDetalhes.consulta(
            TabelaDetalhes.CAMPOS,
            null,
            null,
            null,
            null,
            TabelaDetalhes.CAMPO_ESTADO
        )

        assert(cursorTodasDetalhes.count > 1)

    }

    @Test
    fun consegueLerCarro() {
        val bd = getWritableDataBase()

        val detalhes = Detalhes("Novo", 16908.0, 0.0)
        insereDetalhes(bd, detalhes)

        val carro1 = Carros("Ford","Fold","18/7/98", detalhes.id)
        insereCarro(bd, carro1)

        val carros2 = Carros("Toyota","Supra","15/6/06", detalhes.id)
        insereCarro(bd, carros2)

        val tabelacarro = TabelaCarro(bd)
        val cursor = tabelacarro.consulta(TabelaCarro.CAMPOS, "${BaseColumns._ID}=?", arrayOf(carro1.id.toString()), null, null, null)

        assert(cursor.moveToNext()) //move cursor para o primeiro registo

        val carroBD = Carros.fromCursor(cursor)

        assertEquals(carro1, carroBD)

        val cursorTodasCategorias = tabelacarro.consulta(TabelaCarro.CAMPOS, null, null, null, null, TabelaCarro.CAMPO_MARCA)

        assert(cursorTodasCategorias.count > 1)
    }
}