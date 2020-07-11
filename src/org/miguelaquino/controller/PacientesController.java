/*
Creacion: 1/6/19
modificacion 5/6/19
             8/6/19
             16/6/19
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
import javafx.scene.layout.GridPane;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javafx.scene.control.ComboBox;
import javax.swing.JOptionPane;
import org.miguelaquino.bean.Pacientes;
import org.miguelaquino.db.Conexion;
import org.miguelaquino.sistema.Principal;


public class PacientesController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipodeOperacion = operaciones.NINGUNO;
    private ObservableList<Pacientes> listaPacientes;
    private DatePicker fecha;
    //textfield
    @FXML private TextField txtApellidos;
    @FXML private TextField txtNombres;
    @FXML private GridPane grpFecha;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtEdad;
    @FXML private TextField txtOcupacion;
    @FXML private ComboBox cmbSexo;// cambio por txtSexo
    //table View
    @FXML private TableView tblPacientes;
    @FXML private TableColumn colCodigos;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colEdad;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colOcupacion;
    @FXML private TableColumn colSexo;
    //bottons
    @FXML private Button btnReporte;
    @FXML private Button btnEliminar;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        limpiarControles();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/miguelaquino/resource/DatePicker.css");
        grpFecha.add(fecha,0,0);
        ObservableList<String> sexo = FXCollections.observableArrayList("Masculino","Femenino"); //cambio 
        cmbSexo.setItems(sexo);//cambio
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    
    public void PacientesController(){
    }
    

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaPacientes(){
        escenarioPrincipal.ventanaPacientes();
    }
    
    public void ventanaEspecialidad(){
        escenarioPrincipal.ventanaEspecialidad();
    }
    
    public void ventanaContactoUrg () {
        escenarioPrincipal.ventanaContactoU();
    }
  
    public void desactivarControles(){
        txtApellidos.setEditable(false);    
        txtNombres.setEditable(false);
        grpFecha.setDisable(true);
        txtDireccion.setEditable(false);
        txtEdad.setEditable(false);
        txtOcupacion.setEditable(false);
        cmbSexo.setDisable(true);//cambio
    }
    
    public void activarControles(){
        txtApellidos.setEditable(true);    
        txtNombres.setEditable(true);
        grpFecha.setDisable(false);
        txtDireccion.setEditable(true);
        txtEdad.setEditable(false);
        txtOcupacion.setEditable(true);
        cmbSexo.setDisable(false);
        
    }
    
    public void limpiarControles() {
        txtApellidos.setText("");    
        txtNombres.setText("");
        txtDireccion.setText("");
        //fecha.como limpar---------------------------------------------------
        txtEdad.setText("");
        txtOcupacion.setText("");
        cmbSexo.getSelectionModel().clearSelection();
        tblPacientes.getSelectionModel().clearSelection();
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
                if(txtApellidos.getText().isEmpty()!= true && txtNombres.getText().isEmpty()!= true && txtDireccion.getText().isEmpty() != true 
                        && txtOcupacion.getText().isEmpty()!= true && cmbSexo.getSelectionModel().isEmpty() != true){//cambio sexo
                    guardar();
                    desactivarControles();
                    limpiarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    cargarDatos();
                    tipodeOperacion = operaciones.NINGUNO;
                break;
                }else{
                    JOptionPane.showConfirmDialog(null, "Por favor complete los datos requeridos", "Error!!", JOptionPane.DEFAULT_OPTION);
                }
                //}
    }
    }
   
     public void guardar(){
        Pacientes registro = new Pacientes();
        registro.setApellidos(txtApellidos.getText());
        registro.setNombres(txtNombres.getText());
        registro.setFechadeNacimiento(fecha.getSelectedDate());
        registro.setDireccion(txtDireccion.getText());
        registro.setOcupacion(txtOcupacion.getText());
        registro.setSexo(cmbSexo.getSelectionModel().getSelectedItem().toString());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarPaciente(?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getApellidos());
            procedimiento.setString(2, registro.getNombres());
            procedimiento.setDate(3, new java.sql.Date(registro.getFechadeNacimiento().getTime()));
            procedimiento.setString(4, registro.getDireccion());
            procedimiento.setString(5, registro.getOcupacion());
            procedimiento.setString(6, registro.getSexo());
            procedimiento.execute();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
     
    public void cargarDatos(){
        tblPacientes.setItems(getPacientes());
        colCodigos.setCellValueFactory(new PropertyValueFactory<Pacientes, Integer>("codigoPaciente"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Pacientes, String>("apellidos"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Pacientes, String>("nombres"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Pacientes, Date>("fechadeNacimiento"));
        colEdad.setCellValueFactory(new PropertyValueFactory<Pacientes, Integer>("edad"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Pacientes, String>("direccion"));
        colOcupacion.setCellValueFactory(new PropertyValueFactory<Pacientes, String>("ocupacion"));
        colSexo.setCellValueFactory(new PropertyValueFactory<Pacientes, String>("sexo"));
    }
    
    public ObservableList<Pacientes> getPacientes(){
        ArrayList<Pacientes> lista = new ArrayList<Pacientes>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarPacientes}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Pacientes(resultado.getInt("codigoPaciente"),
                                        resultado.getString("apellidos"),
                                        resultado.getString("nombres"),
                                        resultado.getDate("fechadeNacimiento"),
                                        resultado.getInt("edad"),
                                        resultado.getString("direccion"),
                                        resultado.getString("ocupacion"),
                                        resultado.getString("sexo")));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return listaPacientes = FXCollections.observableList(lista);
    }
    
    public void seleccionarElemento(){
        if(tblPacientes.getSelectionModel().getSelectedItem() != null){
            txtApellidos.setText(((Pacientes)tblPacientes.getSelectionModel().getSelectedItem()).getApellidos());
            txtNombres.setText(((Pacientes)tblPacientes.getSelectionModel().getSelectedItem()).getNombres());
            fecha.setSelectedDate(((Pacientes)tblPacientes.getSelectionModel().getSelectedItem()).getFechadeNacimiento());
            txtEdad.setText(String.valueOf(((Pacientes)tblPacientes.getSelectionModel().getSelectedItem()).getEdad()));
            txtDireccion.setText(((Pacientes)tblPacientes.getSelectionModel().getSelectedItem()).getDireccion());
            txtOcupacion.setText(((Pacientes)tblPacientes.getSelectionModel().getSelectedItem()).getOcupacion());
            cmbSexo.setPromptText(((Pacientes)tblPacientes.getSelectionModel().getSelectedItem()).getSexo());
        }
        
    }    
    
    public Pacientes buscarPaciente (int codigoPaciente){
        Pacientes resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarPaciente(?)}");
            procedimiento.setInt(1, codigoPaciente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Pacientes( registro.getInt("codigoPaciente"),
                                           registro.getString("apellidos"),
                                           registro.getString("nombres"),
                                           registro.getDate("fechadeNaciemiento"),
                                           registro.getInt("edad"),
                                           registro.getString("direccion"),
                                           registro.getString("ocupacion"),
                                           registro.getString("sexo"));
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
                if(tblPacientes.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Paciente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarPaciente(?)}");
                            procedimiento.setInt(1, ((Pacientes)tblPacientes.getSelectionModel().getSelectedItem()).getCodigoPaciente());
                            procedimiento.execute();
                            listaPacientes.remove(tblPacientes.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento");
                }
                break;
                
                case CANCELAR:
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipodeOperacion = operaciones.NINGUNO;
            break;   
        }
        limpiarControles();
    }
    
    public void editar(){
        switch(tipodeOperacion) {
            case NINGUNO:
                if (tblPacientes.getSelectionModel().getSelectedItem()!= null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarPaciente(?,?,?,?,?,?,?)}");
            Pacientes registro = (Pacientes)tblPacientes.getSelectionModel().getSelectedItem();
            registro.setApellidos(txtApellidos.getText());
            registro.setNombres(txtNombres.getText());
            registro.setFechadeNacimiento(fecha.getSelectedDate());
            registro.setDireccion(txtDireccion.getText());
            registro.setOcupacion(txtOcupacion.getText());
            registro.setSexo(cmbSexo.getSelectionModel().getSelectedItem().toString());
            procedimiento.setInt(1, registro.getCodigoPaciente());
            procedimiento.setString(2, registro.getApellidos());
            procedimiento.setString(3, registro.getNombres());
            procedimiento.setDate(4, new java.sql.Date(registro.getFechadeNacimiento().getTime()));
            procedimiento.setString(5, registro.getDireccion());
            procedimiento.setString(6, registro.getOcupacion());
            procedimiento.setString(7, registro.getSexo());
            procedimiento.execute();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        limpiarControles();
    }
}
