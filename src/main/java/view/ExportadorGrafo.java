package view;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.nio.dot.DOTExporter;
import java.io.File;

public class ExportadorGrafo {

    // Exporta o grafo para o formato DOT.
    // Salva o arquivo na raiz do projeto.
    public void exportar(Graph<String, DefaultWeightedEdge> grafo, String nomeArquivo) {
        DOTExporter<String, DefaultWeightedEdge> exporter = new DOTExporter<>(v -> v.replaceAll("[^a-zA-Z0-9]", ""));
        try {
            exporter.exportGraph(grafo, new File(nomeArquivo));
            System.out.println("Grafo exportado com sucesso: " + nomeArquivo);
        } catch (Exception e) {
            System.out.println("Erro ao exportar o grafo: " + e.getMessage());
        }
    }
}
