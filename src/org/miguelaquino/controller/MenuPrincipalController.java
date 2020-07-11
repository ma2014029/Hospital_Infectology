/*
Programador: Miguel Aquino
creacion: 15-05-19
    crear el cambio de escena 20-28/05/19
 */
package org.miguelaquino.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.miguelaquino.sistema.Principal;


public class MenuPrincipalController implements Initializable {
    private Principal escenarioPrincipal;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaMedicos() {
        escenarioPrincipal.ventanaMedicos();
    }
    
    public void acercaProgramador (){
        escenarioPrincipal.acercaProgramador();
    }
    
    public void ventanaAreas(){
        escenarioPrincipal.ventanaAreas();
    }
    
    public void ventanaCargos() {
        escenarioPrincipal.ventanaCargos();
    }
    
    public void ventanaContactoU() {
        escenarioPrincipal.ventanaContactoU();
    }
    
    public void ventanaEspecialidad(){
        escenarioPrincipal.ventanaEspecialidad();
    }
    
    public void ventanaPacientes(){
        escenarioPrincipal.ventanaPacientes();
        
    }
    
    public void ventanaTelefonosMedicos(){
        escenarioPrincipal.ventanaTelefonosMedicos();
    }
    
    public void ventanaHorarios(){
        escenarioPrincipal.ventanaHorarios();
    }
    
    public void ventanaResponsablesTurnos(){
        escenarioPrincipal.ventanaResponsablesTurnos();
    }
    
    public void ventanaEspecialidadesMedicos(){
        escenarioPrincipal.ventanaEspecialidadesMedicos();
    }
    
    public void ventanaTurnos(){
        escenarioPrincipal.ventanaTurnos();
    }
}
