package Techlab.negocio;

import java.time.LocalDateTime;

public class EstacaoTrabalho extends AtivoTI {

    private String departamento;
    private String usuarioAlocado;

    // Construtor novo (sem ID explícito)
    public EstacaoTrabalho(String codigoPatrimonio, String modelo, String status,
                           LocalDateTime dataAquisicao, String departamento,
                           String usuarioAlocado) {
        super(codigoPatrimonio, modelo, status, dataAquisicao);
        this.departamento = departamento;
        this.usuarioAlocado = usuarioAlocado;
    }

    // Construtor de reconstrução — com ID vindo do banco
    public EstacaoTrabalho(int id, String codigoPatrimonio, String modelo, String status,
                           LocalDateTime dataAquisicao, String departamento,
                           String usuarioAlocado) {
        super(id, codigoPatrimonio, modelo, status, dataAquisicao);
        this.departamento = departamento;
        this.usuarioAlocado = usuarioAlocado;
    }

    public String getDepartamento()     { return departamento; }
    public String getUsuarioAlocado()   { return usuarioAlocado; }

    public void setDepartamento(String departamento)        { this.departamento = departamento; }
    public void setUsuarioAlocado(String usuarioAlocado)    { this.usuarioAlocado = usuarioAlocado; }

    // Custo fixo mensal por estação: R$150
    @Override
    public double calcularCustoManutencaoMensal() {
        return 150.0;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Dept=%s | Usuário=%s",
                departamento, usuarioAlocado);
    }
}
