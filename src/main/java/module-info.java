module com.zesko.geoapify_dijkstra {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.net.http;
    requires org.json;
    requires io.github.cdimascio.dotenv.java;

    opens com.zesko.geoapify_dijkstra to javafx.fxml;

    exports com.zesko.geoapify_dijkstra.model;
    exports com.zesko.geoapify_dijkstra.algorithm;
    exports com.zesko.geoapify_dijkstra.utils;
    exports com.zesko.geoapify_dijkstra;
}