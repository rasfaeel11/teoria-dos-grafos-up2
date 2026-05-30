package view;

import model.GrafoRede;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExportadorGrafo {

    // Resolve a cor da aresta com base na capacidade em Gbps.
    private String corPorBanda(int bandaGbps) {
        return switch (bandaGbps) {
            case 200 -> "#00cc66"; // verde
            case 300 -> "#ff3333"; // vermelho
            default  -> "#3366ff"; // azul (100 Gbps ou desconhecido)
        };
    }

    // Exporta o grafo para o formato DOT com cores e labels de distância.
    // Salva o arquivo na raiz do projeto.
    public void exportar(GrafoRede grafoRede, String nomeArquivo) {
        var grafo = grafoRede.getGrafo();

        DOTExporter<String, DefaultWeightedEdge> exporter =
                new DOTExporter<>(v -> v.replaceAll("[^a-zA-Z0-9]", ""));

        // Atributos dos vértices
        exporter.setVertexAttributeProvider(v -> {
            Map<String, Attribute> attrs = new LinkedHashMap<>();
            attrs.put("label", DefaultAttribute.createAttribute(v));
            return attrs;
        });

        // Atributos das arestas: cor por banda + label com distância km
        exporter.setEdgeAttributeProvider(aresta -> {
            Map<String, Attribute> attrs = new LinkedHashMap<>();
            int banda = grafoRede.getBandaGbps(aresta);
            double distancia = grafo.getEdgeWeight(aresta);
            attrs.put("color", DefaultAttribute.createAttribute(corPorBanda(banda)));
            attrs.put("label", DefaultAttribute.createAttribute(
                    String.format("%.0f km | %d Gbps", distancia, banda)));
            return attrs;
        });

        try {
            exporter.exportGraph(grafo, new File(nomeArquivo));
            System.out.println("Grafo exportado com sucesso: " + nomeArquivo);
        } catch (Exception e) {
            System.out.println("Erro ao exportar o grafo: " + e.getMessage());
        }
    }
}
