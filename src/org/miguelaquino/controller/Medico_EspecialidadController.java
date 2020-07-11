/*
creacion: 09/07/19
 */
package org.miguelaquino.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.miguelaquino.bean.Medico_Especialidad;
import org.miguelaquino.db.Conexion;
import org.miguelaquino.sistema.Principal;

public class Medico_EspecialidadController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipodeOperacion = operaciones.NINGUNO;
    private ObservableList<Medico_Especialidad> listaMedico_Especialidad;
    
    @FXML ComboBox cmbMedico;
    @FXML ComboBox cmbEspecialidad;
    @FXML ComboBox cmbHorario;
    @FXML TableView tblMedicoEspecialidad;
    @FXML TableColumn colCodigos;
    @FXML TableColumn colCodigoMedico;
    @FXML TableColumn colCodigoEspecialidad;
    @FXML TableColumn colCodigoHorario;
    @FXML Button btnNuevo;
    @FXML Button btnEditar;
    @FXML Button btnEliminar;
    @FXML Button btnReporte;
    
    public Medico_EspecialidadController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaEspecialidadesMedicos(){
        escenarioPrincipal.ventanaEspecialidadesMedicos();
    }
    
    public void menuPrincipal () {
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaMedicos(){
        escenarioPrincipal.ventanaMedicos();
    }
    
    public void ventanaEspecialidad(){
        escenarioPrincipal.ventanaEspecialidad();
    }
    
    public void ventanaHorarios(){
        escenarioPrincipal.ventanaHorarios();
    }
    
    public ObservableList<Medico_Especialidad> getEspecialidadMedico(){
        ArrayList<Medico_Especialidad> lista = new ArrayList<Medico_Especialidad>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMedico_especialidad}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Medico_Especialidad(  resultado.getInt("codigoMedicoEspecialidad"),
                                                    resultado.getInt("codigoMedico"),
                                                    resultado.getInt("codigoEspecialidad"),
                                                    resultado.getInt("codigoHoraio")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaMedico_Especialidad = FXCollections.observableList(lista);
    }
    public void desactivarControles(){
        cmbEspecialidad.setDisable(true);
        cmbHorario.setDisable(true);
        cmbMedico.setDisable(true);
    }
    
    public void activarControles(){
        cmbEspecialidad.setDisable(false);
        cmbHorario.setDisable(false);
        cmbMedico.setDisable(false);
    }
    
    public void limpiarControles(){
        cmbEspecialidad.getSelectionModel().clearSelection();
        cmbHorario.getSelectionModel().clearSelection();
        cmbMedico.getSelectionModel().clearSelection();
        tblMedicoEspecialidad.getSelectionModel().clearSelection();
    }
    
}
