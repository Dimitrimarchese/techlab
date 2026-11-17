package Techlab.negocio;

import java.time.LocalDateTime;

public abstract class AtivoTI {

    protected static int contadorID = 0;

    protected int id;
    protected String codigoPatrimonio;
    protected String modelo;
    protected String status;
    protected LocalDateTime dataAquisicao;
    public AtivoTI(String codigoPatrimonio, String modelo, String status, LocalDateTime dataAquisicao) {
        contadorID++;
        this.id = contadorID;
        this.codigoPatrimonio = codigoPatrimonio;
        this.modelo = modelo;
        this.status = status;
        this.dataAquisicao = dataAquisicao;
    }
    public AtivoTI(int id, String codigoPatrimonio, String modelo, String status, LocalDateTime dataAquisicao) {
        this.id = id;
        this.codigoPatrimonio = codigoPatrimonio;
        this.modelo = modelo;
        this.status = status;
        this.dataAquisicao = dataAquisicao;
    }

    public int getid()                          { return this.id; }
    public String getcodigoPatrimonio()         { return this.codigoPatrimonio; }
    public String getmodelo()                   { return this.modelo; }
    public String getstatus()                   { return this.status; }
    public LocalDateTime getdataAquisicao()     { return this.dataAquisicao; }

    public void setid(int id)                                       { this.id = id; }
    public void setcodigoPatrimonio(String codigoPatrimonio)        { this.codigoPatrimonio = codigoPatrimonio; }
    public void setmodelo(String modelo)                            { this.modelo = modelo; }
    public void setstatus(String status)                            { this.status = status; }
    public void setdataAquisicao(LocalDateTime dataAquisicao)       { this.dataAquisicao = dataAquisicao; }

    public abstract double calcularCustoManutencaoMensal();

    @Override
    public String toString() {
        return String.format("[%s] ID=%d | Patrimônio=%s | Modelo=%s | Status=%s",
                getClass().getSimpleName(), id, codigoPatrimonio, modelo, status);
    }
}
