/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Library;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JOptionPane;

/**
 *
 * @author AJPDHN
 */
public class Ticket {

    private final StringBuilder lineas = new StringBuilder();
    private final int maxCaracter = 40;
    private int stop;
    private final FormatDecimal _format = new FormatDecimal();

    public String LineasGuion() {
        String linea = "";
        for (int i = 0; i < maxCaracter; i++) {
            linea += "-";
        }
        return lineas.append(linea).append("\n").toString();
    }

    public String LineAsteriscos() {
        String asterisco = "";
        for (int i = 0; i < maxCaracter; i++) {
            asterisco += "*";
        }
        return lineas.append(asterisco).append("\n").toString();
    }

    public String Lineaigual() {
        String igual = "";
        for (int i = 0; i < maxCaracter; i++) {
            igual += "=";
        }
        return lineas.append(igual).append("\n").toString();
    }

    public void EncabezadoVenta(String columnas) {
        lineas.append(columnas).append("\n");
    }

    public void TextoIzquierda(String texto) {
        if (texto.length() > maxCaracter) {
            int caracterActual = 0;
            for (int i = texto.length(); i > maxCaracter; i -= maxCaracter) {
                lineas.append(texto.substring(caracterActual, maxCaracter)).append("\n");
                caracterActual += maxCaracter;
            }
            lineas.append(texto.substring(caracterActual, texto.length() - caracterActual)).append("\n");
        } else {
            lineas.append(texto).append("\n");
        }
    }

    public void TextoDerecho(String texto) {
        if (texto.length() > maxCaracter) {
            int caracterActual = 0;
            for (int i = texto.length(); i > maxCaracter; i -= maxCaracter) {
                lineas.append(texto.substring(caracterActual, maxCaracter)).append("\n");
                caracterActual += maxCaracter;
            }
            String espacios = "";
            for (int i = 0; i < (maxCaracter - texto.substring(caracterActual,
                    texto.length() - caracterActual).length()); i++) {
                espacios += " ";
            }
            lineas.append(espacios).append(texto.substring(caracterActual, texto.length() - caracterActual)).append("\n");
        } else {
            String espacios = "";
            for (int i = 0; i < (maxCaracter - texto.length()); i++) {
                espacios += " ";
            }
            lineas.append(espacios).append(texto).append("\n");
        }
    }

    public void TextoCentro(String texto) {
        if (texto.length() > maxCaracter) {
            int caracterActual = 0;
            for (int i = texto.length(); i > maxCaracter; i -= maxCaracter) {
                lineas.append(texto.substring(caracterActual, maxCaracter)).append("\n");
                caracterActual += maxCaracter;
            }
            String espacios = "";
            int centrar = (maxCaracter - texto.substring(caracterActual,
                    texto.length() - caracterActual).length()) / 2;
            for (int i = 0; i < centrar; i++) {
                espacios += " ";
            }
            lineas.append(espacios).append(texto.substring(caracterActual,
                    texto.length() - caracterActual)).append("\n");
        } else {
            String espacios = "";
            int centrar = (maxCaracter - texto.length()) / 2;
            for (int i = 0; i < centrar; i++) {
                espacios += " ";
            }
            lineas.append(espacios).append(texto).append("\n");
        }
    }

    public void TextoExtremo(String izquierdo, String derecho) {
        String der, izq, completo, espacio = "";
        if (izquierdo.length() > 18) {
            stop = izquierdo.length() - 18;
            izq = izquierdo.substring(stop, 19);
        } else {
            izq = izquierdo;
        }
        completo = izq;
        if (derecho.length() > 20) {
            stop = derecho.length() - 20;
            der = derecho.substring(stop, 20);
        } else {
            der = derecho;
        }
        int numEspacios = maxCaracter - (izq.length() + der.length());
        for (int i = 0; i < numEspacios; i++) {
            espacio += " ";
        }
        completo += espacio + derecho;
        lineas.append(completo).append("\n");
    }

    public void AgregarTotales(String texto, double total, String money) {
        String resumen, valor, completo, espacio = "";
        if (texto.length() > 25) {
            stop = texto.length() - 25;
            resumen = texto.substring(stop, 25);
        } else {
            resumen = texto;
        }
        completo = resumen;
        valor = money+_format.decimal(total);
        int numEspacios = maxCaracter - (resumen.length() + valor.length());
        for (int i = 0; i < numEspacios; i++) {
            espacio += " ";
        }
        completo += espacio + valor;
        lineas.append(completo).append("\n");
    }

    public void AgregarArticulo(String articulo, String cant, String precio) {
        String elemento = "", espacios = "";
        boolean bandera = false;
        int numEspacios = 10;
        if (articulo.length() > 20) {
            //colocar la cantida a la derecha
            espacios = "";
            for (int i = 0; i < (numEspacios - cant.length()); i++) {
                espacios += " ";
            }
            elemento += cant + espacios;
            //colocar el precio a la drecha
            espacios = "";
            for (int i = 0; i < (numEspacios - precio.length()); i++) {
                espacios += " ";
            }
            elemento += precio + espacios;
           
            int caracterActual = 0;
            for (int i = articulo.length(); i > 20; i -= 20) {
                if (bandera) {
                    lineas.append(articulo.substring(0,caracterActual)).append("\n");
                } else {
                    lineas.append(articulo.substring(caracterActual, 20)).append(elemento).append("\n");
                    bandera = true;
                }
                caracterActual += 20;
            }
            lineas.append(articulo.substring(0,caracterActual)).append("\n");
        } else {
            for (int i = 0; i < (20 - articulo.length()); i++) {
                espacios += " ";  // Agregar espacios para poner el valor de articulo
            }
            elemento = articulo + espacios;
            //colocar la cantidad a la drecha
            espacios = "";
            for (int i = 0; i < (numEspacios - cant.length()); i++) {
                espacios += " ";   // Agregar espacios para poner el valor de cantidad
            }
            elemento += cant + espacios;
            //colocamos el precio a la derecha
            espacios = "";
            for (int i = 0; i < (numEspacios - precio.length()); i++) {
                espacios += " "; // Agregar espacios para poner el valor de precio
            }
            elemento += precio + espacios;
           
            lineas.append(elemento).append("\n");
        }
    }

    public void print() {
        //Especificamos el tipo de dato a imprimir
        //Tipo: bytes; Subtipo: autodetectado
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;

        //Aca obtenemos el servicio de impresion por defatul
        //Si no quieres ver el dialogo de seleccionar impresora usa esto
        //PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
        //Con esto mostramos el dialogo para seleccionar impresora
        //Si quieres ver el dialogo de seleccionar impresora usalo
        //Solo mostrara las impresoras que soporte arreglo de bits
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
        PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
        PrintService service = ServiceUI.printDialog(null, 700, 200, printService,
                defaultService, flavor, pras);

        //Creamos un arreglo de tipo byte
        byte[] bytes;
        //Aca convertimos el string(cuerpo del ticket) a bytes tal como
        //lo maneja la impresora(mas bien ticketera :p)
        bytes = lineas.toString().getBytes();
        //Creamos un documento a imprimir, a el se le appendeara
        //el arreglo de bytes
        Doc doc = new SimpleDoc(bytes, flavor, null);
        //Creamos un trabajo de impresión
        DocPrintJob job = service.createPrintJob();

        //Imprimimos dentro de un try de a huevo
        try {
            //El metodo print imprime
            job.print(doc, null);
        } catch (Exception er) {
            JOptionPane.showMessageDialog(null, "Error al imprimir: " + er.getMessage());
        }
    }
}
