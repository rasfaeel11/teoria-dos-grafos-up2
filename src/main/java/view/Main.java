package view;

import controller.AnalisadorRede;
import controller.OSMClient;
import model.Conexao;
import model.GrafoRede;

import java.util.List;

public class Main {
    // Ponto de entrada do sistema.
    // Inicializa o grafo, realiza análises e exporta o resultado.
    public static void main(String[] args) {
        GrafoRede grafoRede = new GrafoRede();
        OSMClient osmClient = new OSMClient();

        List<Conexao> conexoes = List.of(
            new Conexao("Rio Branco - AC", "Porto Velho - RO", 100), new Conexao("Porto Velho - RO", "Manaus - AM", 100), new Conexao("Manaus - AM", "Boa Vista - RR", 100), new Conexao("Manaus - AM", "Macapá - AP", 100), new Conexao("Manaus - AM", "Santarém - PA", 100), new Conexao("Macapá - AP", "Belém - PA", 100), new Conexao("Belém - PA", "Palmas - TO", 100), new Conexao("São Luís - MΑ", "Palmas - TO", 100), new Conexao("São Luís - MΑ", "Brasília - DF", 100), new Conexao("Palmas - TO", "Brasília - DF", 100), new Conexao("Brasília - DF", "Goiânia - GO", 100), new Conexao("Goiânia - GO", "Campo Grande - MS", 100), new Conexao("Campo Grande - MS", "Cuiabá - MT", 100), new Conexao("Campo Grande - MS", "São Paulo - SP", 100), new Conexao("São Paulo - SP", "Florianópolis - SC", 100), new Conexao("Florianópolis - SC", "Porto Alegre - RS", 100), new Conexao("São Paulo - SP", "Belo Horizonte - MG", 100), new Conexao("Brasília - DF", "Belo Horizonte - MG", 100), new Conexao("Belo Horizonte - MG", "Salvador - BA", 100), new Conexao("Vitória - ES", "Salvador - BA", 100), new Conexao("Salvador - BA", "Recife - PE", 100), new Conexao("São Luís - MΑ", "Teresina - PI", 100), new Conexao("Fortaleza - CE", "Campina Grande - PB", 100), new Conexao("Vitória - ES", "Rio de Janeiro - RJ", 200), new Conexao("Rio de Janeiro - RJ", "São Paulo - SP", 200), new Conexao("Rio de Janeiro - RJ", "Belo Horizonte - MG", 200), new Conexao("Recife - PE", "Campina Grande - PB", 200), new Conexao("Campina Grande - PB", "João Pessoa - PB", 200), new Conexao("Fortaleza - CE", "São Paulo - SP", 300), new Conexao("São Paulo - SP", "Curitiba - PR", 300)
        );

        for (Conexao conexao : conexoes) {
            double distancia = osmClient.calcularDistancia(conexao.pontoA(), conexao.pontoB());
            grafoRede.adicionarConexao(conexao, distancia);
        }

        AnalisadorRede analisador = new AnalisadorRede();
        analisador.calcularKruskal(grafoRede.getGrafo());
        analisador.calcularPrim(grafoRede.getGrafo());
        analisador.colorirVertices(grafoRede.getGrafo());

        ExportadorGrafo exportador = new ExportadorGrafo();
        exportador.exportar(grafoRede.getGrafo(), "rede-ipe.dot");
    }
}
