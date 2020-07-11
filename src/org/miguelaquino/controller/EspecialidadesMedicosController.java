/*
 Creación: 1/06/19
modificacion 5/6/19
             8/6/19
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
import org.miguelaquino.bean.Especialidades;
import org.miguelaquino.db.Conexion;
import org.miguelaquino.sistema.Principal;

public class EspecialidadesMedicosController implements Initializable{
   private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipodeOperacion = operaciones.NINGUNO;
    private ObservableList<Especialidades> listaEspecialidades;
    
    @FXML TextField txtNombre;
    @FXML TableView tblEspecialidades;
    @FXML TableColumn colCodigos;
    @FXML TableColumn colNombres;
    @FXML Button btnReporte;
    @FXML Button btnEliminar;
    @FXML Button btnNuevo;
    @FXML Button btnEditar;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

    public EspecialidadesMedicosController() {
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaEspecialidad(){
        escenarioPrincipal.ventanaEspecialidad();
    }
    
    public void desactivarControles(){
        txtNombre.setEditable(false);
        
    }
    
    public void activarControles(){
        txtNombre.setEditable(true);
        
    }
    
    public void limpiarControles() {
        txtNombre.setText("");
        tblEspecialidades.getSelectionModel().clearSelection();
        
    }
    
    public void nuevo() {
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
                if (txtNombre.getText().isEmpty()!= true){
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
        Especialidades registro = new Especialidades();
        registro.setNombreEspecialidad(txtNombre.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarEspecialidad(?)}");
            procedimiento.setString(1, registro.getNombreEspecialidad());
            procedimiento.execute();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public ObservableList<Especialidades> getEspecialidad(){
        ArrayList<Especialidades> lista = new ArrayList<Especialidades>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarEspecialidad}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Especialidades(resultado.getInt("codigoEspecialidad"), 
                                   resultado.getString("nombreEspecialidad")));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return listaEspecialidades = FXCollections.observableList(lista);
    }
    
    public void cargarDatos(){
        tblEspecialidades.setItems(getEspecialidad());
        colCodigos.setCellValueFactory(new PropertyValueFactory<Especialidades, Integer>("codigoEspecialidad"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Especialidades, String>("nombreEspecialidad"));
        
    }
    
    public void seleccionarElemento(){
        if(tblEspecialidades.getSelectionModel().getSelectedItem()!= null){
           txtNombre.setText(((Especialidades)tblEspecialidades.getSelectionModel().getSelectedItem()).getNombreEspecialidad());
        }
    }
    
    public Especialidades buscarEspecialidad( int codigoEspecialidad){
        Especialidades resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMedico_Especialidad(?)}");
            procedimiento.setInt(1, codigoEspecialidad);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Especialidades (registro.getInt("codigoEspecialidad"),
                                                registro.getString("nombreEspecialidad"));
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
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipodeOperacion = operaciones.GUARDAR;//pendiente
            break;
            
            default:
                if(tblEspecialidades.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Especialidad Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarEspecialidad(?)}");
                            procedimiento.setInt(1, ((Especialidades)tblEspecialidades.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
                            procedimiento.execute();
                            listaEspecialidades.remove(tblEspecialidades.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }  
        }
    }
    
    public void editar(){
        switch(tipodeOperacion) {
            case NINGUNO:
                if (tblEspecialidades.getSelectionModel().getSelectedItem()!= null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarEspecialidad(?,?)}");
            Especialidades registro = (Especialidades)tblEspecialidades.getSelectionModel().getSelectedItem();
            registro.setNombreEspecialidad(txtNombre.getText());
            procedimiento.setInt(1, registro.getCodigoEspecialidad());
            procedimiento.setString(2, registro.getNombreEspecialidad());
            procedimiento.execute();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        limpiarControles();
    }
}
