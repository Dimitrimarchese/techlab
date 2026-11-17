package Techlab.db;

import Techlab.excecoes.FalhaConexaoBDException;
import Techlab.interfaces.IConexaoBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoBanco implements IConexaoBD {

    // Caminho da URL de conexão SQLite — arquivo local gerado na raiz do projeto
    private static final String URL = "jdbc:sqlite:techlab.db";

    public ConexaoBanco() {}

    @Override
    public Connection obterConexao() throws SQLException {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new FalhaConexaoBDException("Falha ao conectar ao banco SQLite: " + e.getMessage(), e);
        }
    }

    @Override
    public void inicializarBanco() {
        String ddlAtivos =
            "CREATE TABLE IF NOT EXISTS ativos ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "patrimonio TEXT NOT NULL UNIQUE, "
            + "nome TEXT NOT NULL, "
            + "categoria TEXT NOT NULL, "
            + "detalhe_especifico TEXT);";

        String ddlChamados =
            "CREATE TABLE IF NOT EXISTS chamados ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "titulo TEXT NOT NULL, "
            + "descricao TEXT NOT NULL, "
            + "prioridade TEXT NOT NULL, "
            + "data_abertura TEXT NOT NULL, "
            + "ativo_id INTEGER NOT NULL, "
            + "FOREIGN KEY (ativo_id) REFERENCES ativos(id) ON DELETE CASCADE);";

        try (Connection conexao = obterConexao();
             Statement stmt = conexao.createStatement()) {

            stmt.execute(ddlAtivos);
            stmt.execute(ddlChamados);
            System.out.println("[INFO] Banco 'techlab.db' inicializado — tabelas prontas.");

        } catch (SQLException e) {
            throw new FalhaConexaoBDException("Falha ao inicializar o banco de dados.", e);
        }
    }
}
