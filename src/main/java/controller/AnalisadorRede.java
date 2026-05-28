package controller;

import org.jgrapht.Graph;
import org.jgrapht.alg.color.GreedyColoring;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.alg.spanning.PrimMinimumSpanningTree;
import org.jgrapht.graph.DefaultWeightedEdge;

public class AnalisadorRede {

    // Calcula a Árvore Geradora Mínima via Kruskal.
    // Imprime o custo total.
    public void calcularKruskal(Graph<String, DefaultWeightedEdge> grafo) {
        KruskalMinimumSpanningTree<String, DefaultWeightedEdge> kruskal = new KruskalMinimumSpanningTree<>(grafo);
        System.out.println("Custo MST (Kruskal): " + kruskal.getSpanningTree().getWeight());
    }

    // Calcula a Árvore Geradora Mínima via Prim.
    // Imprime o custo total.
    public void calcularPrim(Graph<String, DefaultWeightedEdge> grafo) {
        PrimMinimumSpanningTree<String, DefaultWeightedEdge> prim = new PrimMinimumSpanningTree<>(grafo);
        System.out.println("Custo MST (Prim): " + prim.getSpanningTree().getWeight());
    }

    // Aplica coloração gulosa aos vértices.
    // Imprime o mapeamento Vértice -> Cor.
    public void colorirVertices(Graph<String, DefaultWeightedEdge> grafo) {
        GreedyColoring<String, DefaultWeightedEdge> coloring = new GreedyColoring<>(grafo);
        System.out.println("Coloração dos Vértices: " + coloring.getColoring().getColors());
    }
}
