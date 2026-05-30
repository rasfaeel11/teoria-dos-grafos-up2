package view;

import model.Conexao;
import org.jgrapht.Graph;
import org.jgrapht.alg.color.LargestDegreeFirstColoring;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Exporta o grafo da Rede Ipê para o formato DOT (Graphviz),
 * aplicando duas camadas de coloração independentes:
 *
 * 1. Coloração de ARESTAS por capacidade de banda:
 * - 100 Gb/s → Azul (#3366ff)
 * - 200 Gb/s → Verde (#00cc66)
 * - 300 Gb/s → Vermelho (#ff3333)
 *
 * 2. Coloração de VÉRTICES por canal de frequência (Greedy/LargestDegreeFirst):
 * - Canal 0 → Rosa (#ff9999)
 * - Canal 1 → Azul (#99ccff)
 * - Canal 2 → Verde (#99ff99)
 *
 * O arquivo .dot gerado pode ser aberto diretamente no Gephi
 * (File → Open) ou renderizado via Graphviz: dot -Tpng rede-ipe.dot -o
 * rede-ipe.png
 */
public class ExportadorGrafo {

    // ── Paleta de cores de arestas (capacidade de banda) ──────────────────
    private static final String COR_100_GBPS = "#3366ff"; // Azul — backbone padrão
    private static final String COR_200_GBPS = "#00cc66"; // Verde — corredor Sudeste/Nordeste
    private static final String COR_300_GBPS = "#ff3333"; // Vermelho — ultracapacidade

    // ── Paleta de cores de vértices (canal de frequência) ─────────────────
    private static final String[] CORES_CANAL = {
            "#ff9999", // Canal 0 — Rosa
            "#99ccff", // Canal 1 — Azul claro
            "#99ff99", // Canal 2 — Verde claro
            "#ffcc99", // Canal 3 — Laranja (fallback, caso χ(G) > 3)
            "#cc99ff", // Canal 4 — Lilás (fallback)
    };

    /**
     * Exporta o grafo para um arquivo .dot.
     *
     * @param grafo    O grafo JGraphT a ser exportado.
     * @param conexoes A lista original de conexões com as capacidades de banda.
     *                 Necessária para mapear cada aresta à sua capacidade,
     *                 já que o JGraphT só armazena o peso (distância km).
     * @param arquivo  Caminho do arquivo de saída (ex: "rede-ipe.dot").
     */
    public void exportar(
            Graph<String, DefaultWeightedEdge> grafo,
            List<Conexao> conexoes,
            String arquivo) {

        // 1. Calcular coloração de vértices (LargestDegreeFirst)
        LargestDegreeFirstColoring<String, DefaultWeightedEdge> coloring = new LargestDegreeFirstColoring<>(grafo);
        Map<String, Integer> coresVertices = coloring.getColoring().getColors();

        // 2. Montar mapa de capacidade por par de vértices
        // Chave: "cidadeA|||cidadeB" (normalizado para ser bidirecional)
        Map<String, Integer> capacidadePorAresta = new LinkedHashMap<>();
        for (Conexao conexao : conexoes) {
            String chave = chaveAresta(conexao.pontoA(), conexao.pontoB());
            capacidadePorAresta.put(chave, conexao.bandaGbps());
        }

        // 3. Configurar o exportador DOT
        DOTExporter<String, DefaultWeightedEdge> exporter = new DOTExporter<>();

        // 3a. Atributos globais do grafo
        exporter.setGraphAttributeProvider(() -> {
            Map<String, Attribute> attrs = new LinkedHashMap<>();
            attrs.put("label", DefaultAttribute.createAttribute("Rede Ipê — RNP"));
            attrs.put("labelloc", DefaultAttribute.createAttribute("t"));
            attrs.put("fontsize", DefaultAttribute.createAttribute("18"));
            attrs.put("fontname", DefaultAttribute.createAttribute("Arial"));
            attrs.put("bgcolor", DefaultAttribute.createAttribute("white"));
            attrs.put("splines", DefaultAttribute.createAttribute("true"));
            attrs.put("overlap", DefaultAttribute.createAttribute("false"));
            attrs.put("sep", DefaultAttribute.createAttribute("0.5"));
            return attrs;
        });

        // 3b. Atributos dos vértices: cor por canal de frequência
        exporter.setVertexAttributeProvider(vertice -> {
            Map<String, Attribute> attrs = new LinkedHashMap<>();

            int canal = coresVertices.getOrDefault(vertice, 0);
            String corFundo = canal < CORES_CANAL.length
                    ? CORES_CANAL[canal]
                    : CORES_CANAL[CORES_CANAL.length - 1];

            // Label limpo (remove o sufixo "- UF" para exibição, mantém nome curto)
            String label = vertice.contains(" - ")
                    ? vertice.substring(0, vertice.lastIndexOf(" - ")).trim()
                    : vertice;

            attrs.put("label", DefaultAttribute.createAttribute(label + "\\nCanal " + canal));
            attrs.put("style", DefaultAttribute.createAttribute("filled"));
            attrs.put("fillcolor", DefaultAttribute.createAttribute(corFundo));
            attrs.put("fontcolor", DefaultAttribute.createAttribute("#333333"));
            attrs.put("fontname", DefaultAttribute.createAttribute("Arial"));
            attrs.put("fontsize", DefaultAttribute.createAttribute("10"));
            attrs.put("shape", DefaultAttribute.createAttribute("ellipse"));
            attrs.put("width", DefaultAttribute.createAttribute("1.4"));
            attrs.put("height", DefaultAttribute.createAttribute("0.5"));
            return attrs;
        });

        // 3c. Atributos das arestas: cor por capacidade de banda + label com km
        exporter.setEdgeAttributeProvider(aresta -> {
            Map<String, Attribute> attrs = new LinkedHashMap<>();

            String origem = grafo.getEdgeSource(aresta);
            String destino = grafo.getEdgeTarget(aresta);
            String chave = chaveAresta(origem, destino);
            int banda = capacidadePorAresta.getOrDefault(chave, 100);
            double peso = grafo.getEdgeWeight(aresta);

            String cor = switch (banda) {
                case 200 -> COR_200_GBPS;
                case 300 -> COR_300_GBPS;
                default -> COR_100_GBPS;
            };

            String labelAresta = String.format("%.0f km\\n%d Gb/s", peso, banda);

            attrs.put("color", DefaultAttribute.createAttribute(cor));
            attrs.put("label", DefaultAttribute.createAttribute(labelAresta));
            attrs.put("fontsize", DefaultAttribute.createAttribute("8"));
            attrs.put("fontcolor", DefaultAttribute.createAttribute(cor));
            attrs.put("fontname", DefaultAttribute.createAttribute("Arial"));
            attrs.put("penwidth", DefaultAttribute.createAttribute(penwidth(banda)));
            return attrs;
        });

        // 4. Exportar para arquivo
        try (FileWriter writer = new FileWriter(arquivo)) {
            exporter.exportGraph(grafo, writer);
            System.out.println("Grafo exportado com sucesso: " + arquivo);
            System.out.println("  Vértices: " + grafo.vertexSet().size());
            System.out.println("  Arestas:  " + grafo.edgeSet().size());
            System.out.println("  Canais de frequência (χ(G)): " + coloring.getColoring().getNumberColors());
            System.out.println();
            System.out.println("Para renderizar com Graphviz:");
            System.out.println("  dot -Tpng " + arquivo + " -o rede-ipe.png");
            System.out.println("  dot -Tsvg " + arquivo + " -o rede-ipe.svg");
            System.out.println();
            System.out.println("Para abrir no Gephi:");
            System.out.println("  File → Open → selecione " + arquivo);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao exportar grafo para " + arquivo, e);
        }
    }

    // ── Helpers privados ──────────────────────────────────────────────────

    /**
     * Gera chave normalizada para lookup bidirecional de capacidade.
     * Ordena os dois nomes alfabeticamente para garantir que
     * (A, B) e (B, A) gerem a mesma chave.
     */
    private String chaveAresta(String a, String b) {
        return a.compareTo(b) <= 0
                ? a + "|||" + b
                : b + "|||" + a;
    }

    /**
     * Espessura da aresta proporcional à capacidade:
     * 100 Gb/s → 1.5 | 200 Gb/s → 2.5 | 300 Gb/s → 4.0
     */
    private String penwidth(int banda) {
        return switch (banda) {
            case 200 -> "2.5";
            case 300 -> "4.0";
            default -> "1.5";
        };
    }
}
