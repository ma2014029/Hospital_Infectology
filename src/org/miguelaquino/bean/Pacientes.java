/*
creacion: 5/6/19
 */
package org.miguelaquino.bean;

import java.util.Date;



public class Pacientes {
    private int codigoPaciente;
    private String apellidos;
    private String nombres;
    private Date fechadeNacimiento;
    private int edad;
    private String direccion;
    private String ocupacion;
    private String sexo ;
    

    public Pacientes() {
    }

    public Pacientes(int codigoPaciente, String apellidos, String nombres, Date fechadeNacimiento, int edad, String direccion, String ocupacion, String sexo) {
        this.codigoPaciente = codigoPaciente;
        this.apellidos = apellidos;
        this.nombres = nombres;
        this.fechadeNacimiento = fechadeNacimiento;
        this.edad = edad;
        this.direccion = direccion;
        this.ocupacion = ocupacion;
        this.sexo = sexo;
    }

    public int getCodigoPaciente() {
        return codigoPaciente;
    }

    public void setCodigoPaciente(int codigoPaciente) {
        this.codigoPaciente = codigoPaciente;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Date getFechadeNacimiento() {
        return fechadeNacimiento;
    }

    public void setFechadeNacimiento(Date fechadeNacimiento) {
        this.fechadeNacimiento = fechadeNacimiento;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
    
    public String toString (){
        return getCodigoPaciente()+ " | " +getNombres() + "," + getApellidos();
        
    }
    
}
