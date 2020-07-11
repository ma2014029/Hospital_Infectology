
package org.miguelaquino.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.miguelaquino.sistema.Principal;


public class AcercaProgramadorController implements Initializable{
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
    
    public void acercaProgramador (){
        escenarioPrincipal.acercaProgramador();
    }
    
    public void menuPrincipal () {
        escenarioPrincipal.menuPrincipal();
    }
}
