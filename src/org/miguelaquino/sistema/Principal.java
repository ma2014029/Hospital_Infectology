/*
Programador: Miguel Aquino
creacion: 15-05-19
modificaciones: 12-06-19
                30-06-19
*/
package org.miguelaquino.sistema;

import java.io.InputStream;
import javafx.application.Application;// mantener siempre las librerias en orden por Standarizacion
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.miguelaquino.controller.AcercaProgramadorController;
import org.miguelaquino.controller.AreasController;
import org.miguelaquino.controller.CargosController;
import org.miguelaquino.controller.ContactoUrgenciaController;
import org.miguelaquino.controller.EspecialidadesMedicosController;
import org.miguelaquino.controller.HorariosController;
import org.miguelaquino.controller.MedicoController;
import org.miguelaquino.controller.Medico_EspecialidadController;
import org.miguelaquino.controller.MenuPrincipalController;
import org.miguelaquino.controller.PacientesController;
import org.miguelaquino.controller.ResponsableTurnoController;
import org.miguelaquino.controller.TelefonosMedicosController;
import org.miguelaquino.controller.TurnosController;

public class Principal extends Application{
    private final String PAQUETE_VISTA = "/org/miguelaquino/view/";//constante que contiene la ruta de las vistas
    private String PAQUETE_CSS = "/org/miguelaquino/resource/Estilos.css";
    private Stage escenarioPrincipal; //lo usamos en todas los controladores PAra mantener la estructura de que todo pasa en Principal y solo hacer el cambio de vista
    private Scene escena;
    
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        escenarioPrincipal.setTitle("Hospital de Infectologia");
        escenarioPrincipal.getIcons().add(new Image("/org/miguelaquino/images/iconoHospital.png"));
        escenarioPrincipal.setResizable(false);
        escenarioPrincipal.initStyle(StageStyle.UNIFIED);
        menuPrincipal();//metodo que levanta la vista
        escenarioPrincipal.show();
    }
    
    public void menuPrincipal(){// metodo para levantar la ventana Principal
        try{
            MenuPrincipalController menuPrincipal = 
                    (MenuPrincipalController)cambiarEscena("MenuPrincipalview.fxml", 960, 610);
            menuPrincipal.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void acercaProgramador(){ 
        try {
            AcercaProgramadorController acercaDe =
                   (AcercaProgramadorController)cambiarEscena("AcercaDeProgramadorView.fxml", 505, 631);
            acercaDe.setEscenarioPrincipal(this);
        } catch (Exception ex) {
           ex.printStackTrace();
        }
    }
    
    
    public void ventanaMedicos(){ 
        try{ 
            MedicoController medicoController = (MedicoController)cambiarEscena("MedicoView.fxml",1050,610);
            medicoController.setEscenarioPrincipal(this);
        }catch(Exception e){ 
            e.printStackTrace();
        }
    }
    
    public void ventanaAreas (){
        try{
            AreasController areas = (AreasController)cambiarEscena("AreasView.fxml", 960, 610); 
            areas.setEscenarioPrincipal(this);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void ventanaCargos () {
        try{
            CargosController cargos = (CargosController)cambiarEscena("CargosView.fxml", 960, 610);
            cargos.setEscenarioPrincipal(this);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void ventanaContactoU () {
        try{
            ContactoUrgenciaController contacto= (ContactoUrgenciaController)cambiarEscena("ContactoEmergenciaView.fxml", 1020, 587);
            contacto.setEscenarioPrincipal(this);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void ventanaEspecialidad (){
        try{
            EspecialidadesMedicosController especialidades = (EspecialidadesMedicosController)cambiarEscena("EspecialidadesView.fxml", 960, 610);
            especialidades.setEscenarioPrincipal(this);
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void ventanaPacientes (){
        try{
            PacientesController pacientes = (PacientesController)cambiarEscena("PacientesView.fxml", 1050, 610);
            pacientes.setEscenarioPrincipal(this);
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void ventanaTelefonosMedicos (){
        try{
            TelefonosMedicosController telefono = (TelefonosMedicosController)cambiarEscena("TelefonosMedicosView.fxml", 1020, 587);
            telefono.setEscenarioPrincipal(this);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void ventanaHorarios (){
        try{
            HorariosController horario = (HorariosController)cambiarEscena("HorariosMedicosView.fxml",960,610);
            horario.setEscenarioPrincipal(this);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void ventanaResponsablesTurnos(){
        try {
            ResponsableTurnoController turno= (ResponsableTurnoController)cambiarEscena("TurnoResponsableView.fxml",1050,610);
            turno.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaEspecialidadesMedicos(){
        try {
            Medico_EspecialidadController especialidad = (Medico_EspecialidadController)cambiarEscena("Medico_Especialidad.View.fxml",1019,610);
            especialidad.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaTurnos(){
        try {
            TurnosController turnos = (TurnosController)cambiarEscena("TurnosView.fxml",1050,632);
            turnos.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Initializable cambiarEscena(String fxml, int ancho,int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader(); 
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);// es la encargada de ir a buscar la vista digitalmente
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());//lo que hace es crear el cargador del archivo 
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto);
        escena.getStylesheets().add(PAQUETE_CSS);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();//maneja el tamaño especifico del archivo
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
    }
    
    public static void main(String[] args){
        launch(args);
    }

}
