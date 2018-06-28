package com.ps2.petsfinder.petsfinder;

public enum CorAnimal {
    VERMELHO(0),
    ROSA(1),
    ROXO(2),
    AZUL(3),
    CIANO(4),
    VERDE(5),
    VERDEAMARELADO(6),
    AMARELO(7),
    LARANJA(8),
    MARRON(9),
    CINZA(10),
    PRETO(11);

    private int cor;

    CorAnimal(int cor) {
        this.cor = cor;
    }

    public int getCor() {
        return cor;
    }
}
