/*
creacion: 9/7/19
 */
package org.miguelaquino.controller;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
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
import javax.swing.JOptionPane;
import org.miguelaquino.bean.Turnos;
import org.miguelaquino.db.Conexion;
import org.miguelaquino.sistema.Principal;

public class TurnosController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipodeOperacion = operaciones.NINGUNO;
    private ObservableList<Turnos> listaTurnos;
    
    @FXML TextField txtFechaTurno;
    @FXML TextField txtFechaCita;
    @FXML TextField txtValor;
    @FXML ComboBox cmbMedicoEspecialidad;
    @FXML ComboBox cmbTurnoResponsable;
    @FXML ComboBox cmbPaciente;
    @FXML TableView tblTurnos;
    @FXML TableColumn colCodigo;
    @FXML TableColumn colFechaTurno;
    @FXML TableColumn colFechaCita;
    @FXML TableColumn colValor;
    @FXML TableColumn colMedico;
    @FXML TableColumn colTurnoResponsable;
    @FXML TableColumn colPaciente;
    @FXML Button btnNuevo;
    @FXML Button btnEditar;
    @FXML Button btnEliminar;
    @FXML Button btnReporte;
    
    public TurnosController() {
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
    
    public void ventanaTurnos(){
        escenarioPrincipal.ventanaTurnos();
    }
    
    public void ventanaResponsablesTurnos(){
        escenarioPrincipal.ventanaResponsablesTurnos();
    }
    
    public void ventanaPacientes(){
        escenarioPrincipal.ventanaPacientes();
    }
    
    public void ventanaEspecialidadesMedicos(){
        escenarioPrincipal.ventanaEspecialidadesMedicos();
    }
    
    public void menuPrincipal () {
        escenarioPrincipal.menuPrincipal();
    }
    
    public ObservableList<Turnos> getResponsable(){
        ArrayList<Turnos> lista = new ArrayList<Turnos>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTurnos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Turnos( resultado.getInt("codigoTurno"),
                                                resultado.getDate("fechaTurno"),
                                                resultado.getDate("fechaCita"),
                                                resultado.getDouble("valorCita"),
                                                resultado.getInt("codigoMedicoEspecialidad"),
                                                resultado.getInt("codigoResponsable"),
                                                resultado.getInt("codigoPaciente")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTurnos = FXCollections.observableList(lista);
    }
    
     public void desactivarControles(){
        txtFechaTurno.setEditable(false);
        txtFechaCita.setEditable(false);
        txtValor.setEditable(false);
        cmbMedicoEspecialidad.setDisable(true);
        cmbPaciente.setDisable(true);
        cmbTurnoResponsable.setDisable(true);
    }
    
    public void activarControles(){
        txtFechaTurno.setEditable(true);
        txtFechaCita.setEditable(true);
        txtValor.setEditable(true);
        cmbMedicoEspecialidad.setDisable(false);
        cmbPaciente.setDisable(false);
        cmbTurnoResponsable.setDisable(false);
    }
    
    public void limpiarControles(){
        txtFechaTurno.setText("");
        txtFechaCita.setText("");
        txtValor.setText("");
        cmbMedicoEspecialidad.getSelectionModel().clearSelection();
        cmbPaciente.getSelectionModel().clearSelection();
        cmbTurnoResponsable.getSelectionModel().clearSelection();
        tblTurnos.getSelectionModel().clearSelection();
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
                if (txtFechaTurno.getText().isEmpty()!= true && txtFechaCita.getText().isEmpty()!= true &&
                            cmbMedicoEspecialidad.getSelectionModel().isEmpty()!= true && cmbPaciente.getSelectionModel().isEmpty()!= true &&
                                cmbTurnoResponsable.getSelectionModel().isEmpty()!= true){
                    guardar();
                    desactivarControles();
                    limpiarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    tipodeOperacion = operaciones.NINGUNO;
                    //cargarDatos();
                }else{
                    JOptionPane.showConfirmDialog(null, "Por favor complete los datos requeridos", "Error!!", JOptionPane.DEFAULT_OPTION);
                }
            break;
        }
    }
    
    public void guardar(){
        Turnos registro = new Turnos();
        registro.setFechaTurno((Date.valueOf(txtFechaTurno.getText())));
        registro.setFechaCita((Date.valueOf(txtFechaCita.getText())));
        registro.setValorCita((Double.valueOf(txtValor.getText())));
        registro.setCodigoMedicoEspecialidad(((Turnos)cmbMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad());
        registro.setCodigoResponsable(((Turnos)cmbTurnoResponsable.getSelectionModel().getSelectedItem()).getCodigoResponsable());
        registro.setCodigoPaciente(((Turnos)cmbPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTurno(?,?,?,?,?)}");
            procedimiento.setDate(1, registro.getFechaTurno());
            procedimiento.setDate(2, registro.getFechaCita());
            procedimiento.setDouble(3, registro.getValorCita());
            procedimiento.setInt(4, registro.getCodigoMedicoEspecialidad());
            procedimiento.setInt(5, registro.getCodigoResponsable());
            procedimiento.setInt(6, registro.getCodigoPaciente());
            procedimiento.execute();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void cargarDatos(){
        
    }
}
