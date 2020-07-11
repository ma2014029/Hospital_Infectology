/*
 9/7/19
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
import javafx.scene.control.TextField;
import org.miguelaquino.bean.ResponsableTurno;
import org.miguelaquino.db.Conexion;
import org.miguelaquino.sistema.Principal;


public class ResponsableTurnoController implements Initializable{
    
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipodeOperacion = operaciones.NINGUNO;
    private ObservableList<ResponsableTurno> listaResponsable;
    
    @FXML TextField txtNombres;
    @FXML TextField txtApellidos;
    @FXML TextField txtTelefono;
    @FXML ComboBox cmbArea;
    @FXML ComboBox cmbCargo;
    @FXML TableView tblTurno;
    @FXML TableColumn colCodigoTurno;
    @FXML TableColumn colNombres;
    @FXML TableColumn colApellidos;
    @FXML TableColumn colTelefonos;
    @FXML TableColumn colArea;
    @FXML TableColumn colCargo;
    @FXML Button btnNuevo;
    @FXML Button btnEditar;
    @FXML Button btnEliminar;
    @FXML Button btnReporte;

    public ResponsableTurnoController() {
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //cargarDatos();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaResponsablesTurnos(){
        escenarioPrincipal.ventanaResponsablesTurnos();
    }
    
    public void ventanaAreas(){
        escenarioPrincipal.ventanaAreas();
    }
    
    public void menuPrincipal () {
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaCargos(){
        escenarioPrincipal.ventanaCargos();
    }
    
    public ObservableList<ResponsableTurno> getResponsable(){
        ArrayList<ResponsableTurno> lista = new ArrayList<ResponsableTurno>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarResponsableTurno}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ResponsableTurno( resultado.getInt("codigoResponsable"),
                                                resultado.getString("nombresResponsable"),
                                                resultado.getString("apellidosResponsable"),
                                                resultado.getString("telefonoPersonal"),
                                                resultado.getInt("codigoCargo"),
                                                resultado.getInt("codigoArea")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaResponsable = FXCollections.observableList(lista);
    }
    
    public void desactivarControles(){
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtTelefono.setEditable(false);
        cmbArea.setDisable(true);
        cmbCargo.setDisable(true);
    }
    
    public void activarControles(){
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtTelefono.setEditable(true);
        cmbArea.setDisable(false);
        cmbCargo.setDisable(false);
    }
    
    public void limpiarControles(){
        txtNombres.setText("");
        txtApellidos.setText("");
        txtTelefono.setText("");
        cmbArea.getSelectionModel().clearSelection();
        cmbCargo.getSelectionModel().clearSelection();
        tblTurno.getSelectionModel().clearSelection();
    }
}
