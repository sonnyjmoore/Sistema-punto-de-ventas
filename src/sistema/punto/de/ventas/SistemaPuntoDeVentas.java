/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sistema.punto.de.ventas;

import Views.Sistema;
import static java.awt.Frame.MAXIMIZED_BOTH;
import javax.swing.UIManager;
 

/**
 *
 * @author Andres Del Rio
 */
public class SistemaPuntoDeVentas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            
        }
        Sistema sistema = new Sistema();
        sistema.setExtendedState(MAXIMIZED_BOTH);
        sistema.setVisible(true);
    }
    
}
