package model;

// Record representando uma conexão entre dois pontos.
// Mantém a capacidade de banda em Gbps.
public record Conexao(String pontoA, String pontoB, int bandaGbps) {
}
