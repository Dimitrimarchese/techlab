package Techlab;

import Techlab.dao.AtivoDAO;
import Techlab.dao.ChamadoDAO;
import Techlab.db.ConexaoBanco;
import Techlab.negocio.*;
import Techlab.negocio.ChamadoSuporte.Prioridade;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        ConexaoBanco db = new ConexaoBanco();
        db.inicializarBanco();

        AtivoDAO ativoDAO     = new AtivoDAO();
        ChamadoDAO chamadoDAO = new ChamadoDAO();

        try {

            System.out.println("\n===== SALVANDO ATIVOS =====");

            Servidor servidor = new Servidor(
                "SRV-001", "Dell PowerEdge R740", "Ativo",
                LocalDateTime.now(), "192.168.1.10", "Ubuntu Server 22.04", 64
            );

            EstacaoTrabalho estacao = new EstacaoTrabalho(
                "EST-002", "Lenovo ThinkPad E14", "Ativo",
                LocalDateTime.now(), "Desenvolvimento", "Carlos Mendes"
            );

            DispositivoRede switch24 = new DispositivoRede(
                "SW-003", "Cisco Catalyst 2960", "Ativo",
                LocalDateTime.now(), 24
            );

            ativoDAO.salvar(servidor);
            ativoDAO.salvar(estacao);
            ativoDAO.salvar(switch24);

            System.out.println("\n===== LISTANDO TODOS OS ATIVOS =====");
            List<AtivoTI> ativos = ativoDAO.listarTodos();
            for (AtivoTI a : ativos) {
                System.out.println(a);
                System.out.printf("  Custo mensal: R$ %.2f%n", a.calcularCustoManutencaoMensal());
            }

            System.out.println("\n===== BUSCANDO ATIVO ID=1 =====");
            AtivoTI encontrado = ativoDAO.buscarPorId(1);
            System.out.println("Encontrado: " + encontrado);

            System.out.println("\n===== ATUALIZANDO ATIVO ID=1 =====");
            encontrado.setmodelo("Dell PowerEdge R840 (atualizado)");
            ativoDAO.atualizar(encontrado);

            System.out.println("\n===== SALVANDO CHAMADOS =====");

            AtivoTI ativoParaChamado = ativoDAO.buscarPorId(1);

            ChamadoSuporte chamado1 = new ChamadoSuporte(
                "Servidor offline",
                "Servidor principal não responde ao ping desde 03h00.",
                Prioridade.ALTA,
                ativoParaChamado
            );

            ChamadoSuporte chamado2 = new ChamadoSuporte(
                "Troca de cabo de rede",
                "Switch da sala 03 com porta 12 danificada.",
                Prioridade.BAIXA,
                ativoDAO.buscarPorId(3)
            );

            chamadoDAO.salvar(chamado1);
            chamadoDAO.salvar(chamado2);

            System.out.println("\n===== LISTANDO TODOS OS CHAMADOS =====");
            List<ChamadoSuporte> chamados = chamadoDAO.listarTodos();
            for (ChamadoSuporte c : chamados) {
                System.out.println(c);
            }

            System.out.println("\n===== CHAMADOS DE PRIORIDADE ALTA =====");
            List<ChamadoSuporte> alta = chamadoDAO.listarPorPrioridade(Prioridade.ALTA);
            alta.forEach(System.out::println);

            System.out.println("\n===== DELETANDO ATIVO ID=3 =====");
            ativoDAO.deletar(3);

            System.out.println("\n===== ATIVOS RESTANTES =====");
            ativoDAO.listarTodos().forEach(System.out::println);

        } catch (SQLException e) {
            System.err.println("[ERRO] Operação no banco falhou: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
