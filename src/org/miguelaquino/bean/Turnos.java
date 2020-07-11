/*
creacion: 8/7/19
 */
package org.miguelaquino.bean;

import java.sql.Date;

public class Turnos {
    private int codigoTurno;
    private Date fechaTurno;
    private Date fechaCita;
    private Double valorCita;
    private int codigoMedicoEspecialidad;//cambiar
    private int codigoResponsable;    //fijarse
    private int codigoPaciente;    
        
    public Turnos() {
    }

    public Turnos(int codigoTurno, Date fechaTurno, Date fechaCita, Double valorCita, int codigoMedicoEspecialidad, int codigoResponsable, int codigoPaciente) {
        this.codigoTurno = codigoTurno;
        this.fechaTurno = fechaTurno;
        this.fechaCita = fechaCita;
        this.valorCita = valorCita;
        this.codigoMedicoEspecialidad = codigoMedicoEspecialidad;
        this.codigoResponsable = codigoResponsable;
        this.codigoPaciente = codigoPaciente;
    }

    public int getCodigoTurno() {
        return codigoTurno;
    }

    public void setCodigoTurno(int codigoTurno) {
        this.codigoTurno = codigoTurno;
    }

    public Date getFechaTurno() {
        return fechaTurno;
    }

    public void setFechaTurno(Date fechaTurno) {
        this.fechaTurno = fechaTurno;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public Double getValorCita() {
        return valorCita;
    }

    public void setValorCita(Double valorCita) {
        this.valorCita = valorCita;
    }

    public int getCodigoMedicoEspecialidad() {
        return codigoMedicoEspecialidad;
    }

    public void setCodigoMedicoEspecialidad(int codigoMedicoEspecialidad) {
        this.codigoMedicoEspecialidad = codigoMedicoEspecialidad;
    }

    public int getCodigoResponsable() {
        return codigoResponsable;
    }

    public void setCodigoResponsable(int codigoResponsable) {
        this.codigoResponsable = codigoResponsable;
    }

    public int getCodigoPaciente() {
        return codigoPaciente;
    }

    public void setCodigoPaciente(int codigoPaciente) {
        this.codigoPaciente = codigoPaciente;
    }

    public void getFechaTurno(String text) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void getFechaCita(String text) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void getValorCita(String text) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
