package view;

import controller.AnalisadorRede;
import controller.OSMClient;
import model.Conexao;
import model.GrafoRede;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        GrafoRede grafoRede = new GrafoRede();
        OSMClient osmClient = new OSMClient();

        // Lista de conexões com capacidade de banda (100 / 200 / 300 Gb/s)
        // O terceiro parâmetro é a bandaGbps — usada pelo ExportadorGrafo
        // para colorir as arestas no arquivo .dot
        List<Conexao> conexoes = List.of(
                new Conexao("Rio Branco - AC", "Porto Velho - RO", 100),
                new Conexao("Porto Velho - RO", "Manaus - AM", 100),
                new Conexao("Manaus - AM", "Boa Vista - RR", 100),
                new Conexao("Manaus - AM", "Macapá - AP", 100),
                new Conexao("Manaus - AM", "Santarém - PA", 100),
                new Conexao("Macapá - AP", "Belém - PA", 100),
                new Conexao("Belém - PA", "Palmas - TO", 100),
                new Conexao("São Luís - MA", "Palmas - TO", 100),
                new Conexao("São Luís - MA", "Brasília - DF", 100),
                new Conexao("Palmas - TO", "Brasília - DF", 100),
                new Conexao("Brasília - DF", "Goiânia - GO", 100),
                new Conexao("Goiânia - GO", "Campo Grande - MS", 100),
                new Conexao("Campo Grande - MS", "Cuiabá - MT", 100),
                new Conexao("Campo Grande - MS", "São Paulo - SP", 100),
                new Conexao("São Paulo - SP", "Florianópolis - SC", 100),
                new Conexao("Florianópolis - SC", "Porto Alegre - RS", 100),
                new Conexao("São Paulo - SP", "Belo Horizonte - MG", 100),
                new Conexao("Brasília - DF", "Belo Horizonte - MG", 100),
                new Conexao("Belo Horizonte - MG", "Salvador - BA", 100),
                new Conexao("Vitória - ES", "Salvador - BA", 100),
                new Conexao("Salvador - BA", "Recife - PE", 100),
                new Conexao("São Luís - MA", "Teresina - PI", 100),
                new Conexao("Fortaleza - CE", "Campina Grande - PB", 100),
                new Conexao("Vitória - ES", "Rio de Janeiro - RJ", 200),
                new Conexao("Rio de Janeiro - RJ", "São Paulo - SP", 200),
                new Conexao("Rio de Janeiro - RJ", "Belo Horizonte - MG", 200),
                new Conexao("Recife - PE", "Campina Grande - PB", 200),
                new Conexao("Campina Grande - PB", "João Pessoa - PB", 200),
                new Conexao("Fortaleza - CE", "São Paulo - SP", 300),
                new Conexao("São Paulo - SP", "Curitiba - PR", 300));

        // Constrói o grafo consultando OSRM para cada aresta
        for (Conexao conexao : conexoes) {
            double distancia = osmClient.calcularDistancia(conexao.pontoA(), conexao.pontoB());
            grafoRede.adicionarConexao(conexao, distancia);
        }

        // Executa os algoritmos de análise
        AnalisadorRede analisador = new AnalisadorRede();
        analisador.calcularKruskal(grafoRede.getGrafo());
        analisador.calcularPrim(grafoRede.getGrafo());
        analisador.colorirVertices(grafoRede.getGrafo());
        analisador.analisarConectividade(grafoRede.getGrafo());

        // Exporta o grafo colorido para .dot
        // Passa a lista de conexoes para que o ExportadorGrafo saiba
        // a capacidade de banda de cada aresta (100 / 200 / 300 Gb/s)
        ExportadorGrafo exportador = new ExportadorGrafo();
        exportador.exportar(grafoRede.getGrafo(), conexoes, "rede-ipe.dot");
    }
}