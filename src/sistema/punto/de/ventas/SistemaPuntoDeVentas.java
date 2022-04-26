/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistema.punto.de.ventas;

import Models.Caja.TCajas;
import Models.Usuario.TUsuarios;
import ViewModels.LoginVM;
import Views.Login;
import Views.Sistema;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.util.List;
import javax.swing.UIManager;

/**
 *
 * @author AJPDHN
 */
public class SistemaPuntoDeVentas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {

        }
        var login = new LoginVM();
        Object[] objects = login.Verificar();
        var listUsuario = (List<TUsuarios>) objects[0];
        var caja = (TCajas) objects[1];
        if (!listUsuario.isEmpty() && caja != null) {
            Sistema sisten = new Sistema(listUsuario.get(0),caja);
            sisten.setVisible(true);
            sisten.setExtendedState(MAXIMIZED_BOTH);
        } else {
            Login sistema = new Login();
            // sistema.setExtendedState(MAXIMIZED_BOTH);
            sistema.setVisible(true);
        }

    }

}
