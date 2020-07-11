/*
creacion: 1/06/19
modificacion: 7/6/19
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;//
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.miguelaquino.bean.Medico;
import org.miguelaquino.bean.TelefonosMedicos;
import org.miguelaquino.db.Conexion;
import org.miguelaquino.sistema.Principal;


public class TelefonosMedicosController  implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR,ELIMINAR,EDITAR, ACTUALIZAR,CANCELAR, NINGUNO};
    private operaciones tipodeOperaciones = operaciones.NINGUNO;
    private ObservableList<TelefonosMedicos> listaTelefonosMedicos;
    private ObservableList<Medico> listaMedico;
    
    @FXML  private ComboBox cmbCodigoMedico;
    //TextField
    @FXML private TextField txtTelefonoPersonal;
    @FXML private TextField txtTelefonoTrabajo;
    //tableView y columns
    @FXML private TableView tblTelefonosMedicos;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colTelefonoPersonal;
    @FXML private TableColumn colTelefonoTrabajo;
    @FXML private TableColumn colCodigoMedico;
    //Buttons
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoMedico.setItems(getMedico());
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
    
    public void ventanaTelefonosMedicos () {
        escenarioPrincipal.ventanaTelefonosMedicos();
    }
    
    public void desactivarControles(){//evemto lo que puedo hacer con el objeto
        txtTelefonoPersonal.setEditable(false);// propiedad son caracterizticas del objeto 
        txtTelefonoTrabajo.setEditable(false);
        cmbCodigoMedico.setDisable(true);
    }
    
    public void activarControles(){
        txtTelefonoPersonal.setEditable(true);
        txtTelefonoTrabajo.setEditable(true);
        cmbCodigoMedico.setDisable(false);
    }
    
    public void limpiarControles(){
        txtTelefonoPersonal.setText("");
        txtTelefonoTrabajo.setText("");
       cmbCodigoMedico.getSelectionModel().clearSelection();
       tblTelefonosMedicos.getSelectionModel().clearSelection();
    }
    
    public void nuevo(){
        switch(tipodeOperaciones){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipodeOperaciones = operaciones.GUARDAR;
            break;
            
            case GUARDAR:
                if (txtTelefonoPersonal.getText().isEmpty() != true && txtTelefonoTrabajo.getText().isEmpty() != true){
                    guardar();
                    desactivarControles();
                    limpiarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    tipodeOperaciones = operaciones.NINGUNO;
                    cargarDatos();
                }else{
                    JOptionPane.showConfirmDialog(null, "Por favor complete los datos requeridos", "Error!!", JOptionPane.DEFAULT_OPTION);
                }
            break;
        }
    }
    
    public void guardar(){
        TelefonosMedicos registro = new TelefonosMedicos();
        registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
        registro.setTelefonoTrabajo(txtTelefonoTrabajo.getText());
        registro.setCodigoMedico(((Medico)cmbCodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
        try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTelefonoMedico(?,?,?)}");
           procedimiento.setString(1, registro.getTelefonoPersonal());// especificamos los valores que mandaremos al procedimiento 
           procedimiento.setString(2, registro.getTelefonoTrabajo());
           procedimiento.setInt(3, registro.getCodigoMedico());
           procedimiento.execute();
           listaTelefonosMedicos.add(registro);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void cargarDatos(){
        tblTelefonosMedicos.setItems(getTelefonosMedicos());
        colCodigo.setCellValueFactory(new PropertyValueFactory<TelefonosMedicos,Integer>("codigoTelefonoMedico"));
        colTelefonoPersonal.setCellValueFactory(new PropertyValueFactory<TelefonosMedicos,String>("telefonoPersonal"));
        colTelefonoTrabajo.setCellValueFactory(new PropertyValueFactory<TelefonosMedicos,String>("telefonoTrabajo"));
        colCodigoMedico.setCellValueFactory(new PropertyValueFactory<TelefonosMedicos,Integer>("codigoMedico"));
        
    }
    
    public ObservableList<TelefonosMedicos> getTelefonosMedicos(){
        ArrayList<TelefonosMedicos> lista = new ArrayList<TelefonosMedicos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTelefonosMedicos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TelefonosMedicos(resultado.getInt("codigoTelefonoMedico"),
                                              resultado.getString("telefonoPersonal"),
                                              resultado.getString("telefonoTrabajo"),
                                              resultado.getInt("codigoMedico")));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    return listaTelefonosMedicos = FXCollections.observableList(lista);
    }
    
    public ObservableList <Medico> getMedico(){ // utilizamos el get de la PK en la tabla que utlice el cmb
        ArrayList<Medico> lista = new ArrayList<Medico>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMedico}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Medico(resultado.getInt("codigoMedico"),// etiquetas
                                    resultado.getInt("licenciaMedica"),
                                    resultado.getString("nombres"),
                                    resultado.getString("apellidos"),
                                    resultado.getString("HoraEntrada"),
                                    resultado.getString("HoraSalida"),
                                    resultado.getInt("turnoMaximo"),
                                    resultado.getString("sexo")));
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }   
            return listaMedico = FXCollections.observableList(lista);
    }
    public Medico buscarMedico(int codigoMedico){
        Medico resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMedico(?)}");
            procedimiento.setInt(1, codigoMedico);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Medico ( registro.getInt("codigoMedico"),
                                         registro.getInt("licenciaMedica"),
                                         registro.getString("nombres"),
                                         registro.getString("apellidos"),
                                         registro.getString("horaEntrada"),
                                         registro.getString("horaSalida"),
                                         registro.getInt("turnoMaximo"),
                                         registro.getString("sexo"));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return resultado;
    }
    
    public void seleccionarElementos(){
        if(tblTelefonosMedicos.getSelectionModel().getSelectedItem()!= null){
            txtTelefonoPersonal.setText(((TelefonosMedicos)tblTelefonosMedicos.getSelectionModel().getSelectedItem()).getTelefonoPersonal());
            txtTelefonoTrabajo.setText(((TelefonosMedicos)tblTelefonosMedicos.getSelectionModel().getSelectedItem()).getTelefonoTrabajo());
            cmbCodigoMedico.setPromptText(String.valueOf(((TelefonosMedicos)tblTelefonosMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico()));
            
        }
    }
    
    public TelefonosMedicos buscarTelefono (int codigoTelefonoMedico){
        TelefonosMedicos resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTelefonosMedicos(?)}");
            procedimiento.setInt(1, codigoTelefonoMedico);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado= new TelefonosMedicos( registro.getInt("codigoTelefonoMedico"),
                                                 registro.getString("telefonoPersonal"),
                                                 registro.getString("telefonoTrabajo"),
                                                 registro.getInt("codigoMedico"));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return resultado;
    }
    
    public void eliminar(){
        switch(tipodeOperaciones){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipodeOperaciones = operaciones.GUARDAR;//pendiente
            break;
            
            default:
                if(tblTelefonosMedicos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null , "¿Esta seguro de eliminar el registro?", "Eliminar Telefono Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTelefonoMedico(?)}");
                            procedimiento.setInt(1, ((TelefonosMedicos)tblTelefonosMedicos.getSelectionModel().getSelectedItem()).getCodigoTelefonoMedico());
                            procedimiento.execute();
                            listaTelefonosMedicos.remove(tblTelefonosMedicos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch(Exception ex){
                        ex.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break;
        }
        limpiarControles();
    }
    
    public void editar(){
        switch(tipodeOperaciones) {
            case NINGUNO:
                if (tblTelefonosMedicos.getSelectionModel().getSelectedItem()!= null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");//hay que hacer cancelar
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipodeOperaciones = operaciones.ACTUALIZAR;
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
                tipodeOperaciones = operaciones.NINGUNO;
                cargarDatos();
            break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarTelefonoMedico(?,?,?,?)}");
            TelefonosMedicos registro = (TelefonosMedicos)tblTelefonosMedicos.getSelectionModel().getSelectedItem();
            registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
            registro.setTelefonoTrabajo(txtTelefonoTrabajo.getText());
            registro.setCodigoMedico(((Medico)cmbCodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
            procedimiento.setInt(1, registro.getCodigoTelefonoMedico());
            procedimiento.setString(2, registro.getTelefonoPersonal());
            procedimiento.setString(3, registro.getTelefonoTrabajo());
            procedimiento.setInt(4, registro.getCodigoMedico());
            procedimiento.execute();
             
        }catch(Exception ex){
            ex.printStackTrace();
        }
        limpiarControles();
    }
}
