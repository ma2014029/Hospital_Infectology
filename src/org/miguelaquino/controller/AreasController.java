/*
Creacion: 1/06/2019
modificacion: 5/6/19
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
import org.miguelaquino.bean.Area;
import org.miguelaquino.db.Conexion;
import org.miguelaquino.sistema.Principal;

public class AreasController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipodeOperacion = operaciones.NINGUNO;
    private ObservableList<Area> listaArea;
    
    @FXML TextField txtNombreArea;
    @FXML TableView tblArea;
    @FXML TableColumn colCodigo;
    @FXML TableColumn colNombres;
    @FXML Button btnNuevo;
    @FXML Button btnEliminar;
    @FXML Button btnEditar;
    @FXML Button btnReporte;

    public AreasController() {
    }
    
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
    
    public void ventanaAreas(){
        escenarioPrincipal.ventanaAreas();
    }
    
    public void menuPrincipal () {
        escenarioPrincipal.menuPrincipal();
    }
    
    public ObservableList<Area> getArea(){
        ArrayList<Area> lista = new ArrayList<Area>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_listarAreas}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Area(resultado.getInt("codigoArea"), 
                                   resultado.getString("nombreArea")));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return listaArea = FXCollections.observableList(lista);
    }
   
    public void desactivarControles(){
        txtNombreArea.setEditable(false);
    }
    
    public void activarControles(){
        txtNombreArea.setEditable(true);
    }
    
    public void limpiarControles(){
        txtNombreArea.setText("");
        tblArea.getSelectionModel().clearSelection();
    }
    
    public void nuevo(){ //opcion  de cancelar Arreglar
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
                if (txtNombreArea.getText().isEmpty()!= true){
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
        Area registro = new Area();
        registro.setNombreArea(txtNombreArea.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarArea(?)}");
            procedimiento.setString(1, registro.getNombreArea());
            procedimiento.execute();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void cargarDatos(){
        tblArea.setItems(getArea());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Area, Integer>("codigoArea"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Area, String>("nombreArea"));
    }
    
    public void seleccionarElemento(){
        if(tblArea.getSelectionModel().getSelectedItem()!= null){
            txtNombreArea.setText(((Area)tblArea.getSelectionModel().getSelectedItem()).getNombreArea());
        }
    }
    
    public Area buscarArea(int codigoArea){
        Area resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarArea(?)}");
            procedimiento.setInt(1, codigoArea);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Area(registro.getInt("codigoArea"),
                                    registro.getString("nombreArea"));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return resultado;
    }
    
    public void eliminarArea(){
        switch(tipodeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipodeOperacion = operaciones.GUARDAR;
            break;
            
            default:
                if(tblArea.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", 
                            "Eliminar Area", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarArea(?)}");
                            procedimiento.setInt(1, ((Area)tblArea.getSelectionModel().getSelectedItem()).getCodigoArea());
                            procedimiento.execute();
                            listaArea.remove(tblArea.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento");
                }
                break;
        }
        limpiarControles();
    }
    
    public void editar(){//crear la opcioon cancelar
        switch(tipodeOperacion) {
            case NINGUNO:
                if (tblArea.getSelectionModel().getSelectedItem()!= null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarArea(?,?)}");
            Area registro = (Area)tblArea.getSelectionModel().getSelectedItem();
            registro.setNombreArea(txtNombreArea.getText());
            procedimiento.setInt(1, registro.getCodigoArea());
            procedimiento.setString(2, registro.getNombreArea());
            procedimiento.execute();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        limpiarControles();
    }
}
 