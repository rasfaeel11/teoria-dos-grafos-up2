package view;

import controller.OSMClient;
import model.Conexao;
import model.GrafoRede;
import java.util.List;

public class Main {
    // Ponto de entrada do sistema.
    // Inicializa o grafo, adiciona as conexões e exibe o resumo.
    public static void main(String[] args) {
        GrafoRede grafo = new GrafoRede();
        OSMClient osmClient = new OSMClient();

        List<Conexao> conexoes = List.of(
            new Conexao("Rio Branco-AC", "Porto Velho-RO", 100),
            new Conexao("Porto Velho-RO", "Manaus-AM", 100),
            new Conexao("Vitória-ES", "Rio de Janeiro-RJ", 200),
            new Conexao("Fortaleza-CE", "São Paulo-SP", 300)
        );

        for (Conexao conexao : conexoes) {
            double distancia = osmClient.calcularDistancia(conexao.pontoA(), conexao.pontoB());
            grafo.adicionarConexao(conexao, distancia);
        }

        System.out.println("Total de vértices: " + grafo.getQuantidadeVertices());
        System.out.println("Total de arestas: " + grafo.getQuantidadeArestas());
    }
}
