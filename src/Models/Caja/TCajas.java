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
public class TCajas {
    private int IdCaja;
    private int Caja;
    private boolean Estado;
    private Date Fecha;

    public TCajas() {
    }

    public int getIdCaja() {
        return IdCaja;
    }

    public void setIdCaja(int IdCaja) {
        this.IdCaja = IdCaja;
    }

    public int getCaja() {
        return Caja;
    }

    public void setCaja(int Caja) {
        this.Caja = Caja;
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
