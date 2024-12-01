import java.util.ArrayList;
import java.util.List;

class Jogador {
    String nome;
    double saldo;
    int posicao;
    List<Casa> propriedades;
    int rodadasPreso;

    public Jogador(String nome, double saldo) {
        this.nome = nome;
        this.saldo = saldo;
        this.posicao = 0;
        this.propriedades = new ArrayList<>();
        this.rodadasPreso = 0;
    }
}
