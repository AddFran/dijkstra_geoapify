package com.zesko.geoapify_dijkstra.algorithm;

import com.zesko.geoapify_dijkstra.model.Arista;
import com.zesko.geoapify_dijkstra.model.Grafo;
import com.zesko.geoapify_dijkstra.model.Node;
import java.util.*;

public class Dijkstra {
    public static List<Node> calcularRuta(Grafo grafo, Node inicio, Node destino){
        Map<Node, Double> distancias = new HashMap<>();
        Map<Node, Node> anteriores = new HashMap<>();
        Set<Node> visitados = new HashSet<>();
        PriorityQueue<Node> cola = new PriorityQueue<>(Comparator.comparingDouble(distancias::get));

        for(Node nodo : grafo.getNodos()){
            distancias.put(nodo, Double.MAX_VALUE);
        }
        distancias.put(inicio, 0.0);
        cola.add(inicio);

        while(!cola.isEmpty()){
            Node actual = cola.poll();
            if (visitados.contains(actual))
                continue;
            visitados.add(actual);
            if(actual.equals(destino))
                break;

            for(Arista arista : grafo.getVecinos(actual)){
                Node vecino = arista.getDestino();
                double nuevaDistancia = distancias.get(actual) + arista.getDistancia();
                if(nuevaDistancia < distancias.get(vecino)){
                    distancias.put(vecino, nuevaDistancia);
                    anteriores.put(vecino, actual);
                    cola.add(vecino);
                }
            }
        }

        if (!anteriores.containsKey(destino) && !inicio.equals(destino)) {
            return new ArrayList<>();
        }

        List<Node> ruta = new ArrayList<>();
        Node paso = destino;

        while (paso != null) {
            ruta.add(paso);
            paso = anteriores.get(paso);
        }

        Collections.reverse(ruta);
        return ruta;
    }
}
