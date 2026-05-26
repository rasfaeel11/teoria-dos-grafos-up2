package controller;

import java.net.http.HttpClient;
import java.time.Duration;

public class OSMClient {
    private final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).connectTimeout(Duration.ofSeconds(10)).build();

    // Obtém coordenadas de uma cidade via Nominatim.
    public double[] obterCoordenadas(String cidade) {
        return new double[]{ -9.974, -67.807 };
    }

    // Calcula distância simulada via OSRM.
    public double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        return 1250.5;
    }
}
