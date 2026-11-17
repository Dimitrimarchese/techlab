package Techlab.negocio;

import java.time.LocalDateTime;

public class Servidor extends AtivoTI {

    private String ipEstatico;
    private String sistemaOperacional;
    private int capacidadeRAM; // em GB

    public Servidor(String codigoPatrimonio, String modelo, String status,
                    LocalDateTime dataAquisicao, String ipEstatico,
                    String sistemaOperacional, int capacidadeRAM) {
        super(codigoPatrimonio, modelo, status, dataAquisicao);
        this.ipEstatico = ipEstatico;
        this.sistemaOperacional = sistemaOperacional;
        this.capacidadeRAM = capacidadeRAM;
    }

    public Servidor(int id, String codigoPatrimonio, String modelo, String status,
                    LocalDateTime dataAquisicao, String ipEstatico,
                    String sistemaOperacional, int capacidadeRAM) {
        super(id, codigoPatrimonio, modelo, status, dataAquisicao);
        this.ipEstatico = ipEstatico;
        this.sistemaOperacional = sistemaOperacional;
        this.capacidadeRAM = capacidadeRAM;
    }

    public String getIpEstatico()           { return ipEstatico; }
    public String getSistemaOperacional()   { return sistemaOperacional; }
    public int getCapacidadeRAM()           { return capacidadeRAM; }

    public void setIpEstatico(String ip)                { this.ipEstatico = ip; }
    public void setSistemaOperacional(String so)        { this.sistemaOperacional = so; }
    public void setCapacidadeRAM(int ram)               { this.capacidadeRAM = ram; }

    @Override
    public double calcularCustoManutencaoMensal() {
        return this.capacidadeRAM * 50.0;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | IP=%s | SO=%s | RAM=%dGB",
                ipEstatico, sistemaOperacional, capacidadeRAM);
    }
}
