package com.zesko.geoapify_dijkstra;

import com.zesko.geoapify_dijkstra.algorithm.Dijkstra;
import com.zesko.geoapify_dijkstra.model.Grafo;
import com.zesko.geoapify_dijkstra.model.Node;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.*;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;

public class MainApp extends Application {
    private WebView webView;
    private ComboBox<String> cbOrigen;
    private ComboBox<String> cbDestino;
    private Grafo grafo;
    private Label lblDistancia;

    @Override
    public void start(Stage stage) {
        grafo = new Grafo().iniciarGrafo();

        // Crear UI
        BorderPane root = new BorderPane();
        root.setLeft(crearPanel());
        root.setCenter(crearMapa());

        Scene scene = new Scene(root, 1000, 700);

        stage.setTitle("Ruta Peru con Dijkstra");
        stage.setScene(scene);
        stage.show();

        // Llenar ComboBox
        cargarNodosEnComboBox();

        // Eventos
        configurarEventos();
        configurarCargaMapa();
    }

    // Crear mapa
    private WebView crearMapa() {
        webView = new WebView();
        webView.getEngine().setJavaScriptEnabled(true);

        String url = MainApp.class.getResource("/map.html").toExternalForm();
        webView.getEngine().load(url);

        return webView;
    }

    // Crear panel lateral
    private VBox crearPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));

        Label lblOrigen = new Label("Origen:");
        cbOrigen = new ComboBox<>();

        Label lblDestino = new Label("Destino:");
        cbDestino = new ComboBox<>();

        Button btnCalcular = new Button("Calcular Ruta");

        lblDistancia = new Label("Distancia total: -");


        btnCalcular.setOnAction(e -> calcularRuta());

        panel.getChildren().addAll(
                lblOrigen, cbOrigen,
                lblDestino, cbDestino,
                btnCalcular,
                lblDistancia
        );

        return panel;
    }

    // Cargar nodos en ComboBox
    private void cargarNodosEnComboBox() {
        for (Node nodo : grafo.getNodos()) {
            cbOrigen.getItems().add(nodo.getName());
            cbDestino.getItems().add(nodo.getName());
        }

        // Valores por defecto
        cbOrigen.setValue("Puno");
        cbDestino.setValue("Lima");
    }

    // Eventos
    private void configurarEventos() {
        webView.getEngine().setOnAlert(event ->
                System.out.println("JS Alert: " + event.getData())
        );

        webView.getEngine().getLoadWorker().exceptionProperty().addListener((obs, old, ex) -> {
            if (ex != null) ex.printStackTrace();
        });
    }

    // Cuando el mapa carga
    private void configurarCargaMapa() {
        webView.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {

            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {

                dibujarNodos();
                calcularRuta(); // ruta inicial
            }
        });
    }

    // Dibujar nodos
    private void dibujarNodos() {
        for (Node nodo : grafo.getNodos()) {
            String script = String.format(
                    "agregarNodo(%f, %f, \"%s\")",
                    nodo.getLatitud(),
                    nodo.getLongitud(),
                    nodo.getName()
            );
            webView.getEngine().executeScript(script);
        }
    }

    // Calcular y dibujar ruta
    private void calcularRuta() {
        double distanciaTotal = 0;
        String origenNombre = cbOrigen.getValue();
        String destinoNombre = cbDestino.getValue();

        if (origenNombre == null || destinoNombre == null) return;

        Node origen = grafo.buscarNombre(origenNombre);
        Node destino = grafo.buscarNombre(destinoNombre);

        List<Node> ruta = Dijkstra.calcularRuta(grafo, origen, destino);

        StringBuilder jsArray = new StringBuilder("[");
        for (Node n : ruta) {
            jsArray.append(String.format("[%f,%f],", n.getLatitud(), n.getLongitud()));
        }
        jsArray.deleteCharAt(jsArray.length() - 1);
        jsArray.append("]");

        String rutaCompleta = construirRutaCompleta(ruta);
        webView.getEngine().executeScript("dibujarRuta(" + rutaCompleta + ")");

        for (int i = 0; i < ruta.size() - 1; i++) {
            Node actual = ruta.get(i);
            Node siguiente = ruta.get(i + 1);

            distanciaTotal += calcularDistancia(actual, siguiente);
        }
        lblDistancia.setText(String.format("Distancia total: %.2f km", distanciaTotal));
        lblDistancia.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
    }

    private double calcularDistancia(Node a, Node b) {
        final int R = 6371;

        double lat1 = Math.toRadians(a.getLatitud());
        double lon1 = Math.toRadians(a.getLongitud());
        double lat2 = Math.toRadians(b.getLatitud());
        double lon2 = Math.toRadians(b.getLongitud());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double haversine = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(haversine), Math.sqrt(1 - haversine));

        return R * c;
    }

    private String obtenerRutaGeoapify(Node origen, Node destino) {
        try {
            Dotenv dotenv = Dotenv.load();
            String apiKey = dotenv.get("GEOAPIFY_API_KEY");

            webView.getEngine().executeScript(
                    "setApiKey('" + apiKey + "')"
            );

            URI uri = new URI(
                    "https",
                    "api.geoapify.com",
                    "/v1/routing",
                    String.format(java.util.Locale.US,
                            "waypoints=%f,%f|%f,%f&mode=drive&apiKey=%s",
                            origen.getLatitud(), origen.getLongitud(),
                            destino.getLatitud(), destino.getLongitud(),
                            apiKey
                    ),
                    null
            );

            // System.out.println("URL: " + url);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //System.out.println(response.body());

            JSONObject json = new JSONObject(response.body());

            JSONArray coords = json
                    .getJSONArray("features")
                    .getJSONObject(0)
                    .getJSONObject("geometry")
                    .getJSONArray("coordinates");

            // Convertir a formato JS: [[lat,lon],...]
            StringBuilder jsArray = new StringBuilder("[");

            // bajar un nivel
            JSONArray lineString = coords.getJSONArray(0);

            for (int i = 0; i < lineString.length(); i++) {

                JSONArray punto = lineString.getJSONArray(i);

                double lon = punto.getDouble(0);
                double lat = punto.getDouble(1);

                jsArray.append(String.format(java.util.Locale.US,
                        "[%f,%f],", lat, lon));
            }

            jsArray.deleteCharAt(jsArray.length() - 1);
            jsArray.append("]");

            return jsArray.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    private String construirRutaCompleta(List<Node> ruta) {
        StringBuilder rutaFinal = new StringBuilder("[");

        try {
            for (int i = 0; i < ruta.size() - 1; i++) {
                Node origen = ruta.get(i);
                Node destino = ruta.get(i + 1);

                String tramo = obtenerRutaGeoapify(origen, destino);

                // quitar [ y ] para concatenar
                tramo = tramo.substring(1, tramo.length() - 1);

                rutaFinal.append(tramo).append(",");
            }

            rutaFinal.deleteCharAt(rutaFinal.length() - 1);
            rutaFinal.append("]");

        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }

        return rutaFinal.toString();
    }

    public static void main(String[] args) {
        launch();
    }
}