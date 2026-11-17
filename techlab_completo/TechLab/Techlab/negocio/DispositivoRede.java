package Techlab.negocio;

import java.time.LocalDateTime;

public class DispositivoRede extends AtivoTI {

    private int numeroPortas;

    // Construtor novo (sem ID explícito)
    public DispositivoRede(String codigoPatrimonio, String modelo, String status,
                           LocalDateTime dataAquisicao, int numeroPortas) {
        super(codigoPatrimonio, modelo, status, dataAquisicao);
        this.numeroPortas = numeroPortas;
    }

    // Construtor de reconstrução — com ID vindo do banco
    public DispositivoRede(int id, String codigoPatrimonio, String modelo, String status,
                           LocalDateTime dataAquisicao, int numeroPortas) {
        super(id, codigoPatrimonio, modelo, status, dataAquisicao);
        this.numeroPortas = numeroPortas;
    }

    public int getNumeroPortas()                { return numeroPortas; }
    public void setNumeroPortas(int portas)     { this.numeroPortas = portas; }

    // Custo por porta: R$200
    @Override
    public double calcularCustoManutencaoMensal() {
        return this.numeroPortas * 200.0;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Portas=%d", numeroPortas);
    }
}
