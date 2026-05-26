package controller;

import model.Conexao;
import model.GrafoRede;
import java.util.List;

/**
 * Controlador principal que orquestra a construção do grafo com dados de APIs externas.
 */
public class RedeIpeController {
    private final OSMClient osmClient;

    public RedeIpeController() {
        this.osmClient = new OSMClient();
    }

    public void construirGrafo(GrafoRede grafoRede, List<Conexao> conexoes) {
        for (Conexao c : conexoes) {
            String v1 = c.ponto1();
            String v2 = c.ponto2();

            grafoRede.adicionarVertice(v1);
            grafoRede.adicionarVertice(v2);

            double[] coord1 = osmClient.obterCoordenadas(v1);
            double[] coord2 = osmClient.obterCoordenadas(v2);
            double distanciaKm = osmClient.calcularDistancia(coord1[0], coord1[1], coord2[0], coord2[1]);

            grafoRede.adicionarAresta(v1, v2, distanciaKm);
        }
    }
}
