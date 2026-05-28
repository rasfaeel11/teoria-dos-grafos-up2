package view;

import controller.AnalisadorRede;
import controller.LeitorDados;
import controller.OSMClient;
import model.Conexao;
import model.GrafoRede;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    
    // Ingestão de dados e formação do grafo.
    public static void main(String[] args) {
        List<Conexao> dados = LeitorDados.lerConexoes();
        
        GrafoRede grafoRede = new GrafoRede();
        OSMClient osmClient = new OSMClient();
        Map<String, double[]> coordenadasCache = new HashMap<>();
        
        System.out.println("Iniciando ingestão e requisições às APIs...");
        
        dados.forEach(c -> {
            grafoRede.adicionarVertice(c.ponto1());
            grafoRede.adicionarVertice(c.ponto2());
            
            double[] coord1 = coordenadasCache.computeIfAbsent(c.ponto1(), osmClient::obterCoordenadas);
            double[] coord2 = coordenadasCache.computeIfAbsent(c.ponto2(), osmClient::obterCoordenadas);
            
            double dist = 0.0;
            if (coord1[0] != 0.0 && coord2[0] != 0.0) {
                dist = osmClient.calcularDistancia(coord1[0], coord1[1], coord2[0], coord2[1]);
            }
            
            grafoRede.adicionarAresta(c.ponto1(), c.ponto2(), dist);
            System.out.println("Aresta conectada: " + c.ponto1() + " - " + c.ponto2() + " (" + String.format(java.util.Locale.US, "%.2f", dist) + " km)");
        });
        
        // $O(E \log V)$ para a execução do algoritmo Kruskal.
        new AnalisadorRede().calcularKruskal(grafoRede);
    }
}
