package controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class OSMClient {
    private final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).connectTimeout(Duration.ofSeconds(10)).build();
    private final ObjectMapper mapper = new ObjectMapper();

    // Obtém coordenadas de uma cidade via Nominatim.
    public double[] obterCoordenadas(String cidade) {
        try {
            String url = "https://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(cidade, StandardCharsets.UTF_8) + "&format=json";
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("User-Agent", "RedeIpe-GrafoApp").GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            Thread.sleep(1500);
            
            JsonNode root = mapper.readTree(response.body());
            if (root.isArray() && !root.isEmpty()) {
                JsonNode first = root.get(0);
                return new double[]{first.get("lat").asDouble(), first.get("lon").asDouble()};
            }
        } catch (Exception e) {
            System.err.println("Erro ao obter coordenadas de " + cidade + ": " + e.getMessage());
        }
        return new double[]{0.0, 0.0};
    }

    // Calcula distância real em km via OSRM.
    public double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        try {
            String url = String.format(java.util.Locale.US, "http://router.project-osrm.org/route/v1/driving/%f,%f;%f,%f?overview=false", lon1, lat1, lon2, lat2);
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            Thread.sleep(1500);

            JsonNode root = mapper.readTree(response.body());
            if (root.has("routes") && root.get("routes").isArray() && !root.get("routes").isEmpty()) {
                double distanceMeters = root.get("routes").get(0).get("distance").asDouble();
                return distanceMeters / 1000.0;
            }
        } catch (Exception e) {
            System.err.println("Erro ao calcular distância: " + e.getMessage());
        }
        return 0.0;
    }
}
