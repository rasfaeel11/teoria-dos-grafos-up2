package controller;

import org.jgrapht.Graph;
import org.jgrapht.alg.color.LargestDegreeFirstColoring;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.alg.spanning.PrimMinimumSpanningTree;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AnalisadorRede {

    // Calcula a Árvore Geradora Mínima via Kruskal.
    // Imprime o custo total e cada aresta selecionada, ordenadas por peso crescente.
    public void calcularKruskal(Graph<String, DefaultWeightedEdge> grafo) {
        KruskalMinimumSpanningTree<String, DefaultWeightedEdge> kruskal = new KruskalMinimumSpanningTree<>(grafo);
        double custoTotal = kruskal.getSpanningTree().getWeight();
        System.out.println("Custo MST (Kruskal): " + custoTotal);

        List<DefaultWeightedEdge> arestas = kruskal.getSpanningTree().getEdges().stream()
                .sorted(Comparator.comparingDouble(grafo::getEdgeWeight))
                .collect(Collectors.toList());

        int i = 1;
        for (DefaultWeightedEdge aresta : arestas) {
            String origem = grafo.getEdgeSource(aresta);
            String destino = grafo.getEdgeTarget(aresta);
            double peso = grafo.getEdgeWeight(aresta);
            System.out.printf("  [%d] %s → %s | %.1f km%n", i++, origem, destino, peso);
        }
    }

    // Calcula a Árvore Geradora Mínima via Prim.
    // Imprime o custo total, cada aresta selecionada e compara com a MST de Kruskal.
    public void calcularPrim(Graph<String, DefaultWeightedEdge> grafo) {
        PrimMinimumSpanningTree<String, DefaultWeightedEdge> prim = new PrimMinimumSpanningTree<>(grafo);
        double custoTotalPrim = prim.getSpanningTree().getWeight();
        System.out.println("Custo MST (Prim): " + custoTotalPrim);

        List<DefaultWeightedEdge> arestas = prim.getSpanningTree().getEdges().stream()
                .sorted(Comparator.comparingDouble(grafo::getEdgeWeight))
                .collect(Collectors.toList());

        int i = 1;
        for (DefaultWeightedEdge aresta : arestas) {
            String origem = grafo.getEdgeSource(aresta);
            String destino = grafo.getEdgeTarget(aresta);
            double peso = grafo.getEdgeWeight(aresta);
            System.out.printf("  [%d] %s → %s | %.1f km%n", i++, origem, destino, peso);
        }

        // Comparação com Kruskal
        KruskalMinimumSpanningTree<String, DefaultWeightedEdge> kruskal = new KruskalMinimumSpanningTree<>(grafo);
        double custoTotalKruskal = kruskal.getSpanningTree().getWeight();
        int qtdKruskal = kruskal.getSpanningTree().getEdges().size();
        int qtdPrim = arestas.size();

        boolean mstIdenticas = Double.compare(custoTotalKruskal, custoTotalPrim) == 0 && qtdKruskal == qtdPrim;
        System.out.println("MSTs de Kruskal e Prim são idênticas: " + mstIdenticas
                + " (peso=" + custoTotalKruskal + " vs " + custoTotalPrim
                + ", arestas=" + qtdKruskal + " vs " + qtdPrim + ")");
    }

    // Aplica coloração por maior grau primeiro (LargestDegreeFirst) aos vértices.
    // Imprime o mapeamento Vértice -> Cor e o número cromático χ(G).
    public void colorirVertices(Graph<String, DefaultWeightedEdge> grafo) {
        LargestDegreeFirstColoring<String, DefaultWeightedEdge> coloring = new LargestDegreeFirstColoring<>(grafo);
        System.out.println("Coloração dos Vértices: " + coloring.getColoring().getColors());
        System.out.println("Número cromático χ(G) = " + coloring.getColoring().getNumberColors());
    }
}
