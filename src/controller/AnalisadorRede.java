package controller;

import model.GrafoRede;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.graph.DefaultWeightedEdge;

public class AnalisadorRede {

    // Aplica o algoritmo de Kruskal para obter a MST.
    public void calcularKruskal(GrafoRede grafoRede) {
        KruskalMinimumSpanningTree<String, DefaultWeightedEdge> kruskal = new KruskalMinimumSpanningTree<>(grafoRede.getGrafo());
        System.out.println("Custo Total (MST): " + kruskal.getSpanningTree().getWeight());
    }
}
