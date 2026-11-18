package br.com.tattoomanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import br.com.tattoomanager.data.AppDatabase;
import br.com.tattoomanager.data.Cliente;
import br.com.tattoomanager.data.ClienteDao;

@RunWith(AndroidJUnit4.class)
public class ClienteDaoInstrumentedTest {

    private AppDatabase database;
    private ClienteDao clientDao;

    @Before
    public void createDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(
                        context,
                        AppDatabase.class
                )
                .allowMainThreadQueries()
                .build();
        clientDao = database.clienteDao();
    }

    @After
    public void closeDatabase() {
        database.close();
    }

    @Test
    public void insertListAndCountClient() {
        Cliente client = new Cliente(
                "Cliente Teste",
                "(11) 99999-9999",
                "teste@email.com",
                "01/01/2000",
                true
        );

        long generatedId = clientDao.inserir(client);
        List<Cliente> clients = clientDao.listarTodos();

        assertTrue(generatedId > 0);
        assertEquals(1, clientDao.contarTodos());
        assertEquals(1, clients.size());
        assertEquals("Cliente Teste", clients.get(0).getNome());
    }
}
