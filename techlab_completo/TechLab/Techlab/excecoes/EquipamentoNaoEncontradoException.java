package Techlab.excecoes;

public class EquipamentoNaoEncontradoException extends Exception {

    private final int idBuscado;

    public EquipamentoNaoEncontradoException(int id) {
        super("Equipamento com ID " + id + " não foi encontrado na base de dados.");
        this.idBuscado = id;
    }

    public EquipamentoNaoEncontradoException(String mensagem) {
        super(mensagem);
        this.idBuscado = -1;
    }

    public int getIdBuscado() {
        return idBuscado;
    }
}
