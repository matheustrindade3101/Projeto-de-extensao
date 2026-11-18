package br.com.tattoomanager.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ClienteDao {

    @Insert
    long inserir(Cliente cliente);

    @Update
    int atualizar(Cliente cliente);

    @Query("SELECT * FROM clientes ORDER BY nome COLLATE NOCASE")
    List<Cliente> listarTodos();

    @Query("SELECT COUNT(*) FROM clientes")
    int contarTodos();

    @Query("SELECT * FROM clientes WHERE id = :clienteId LIMIT 1")
    Cliente buscarPorId(long clienteId);
}
