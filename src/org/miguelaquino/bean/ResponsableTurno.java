/*
creacion: 8/7/19
 */
package org.miguelaquino.bean;

public class ResponsableTurno {
    private int codigoResponsable;
    private String nombresResponsable;
    private String apellidosResponsable;
    private String telefonoPersonal;
    private int codigoCargo; //fk
    private int codigoArea; //fk
    
    public ResponsableTurno() {
    }

    public ResponsableTurno(int codigoResponsable, String nombresResponsable, String apellidosResponsable, String telefonoPersonal, int codigoCargo, int codigoArea) {
        this.codigoResponsable = codigoResponsable;
        this.nombresResponsable = nombresResponsable;
        this.apellidosResponsable = apellidosResponsable;
        this.telefonoPersonal = telefonoPersonal;
        this.codigoCargo = codigoCargo;
        this.codigoArea = codigoArea;
    }

    public int getCodigoResponsable() {
        return codigoResponsable;
    }

    public void setCodigoResponsable(int codigoResponsable) {
        this.codigoResponsable = codigoResponsable;
    }

    public String getNombresResponsable() {
        return nombresResponsable;
    }

    public void setNombresResponsable(String nombresResponsable) {
        this.nombresResponsable = nombresResponsable;
    }

    public String getApellidosResponsable() {
        return apellidosResponsable;
    }

    public void setApellidosResponsable(String apellidosResponsable) {
        this.apellidosResponsable = apellidosResponsable;
    }

    public String getTelefonoPersonal() {
        return telefonoPersonal;
    }

    public void setTelefonoPersonal(String telefonoPersonal) {
        this.telefonoPersonal = telefonoPersonal;
    }

    public int getCodigoCargo() {
        return codigoCargo;
    }

    public void setCodigoCargo(int codigoCargo) {
        this.codigoCargo = codigoCargo;
    }

    public int getCodigoArea() {
        return codigoArea;
    }

    public void setCodigoArea(int codigoArea) {
        this.codigoArea = codigoArea;
    }
    
    
}
