class No {
    Casa casa;
    No proximo;

    public No(Casa casa) {
        this.casa = casa;
    }
}

class Tabuleiro {
    No inicio;
    No fim;
    int tamanho;

    public void adicionarCasa(Casa casa) {
        No novoNo = new No(casa);
        if (inicio == null) {
            inicio = novoNo;
        } else {
            fim.proximo = novoNo;
        }
        fim = novoNo;
        fim.proximo = inicio;
        tamanho++;
    }

    public No mover(No atual, int passos) {
        for (int i = 0; i < passos; i++) {
            atual = atual.proximo;
        }
        return atual;
    }

    public int contarCasas() {
        return tamanho;
    }
}
