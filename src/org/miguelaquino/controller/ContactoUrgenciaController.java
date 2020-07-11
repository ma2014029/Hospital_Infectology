/*
 creación: 1/06/19
modificacion: 5/6/19
              8/06/19
              11/6/19
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.miguelaquino.bean.ContactoUrgencia;
import org.miguelaquino.bean.Pacientes;
import org.miguelaquino.db.Conexion;
import org.miguelaquino.sistema.Principal;

public class ContactoUrgenciaController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipodeOperacion = operaciones.NINGUNO;
    private ObservableList<ContactoUrgencia> listaContacto;
    private ObservableList<Pacientes> listaPacientes;
    
    @FXML private ComboBox cmbCodigoPaciente;
    //TextField
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtTelefono;
    //TableView
    @FXML private TableView tblContacto;
    @FXML private TableColumn colCodigos;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colTelefonos;
    @FXML private TableColumn colCodigoPaciente;
    //Button
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       cargarDatos();
       cmbCodigoPaciente.setItems(getPacientes());
    }
    
    public void ContactoUrgenciaController(){
    
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal () {
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaContactoU(){
        escenarioPrincipal.ventanaContactoU();
    }
    
    public void ventanaPacientes(){
        escenarioPrincipal.ventanaPacientes();
    }
    
    public void desactivarControles(){
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtTelefono.setEditable(false);
        cmbCodigoPaciente.setDisable(true);
    }
    
    public void activarControles(){
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtTelefono.setEditable(true);
        cmbCodigoPaciente.setDisable(false);
    }
    
    public void limpiarControles() {
        txtNombres.setText("");
        txtApellidos.setText("");
        txtTelefono.setText("");
        cmbCodigoPaciente.getSelectionModel().clearSelection();
        tblContacto.getSelectionModel().clearSelection();
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
                if(txtNombres.getText().isEmpty()!=true && txtApellidos.getText().isEmpty()!=true && txtTelefono.getText().isEmpty()!= true 
                        && cmbCodigoPaciente.getSelectionModel().isEmpty() != true){
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
        ContactoUrgencia registro = new ContactoUrgencia();
        registro.setNombres(txtNombres.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setNumeroContacto(txtTelefono.getText());
        registro.setCodigoPaciente(((Pacientes)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarContactoUrgencia(?, ?, ?, ?)}");
            procedimiento.setString(1, registro.getNombres());
            procedimiento.setString(2, registro.getApellidos());
            procedimiento.setString(3, registro.getNumeroContacto());
            procedimiento.setInt(4, registro.getCodigoPaciente());//comboBox
            procedimiento.execute();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
     public ObservableList<ContactoUrgencia> getContacto(){
        ArrayList<ContactoUrgencia> lista = new ArrayList<ContactoUrgencia>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarContactoUrgencia}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ContactoUrgencia(resultado.getInt("codigoContactoUrgencia"), 
                                   resultado.getString("nombres"),
                                    resultado.getString("apellidos"),
                                    resultado.getString("numeroContacto"),
                                    resultado.getInt("codigoPaciente")));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return listaContacto = FXCollections.observableList(lista);
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
    
    public void cargarDatos(){
        tblContacto.setItems(getContacto());
        colCodigos.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, Integer>("codigoContactoUrgencia"));
        colNombres.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, String>("apellidos"));
        colTelefonos.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, String>("numeroContacto"));
        colCodigoPaciente.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, Integer>("codigoPaciente"));
    }
    
    public void seleccionarElemento(){
        if(tblContacto.getSelectionModel().getSelectedItem()!= null){
            txtNombres.setText(((ContactoUrgencia)tblContacto.getSelectionModel().getSelectedItem()).getNombres());
            txtApellidos.setText(((ContactoUrgencia)tblContacto.getSelectionModel().getSelectedItem()).getApellidos());
            txtTelefono.setText(((ContactoUrgencia)tblContacto.getSelectionModel().getSelectedItem()).getNumeroContacto());
            cmbCodigoPaciente.setPromptText(String.valueOf(((ContactoUrgencia)tblContacto.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
        }
    }
    
    public ContactoUrgencia buscarContacto (int codigoContactoUrgencia){
        ContactoUrgencia resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarContactoUrgencia(?)}");
            procedimiento.setInt(1, codigoContactoUrgencia);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new ContactoUrgencia(registro.getInt("codigoContactoUrgencia"),
                                                 registro.getString("nombres"),
                                                 registro.getString("apellidos"),
                                                 registro.getString("numeroContacto"),
                                                 registro.getInt("codigoPaciente"));
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
                if(tblContacto.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Contacto Urgencia", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarContactoUrgencia(?)}");
                            procedimiento.execute();
                            listaContacto.remove(tblContacto.getSelectionModel().getFocusedIndex());
                            limpiarControles();
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                } else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
        limpiarControles();
    }
    
    public void editar(){
        switch(tipodeOperacion) {
            case NINGUNO:
                if (tblContacto.getSelectionModel().getSelectedItem()!= null){
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
                tipodeOperacion = operaciones.CANCELAR;
                cargarDatos();
            break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarContactoUrgencia(?,?,?,?,?)}");
            ContactoUrgencia registro = (ContactoUrgencia)tblContacto.getSelectionModel().getSelectedItem();
            registro.setNombres(txtNombres.getText());
            registro.setApellidos(txtApellidos.getText());
            registro.setNumeroContacto(txtTelefono.getText());
            registro.setCodigoPaciente(((Pacientes)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
            procedimiento.setInt(1, registro.getCodigoContactoUrgencia());
            procedimiento.setString(2, registro.getNombres());
            procedimiento.setString(3, registro.getApellidos());
            procedimiento.setString(4, registro.getNumeroContacto());
            procedimiento.setInt(5, registro.getCodigoPaciente());
            procedimiento.execute();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        limpiarControles();
    }
}
