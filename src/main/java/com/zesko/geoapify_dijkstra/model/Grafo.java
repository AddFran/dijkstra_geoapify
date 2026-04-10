package com.zesko.geoapify_dijkstra.model;

import java.util.ArrayList;
import java.util.List;

public class Grafo {
    private List<Node> nodos;
    private List<Arista> aristas;

    public Grafo(){
        this.nodos = new ArrayList<>();
        this.aristas = new ArrayList<>();
    }

    public void addNodo(Node nodo){
        nodos.add(nodo);
    }
    public void addArista(Node inicio, Node destino){
        double distancia = calcularDistancia(inicio, destino);
        Arista arista = new Arista(inicio, destino, distancia);
        aristas.add(arista);
    }
    public List<Arista> getVecinos(Node nodo){
        List<Arista> vecinos = new ArrayList<>();
        for(Arista arista : aristas){
            if(arista.getInicio().equals(nodo)){
                vecinos.add(arista);
            }
        }
        return vecinos;
    }
    private double calcularDistancia(Node a, Node b){
        final int R = 6371; // Radio de la Tierra en km
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
        return R * c; // distancia en km
    }
    public void addAristaBidireccional(Node a, Node b){
        addArista(a,b);
        addArista(b,a);
    }

    public List<Node> getNodos(){return nodos;}
    public List<Arista> getAristas(){return aristas;}

    public Grafo iniciarGrafo(){
        Grafo grafo = new Grafo();
        Node amazonas = new Node("Amazonas", -6.23, -77.87);       // Chachapoyas
        Node ancash = new Node("Ancash", -9.53, -77.53);           // Huaraz
        Node apurimac = new Node("Apurimac", -13.63, -72.88);      // Abancay
        Node arequipa = new Node("Arequipa", -16.40, -71.53);
        Node ayacucho = new Node("Ayacucho", -13.16, -74.22);
        Node cajamarca = new Node("Cajamarca", -7.16, -78.50);
        Node callao = new Node("Callao", -12.06, -77.15);
        Node cusco = new Node("Cusco", -13.52, -71.97);
        Node huancavelica = new Node("Huancavelica", -12.78, -74.98);
        Node huanuco = new Node("Huanuco", -9.93, -76.24);
        Node ica = new Node("Ica", -14.07, -75.73);
        Node junin = new Node("Junin", -12.07, -75.21);             // Huancayo
        Node laLibertad = new Node("La Libertad", -8.11, -79.03);   // Trujillo
        Node lambayeque = new Node("Lambayeque", -6.77, -79.84);    // Chiclayo
        Node lima = new Node("Lima", -12.05, -77.04);
        Node loreto = new Node("Loreto", -3.75, -73.25);            // Iquitos
        Node madreDeDios = new Node("Madre de Dios", -12.59, -69.18); // Puerto Maldonado
        Node moquegua = new Node("Moquegua", -17.19, -70.93);
        Node pasco = new Node("Pasco", -10.68, -76.26);             // Cerro de Pasco
        Node piura = new Node("Piura", -5.19, -80.63);
        Node puno = new Node("Puno", -15.84, -70.02);
        Node sanMartin = new Node("San Martin", -6.49, -76.37);     // Moyobamba
        Node tacna = new Node("Tacna", -18.01, -70.25);
        Node tumbes = new Node("Tumbes", -3.57, -80.45);
        Node ucayali = new Node("Ucayali", -8.38, -74.55);          // Pucallpa

        // AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
        grafo.addNodo(amazonas);
        grafo.addNodo(ancash);
        grafo.addNodo(apurimac);
        grafo.addNodo(arequipa);
        grafo.addNodo(ayacucho);
        grafo.addNodo(cajamarca);
        grafo.addNodo(callao);
        grafo.addNodo(cusco);
        grafo.addNodo(huancavelica);
        grafo.addNodo(huanuco);
        grafo.addNodo(ica);
        grafo.addNodo(junin);
        grafo.addNodo(laLibertad);
        grafo.addNodo(lambayeque);
        grafo.addNodo(lima);
        grafo.addNodo(loreto);
        grafo.addNodo(madreDeDios);
        grafo.addNodo(moquegua);
        grafo.addNodo(pasco);
        grafo.addNodo(piura);
        grafo.addNodo(puno);
        grafo.addNodo(sanMartin);
        grafo.addNodo(tacna);
        grafo.addNodo(tumbes);
        grafo.addNodo(ucayali);

        // AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
        grafo.addAristaBidireccional(tumbes, piura);
        grafo.addAristaBidireccional(piura,lambayeque);
        grafo.addAristaBidireccional(piura,cajamarca);

        grafo.addAristaBidireccional(lambayeque,cajamarca);
        grafo.addAristaBidireccional(lambayeque,laLibertad);

        grafo.addAristaBidireccional(cajamarca,amazonas);
        grafo.addAristaBidireccional(cajamarca,laLibertad);

        grafo.addAristaBidireccional(amazonas,loreto);
        grafo.addAristaBidireccional(amazonas,sanMartin);
        grafo.addAristaBidireccional(amazonas,laLibertad);

        grafo.addAristaBidireccional(laLibertad,sanMartin);
        grafo.addAristaBidireccional(laLibertad,ancash);
        grafo.addAristaBidireccional(laLibertad,huanuco);

        grafo.addAristaBidireccional(loreto,sanMartin);
        grafo.addAristaBidireccional(loreto,huanuco);
        grafo.addAristaBidireccional(loreto,ucayali);

        grafo.addAristaBidireccional(sanMartin,huanuco);

        grafo.addAristaBidireccional(ancash,huanuco);
        grafo.addAristaBidireccional(ancash,lima);

        grafo.addAristaBidireccional(huanuco,ucayali);
        grafo.addAristaBidireccional(huanuco,lima);
        grafo.addAristaBidireccional(huanuco,pasco);

        grafo.addAristaBidireccional(ucayali,pasco);
        grafo.addAristaBidireccional(ucayali,junin);
        grafo.addAristaBidireccional(ucayali,cusco);
        grafo.addAristaBidireccional(ucayali,madreDeDios);

        grafo.addAristaBidireccional(lima,callao);
        grafo.addAristaBidireccional(lima,pasco);
        grafo.addAristaBidireccional(lima,junin);
        grafo.addAristaBidireccional(lima,huancavelica);
        grafo.addAristaBidireccional(lima,ica);

        grafo.addAristaBidireccional(pasco,junin);

        grafo.addAristaBidireccional(junin,huancavelica);
        grafo.addAristaBidireccional(junin,ayacucho);
        grafo.addAristaBidireccional(junin,cusco);

        grafo.addAristaBidireccional(ica,huancavelica);
        grafo.addAristaBidireccional(ica,ayacucho);
        grafo.addAristaBidireccional(ica,arequipa);

        grafo.addAristaBidireccional(huancavelica,ayacucho);

        grafo.addAristaBidireccional(ayacucho,cusco);
        grafo.addAristaBidireccional(ayacucho,apurimac);
        grafo.addAristaBidireccional(ayacucho,arequipa);

        grafo.addAristaBidireccional(cusco,apurimac);
        grafo.addAristaBidireccional(cusco,madreDeDios);
        grafo.addAristaBidireccional(cusco,puno);
        grafo.addAristaBidireccional(cusco,arequipa);

        grafo.addAristaBidireccional(madreDeDios,puno);

        grafo.addAristaBidireccional(apurimac,arequipa);

        grafo.addAristaBidireccional(arequipa,puno);
        grafo.addAristaBidireccional(arequipa,moquegua);

        grafo.addAristaBidireccional(puno,moquegua);
        grafo.addAristaBidireccional(puno,tacna);

        grafo.addAristaBidireccional(moquegua,tacna);

        return grafo;
    }

    public Node buscarNombre(String name){
        for (Node nodo : getNodos()) {
            if(nodo.getName()==name){
                return nodo;
            }
        }
        return null;
    }
}
