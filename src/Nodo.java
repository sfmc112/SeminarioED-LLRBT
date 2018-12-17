public class Nodo<T> {
    public static final int PRETO = 0;
    public static final int VERMELHO = 1;

    T valor;
    int cor;
    Nodo<T> esq;
    Nodo<T> dir;

    public Nodo(T valor) {
        this.valor = valor;
        this.cor = VERMELHO;
        esq = dir = null;
    }

    public boolean eVermelho() {
        return cor == VERMELHO;
    }

    public void comutaCores() {
        this.comutaCor();
        esq.comutaCor();
        dir.comutaCor();
    }

    public Nodo<T> rodaEsquerda() {
        Nodo<T> n = this.dir;
        this.dir = n.esq;
        n.esq = this;
        n.cor = n.esq.cor;
        n.esq.cor = VERMELHO;
        return n;
    }

    public Nodo<T> rodaDireita() {
        Nodo<T> n = this.esq;
        this.esq = n.dir;
        n.dir = this;
        n.cor = n.dir.cor;
        n.dir.cor = VERMELHO;
        return n;
    }

    private void comutaCor() {
        if (cor == PRETO)
            cor = VERMELHO;
        else
            cor = PRETO;
    }
}
