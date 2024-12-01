import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class Jogo {
    Tabuleiro tabuleiro;
    List<Jogador> jogadores;
    double salario;
    int numRodadasMax;
    Random random;

    public Jogo(int numRodadasMax) {
        this.tabuleiro = new Tabuleiro();
        this.jogadores = new ArrayList<>();
        this.numRodadasMax = numRodadasMax;
        this.random = new Random();
    }

    public void definirSaldoInicial(Scanner scanner) {
        System.out.println("Defina o saldo inicial dos jogadores:");
        double saldoInicial = scanner.nextDouble();
        for (Jogador jogador : jogadores) {
            jogador.saldo = saldoInicial;
        }
    }

    public void definirSalario(Scanner scanner) {
        System.out.println("Defina o salário dos jogadores:");
        this.salario = scanner.nextDouble();
    }

    public void definirNumeroMaximoRodadas(Scanner scanner) {
        System.out.println("Defina o número máximo de rodadas:");
        this.numRodadasMax = scanner.nextInt();
    }

    public void cadastrarImovel(Scanner scanner) {
        while (true) {
            System.out.println("Deseja cadastrar um novo imóvel? (s/n)");
            String resposta = scanner.nextLine();
            if (resposta.equalsIgnoreCase("n")) break;
            if (resposta.equalsIgnoreCase("s")) {
                System.out.println("Digite o nome do imóvel:");
                String nome = scanner.nextLine();
                System.out.println("Digite o preço do imóvel:");
                double preco = scanner.nextDouble();
                scanner.nextLine();
                System.out.println("Digite o valor do aluguel:");
                double aluguel = scanner.nextDouble();
                scanner.nextLine();
                tabuleiro.adicionarCasa(new Casa(nome, "Imóvel", preco, aluguel));
            } else {
                System.out.println("Resposta inválida. Por favor, digite 's' ou 'n'.");
            }
        }
    }


    public void adicionarCasa(Casa casa) {
        tabuleiro.adicionarCasa(casa);
    }

    public void cadastrarJogador(Scanner scanner) {
        System.out.println("Digite o nome do jogador:");
        scanner.nextLine();
        String nome = scanner.nextLine();
        jogadores.add(new Jogador(nome, 0));
    }

    public void comprarImovel(Jogador jogador, Casa imovel) {
        if (jogador.saldo >= imovel.preco) {
            jogador.saldo -= imovel.preco;
            jogador.propriedades.add(imovel);
            imovel.proprietario = jogador;
            System.out.println(jogador.nome + " comprou " + imovel.nome + " por " + imovel.preco);
        } else {
            System.out.println("Saldo insuficiente para comprar " + imovel.nome);
        }
    }

    public void interagirComCasa(Jogador jogador, Casa casa) {
        switch (casa.tipo) {
            case "Imóvel":
                if (casa.proprietario == null) {
                    System.out.println(jogador.nome + " encontrou um imóvel disponível: " + casa.nome);
                    comprarImovel(jogador, casa);
                } else if (casa.proprietario != jogador) {
                    double aluguel = casa.aluguel;
                    jogador.saldo -= aluguel;
                    casa.proprietario.saldo += aluguel;
                    System.out.println(jogador.nome + " pagou " + aluguel + " de aluguel para " + casa.proprietario.nome);
                }
                break;
            case "Imposto":
                double imposto = jogador.saldo * 0.05;
                jogador.saldo -= imposto;
                System.out.println(jogador.nome + " pagou " + imposto + " de imposto");
                break;
            case "Restituição":
                double restituicao = salario * 0.10;
                jogador.saldo += restituicao;
                System.out.println(jogador.nome + " recebeu " + restituicao + " de restituição");
                break;
            case "Prisão":
                jogador.rodadasPreso = casa.numRodadasPreso;
                System.out.println(jogador.nome + " está preso por " + casa.numRodadasPreso + " rodadas");
                break;
            case "Sorte":
                jogador.saldo += casa.valor;
                System.out.println(jogador.nome + " teve sorte e ganhou " + casa.valor);
                break;
            case "Revés":
                jogador.saldo -= casa.valor;
                System.out.println(jogador.nome + " teve revés e perdeu " + casa.valor);
                break;
        }
    }

    public void movimentarJogador(Jogador jogador, int valorDado) {
        if (jogador.rodadasPreso > 0) {
            jogador.rodadasPreso--;
            System.out.println(jogador.nome + " está preso e não pode se mover");
            return;
        }

        No noAtual = tabuleiro.inicio;
        for (int i = 0; i < jogador.posicao; i++) {
            noAtual = noAtual.proximo;
        }

        No novoNo = tabuleiro.mover(noAtual, valorDado);
        jogador.posicao = (jogador.posicao + valorDado) % tabuleiro.tamanho;

        if (jogador.posicao < valorDado) {
            jogador.saldo += salario;
            System.out.println(jogador.nome + " passou pelo início e recebeu seu salário de " + salario);
        }

        interagirComCasa(jogador, novoNo.casa);
    }

    public void adicionarCasasEspeciais() {
        adicionarCasa(new Casa("Prisão", "Prisão", 3)); // Jogador fica preso por 3 rodadas
        adicionarCasa(new Casa("Sorte", "Sorte", 200)); // Jogador ganha 200
        adicionarCasa(new Casa("Revés", "Revés", 100)); // Jogador perde 100
    }

    public void iniciarJogo() {
        Scanner scanner = new Scanner(System.in);
        for (int rodada = 1; rodada <= numRodadasMax; rodada++) {
            System.out.println("\nRodada " + rodada);

            for (Jogador jogador : jogadores) {
                if (jogador.rodadasPreso > 0) {
                    jogador.rodadasPreso--;
                    System.out.println(jogador.nome + " está preso e não pode se mover");
                    continue;
                }

                System.out.println(jogador.nome + ", pressione Enter para jogar o dado.");
                scanner.nextLine();
                int valorDado = random.nextInt(6) + 1;
                System.out.println(jogador.nome + " tirou " + valorDado + " no dado");
                movimentarJogador(jogador, valorDado);

                if (jogador.saldo < 0) {
                    System.out.println(jogador.nome + " está falido!");
                    while (jogador.saldo < 0 && !jogador.propriedades.isEmpty()) {
                        System.out.println(jogador.nome + " precisa vender uma propriedade para continuar no jogo.");
                        System.out.println("Propriedades disponíveis para venda:");
                        for (int i = 0; i < jogador.propriedades.size(); i++) {
                            Casa propriedade = jogador.propriedades.get(i);
                            System.out.println((i + 1) + ". " + propriedade.nome + " por " + propriedade.preco);
                        }
                        System.out.println("Escolha uma propriedade para vender (digite o número):");
                        int escolha = scanner.nextInt();
                        Casa propriedade = jogador.propriedades.get(escolha - 1);
                        jogador.saldo += propriedade.preco;
                        System.out.println(jogador.nome + " vendeu " + propriedade.nome + " por " + propriedade.preco);
                        jogador.propriedades.remove(propriedade);
                    }

                    if (jogador.saldo < 0) {
                        System.out.println(jogador.nome + " está falido e não pode continuar no jogo.");
                        jogadores.remove(jogador);
                    }
                }
            }

            if (jogadores.size() == 1) {
                System.out.println("Fim do jogo! O vencedor é " + jogadores.getFirst().nome);
                return;
            }
        }

        Jogador vencedor = jogadores.getFirst();
        for (Jogador jogador : jogadores) {
            if (jogador.saldo > vencedor.saldo) {
                vencedor = jogador;
            }
        }
        System.out.println("Fim do jogo! O vencedor é " + vencedor.nome + " com saldo de " + vencedor.saldo);
    }
}
