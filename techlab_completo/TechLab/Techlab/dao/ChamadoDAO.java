package Techlab.dao;

import Techlab.db.ConexaoBanco;
import Techlab.interfaces.IConexaoBD;
import Techlab.interfaces.IPersistencia;
import Techlab.negocio.AtivoTI;
import Techlab.negocio.ChamadoSuporte;
import Techlab.negocio.ChamadoSuporte.Prioridade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChamadoDAO implements IPersistencia<ChamadoSuporte> {

    private final IConexaoBD conexaoBD = new ConexaoBanco();
    // AtivoDAO reutilizado para buscar o objeto vinculado ao chamado
    private final AtivoDAO ativoDAO = new AtivoDAO();

    @Override
    public void salvar(ChamadoSuporte chamado) throws SQLException {
        String sql = "INSERT INTO chamados (titulo, descricao, prioridade, data_abertura, ativo_id) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = conexaoBD.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, chamado.getTitulo());
            stmt.setString(2, chamado.getDescricao());
            stmt.setString(3, chamado.getPrioridade().name());
            stmt.setString(4, chamado.getDataAbertura().toString());
            stmt.setInt(5, chamado.getAtivoRelacionado().getid());
            stmt.executeUpdate();

            System.out.println("[LOG DAO] Chamado salvo: " + chamado.getTitulo());
        }
    }

    @Override
    public ChamadoSuporte buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, titulo, descricao, prioridade, data_abertura, ativo_id "
                   + "FROM chamados WHERE id = ?";

        try (Connection conexao = conexaoBD.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetParaObjeto(rs);
                }
            }
        }
        throw new SQLException("Chamado com ID " + id + " não encontrado.");
    }

    @Override
    public List<ChamadoSuporte> listarTodos() throws SQLException {
        List<ChamadoSuporte> lista = new ArrayList<>();
        String sql = "SELECT id, titulo, descricao, prioridade, data_abertura, ativo_id "
                   + "FROM chamados ORDER BY id ASC";

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
    public void atualizar(ChamadoSuporte chamado) throws SQLException {
        String sql = "UPDATE chamados SET titulo = ?, descricao = ?, prioridade = ? WHERE id = ?";

        try (Connection conexao = conexaoBD.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, chamado.getTitulo());
            stmt.setString(2, chamado.getDescricao());
            stmt.setString(3, chamado.getPrioridade().name());
            stmt.setInt(4, chamado.getId());
            stmt.executeUpdate();

            System.out.println("[LOG DAO] Chamado ID " + chamado.getId() + " atualizado.");
        }
    }

    @Override
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM chamados WHERE id = ?";

        try (Connection conexao = conexaoBD.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

            System.out.println("[LOG DAO] Chamado ID " + id + " removido.");
        }
    }

    // Filtra chamados por prioridade — útil para triagem de suporte
    public List<ChamadoSuporte> listarPorPrioridade(Prioridade prioridade) throws SQLException {
        List<ChamadoSuporte> lista = new ArrayList<>();
        String sql = "SELECT id, titulo, descricao, prioridade, data_abertura, ativo_id "
                   + "FROM chamados WHERE prioridade = ? ORDER BY id ASC";

        try (Connection conexao = conexaoBD.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, prioridade.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearResultSetParaObjeto(rs));
                }
            }
        }
        return lista;
    }

    private ChamadoSuporte mapearResultSetParaObjeto(ResultSet rs) throws SQLException {
        int id                      = rs.getInt("id");
        String titulo               = rs.getString("titulo");
        String descricao            = rs.getString("descricao");
        Prioridade prioridade       = Prioridade.valueOf(rs.getString("prioridade"));
        LocalDateTime dataAbertura  = LocalDateTime.parse(rs.getString("data_abertura"));
        int ativoId                 = rs.getInt("ativo_id");

        // Busca o AtivoTI vinculado pelo ID registrado no chamado
        AtivoTI ativo = ativoDAO.buscarPorId(ativoId);

        return new ChamadoSuporte(id, titulo, descricao, prioridade, dataAbertura, ativo);
    }
}
