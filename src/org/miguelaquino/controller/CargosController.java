/*
 creación: 1/06/19
modificacion: 5/6/19
              8/6/19
              11/6/19
              19/6/19
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.miguelaquino.bean.Cargo;
import org.miguelaquino.db.Conexion;
import org.miguelaquino.sistema.Principal;

public class CargosController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipodeOperacion = operaciones.NINGUNO;
    private ObservableList<Cargo> listaCargo;
    
    @FXML TextField txtNombre;
    @FXML TableView tblCargo;
    @FXML TableColumn colCodigo;
    @FXML TableColumn colNombre;
    @FXML Button btnNuevo;
    @FXML Button btnEliminar;
    @FXML Button btnEditar;
    @FXML Button btnReporte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaCargos(){
        escenarioPrincipal.ventanaCargos();
    }
    
    public void menuPrincipal (){
        escenarioPrincipal.menuPrincipal();
    }
    
    public CargosController(){
    }
    
    public void desactivarControles(){
        txtNombre.setEditable(false);
    }
    
    public void activarControles(){
        txtNombre.setEditable(true);
    }
    
    public void limpiarControles(){
        txtNombre.setText("");
        tblCargo.getSelectionModel().clearSelection();
    }
    
    public void nuevo(){
        switch(tipodeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipodeOperacion = operaciones.GUARDAR;
            break;
            
            case GUARDAR:
                if(txtNombre.getText().isEmpty()!=true){
                    guardar();
                    desactivarControles();
                    limpiarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    tipodeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                }else{
                    JOptionPane.showConfirmDialog(null, "Por favor complete los datos requeridos", "Error!!", JOptionPane.DEFAULT_OPTION);
                }
            break;
        }
    }
    
    public void guardar(){
        Cargo registro = new Cargo();
        registro.setNombreCargo(txtNombre.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarCargo(?)}");
            procedimiento.setString(1, registro.getNombreCargo());
            procedimiento.execute();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
     public ObservableList<Cargo> getCargo(){
        ArrayList<Cargo> lista = new ArrayList<Cargo>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarCargos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Cargo(resultado.getInt("codigoCargo"), 
                                   resultado.getString("nombreCargo")));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return listaCargo = FXCollections.observableList(lista);
    }
    
    public void cargarDatos(){
        tblCargo.setItems(getCargo());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Cargo, Integer>("codigoCargo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Cargo, String>("nombreCargo"));
    }
    
    public void seleccionarElemento(){
        if(tblCargo.getSelectionModel().getSelectedItem()!= null){
            txtNombre.setText(((Cargo)tblCargo.getSelectionModel().getSelectedItem()).getNombreCargo());
        }
    }
    
    public Cargo buscarCargo(int codigoCargo){
        Cargo resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarCargo(?)}");
            procedimiento.setInt(1, codigoCargo);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Cargo (registro.getInt("codigoCargo"),
                                       registro.getString("nombreCargo"));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return resultado;
    }
    
    public void eliminar(){
        switch(tipodeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipodeOperacion = operaciones.GUARDAR;//pendiente
            break;
            
            default: 
                if(tblCargo.getSelectionModel().getSelectedItem() != null){
                  int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Cargo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                  if(respuesta == JOptionPane.YES_OPTION){
                      try{
                          PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCargo(?)}");
                          procedimiento.setInt(1, ((Cargo)tblCargo.getSelectionModel().getSelectedItem()).getCodigoCargo());
                          procedimiento.execute();
                          listaCargo.remove(tblCargo.getSelectionModel().getFocusedIndex());
                          limpiarControles();
                      }catch(Exception ex){
                          ex.printStackTrace();
                      }
                  }
                }else{
                    JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento");
                } 
        }
        limpiarControles();
    }
    
    public void editar(){
        switch(tipodeOperacion) {
            case NINGUNO:
                if (tblCargo.getSelectionModel().getSelectedItem()!= null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");//hay que hacer cancelar
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipodeOperacion = operaciones.ACTUALIZAR;
                }else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                    
                }
            break;
            
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipodeOperacion = operaciones.NINGUNO;
                cargarDatos();
            break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarCargo(?,?)}");
            Cargo registro = (Cargo)tblCargo.getSelectionModel().getSelectedItem();
            registro.setNombreCargo(txtNombre.getText());
            procedimiento.setInt(1, registro.getCodigoCargo());
            procedimiento.setString(2, registro.getNombreCargo());
            procedimiento.execute();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        limpiarControles();
    }
}
