package controller;

import model.Conexao;
import java.util.ArrayList;
import java.util.List;

public class LeitorDados {

    // Lê a lista de conexões fixas e faz o parse.
    public static List<Conexao> lerConexoes() {
        List<String> dados = List.of(
            "Rio Branco - AC;Porto Velho - RO;100",
            "Porto Velho - RO;Manaus - AM;100",
            "Manaus - AM;Boa Vista - RR;100",
            "Manaus - AM;Macapá - AP;100",
            "Manaus - AM;Santarém - PA;100",
            "Macapá - AP;Belém - PA;100",
            "Belém - PA;Palmas - TO;100",
            "São Luís - MΑ;Palmas - TO;100",
            "São Luís - MΑ;Brasília - DF;100",
            "Palmas - TO;Brasília - DF;100",
            "Brasília - DF;Goiânia - GO;100",
            "Goiânia - GO;Campo Grande - MS;100",
            "Campo Grande - MS;Cuiabá - MT;100",
            "Campo Grande - MS;São Paulo - SP;100",
            "São Paulo - SP;Florianópolis - SC;100",
            "Florianópolis - SC;Porto Alegre - RS;100",
            "São Paulo - SP;Belo Horizonte - MG;100",
            "Brasília - DF;Belo Horizonte - MG;100",
            "Belo Horizonte - MG;Salvador - BA;100",
            "Vitória - ES;Salvador - BA;100",
            "Salvador - BA;Recife - PE;100",
            "São Luís - MΑ;Teresina - PI;100",
            "Fortaleza - CE;Campina Grande - PB;100",
            "Vitória - ES;Rio de Janeiro - RJ;200",
            "Rio de Janeiro - RJ;São Paulo - SP;200",
            "Rio de Janeiro - RJ;Belo Horizonte - MG;200",
            "Recife - PE;Campina Grande - PB;200",
            "Campina Grande - PB;João Pessoa - PB;200",
            "Fortaleza - CE;São Paulo - SP;300",
            "São Paulo - SP;Curitiba - PR;300"
        );

        List<Conexao> conexoes = new ArrayList<>();
        for (String linha : dados) {
            String[] partes = linha.split(";");
            if (partes.length == 3) {
                conexoes.add(new Conexao(partes[0], partes[1], partes[2] + " Gb/s"));
            }
        }
        return conexoes;
    }
}
