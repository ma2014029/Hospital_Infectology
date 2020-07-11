/*
creado: 29/05/19
modificacion: 4/6/19
              5/6/19  
              11/6/19
              17/06/19
              19/06/19
              3/7/19

 */
package org.miguelaquino.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.miguelaquino.bean.Medico;
import org.miguelaquino.db.Conexion;
import org.miguelaquino.report.GenerarReporte;
import org.miguelaquino.sistema.Principal;

public class MedicoController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO} //todos los enum van con mayusculas va reporte si es necesario
    private Principal escenarioPrincipal;
    private operaciones tipodeOperacion = operaciones.NINGUNO; //que tipo de accion tiene o va a tener
    private ObservableList<Medico> listaMedico; // hacemos referencia de todo lo de la clase Medico
    //textField
    @FXML private TextField txtLicenciaMedica;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtHoraEntrada;
    @FXML private TextField txtHoraSalida;
    @FXML private ComboBox cmbSexo;
    @FXML private TextField txtTurno;
    
    //tableView
    @FXML private TableView tblMedicos;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colLicenciaMedica;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colEntrada;
    @FXML private TableColumn colSalida;
    @FXML private TableColumn colTurno;
    @FXML private TableColumn colSexo;
    //botones
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    public MedicoController(){
        
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       cargarDatos();
       ObservableList<String> sexo = FXCollections.observableArrayList("Masculino","Femenino"); 
        cmbSexo.setItems(sexo);
    }
    
    public ObservableList <Medico> getMedico(){
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
    
     public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaMedicos() {
        escenarioPrincipal.ventanaMedicos();
    }
    
    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }
    public void ventanaTelefonosMedicos () {
        escenarioPrincipal.ventanaTelefonosMedicos();
    }
    
    public void generarReporte(){
        switch(tipodeOperacion){
            case NINGUNO:
                imprimirReporte();
                limpiarControles();
            break;     
            case ACTUALIZAR:
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                tipodeOperacion = operaciones.NINGUNO;
            break;
        }
    }
    
    public void desactivarControles(){
        txtLicenciaMedica.setEditable(false);
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtHoraEntrada.setEditable(false);
        txtHoraSalida.setEditable(false);
        txtTurno.setEditable(false);
        cmbSexo.setDisable(true);
    }
    
    public void activarControles(){
        txtLicenciaMedica.setEditable(true);
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtHoraEntrada.setEditable(true);
        txtHoraSalida.setEditable(true);
        txtTurno.setEditable(false);
        cmbSexo.setDisable(false);
    }
    
    public void limpiarControles(){
        txtLicenciaMedica.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtHoraEntrada.setText("");
        txtHoraSalida.setText("");
        txtTurno.setText("");
        cmbSexo.getSelectionModel().clearSelection();
        tblMedicos.getSelectionModel().clearSelection();
    }
    public void restuararControles(){
        btnNuevo.setDisable(false);
        btnEliminar.setDefaultButton(false);
        limpiarControles();
    }
    
    public void nuevo(){
        switch(tipodeOperacion ){
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
                if (txtLicenciaMedica.getText().isEmpty() != true && txtNombres.getText().isEmpty()!= true && txtApellidos.getText().isEmpty() != true 
                    && txtHoraEntrada.getText().isEmpty() != true && txtHoraSalida.getText().isEmpty()!= true && cmbSexo.getSelectionModel().isEmpty()!= true){ 
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
        Medico registro = new Medico();
                registro.setLicenciaMedica(Integer.parseInt(txtLicenciaMedica.getText()));
                registro.setNombres(txtNombres.getText());
                registro.setApellidos(txtApellidos.getText());
                registro.setHoraEntrada(txtHoraEntrada.getText());
                registro.setHoraSalida(txtHoraSalida.getText());
                registro.setSexo(cmbSexo.getSelectionModel().getSelectedItem().toString());
            try{
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMedico(?,?,?,?,?,?)}");
                procedimiento.setInt(1, registro.getLicenciaMedica());
                procedimiento.setString(2, registro.getNombres());
                procedimiento.setString(3, registro.getApellidos());
                procedimiento.setString(4, registro.getHoraEntrada());
                procedimiento.setString(5, registro.getHoraSalida());
                procedimiento.setString(6, registro.getSexo());
                procedimiento.execute();// es el que ejecuta todo en mysql "el rayo"
                listaMedico.add(registro);
            }catch(Exception e){
                e.printStackTrace();
            }
    }
    
    public void cargarDatos() {
        tblMedicos.setItems(getMedico());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Medico, Integer>("codigoMedico"));
        colLicenciaMedica.setCellValueFactory(new PropertyValueFactory<Medico, Integer>("licenciaMedica"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Medico, String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Medico, String>("apellidos"));
        colEntrada.setCellValueFactory(new PropertyValueFactory<Medico, String>("HoraEntrada"));
        colSalida.setCellValueFactory(new PropertyValueFactory<Medico, String>("HoraSalida"));
        colTurno.setCellValueFactory(new PropertyValueFactory<Medico, Integer>("turnoMaximo"));
        colSexo.setCellValueFactory(new PropertyValueFactory<Medico, String>("sexo"));
    }
    
    public void seleccionarElemento(){
        if(tblMedicos.getSelectionModel().getSelectedItem() != null){
        txtLicenciaMedica.setText(String.valueOf(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getLicenciaMedica()));
        txtNombres.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getNombres());
        txtApellidos.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getApellidos());
        txtHoraEntrada.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getHoraEntrada());
        txtHoraSalida.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getHoraSalida());
        txtTurno.setText(String.valueOf(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getTurnoMaximo()));
        cmbSexo.setPromptText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getSexo());
        }
    }
    
    public Medico buscarMedico(int codigoMedico){//no lo implementamos por que no se necesita
        Medico resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMedico(?)}");
            procedimiento.setInt(1, codigoMedico);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){ // se utiliza cuando no sabemos cuantas vueltas hara
                resultado = new Medico(registro.getInt("codigoMedico"),
                                       registro.getInt("licenciaMedica"),
                                       registro.getString("nombres"),
                                       registro.getString("apellidos"),
                                       registro.getString("HoraEntrada"),
                                       registro.getString("HoraSalida"),
                                       registro.getInt("turnoMaximo"),
                                       registro.getString("sexo"));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return resultado;
    }
    
    public void editar(){
        switch(tipodeOperacion) {
            case NINGUNO:
                if (tblMedicos.getSelectionModel().getSelectedItem()!= null){
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
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarMedico(?,?,?,?,?,?,?)}");
            Medico registro = (Medico)tblMedicos.getSelectionModel().getSelectedItem();
            registro.setLicenciaMedica(Integer.parseInt(txtLicenciaMedica.getText()));
            registro.setNombres(txtNombres.getText());
            registro.setApellidos(txtApellidos.getText());
            registro.setHoraEntrada(txtHoraEntrada.getText());
            registro.setHoraSalida(txtHoraSalida.getText());
            registro.setSexo(cmbSexo.getSelectionModel().getSelectedItem().toString());
            procedimiento.setInt(1, registro.getCodigoMedico());
            procedimiento.setInt(2, registro.getLicenciaMedica());
            procedimiento.setString(3,registro.getNombres());
            procedimiento.setString(4, registro.getApellidos());
            procedimiento.setString(5, registro.getHoraEntrada());
            procedimiento.setString(6, registro.getHoraSalida());
            procedimiento.setString(7, registro.getSexo());
            procedimiento.execute();
        }catch(Exception ex){
            ex.printStackTrace();
        }
         limpiarControles();
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
                if(tblMedicos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", 
                            "Eliminar Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarMedico(?)}");
                            procedimiento.setInt(1, ((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico());
                            procedimiento.execute();
                            listaMedico.remove(tblMedicos.getSelectionModel().getSelectedIndex()); 
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
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoMedico",null);
        GenerarReporte.mostrarReporte("Reporte Medico.jasper", "Reporte de Medicos",parametros);
    }
    
}
