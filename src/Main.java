public static void main() {
    Scanner scanner = new Scanner(System.in);
    Jogo jogo = new Jogo(20);

    jogo.definirSaldoInicial(scanner);
    jogo.definirSalario(scanner);
    jogo.definirNumeroMaximoRodadas(scanner);

    while (true) {
        System.out.println("Deseja cadastrar um novo imóvel? (s/n)");
        String resposta = scanner.nextLine();
        if (resposta.equalsIgnoreCase("n")) break;
        jogo.cadastrarImovel(scanner);
    }

    jogo.adicionarCasasEspeciais();

    while (true) {
        System.out.println("Deseja cadastrar um novo jogador? (s/n)");
        String resposta = scanner.nextLine();
        if (resposta.equalsIgnoreCase("n")) break;
        jogo.cadastrarJogador(scanner);
    }

    if (jogo.tabuleiro.contarCasas() < 10) {
        System.out.println("Não é possível iniciar o jogo com menos de 10 imóveis.");
        return;
    }
    if (jogo.jogadores.size() < 2) {
        System.out.println("Não é possível iniciar o jogo com menos de 2 jogadores.");
        return;
    }

    // Iniciar o jogo
    jogo.iniciarJogo();
    scanner.close();
}
