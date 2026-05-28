package controller;

import java.net.http.HttpClient;

public class OSMClient {
    private final HttpClient httpClient;

    // Instancia o cliente HTTP padrão.
    public OSMClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    // Retorna uma distância mockada temporariamente.
    // Foco na compilação do core do grafo.
    public double calcularDistancia(String origem, String destino) {
        return 1500.0;
    }
}
