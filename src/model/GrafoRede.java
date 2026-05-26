package model;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class GrafoRede {
    private final SimpleWeightedGraph<String, DefaultWeightedEdge> grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

    // Adiciona o vértice caso ainda não exista.
    public void adicionarVertice(String v) {
        grafo.addVertex(v);
    }

    // Adiciona a aresta ponderada entre dois vértices.
    public void adicionarAresta(String v1, String v2, double peso) {
        DefaultWeightedEdge aresta = grafo.addEdge(v1, v2);
        if (aresta != null) grafo.setEdgeWeight(aresta, peso);
    }

    // Retorna a instância do grafo.
    public SimpleWeightedGraph<String, DefaultWeightedEdge> getGrafo() {
        return grafo;
    }
}
