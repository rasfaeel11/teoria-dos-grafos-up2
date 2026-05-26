package view;

import controller.AnalisadorRede;
import controller.OSMClient;
import model.Conexao;
import model.GrafoRede;
import java.util.List;

public class Main {
    
    // Simula dados, monta o grafo e executa Kruskal.
    public static void main(String[] args) {
        List<Conexao> dados = List.of(
            new Conexao("Rio Branco - AC", "Porto Velho - RO", "100 Gb/s"),
            new Conexao("Porto Velho - RO", "Manaus - AM", "100 Gb/s")
        );
        
        GrafoRede grafoRede = new GrafoRede();
        OSMClient osmClient = new OSMClient();
        
        dados.forEach(c -> {
            grafoRede.adicionarVertice(c.ponto1());
            grafoRede.adicionarVertice(c.ponto2());
            double dist = osmClient.calcularDistancia(0,0,0,0);
            grafoRede.adicionarAresta(c.ponto1(), c.ponto2(), dist);
        });
        
        new AnalisadorRede().calcularKruskal(grafoRede);
    }
}
