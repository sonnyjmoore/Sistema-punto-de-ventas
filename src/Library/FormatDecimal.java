/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Library;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 *
 * @author AJPDHN
 */
public class FormatDecimal {
    Number numero;
    DecimalFormat formateador = new DecimalFormat("###,###,##0.00");
    public String decimal(double formato){
        return formateador.format(formato);
    }
    public double reconstruir(String formato){
        try {
            numero = formateador.parse(formato.replace(" ", ""));
        } catch (ParseException ex) {
            System.out.println("Error : " + ex);
        }
        return numero.doubleValue();
    }
}
