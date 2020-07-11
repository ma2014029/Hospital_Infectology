/*
creacion: 4/7/19
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.miguelaquino.bean.Horarios;
import org.miguelaquino.db.Conexion;
import org.miguelaquino.sistema.Principal;

public class HorariosController implements Initializable{
    
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipodeOperacion = operaciones.NINGUNO;
    private ObservableList<Horarios> listaHorarios;
    
    @FXML Button btnNuevo;
    @FXML Button btnEliminar;
    @FXML Button btnEditar;
    @FXML Button btnReporte;
    @FXML CheckBox ckxLunes;
    @FXML CheckBox ckxMartes;
    @FXML CheckBox ckxMiercoles;
    @FXML CheckBox ckxJueves;
    @FXML CheckBox ckxViernes;
    @FXML ComboBox cmbEntradaHoras;
    @FXML ComboBox cmbSalidaHoras;
    @FXML ComboBox cmbEntradaMinutos;
    @FXML ComboBox cmbSalidaMinutos;
    @FXML TableView tblHorarios;
    @FXML TableColumn colCodigoHorario;
    @FXML TableColumn colHorarioEntrada;
    @FXML TableColumn colHorarioSalida;
    @FXML TableColumn colLunes;
    @FXML TableColumn colMartes;
    @FXML TableColumn colMiercoles;
    @FXML TableColumn colJueves;
    @FXML TableColumn colViernes;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      cargarDatos();
      ObservableList<String> horas = FXCollections.observableArrayList("00","01","02","03","04","05","06","07","08","09",
              "10","11","12","13","14","15","16","17","18","19","20","21","22","23");
      ObservableList<String> minutos = FXCollections.observableArrayList("00:00","05:00","10:00","15:00","20:00","25:00","30:00","35:00",
              "40:00","45:00","50:00","55:00");
        cmbEntradaHoras.setItems(horas);
        cmbSalidaHoras.setItems(horas);
        cmbEntradaMinutos.setItems(minutos);
        cmbSalidaMinutos.setItems(minutos);
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaHorarios(){
        escenarioPrincipal.ventanaHorarios();
    }
    
    public void menuPrincipal () {
        escenarioPrincipal.menuPrincipal();
    }
    
    public void activarControles(){
        ckxLunes.setDisable(false);
        ckxMartes.setDisable(false);
        ckxMiercoles.setDisable(false);
        ckxJueves.setDisable(false);
        ckxViernes.setDisable(false);
        cmbEntradaHoras.setDisable(false);
        cmbEntradaMinutos.setDisable(false);
        cmbSalidaHoras.setDisable(false);
        cmbSalidaMinutos.setDisable(false);
    }
    
    public void desactivarControles(){
        ckxLunes.setDisable(true);
        ckxMartes.setDisable(true);
        ckxMiercoles.setDisable(true);
        ckxJueves.setDisable(true);
        ckxViernes.setDisable(true);
        cmbEntradaHoras.setDisable(true);
        cmbEntradaMinutos.setDisable(true);
        cmbSalidaHoras.setDisable(true);
        cmbSalidaMinutos.setDisable(true);
    }
    
    public void limpiarControles(){
        ckxLunes.setSelected(false);
        ckxMartes.setSelected(false);
        ckxMiercoles.setSelected(false);
        ckxJueves.setSelected(false);
        ckxViernes.setSelected(false);
        cmbEntradaHoras.getSelectionModel().clearSelection();
        cmbEntradaMinutos.getSelectionModel().clearSelection();
        cmbSalidaHoras.getSelectionModel().clearSelection();
        cmbSalidaMinutos.getSelectionModel().clearSelection();
        tblHorarios.getSelectionModel().clearSelection();
    }
    
    public void nuevo(){
        switch(tipodeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipodeOperacion = operaciones.GUARDAR;
            break;
            
            case GUARDAR:
                if (cmbEntradaHoras.getSelectionModel().isEmpty()!= true && cmbEntradaMinutos.getSelectionModel().isEmpty()!= true 
                        && cmbSalidaHoras.getSelectionModel().isEmpty()!= true && cmbSalidaMinutos.getSelectionModel().isEmpty()!= true){
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
    
    public void guardar() {
        Horarios registro = new Horarios();
        registro.setHorarioEntrada(cmbEntradaHoras.getSelectionModel().getSelectedItem().toString() +":"+cmbEntradaMinutos.getSelectionModel().getSelectedItem().toString());
        registro.setHoraioSalida(cmbSalidaHoras.getSelectionModel().getSelectedItem().toString() +":"+cmbSalidaMinutos.getSelectionModel().getSelectedItem().toString());
        registro.setLunes(ckxLunes.isSelected());
        registro.setMartes(ckxMartes.isSelected());
        registro.setMiercoles(ckxMiercoles.isSelected());
        registro.setJueves(ckxJueves.isSelected());
        registro.setViernes(ckxViernes.isSelected());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarHorario(?,?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getHorarioEntrada());
            procedimiento.setString(2, registro.getHoraioSalida());
            procedimiento.setBoolean(3, registro.getLunes());
            procedimiento.setBoolean(4, registro.getMartes());
            procedimiento.setBoolean(5, registro.getMiercoles());
            procedimiento.setBoolean(6, registro.getJueves());
            procedimiento.setBoolean(7, registro.getViernes());
            procedimiento.execute();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public ObservableList<Horarios> getHorarios(){
    ArrayList<Horarios> lista = new ArrayList<Horarios>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarHorarios}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Horarios( resultado.getInt("codigoHorario"),
                                        resultado.getString("horarioEntrada"),
                                        resultado.getString("horarioSalida"),
                                        resultado.getBoolean("lunes"),
                                        resultado.getBoolean("martes"),
                                        resultado.getBoolean("miercoles"),
                                        resultado.getBoolean("jueves"),
                                        resultado.getBoolean("viernes")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaHorarios = FXCollections.observableList(lista);
    }
    
    public void cargarDatos(){
    tblHorarios.setItems(getHorarios());
    colCodigoHorario.setCellValueFactory(new PropertyValueFactory<Horarios, Integer>("codigoHorario"));
    colHorarioEntrada.setCellValueFactory(new PropertyValueFactory<Horarios,String>("horarioEntrada"));
    colHorarioSalida.setCellValueFactory(new PropertyValueFactory<Horarios, String>("horaioSalida"));
    colLunes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("lunes"));
    colMartes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("martes"));
    colMiercoles.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("miercoles"));
    colJueves.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("jueves"));
    colViernes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("viernes"));
    }  
    
    public void seleccionarElemento(){
    
    }
    
    public Horarios buscarHorario(int codigoHorario) {
        Horarios resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarHorario(?)}");
            procedimiento.setInt(1, codigoHorario);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Horarios( registro.getInt("codigoHorario"),
                                        registro.getString("horarioEntrada"),
                                        registro.getString("horarioSalida"),
                                        registro.getBoolean("lunes"),
                                        registro.getBoolean("martes"),
                                        registro.getBoolean("miercoles"),
                                        registro.getBoolean("jueves"),
                                        registro.getBoolean("viernes")); 
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                tipodeOperacion = operaciones.GUARDAR;
            break;
            
            default:
                if(tblHorarios.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", 
                            "Eliminar Area", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarHorario(?)}");
                            procedimiento.setInt(1, ((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getCodigoHorario());
                            procedimiento.execute();
                            listaHorarios.remove(tblHorarios.getSelectionModel().getSelectedIndex());
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
                if (tblHorarios.getSelectionModel().getSelectedItem()!= null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarHorario(?,?,?,?,?,?,?,?)}");
            Horarios registro = (Horarios)tblHorarios.getSelectionModel().getSelectedItem();
            registro.setHorarioEntrada(cmbEntradaHoras.getSelectionModel().getSelectedItem().toString() +":"+cmbEntradaMinutos.getSelectionModel().getSelectedItem().toString());
            registro.setHoraioSalida(cmbSalidaHoras.getSelectionModel().getSelectedItem().toString() +":"+cmbSalidaMinutos.getSelectionModel().getSelectedItem().toString());
            registro.setLunes(ckxLunes.isSelected());
            registro.setMartes(ckxMartes.isSelected());
            registro.setMiercoles(ckxMiercoles.isSelected());
            registro.setJueves(ckxJueves.isSelected());
            registro.setViernes(ckxViernes.isSelected());
            procedimiento.setInt(1, registro.getCodigoHorario());
            procedimiento.setString(2, registro.getHorarioEntrada());
            procedimiento.setString(3, registro.getHoraioSalida());
            procedimiento.setBoolean(4, registro.getLunes());
            procedimiento.setBoolean(5, registro.getMartes());
            procedimiento.setBoolean(6, registro.getMiercoles());
            procedimiento.setBoolean(7, registro.getJueves());
            procedimiento.setBoolean(8, registro.getViernes());
            procedimiento.execute();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        limpiarControles();
    }
}
