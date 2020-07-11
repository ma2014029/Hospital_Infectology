/*
creacion: 04/07/19
 */
package org.miguelaquino.bean;

public class Horarios {
    
    private int codigoHorario;
    private String horarioEntrada;
    private String horarioSalida;
    private Boolean lunes;
    private Boolean martes;
    private Boolean miercoles;
    private Boolean jueves;
    private Boolean viernes;
    
    public Horarios() {
    }

    public Horarios(int codigoHorario, String horarioEntrada, String horaioSalida, Boolean lunes, Boolean martes, Boolean miercoles, Boolean jueves, Boolean viernes) {
        this.codigoHorario = codigoHorario;
        this.horarioEntrada = horarioEntrada;
        this.horarioSalida = horaioSalida;
        this.lunes = lunes;
        this.martes = martes;
        this.miercoles = miercoles;
        this.jueves = jueves;
        this.viernes = viernes;
    }

    public int getCodigoHorario() {
        return codigoHorario;
    }

    public void setCodigoHorario(int codigoHorario) {
        this.codigoHorario = codigoHorario;
    }

    public String getHorarioEntrada() {
        return horarioEntrada;
    }

    public void setHorarioEntrada(String horarioEntrada) {
        this.horarioEntrada = horarioEntrada;
    }

    public String getHoraioSalida() {
        return horarioSalida;
    }

    public void setHoraioSalida(String horaioSalida) {
        this.horarioSalida = horaioSalida;
    }

    public Boolean getLunes() {
        return lunes;
    }

    public void setLunes(Boolean lunes) {
        this.lunes = lunes;
    }

    public Boolean getMartes() {
        return martes;
    }

    public void setMartes(Boolean martes) {
        this.martes = martes;
    }

    public Boolean getMiercoles() {
        return miercoles;
    }

    public void setMiercoles(Boolean miercoles) {
        this.miercoles = miercoles;
    }

    public Boolean getJueves() {
        return jueves;
    }

    public void setJueves(Boolean jueves) {
        this.jueves = jueves;
    }

    public Boolean getViernes() {
        return viernes;
    }

    public void setViernes(Boolean viernes) {
        this.viernes = viernes;
    }
    
    
    
}
