package Techlab.interfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConexaoBD {
    // Contrato para obter a sessão ativa de conexão JDBC
    Connection obterConexao() throws SQLException;

    // Contrato para criar as tabelas automaticamente se não existirem
    void inicializarBanco();
}
