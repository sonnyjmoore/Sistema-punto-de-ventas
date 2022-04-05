/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Library;

import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Andres Del Rio
 */
public class CargarImagenes extends javax.swing.JFrame {
    private File archiivo;
    private JFileChooser abrirArchivo; 
    private static String urlOrigen = null; 
    private static byte[] imagenByte = null; 

    public static byte[] getImagenByte() {
        return imagenByte;
    }
    
    
    
    public void CargarImagen(JLabel label){
        abrirArchivo = new JFileChooser();
        abrirArchivo.setFileFilter(new FileNameExtensionFilter("Archivos de Imagen","jpg","png","gif"));
        int respuesta = abrirArchivo.showOpenDialog(this);
        if (respuesta == JFileChooser.APPROVE_OPTION) { //Si los datos son iguales se abre la ventana y sube el archivo
            archiivo = abrirArchivo.getSelectedFile();
            urlOrigen = archiivo.getAbsolutePath(); //obetener la url del archivo
            Image foto = getToolkit().getImage(urlOrigen);
            foto = foto.getScaledInstance(140, 140, 1);
            label.setIcon(new ImageIcon(foto));
            imagenByte = new byte [(int) archiivo.length()];
            
            
        } 
    }
    
}
