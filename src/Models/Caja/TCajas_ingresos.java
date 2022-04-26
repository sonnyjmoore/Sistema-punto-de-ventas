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
public class TCajas_ingresos extends TCajas{
    private int IdCajaIngreso;
    private int IdCaja;
    private int IdUsuario;
    private double Billete;
    private double Moneda;
    private double Ingreso;
    private Date Fecha;

    public TCajas_ingresos() {
    }

    public int getIdCajaIngreso() {
        return IdCajaIngreso;
    }

    public void setIdCajaIngreso(int IdCajaIngreso) {
        this.IdCajaIngreso = IdCajaIngreso;
    }

    public int getIdCaja() {
        return IdCaja;
    }

    public void setIdCaja(int IdCaja) {
        this.IdCaja = IdCaja;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int IdUsuario) {
        this.IdUsuario = IdUsuario;
    }

    public double getBillete() {
        return Billete;
    }

    public void setBillete(double Billete) {
        this.Billete = Billete;
    }

    public double getMoneda() {
        return Moneda;
    }

    public void setMoneda(double Moneda) {
        this.Moneda = Moneda;
    }

    public double getIngreso() {
        return Ingreso;
    }

    public void setIngreso(double Ingreso) {
        this.Ingreso = Ingreso;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date Fecha) {
        this.Fecha = Fecha;
    }
    
    
}
