package com.zesko.geoapify_dijkstra.model;

public class Node {
    private String name;
    private double latitud;
    private double longitud;

    public Node(){}
    public Node(String n,double la,double lo){
        this.name=n;
        this.latitud=la;
        this.longitud=lo;
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj)
            return true;
        if(obj == null || getClass() != obj.getClass())
            return false;
        Node node = (Node) obj;
        return name.equals(node.name);
    }

    @Override
    public int hashCode(){
        return name.hashCode();
    }

    public String getName(){return name;}
    public double getLatitud(){return latitud;}
    public double getLongitud(){return longitud;}

    public void setName(String na){name=na;}
    public void setLatitud(double la){latitud=la;}
    public void setLongitud(double lo){longitud=lo;}
}
