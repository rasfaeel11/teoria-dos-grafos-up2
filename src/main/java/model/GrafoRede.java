package model;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class GrafoRede {
    private final SimpleWeightedGraph<String, DefaultWeightedEdge> grafo;

    // Inicializa o grafo não-direcionado e ponderado.
    public GrafoRede() {
        this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
    }

    // Adiciona os vértices caso não existam e a aresta entre eles.
    // Evita duplicatas pela natureza do SimpleWeightedGraph.
    public void adicionarConexao(Conexao conexao, double distancia) {
        grafo.addVertex(conexao.pontoA());
        grafo.addVertex(conexao.pontoB());
        if (!grafo.containsEdge(conexao.pontoA(), conexao.pontoB())) {
            DefaultWeightedEdge aresta = grafo.addEdge(conexao.pontoA(), conexao.pontoB());
            grafo.setEdgeWeight(aresta, distancia);
        }
    }

    // Retorna o número total de vértices no grafo.
    public int getQuantidadeVertices() {
        return grafo.vertexSet().size();
    }

    // Retorna o número total de arestas no grafo.
    public int getQuantidadeArestas() {
        return grafo.edgeSet().size();
    }
}
