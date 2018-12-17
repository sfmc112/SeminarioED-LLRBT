import java.util.Comparator;

public class ArvoreLLRB<T extends Comparable<? super T>> {
    private Nodo<T> raiz;
    private Comparator<? super T> comparador;

    public ArvoreLLRB() {
        raiz = null;
        comparador = new ComparadorOmissao();
    }

    public ArvoreLLRB(Comparator<? super T> comparador) {
        raiz = null;
        this.comparador = comparador;
    }

    private Nodo<T> ajustaNodosAcima(Nodo<T> atual) {
        // Forçar o Left Leaning
        if (atual.dir != null)
            if (atual.dir.eVermelho())
                atual = atual.rodaEsquerda();

        // Dois nodos vermelhos seguidos - Rotação
        if (atual.esq.esq != null && atual.esq != null)
            if (atual.esq.eVermelho() && atual.esq.esq.eVermelho())
                atual = atual.rodaDireita();

        // Dois descendentes vermelhos = troca cores
        if (atual.dir != null && atual.esq != null)
            if (atual.esq.eVermelho() && atual.dir.eVermelho())
                atual.comutaCores();

        return atual;
    }

    public void insere(T valor) {
        raiz = insere(raiz, valor);
        raiz.cor = Nodo.PRETO;
    }

    private Nodo<T> insere(Nodo<T> atual, T valor) {
        // Inserir novo nodo
        if (atual == null)
            return new Nodo(valor);

        // Procura onde inserir
        int cmp = comparador.compare(atual.valor, valor);
        if (cmp == 0)
            atual.valor = valor; // Subtitui o valor
        else if (cmp > 0)
            atual.esq = insere(atual.esq, valor);
        else
            atual.dir = insere(atual.dir, valor);

        return ajustaNodosAcima(atual);
    }

    private Nodo<T> moveVermelhoEsquerda(Nodo<T> n) {
        n.comutaCores();
        if (n.dir.esq.eVermelho()) {
            n.dir = n.dir.rodaDireita();
            n = n.rodaEsquerda();
            n.comutaCores();
        }
        return n;
    }

    private Nodo<T> moveVermelhoDireita(Nodo<T> n) {
        n.comutaCores();
        if (n.esq.esq.eVermelho()) {
            n = n.rodaDireita();
            n.comutaCores();
        }
        return n;
    }

    public void remove(T valor) {
        raiz = remove(raiz, valor);
        raiz.cor = Nodo.PRETO;
    }

    private Nodo<T> remove(Nodo<T> atual, T valor) {
        int cmp = comparador.compare(atual.valor, valor);

        if (cmp < 0) {
            if (!(atual.esq.eVermelho()) && !(atual.esq.esq.eVermelho()))
                atual = moveVermelhoEsquerda(atual);
            atual.esq = remove(atual.esq, valor);
        } else {
            if (atual.esq.eVermelho())
                atual = atual.rodaDireita();
            if (valor.compareTo(atual.valor) == 0 && (atual.dir == null))
                return null;
            if (!(atual.dir.eVermelho()) && !(atual.dir.esq.eVermelho()))
                atual = moveVermelhoDireita(atual);
            if (valor.compareTo(atual.valor) == 0) {
                atual.valor = minimo(atual.dir).valor;
                atual.dir = removeMinimo(atual.dir);
            } else
                atual.dir = remove(atual.dir, valor);
        }
        return ajustaNodosAcima(atual);
    }

    private Nodo<T> minimo(Nodo<T> atual) {
        if (atual == null)
            return atual;
        return atual.esq == null ? atual : minimo(atual.esq);
    }

    public void removeMinimo() {
        raiz = removeMinimo(raiz);
        raiz.cor = Nodo.PRETO;
    }

    private Nodo<T> removeMinimo(Nodo<T> atual) {
        if (atual == null)
            return atual;

        if (!(atual.esq.eVermelho()) && !(atual.esq.esq.eVermelho()))
            atual = moveVermelhoEsquerda(atual);
        atual.esq = removeMinimo(atual.esq);

        return ajustaNodosAcima(atual);
    }

    class ComparadorOmissao implements Comparator<T> {
        @Override
        public int compare(T o1, T o2) {
            return o1.compareTo(o2);
        }
    }
}
