/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.Ordenador;

import java.util.Date;

/**
 *
 * @author AJPDHN
 */
public class Tordenadores {

    public int IdOrdenador;
    public String Ordenador;
    public boolean Is_active;
    public String Usuario;
    public Date InFecha;
    public Date OutFecha;
    public int IdUsuario;

    public Tordenadores() {
    }

    public int getIdOrdenador() {
        return IdOrdenador;
    }

    public void setIdOrdenador(int IdOrdenador) {
        this.IdOrdenador = IdOrdenador;
    }

    public String getOrdenador() {
        return Ordenador;
    }

    public void setOrdenador(String Ordenador) {
        this.Ordenador = Ordenador;
    }

    public boolean isIs_active() {
        return Is_active;
    }

    public void setIs_active(boolean Is_active) {
        this.Is_active = Is_active;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public Date getInFecha() {
        return InFecha;
    }

    public void setInFecha(Date InFecha) {
        this.InFecha = InFecha;
    }

    public Date getOutFecha() {
        return OutFecha;
    }

    public void setOutFecha(Date OutFecha) {
        this.OutFecha = OutFecha;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int IdUsuario) {
        this.IdUsuario = IdUsuario;
    }

}
