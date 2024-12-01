class Casa {
    String nome;
    String tipo;
    double preco;
    double aluguel;
    Jogador proprietario;
    double valor;
    int numRodadasPreso;

    public Casa(String nome, String tipo, double preco, double aluguel) {
        this.nome = nome;
        this.tipo = tipo;
        this.preco = preco;
        this.aluguel = aluguel;
    }

    public Casa(String nome, String tipo, int numRodadasPreso) {
        this.nome = nome;
        this.tipo = tipo;
        this.numRodadasPreso = numRodadasPreso;
    }
}
