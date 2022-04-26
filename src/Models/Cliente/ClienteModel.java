/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.Cliente;

/**
 *
 * @author AJPDHN
 */
public class ClienteModel {
    private final int ID;
    private final String Nid;
    private final String Nombre;
    private final String Apellido;
    private final String Email;
    private final String Telefono;
    private final int IdReporte;
    private final String FechaLimite;

    public ClienteModel(int ID, String Nid, String Nombre, String Apellido, String Email, String Telefono, int IdReporte, String FechaLimite) {
        this.ID = ID;
        this.Nid = Nid;
        this.Nombre = Nombre;
        this.Apellido = Apellido;
        this.Email = Email;
        this.Telefono = Telefono;
        this.IdReporte = IdReporte;
        this.FechaLimite = FechaLimite;
    }

    public int getID() {
        return ID;
    }

    public String getNid() {
        return Nid;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public String getEmail() {
        return Email;
    }

    public String getTelefono() {
        return Telefono;
    }

    public int getIdReporte() {
        return IdReporte;
    }

    public String getFechaLimite() {
        return FechaLimite;
    }
    
    
}
