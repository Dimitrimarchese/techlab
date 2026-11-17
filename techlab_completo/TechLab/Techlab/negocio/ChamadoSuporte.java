package Techlab.negocio;

import java.time.LocalDateTime;

public class ChamadoSuporte {

    public enum Prioridade { BAIXA, MEDIA, ALTA }

    private int id;
    private String titulo;
    private String descricao;
    private Prioridade prioridade;
    private LocalDateTime dataAbertura;
    private AtivoTI ativoRelacionado;

    // Construtor completo — uso em memória
    public ChamadoSuporte(String titulo, String descricao, Prioridade prioridade, AtivoTI ativoRelacionado) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.dataAbertura = LocalDateTime.now();
        this.ativoRelacionado = ativoRelacionado;
    }

    // Construtor de reconstrução — com ID vindo do banco (ativoRelacionado pode ser null inicialmente)
    public ChamadoSuporte(int id, String titulo, String descricao, Prioridade prioridade,
                          LocalDateTime dataAbertura, AtivoTI ativoRelacionado) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.dataAbertura = dataAbertura;
        this.ativoRelacionado = ativoRelacionado;
    }

    public int getId()                          { return id; }
    public String getTitulo()                   { return titulo; }
    public String getDescricao()                { return descricao; }
    public Prioridade getPrioridade()           { return prioridade; }
    public LocalDateTime getDataAbertura()      { return dataAbertura; }
    public AtivoTI getAtivoRelacionado()        { return ativoRelacionado; }

    public void setId(int id)                                           { this.id = id; }
    public void setTitulo(String titulo)                                { this.titulo = titulo; }
    public void setDescricao(String descricao)                         { this.descricao = descricao; }
    public void setPrioridade(Prioridade prioridade)                   { this.prioridade = prioridade; }
    public void setDataAbertura(LocalDateTime dataAbertura)            { this.dataAbertura = dataAbertura; }
    public void setAtivoRelacionado(AtivoTI ativoRelacionado)          { this.ativoRelacionado = ativoRelacionado; }

    @Override
    public String toString() {
        String ativoInfo = ativoRelacionado != null
                ? ativoRelacionado.getcodigoPatrimonio()
                : "N/A";
        return String.format("[Chamado] ID=%d | %s | Prioridade=%s | Ativo=%s | Abertura=%s",
                id, titulo, prioridade, ativoInfo, dataAbertura);
    }
}
