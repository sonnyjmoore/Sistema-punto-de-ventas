/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.Proveedor;

import java.util.Date;

/**
 *
 * @author AJPDHN
 */
public class TReportes_proveedor extends TProveedor {

    private int IdReporte;
    private double DeudaActual;
    private double Deuda;
    private double Mensual;
    private double Cambio;
    private Date FechaDeuda;
    private double UltimoPago;
    private Date FechaPago;
    private String Ticket;
    private int IdProveedor;
    private String FormaPago;

    public TReportes_proveedor() {
    }

    public int getIdReporte() {
        return IdReporte;
    }

    public void setIdReporte(int IdReporte) {
        this.IdReporte = IdReporte;
    }

    public double getDeudaActual() {
        return DeudaActual;
    }

    public void setDeudaActual(double DeudaActual) {
        this.DeudaActual = DeudaActual;
    }

    public double getDeuda() {
        return Deuda;
    }

    public void setDeuda(double Deuda) {
        this.Deuda = Deuda;
    }

    public double getMensual() {
        return Mensual;
    }

    public void setMensual(double Mensual) {
        this.Mensual = Mensual;
    }

    public double getCambio() {
        return Cambio;
    }

    public void setCambio(double Cambio) {
        this.Cambio = Cambio;
    }

    public Date getFechaDeuda() {
        return FechaDeuda;
    }

    public void setFechaDeuda(Date FechaDeuda) {
        this.FechaDeuda = FechaDeuda;
    }

    public double getUltimoPago() {
        return UltimoPago;
    }

    public void setUltimoPago(double UltimoPago) {
        this.UltimoPago = UltimoPago;
    }

    public Date getFechaPago() {
        return FechaPago;
    }

    public void setFechaPago(Date FechaPago) {
        this.FechaPago = FechaPago;
    }

    public String getTicket() {
        return Ticket;
    }

    public void setTicket(String Ticket) {
        this.Ticket = Ticket;
    }
    public int getIdProveedor() {
        return IdProveedor;
    }

    public void setIdProveedor(int IdProveedor) {
        this.IdProveedor = IdProveedor;
    }

    public String getFormaPago() {
        return FormaPago;
    }

    public void setFormaPago(String FormaPago) {
        this.FormaPago = FormaPago;
    }

}
