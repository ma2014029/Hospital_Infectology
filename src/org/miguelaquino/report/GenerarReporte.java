/*
 Creacion 3/7/19
 */
package org.miguelaquino.report;

import java.util.Map;
import java.io.InputStream;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.miguelaquino.db.Conexion;

public class GenerarReporte {// se puede usar para cualquier reporte
    public static void mostrarReporte(String nombreReporte, String titulo, Map parametros){
        InputStream reporte = GenerarReporte.class.getResourceAsStream(nombreReporte);
        try{
            JasperReport reporteMaestro = (JasperReport)JRLoader.loadObject(reporte);
            JasperPrint reporteImpreso = JasperFillManager.fillReport(reporteMaestro, parametros,Conexion.getInstancia().getConexion());
            JasperViewer visor = new JasperViewer(reporteImpreso, false);
            visor.setTitle(titulo);
            visor.setVisible(true);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
}
