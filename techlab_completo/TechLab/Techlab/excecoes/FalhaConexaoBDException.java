package Techlab.excecoes;

public class FalhaConexaoBDException extends RuntimeException {

    public FalhaConexaoBDException(String mensagem) {
        super(mensagem);
    }

    public FalhaConexaoBDException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
