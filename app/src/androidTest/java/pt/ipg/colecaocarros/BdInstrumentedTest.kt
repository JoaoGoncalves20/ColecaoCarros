package pt.ipg.colecaocarros

import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.util.Calendar

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class wwBdInstrumentedTest {

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
        detalhes.id = TabelaDetalhes(bd).insere(detalhes.toContentValues())
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

        val date = Calendar.getInstance()
        date.set(1999,8,18)

        val carro = Carros("Ford","Focus",date, detalhes.id)
        insereCarro(bd,carro)

        val date2 = Calendar.getInstance()
        date2.set(2012,3,6)

        val carro2= Carros("Toyota","Prius",date2, detalhes.id)
        insereCarro(bd,carro2)

    }

    @Test
    fun consegueLerDetalhes() {
        val bd = getWritableDataBase()

        val detalhes1 = Detalhes("novo", 15900.0, 0.0)
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

        assertEquals(detalhes2, detalhesBD)

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

        val date = Calendar.getInstance()
        date.set(2018,7,15)

        val carro1 = Carros("Ford","Fold",date, detalhes.id)
        insereCarro(bd, carro1)

        val date2 = Calendar.getInstance()
        date2.set(1996,5,26)

        val carros2 = Carros("Toyota","Supra",date2, detalhes.id)
        insereCarro(bd, carros2)

        val tabelacarro = TabelaCarro(bd)
        val cursor = tabelacarro.consulta(TabelaCarro.CAMPOS, "${BaseColumns._ID}=?", arrayOf(carro1.id.toString()), null, null, null)

        assert(cursor.moveToNext()) //move cursor para o primeiro registo

        val carroBD = Carros.fromCursor(cursor)

        assertEquals(carro1, carroBD)

        val cursorTodasCategorias = tabelacarro.consulta(TabelaCarro.CAMPOS, null, null, null, null, TabelaCarro.CAMPO_MARCA)

        assert(cursorTodasCategorias.count > 1)
    }

    @Test
    fun consegueAlterarDetalhes() {
        val bd = getWritableDataBase()

        val detalhes = Detalhes("Novo", 13808.0, 0.0)
        insereDetalhes(bd, detalhes)

        detalhes.estado = "novo"

        val registosAlterados = TabelaDetalhes(bd).altera(
            detalhes.toContentValues(),
            "${BaseColumns._ID}=?",
            arrayOf(detalhes.id.toString())
        )

        assertEquals(1, registosAlterados)
    }

    @Test
    fun consegueAlterarCarros() {
        val bd = getWritableDataBase()

        val detalhes1 = Detalhes("Usado", 16908.0, 85000.0)
        insereDetalhes(bd, detalhes1)

        val detalhes2 = Detalhes("Usado", 14608.0, 135000.0)
        insereDetalhes(bd, detalhes2)

        val date = Calendar.getInstance()
        date.set(2003,5,6)

        val carro = Carros("Mercedez","A350",date, detalhes2.id)
        insereCarro(bd, carro)

        date.set(2003,5,13)

        carro.id_detalhes = detalhes1.id
        carro.modelo = "M3"
        carro.data = date
        carro.marca = "BMW"

        val registosAlterados = TabelaCarro(bd).altera(
            carro.toContentValues(),
            "${BaseColumns._ID}=?",
            arrayOf(carro.id.toString())
        )

        assertEquals(1, registosAlterados)
    }


    @Test
    fun consegueApagarDetalhes(){
        val bd = getWritableDataBase()

        val detalhes = Detalhes("Usado", 7308.83, 76000.0)
        insereDetalhes(bd, detalhes)

        val registosEliminados = TabelaDetalhes(bd).elemina(
            "${BaseColumns._ID}=?",
            arrayOf(detalhes.id.toString())
        )

        assertEquals(1,registosEliminados)
    }

    @Test
    fun consegueApagarCarros(){
        val bd = getWritableDataBase()

        val detalhes = Detalhes("Novo", 37308.83, 0.0)
        insereDetalhes(bd, detalhes)

        val date = Calendar.getInstance()
        date.set(2023,2,6)

        val carro = Carros("Hyundai","Blank",date, detalhes.id)
        insereCarro(bd, carro)

        val registosEliminados = TabelaCarro(bd).elemina(
            "${BaseColumns._ID}=?",
            arrayOf(carro.id.toString())
        )

        assertEquals(1,registosEliminados)
    }

}