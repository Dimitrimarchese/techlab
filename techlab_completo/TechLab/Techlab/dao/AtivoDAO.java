package Techlab.dao;

import Techlab.db.ConexaoBanco;
import Techlab.excecoes.EquipamentoNaoEncontradoException;
import Techlab.interfaces.IConexaoBD;
import Techlab.interfaces.IPersistencia;
import Techlab.negocio.AtivoTI;
import Techlab.negocio.DispositivoRede;
import Techlab.negocio.EstacaoTrabalho;
import Techlab.negocio.Servidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AtivoDAO implements IPersistencia<AtivoTI> {

    private final IConexaoBD conexaoBD = new ConexaoBanco();

    @Override
    public void salvar(AtivoTI ativo) throws SQLException {
        String sql = "INSERT INTO ativos (patrimonio, nome, categoria, detalhe_especifico) VALUES (?, ?, ?, ?)";

        try (Connection conexao = conexaoBD.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, ativo.getcodigoPatrimonio());
            stmt.setString(2, ativo.getmodelo());
            stmt.setString(3, ativo.getClass().getSimpleName());
            stmt.setString(4, extrairDetalhe(ativo));
            stmt.executeUpdate();

            System.out.println("[LOG DAO] Ativo salvo: " + ativo.getcodigoPatrimonio());
        }
    }

    @Override
    public AtivoTI buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, patrimonio, nome, categoria, detalhe_especifico FROM ativos WHERE id = ?";

        try (Connection conexao = conexaoBD.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetParaObjeto(rs);
                }
            }
        }
        // Lança exceção customizada em vez de retornar null
        try {
            throw new EquipamentoNaoEncontradoException(id);
        } catch (EquipamentoNaoEncontradoException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }

    @Override
    public List<AtivoTI> listarTodos() throws SQLException {
        List<AtivoTI> lista = new ArrayList<>();
        String sql = "SELECT id, patrimonio, nome, categoria, detalhe_especifico FROM ativos ORDER BY id ASC";

        try (Connection conexao = conexaoBD.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearResultSetParaObjeto(rs));
            }
        }
        return lista;
    }

    @Override
    public void atualizar(AtivoTI ativo) throws SQLException {
        String sql = "UPDATE ativos SET patrimonio = ?, nome = ?, detalhe_especifico = ? WHERE id = ?";

        try (Connection conexao = conexaoBD.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, ativo.getcodigoPatrimonio());
            stmt.setString(2, ativo.getmodelo());
            stmt.setString(3, extrairDetalhe(ativo));
            stmt.setInt(4, ativo.getid());
            stmt.executeUpdate();

            System.out.println("[LOG DAO] Ativo ID " + ativo.getid() + " atualizado.");
        }
    }

    @Override
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM ativos WHERE id = ?";

        try (Connection conexao = conexaoBD.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

            System.out.println("[LOG DAO] Ativo ID " + id + " removido.");
        }
    }

    // Extrai o campo detalhe_especifico de acordo com o tipo concreto do ativo
    private String extrairDetalhe(AtivoTI ativo) {
        if (ativo instanceof Servidor) {
            return ((Servidor) ativo).getIpEstatico();
        } else if (ativo instanceof EstacaoTrabalho) {
            return ((EstacaoTrabalho) ativo).getDepartamento();
        } else if (ativo instanceof DispositivoRede) {
            return String.valueOf(((DispositivoRede) ativo).getNumeroPortas());
        }
        return "";
    }

    // Reconstrói o objeto Java a partir de uma linha do ResultSet
    private AtivoTI mapearResultSetParaObjeto(ResultSet rs) throws SQLException {
        int id              = rs.getInt("id");
        String patrimonio   = rs.getString("patrimonio");
        String modelo       = rs.getString("nome");
        String categoria    = rs.getString("categoria");
        String detalhe      = rs.getString("detalhe_especifico");

        switch (categoria) {
            case "Servidor":
                return new Servidor(id, patrimonio, modelo, "Ativo", null, detalhe, "Linux", 32);
            case "EstacaoTrabalho":
                return new EstacaoTrabalho(id, patrimonio, modelo, "Ativo", null, detalhe, "Operador");
            default:
                int portas = 0;
                try { portas = Integer.parseInt(detalhe); } catch (NumberFormatException ignored) {}
                return new DispositivoRede(id, patrimonio, modelo, "Ativo", null, portas);
        }
    }
}
