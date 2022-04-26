/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.Caja;

import java.util.Date;

/**
 *
 * @author ajpag
 */
public class TCajas_registros {
    private int IdCajaRegistro;
    private int IdUsuario;
    private int IdCaja;
    private boolean Estado;
    private Date Fecha;

    public TCajas_registros() {
    }

    public int getIdCajaRegistro() {
        return IdCajaRegistro;
    }

    public void setIdCajaRegistro(int IdCajaRegistro) {
        this.IdCajaRegistro = IdCajaRegistro;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int IdUsuario) {
        this.IdUsuario = IdUsuario;
    }

    public int getIdCaja() {
        return IdCaja;
    }

    public void setIdCaja(int IdCaja) {
        this.IdCaja = IdCaja;
    }

    public boolean isEstado() {
        return Estado;
    }

    public void setEstado(boolean Estado) {
        this.Estado = Estado;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date Fecha) {
        this.Fecha = Fecha;
    }
    
    
}
