package controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

public class OSMClient {
    private final HttpClient httpClient;
    private final ObjectMapper mapper;
    private final Map<String, double[]> cacheCoordenadas;

    // Instancia o cliente HTTP e os demais utilitários.
    public OSMClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
        this.cacheCoordenadas = new HashMap<>();
    }

    // Obtém coordenadas usando Nominatim com cache
    private double[] obterCoordenadas(String cidade) {
        if (cacheCoordenadas.containsKey(cidade)) {
            return cacheCoordenadas.get(cidade);
        }

        try {
            String encodedCidade = URLEncoder.encode(cidade, StandardCharsets.UTF_8);
            String url = "https://nominatim.openstreetmap.org/search?q=" + encodedCidade + "&format=json&limit=1";
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "RedeIpe-GrafoApp")
                    .GET()
                    .build();
                    
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() != 200) {
                throw new RuntimeException("Erro de rede ao consultar Nominatim para a cidade: " + cidade + " (Status: " + response.statusCode() + ")");
            }
            
            JsonNode root = mapper.readTree(response.body());
            if (root.isArray() && !root.isEmpty()) {
                JsonNode first = root.get(0);
                double lat = first.get("lat").asDouble();
                double lon = first.get("lon").asDouble();
                double[] coords = new double[]{lat, lon};
                cacheCoordenadas.put(cidade, coords);
                return coords;
            } else {
                throw new RuntimeException("Cidade não encontrada no Nominatim: " + cidade);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("A requisição foi interrompida para a cidade: " + cidade, e);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter coordenadas de " + cidade, e);
        }
    }

    // Calcula a distância rodoviária real em km usando OSRM
    public double calcularDistancia(String origem, String destino) {
        double[] coordsOrigem = obterCoordenadas(origem);
        double[] coordsDestino = obterCoordenadas(destino);

        double lat1 = coordsOrigem[0];
        double lon1 = coordsOrigem[1];
        double lat2 = coordsDestino[0];
        double lon2 = coordsDestino[1];

        try {
            String url = String.format(Locale.US, "http://router.project-osrm.org/route/v1/driving/%f,%f;%f,%f?overview=false", lon1, lat1, lon2, lat2);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Erro de rede ao consultar OSRM. Status: " + response.statusCode());
            }

            JsonNode root = mapper.readTree(response.body());
            if (root.has("routes") && root.get("routes").isArray() && !root.get("routes").isEmpty()) {
                double distanceMeters = root.get("routes").get(0).get("distance").asDouble();
                return distanceMeters / 1000.0; // Converte para km
            } else {
                throw new RuntimeException("Rota não encontrada entre " + origem + " e " + destino);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("A requisição para calcular distância foi interrompida", e);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular distância entre " + origem + " e " + destino, e);
        }
    }
}
