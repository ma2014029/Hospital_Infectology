/*
creacion: 4/06/19
 */
package org.miguelaquino.bean;

public class Area {
    private int codigoArea;
    private String nombreArea;

    public Area() {
    }

    public Area(int codigoArea, String nombreArea) {
        this.codigoArea = codigoArea;
        this.nombreArea = nombreArea;
    }

    public int getCodigoArea() {
        return codigoArea;
    }

    public void setCodigoArea(int codigoArea) {
        this.codigoArea = codigoArea;
    }

    public String getNombreArea() {
        return nombreArea;
    }

    public void setNombreArea(String nombreArea) {
        this.nombreArea = nombreArea;
    }
    
    
}
