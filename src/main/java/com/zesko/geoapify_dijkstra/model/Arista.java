package com.zesko.geoapify_dijkstra.model;

public class Arista {
    private Node inicio;
    private Node destino;
    private double distancia;

    public Arista(){}
    public Arista(Node in,Node de,double di){
        this.inicio=in;
        this.destino=de;
        this.distancia=di;
    }

    public Node getInicio(){return inicio;}
    public Node getDestino(){return destino;}
    public double getDistancia(){return distancia;}

    public void setInicio(Node in){inicio=in;}
    public void setDestino(Node de){destino=de;}
    public void setDistancia(double di){distancia=di;}
}
