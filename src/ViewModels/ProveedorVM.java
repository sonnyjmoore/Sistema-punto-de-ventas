/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewModels;

import Library.Calendario;
import Library.Codigos;
import Library.FormatDecimal;
import Library.Objectos;
import Library.Paginador;
import Library.Ticket;
import Library.Uploadimage;
import Models.Proveedor.TPagos_proveedor;
import Models.Proveedor.TProveedor;
import Models.Usuario.TUsuarios;
import datechooser.beans.DateChooserCombo;
import java.awt.Color;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

/**
 *
 * @author AJPDHN
 */
public class ProveedorVM extends Objectos {

    private String _accion = "insert", _mony;
    private ArrayList<JLabel> _label;
    private ArrayList<JTextField> _textField;
    private JTable _tableProveedor, _tableReporte, _tablePagosCuotas;
    private DefaultTableModel modelo1, modelo2, modelo3;
    private JSpinner _spinnerPaginas, _spinnerCuotas;
    private Paginador<TProveedor> _paginadorProveedor;
    private Paginador<TProveedor> _paginadorReportes;
    private Paginador<TPagos_proveedor> _paginadorPagos;
    private FormatDecimal _format;
    private Codigos _codigos;
    private static TUsuarios _dataUsuario;
    private SimpleDateFormat formateador;
    private DateChooserCombo _dateChooser1, _dateChooser2;
    private Ticket Ticket1 = new Ticket();
    private JRadioButton _radioButton1, _radioButton2;
    public int seccion;
    private int _reg_por_pagina = 10;
    private int _num_pagina = 1;

    public ProveedorVM(TUsuarios dataUsuario) {
        _dataUsuario = dataUsuario;
        formateador = new SimpleDateFormat("dd/MM/yyyy");
    }

    public ProveedorVM(
            Object[] objects,
            ArrayList<JLabel> label,
            ArrayList<JTextField> textField) {
        _label = label;
        _textField = textField;
        _tableProveedor = (JTable) objects[0];
        _spinnerPaginas = (JSpinner) objects[1];
        _tableReporte = (JTable) objects[2];
        _dateChooser1 = (DateChooserCombo) objects[3];
        _dateChooser2 = (DateChooserCombo) objects[4];
        _tablePagosCuotas = (JTable) objects[5];
        _radioButton1 = (JRadioButton) objects[6];
        _radioButton2 = (JRadioButton) objects[7];
        _spinnerCuotas = (JSpinner) objects[8];
        _mony = ConfigurationVM.Mony;
        formateador = new SimpleDateFormat("dd/MM/yyyy");
        _format = new FormatDecimal();
        _codigos = new Codigos();
        restablecer();
        RestablecerReport();
        restablecerFormaPago();
    }

    // <editor-fold defaultstate="collapsed" desc="CODIGO DE REGISTRAR PROVEEDOR">
    public void RegistrarProveedor() {
        if (_textField.get(0).getText().equals("")) {
            _label.get(0).setText("Ingrese el proveedor");
            _label.get(0).setForeground(Color.RED);
            _textField.get(0).requestFocus();
        } else {
            if (_textField.get(1).getText().equals("")) {
                _label.get(1).setText("Ingrese el email");
                _label.get(1).setForeground(Color.RED);
                _textField.get(1).requestFocus();
            } else {
                if (!eventos.isEmail(_textField.get(1).getText())) {
                    _label.get(1).setText("Ingrese un email valido");
                    _label.get(1).setForeground(Color.RED);
                    _textField.get(1).requestFocus();
                } else {
                    if (_textField.get(2).getText().equals("")) {
                        _label.get(2).setText("Ingrese el telefono");
                        _label.get(2).setForeground(Color.RED);
                        _textField.get(2).requestFocus();
                    } else {
                        if (_textField.get(3).getText().equals("")) {
                            _label.get(3).setText("Ingrese la direccion");
                            _label.get(3).setForeground(Color.RED);
                            _textField.get(3).requestFocus();
                        } else {
                            List<TProveedor> listEmail = proveedores().stream()
                                    .filter(u -> u.getEmail()
                                    .equals(_textField.get(1).getText()))
                                    .collect(Collectors.toList());
                            try {
                                switch (_accion) {
                                    case "insert":
                                        if (listEmail.size() == 0) {
                                            SaveData();
                                        } else {
                                            if (!listEmail.isEmpty()) {
                                                _label.get(1).setText("El email ya esta registrado");
                                                _label.get(1).setForeground(Color.RED);
                                                _textField.get(1).requestFocus();
                                            }
                                        }
                                        break;
                                    case "update":
                                        if (listEmail.size() > 0) {
                                            if (listEmail.get(0).getID() == _idProveedor) {
                                                SaveData();
                                            } else {

                                                if (listEmail.get(0).getID() != _idProveedor) {
                                                    _label.get(1).setText("El email ya esta registrado");
                                                    _label.get(1).setForeground(Color.RED);
                                                    _textField.get(1).requestFocus();
                                                }
                                            }
                                        } else {
                                            if (listEmail.size() == 0) {
                                                SaveData();
                                            }
                                        }
                                        break;
                                }
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, e);
                            }
                        }
                    }
                }
            }
        }
    }

    private void SaveData() throws SQLException {
        try {
            final QueryRunner qr = new QueryRunner(true);
            getConn().setAutoCommit(false);
            byte[] image = Uploadimage.getImageByte();
            if (image == null) {
                image = Objectos.uploadimage.getTransFoto(_label.get(4));
            }
            switch (_accion) {
                case "insert":
                    String sqlProveedor1 = "INSERT INTO tproveedor (Proveedor,Email,"
                            + " Telefono,Direccion,Fecha,Imagen) VALUES(?,?,?,?,?,?)";
                    Object[] dataProveedor1 = {
                        _textField.get(0).getText(),
                        _textField.get(1).getText(),
                        _textField.get(2).getText(),
                        _textField.get(3).getText(),
                        new Date(),
                        image,};
                    qr.insert(getConn(), sqlProveedor1, new ColumnListHandler(), dataProveedor1);

                    String sqlReport = "INSERT INTO treportes_proveedor (Deuda,Mensual,Cambio,"
                            + "DeudaActual,FechaDeuda,UltimoPago,FechaPago,Ticket,IdProveedor)"
                            + " VALUES (?,?,?,?,?,?,?,?,?)";
                    List<TProveedor> proveedor = proveedores();
                    Object[] dataReport = {
                        0,
                        0,
                        0,
                        0,
                        null,
                        0,
                        null,
                        "0000000000",
                        proveedor.get(proveedor.size() - 1).getID(),};
                    qr.insert(getConn(), sqlReport, new ColumnListHandler(), dataReport);
                    break;
                case "update":
//                    Object[] dataProveedor2 = {
//                        _textField.get(0).getText(),
//                        _textField.get(1).getText(),
//                        _textField.get(2).getText(),
//                        _textField.get(3).getText(),
//                        image
//                    };
//                    String sqlProveedor2 = "UPDATE tproveedor SET Proveedor = ?,Email = ?,"
//                            + "Telefono = ?,Direccion = ?,Imagen = ? WHERE ID =" + _idProveedor;
//                    qr.update(getConn(), sqlProveedor2, dataProveedor2);
                    break;
            }
            getConn().commit();
            restablecer();
        } catch (Exception e) {
            getConn().rollback();
            JOptionPane.showMessageDialog(null, e);
        }
    }
    private List<TProveedor> proveedorFilter;

    public void SearchProveedores(String campo) {
        String[] titulos = {"Id", "Proveedor",
            "Email", "Dirección", "Telefono", "Image"};
        modelo1 = new DefaultTableModel(null, titulos);
        int inicio = (_num_pagina - 1) * _reg_por_pagina;
        if (campo.equals("")) {
            proveedorFilter = proveedores();
        } else {
            proveedorFilter = proveedores().stream()
                    .filter(C -> C.getProveedor().startsWith(campo)
                    || C.getEmail().startsWith(campo))
                    .collect(Collectors.toList());
        }
        var data = proveedorFilter;
        var list = data.stream()
                .skip(inicio).limit(_reg_por_pagina)
                .collect(Collectors.toList());
        if (!list.isEmpty()) {
            list.forEach(item -> {
                Object[] registros = {
                    item.getID(),
                    item.getProveedor(),
                    item.getEmail(),
                    item.getDireccion(),
                    item.getTelefono(),
                    item.getImagen()
                };
                modelo1.addRow(registros);
            });
        }
        _tableProveedor.setModel(modelo1);
        _tableProveedor.setRowHeight(30);
        _tableProveedor.getColumnModel().getColumn(0).setMaxWidth(0);
        _tableProveedor.getColumnModel().getColumn(0).setMinWidth(0);
        _tableProveedor.getColumnModel().getColumn(0).setPreferredWidth(0);
        _tableProveedor.getColumnModel().getColumn(5).setMaxWidth(0);
        _tableProveedor.getColumnModel().getColumn(5).setMinWidth(0);
        _tableProveedor.getColumnModel().getColumn(5).setPreferredWidth(0);

    }
    private int _idProveedor = 0;

    public void GetProveedor() {
        _accion = "update";
        int filas = _tableProveedor.getSelectedRow();
        _idProveedor = (Integer) modelo1.getValueAt(filas, 0);
        _textField.get(0).setText((String) modelo1.getValueAt(filas, 1));
        _textField.get(1).setText((String) modelo1.getValueAt(filas, 2));
        _textField.get(3).setText((String) modelo1.getValueAt(filas, 3));
        _textField.get(2).setText((String) modelo1.getValueAt(filas, 4));
        uploadimage.byteImage(_label.get(4), (byte[]) modelo1.getValueAt(filas, 5));
    }

    public final void restablecer() {
        seccion = 0;
        _accion = "insert";
        _textField.get(0).setText("");
        _textField.get(1).setText("");
        _textField.get(2).setText("");
        _textField.get(3).setText("");
        _label.get(0).setText("Proveedor");
        _label.get(0).setForeground(new Color(102, 102, 102));
        _label.get(1).setText("Email");
        _label.get(1).setForeground(new Color(102, 102, 102));
        _label.get(2).setText("Telefono");
        _label.get(2).setForeground(new Color(102, 102, 102));
        _label.get(3).setText("Direccion");
        _label.get(3).setForeground(new Color(102, 102, 102));
        _label.get(4).setIcon(new ImageIcon(getClass().getClassLoader()
                .getResource("Resources/logo-google_1.png")));
        SearchProveedores("");
        SpinnerNumberModel model = new SpinnerNumberModel(
                10.0, // Dato visualizado al inicio en el spinner 
                1.0, // Límite inferior 
                100.0, // Límite superior 
                1.0 // incremento-decremento 
        );
        _spinnerPaginas.setModel(model);
        proveedorFilter = proveedores();
        if (!proveedorFilter.isEmpty()) {
            _paginadorProveedor = new Paginador<>(proveedorFilter,
                    _label.get(5), _reg_por_pagina);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CODIGO DE PAGOS Y REPORTES">
    private int _idReport, _idProveedorRport;
    private Double _deudaActual = 0.0, _cambio = 0.0, pagosCuotas = 0.0;
    private Double _pago = 0.0, _mensual = 0.0, _deudaActualProveedor = 0.0, _deuda;
    private String _ticketCuota, nameProveedor;
    public int _seccion1 = 0;
    private String _formaPago;

    private List<TProveedor> reporteFilter;

    public void SearchReportes(String valor) {
        String[] titulos = {"Id", "Proveedor",
            "Email", "Direccion", "Telefono"};
        modelo2 = new DefaultTableModel(null, titulos);
        int inicio = (_num_pagina - 1) * _reg_por_pagina;
        if (valor.equals("")) {
            reporteFilter = proveedores();
        } else {
            reporteFilter = proveedores().stream()
                    .filter(C -> C.getProveedor().startsWith(valor)
                    || C.getEmail().startsWith(valor))
                    .collect(Collectors.toList());
        }
        var data = reporteFilter;
        var list = data.stream()
                .skip(inicio).limit(_reg_por_pagina)
                .collect(Collectors.toList());
        if (!list.isEmpty()) {
            list.forEach(item -> {
                Object[] registros = {
                    item.getID(),
                    item.getProveedor(),
                    item.getEmail(),
                    item.getDireccion(),
                    item.getTelefono(),};
                modelo2.addRow(registros);
            });
        }
        _tableReporte.setModel(modelo2);
        _tableReporte.setRowHeight(30);
        _tableReporte.getColumnModel().getColumn(0).setMaxWidth(0);
        _tableReporte.getColumnModel().getColumn(0).setMinWidth(0);
        _tableReporte.getColumnModel().getColumn(0).setPreferredWidth(0);

    }

    public void GetReportProveedor() {
        int filas = _tableReporte.getSelectedRow();
        _idProveedorRport = (Integer) modelo2.getValueAt(filas, 0);
        var proveedoreFilter = reportesProveedores(_idProveedorRport);
        if (!proveedoreFilter.isEmpty()) {
            var proveedor = proveedoreFilter.get(0);
            _idReport = proveedor.getIdReporte();
            nameProveedor = proveedor.getProveedor();
            _label.get(6).setText(nameProveedor);
            _deudaActual = (Double) proveedor.getDeudaActual();
            _deuda = (Double) proveedor.getDeuda();
            _label.get(7).setText(_mony + _format.decimal(_deudaActual));
            _label.get(8).setText(_mony + _format.decimal((Double) proveedor.getUltimoPago()));
            _ticketCuota = proveedor.getTicket();
            _label.get(9).setText(_ticketCuota);
            if (null != proveedor.getFechaPago()) {
                _label.get(10).setText(proveedor.getFechaPago().toString());
            }

            _label.get(11).setText(_mony + _format.decimal((Double) proveedor.getMensual()));

            historialPagos(false);

            //FORMA DE PAGOS
            _formaPago = proveedor.getFormaPago();
            _mensual = proveedor.getMensual();
            if (_formaPago == null || _mensual.equals(0.0)) {
                _label.get(24).setText("Establezca una forma de pago");
                _label.get(24).setForeground(Color.RED);
            } else {
                var forma = _formaPago.equals("Q") ? "Cuotas quincenales " : "Cuotas por mes ";
                _label.get(24).setText(forma);
                _label.get(24).setForeground(new Color(102, 102, 102));
            }
            _label.get(21).setText(_mony + _format.decimal(_deudaActual));
            getCuotas();
            Pagos();
        }
    }
    private int cuotas1;

    public void Pagos() {
        var value = (Number) _spinnerCuotas.getValue();
        cuotas1 = value.intValue();
        if (cuotas1 == 0) {
            _label.get(12).setText("Ingrese las cuotas a pagar");
            _label.get(12).setForeground(Color.RED);
        } else {
            if (!_textField.get(4).getText().isEmpty()) {
                _label.get(12).setText("Ingrese el pago");
                _label.get(12).setForeground(Color.RED);
                if (_idReport == 0) {
                    _label.get(12).setText("Seleccione un proveedor");
                    _label.get(12).setForeground(Color.RED);
                } else {
                    if (_formaPago == null || _mensual.equals(0.0)) {
                        JOptionPane.showConfirmDialog(null,
                                "Establezca una forma de pago", "Forma de pago",
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    } else {
                        if (_deudaActual > 0) {
                            var valor1 = Math.ceil((Double) _deudaActual / (Double) _mensual);
                            var cuotas2 = (int) Math.ceil(valor1);
                            if (cuotas2 >= cuotas1) {
                                if (!_textField.get(4).getText().isEmpty()) {
                                    _pago = _format.reconstruir(_textField.get(4).getText());
                                    pagosCuotas = _mensual * cuotas1;
                                    if (_pago >= pagosCuotas) {
                                        if (_pago > pagosCuotas) {
                                            _cambio = _pago - pagosCuotas;
                                            _label.get(12).setText("Cambio para el sistema " + _mony
                                                    + _format.decimal(_cambio));
                                            _label.get(12).setForeground(Color.RED);
                                        } else {
                                            _cambio = 0.0;
                                        }
                                        _deudaActualProveedor = _deudaActual - pagosCuotas;

                                    } else {
                                        _cambio = 0.0;
                                        var deuda = pagosCuotas - _pago;
                                        _label.get(12).setText("Importe faltante " + _mony + _format.decimal(deuda));
                                        _label.get(12).setForeground(Color.RED);
                                    }
                                }
                            } else {
                                _label.get(12).setText("Se sobrepaso de las cuotas a pagar");
                                _label.get(12).setForeground(Color.RED);
                            }
                        } else {
                            _label.get(12).setText("El sistema no contiene deuda con el proveedor");
                            _label.get(12).setForeground(Color.RED);
                        }
                    }
                }
            } else {
                _label.get(12).setText("Ingresar el pago");
                _label.get(7).setText(_mony + _format.decimal(_deudaActual));
            }
        }
    }

    public void EjecutarPago() throws SQLException {
        final QueryRunner qr = new QueryRunner(true);
        if (Objects.equals(_idReport, 0)) {
            _label.get(12).setText("Seleccione un proveedor");
        } else {
            if (_formaPago == null || _mensual.equals(0.0)) {
                JOptionPane.showConfirmDialog(null,
                        "Establezca una forma de pago", "Forma de pago",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            } else {
                if (_textField.get(4).getText().isEmpty()) {
                    _label.get(12).setText("Ingrese el pago");
                    _textField.get(4).requestFocus();
                } else {
                    String fecha = new Calendario().getFecha();
                    //Realizar consulta al usuario que inicia sesión
                    var usuario = _dataUsuario.getNombre() + " " + _dataUsuario.getApellido();
                    if (!_deuda.equals(0) || !_deuda.equals(0.0)) {
                        if (_pago >= pagosCuotas) {
                            try {
                                getConn().setAutoCommit(false);
                                String ticket = _codigos.codesTickets(_ticketCuota);
                                String query1 = "INSERT INTO tpagos_proveedor(Deuda,Saldo, Pago,Cambio,"
                                        + "Fecha,Ticket,IdUsuario,Usuario,IdProveedor,FechaDeuda,Cuota,FormaPago)"
                                        + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
                                var dataReport = ReporteProveedor().stream()
                                        .filter(u -> u.getIdProveedor() == _idProveedorRport)
                                        .collect(Collectors.toList()).get(0);
                                Object[] data1 = {
                                    _deuda,
                                    _deudaActualProveedor,
                                    _pago,
                                    _cambio,
                                    new Date(),
                                    ticket,
                                    _dataUsuario.getIdUsuario(),
                                    usuario,
                                    _idProveedorRport,
                                    dataReport.getFechaDeuda(),
                                    dataReport.getMensual(),
                                    dataReport.getFormaPago()
                                };
                                qr.insert(getConn(), query1, new ColumnListHandler(), data1);
                                if (_deudaActualProveedor.equals(0.0)) {
                                    String query2 = "UPDATE treportes_proveedor SET Deuda = ?,"
                                            + "Mensual = ?,FechaDeuda = ?,DeudaActual = ?,"
                                            + "UltimoPago = ?,Cambio = ?,FechaPago = ?,Ticket = ? "
                                            + " WHERE IdReporte =" + _idReport;
                                    Object[] data2 = {
                                        0.0,
                                        0.0,
                                        null,
                                        0.0,
                                        0.0,
                                        0.0,
                                        null,
                                        "0000000000"};
                                    qr.update(getConn(), query2, data2);
                                } else {
                                    String query2 = "UPDATE treportes_proveedor SET DeudaActual = ?,"
                                            + "UltimoPago = ?,Cambio = ?,FechaPago = ?,Ticket = ?"
                                            + " WHERE IdReporte =" + _idReport;
                                    Object[] data2 = {
                                        _deudaActualProveedor,
                                        _pago,
                                        _cambio,
                                        new Date(),
                                        ticket,};
                                    qr.update(getConn(), query2, data2);
                                }
                                Ticket1.TextoCentro("Sistema de ventas PDHN"); // imprime en el centro 
                                Ticket1.TextoIzquierda("Direccion");
                                Ticket1.TextoIzquierda("La Ceiba, Atlantidad");
                                Ticket1.TextoIzquierda("Tel 658912146");
                                Ticket1.LineasGuion();
                                Ticket1.TextoCentro("FACTURA"); // imprime en el centro 
                                Ticket1.LineasGuion();
                                Ticket1.TextoIzquierda("Factura: " + ticket);
                                Ticket1.TextoIzquierda("Cliente: " + nameProveedor);
                                Ticket1.TextoIzquierda("Fecha: " + fecha);
                                Ticket1.TextoIzquierda("Usuario: " + usuario);
                                Ticket1.LineasGuion();
                                Ticket1.TextoCentro("Deuda: " + _mony + _format.decimal(_deuda));
                                Ticket1.LineasGuion();
                                var agreement = _formaPago.equals("Q") ? "Cuotas quincenales " : "Cuotas por mes ";
                                Ticket1.TextoExtremo(agreement, _mony + _format.decimal(_mensual));
                                Ticket1.TextoExtremo("Cantidad de cuotas pagadas:", String.valueOf(cuotas1));
                                Ticket1.TextoExtremo("Pago de cuotas:", _mony + _format.decimal(pagosCuotas));
                                Ticket1.TextoExtremo("Deuda anterior:", _mony + _format.decimal(_deudaActual));
                                Ticket1.TextoExtremo("Pago:", _mony + _format.decimal(_pago));
                                Ticket1.TextoExtremo("Cambio:", _mony + _format.decimal(_cambio));
                                Ticket1.TextoExtremo("Deuda actual:", _mony + _format.decimal(_deudaActualProveedor));
                                Ticket1.TextoCentro("PDHN");
                                Ticket1.print();
                                getConn().commit();
                                RestablecerReport();
                            } catch (Exception e) {
                                getConn().rollback();
                                JOptionPane.showMessageDialog(null, e);
                            }
                        }
                    } else {
                        _label.get(12).setText("El sistema no tiene deuda");
                    }
                }
            }
        }
    }
    private List<TPagos_proveedor> listPagos;

    public void historialPagos(boolean filtrar) {
        listPagos = new ArrayList<>();
        try {
            _dateChooser1.setFormat(3);
            _dateChooser2.setFormat(3);
            var cal = new Calendario();
            String[] titulos = {"Id", "Deuda", "Saldo", "Pago",
                "Cambio", "Fecha", "Ticket", "Fecha Deuda", "Cuota", "FormaPago"};
            modelo3 = new DefaultTableModel(null, titulos);
            var date1 = formateador.parse(_dateChooser1.getSelectedPeriodSet().toString());
            var date2 = formateador.parse(_dateChooser2.getSelectedPeriodSet().toString());
            if (filtrar) {
                if (date2.after(date1) || date1.equals(date2)) {
                    var listPagos1 = new ArrayList<TPagos_proveedor>();
                    var pagos = Pagos_proveedor().stream()
                            .filter(u -> u.getIdProveedor() == _idProveedorRport)
                            .collect(Collectors.toList());
                    formateador = new SimpleDateFormat("yyyy/MM/dd");
                    _dateChooser1.setFormat(3);
                    var date3 = _dateChooser1.getCurrent();
                    var date4 = formateador.parse(cal.getFecha(date3));
                    for (TPagos_proveedor pago : pagos) {
                        var date5 = pago.getFecha();
                        if (date4.equals(date5) || date4.before(date5)) {
                            listPagos1.add(pago);
                        }
                    }
                    _dateChooser2.setFormat(3);
                    var date6 = _dateChooser2.getCurrent();
                    var date7 = formateador.parse(cal.getFecha(date6));
                    for (TPagos_proveedor pago : listPagos1) {
                        var date8 = pago.getFecha();
                        if (date7.equals(date8) || date7.after(date8)) {
                            Object[] registros = {
                                pago.getId(),
                                _mony + _format.decimal(pago.getDeuda()),
                                _mony + _format.decimal(pago.getSaldo()),
                                _mony + _format.decimal(pago.getPago()),
                                _mony + _format.decimal(pago.getCambio()),
                                pago.getFecha(),
                                pago.getTicket(),
                                pago.getFechaDeuda(),
                                _mony + _format.decimal(pago.getCuota()),
                                pago.getFormaPago()
                            };
                            modelo3.addRow(registros);
                            listPagos.add(pago);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "La fecha final debe ser mayor a la fecha inicial ");
                }
            } else {
                int inicio = (_num_pagina - 1) * _reg_por_pagina;
                var pagos = Pagos_proveedor().stream()
                        .filter(u -> u.getIdProveedor() == _idProveedorRport)
                        .collect(Collectors.toList());
                listPagos = pagos;
                var data = pagos.stream()
                        .skip(inicio).limit(_reg_por_pagina)
                        .collect(Collectors.toList());
                Collections.reverse(listPagos);
                Collections.reverse(data);
                for (TPagos_proveedor pago : data) {
                    Object[] registros = {
                        pago.getId(),
                        _mony + _format.decimal(pago.getDeuda()),
                        _mony + _format.decimal(pago.getSaldo()),
                        _mony + _format.decimal(pago.getPago()),
                        _mony + _format.decimal(pago.getCambio()),
                        pago.getFecha(),
                        pago.getTicket(),
                        pago.getFechaDeuda(),
                        _mony + _format.decimal(pago.getCuota()),
                        pago.getFormaPago()
                    };
                    modelo3.addRow(registros);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        _tablePagosCuotas.setModel(modelo3);
        _tablePagosCuotas.setRowHeight(30);
        _tablePagosCuotas.getColumnModel().getColumn(0).setMaxWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(0).setMinWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(0).setPreferredWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(7).setMaxWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(7).setMinWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(7).setPreferredWidth(0);
//        _tablePagosCuotas.getColumnModel().getColumn(8).setMaxWidth(0);
//        _tablePagosCuotas.getColumnModel().getColumn(8).setMinWidth(0);
//        _tablePagosCuotas.getColumnModel().getColumn(8).setPreferredWidth(0);
    }
    private int _idHistorial;

    public void GetHistorialPago() {
        formateador = new SimpleDateFormat("dd/MM/yyyy");
        int filas = _tablePagosCuotas.getSelectedRow();
        _idHistorial = (Integer) modelo3.getValueAt(filas, 0);
        var deuda = (String) modelo3.getValueAt(filas, 1);
        _label.get(13).setText(deuda);
        var saldo = (String) modelo3.getValueAt(filas, 2);
        _label.get(14).setText(saldo);
        var fechaDeuda = (Date) modelo3.getValueAt(filas, 7);
        if (fechaDeuda != null) {
            _label.get(15).setText(fechaDeuda.toString());
        }
        var ticket = (String) modelo3.getValueAt(filas, 6);
        _label.get(16).setText(ticket);
        var pago = (String) modelo3.getValueAt(filas, 3);
        _label.get(17).setText(pago);
        var mensual = (String) modelo3.getValueAt(filas, 8);
        _label.get(18).setText(mensual);
        var fechaPago = (Date) modelo3.getValueAt(filas, 5);
        if (fechaPago != null) {
            _label.get(19).setText(fechaPago.toString());
        }
        var cambio = (String) modelo3.getValueAt(filas, 4);
        _label.get(20).setText(cambio);
        var usuario = _dataUsuario.getNombre() + " " + _dataUsuario.getApellido();

        var forma = (String) modelo3.getValueAt(filas, 9);
        var agreement = forma.equals("Q") ? "Cuotas quincenales " : "Cuotas por mes ";
        _label.get(25).setText(agreement);
        var pagosCuotas = _format.reconstruir(pago.replace(_mony, ""))
                - _format.reconstruir(cambio.replace(_mony, ""));
        var deuda1 = _format.reconstruir(deuda.replace(_mony, ""));
        var saldo1 = _format.reconstruir(saldo.replace(_mony, ""));
        var deudaAnterior = (deuda1 - (deuda1 - saldo1) - pagosCuotas);
        var cuotas = pagosCuotas / _format.reconstruir(mensual.replace(_mony, ""));

        Ticket1.TextoCentro("Sistema de ventas PDHN"); // imprime en el centro 
        Ticket1.TextoIzquierda("Direccion");
        Ticket1.TextoIzquierda("La Ceiba, Atlantidad");
        Ticket1.TextoIzquierda("Tel 658912146");
        Ticket1.LineasGuion();
        Ticket1.TextoCentro("FACTURA"); // imprime en el centro  
        Ticket1.LineasGuion();
        Ticket1.TextoIzquierda("Factura: " + ticket);
        Ticket1.TextoIzquierda("Proveedor: " + nameProveedor);
        Ticket1.TextoIzquierda("Fecha: " + fechaPago);
        Ticket1.TextoIzquierda("Usuario: " + usuario);
        Ticket1.LineasGuion();
        Ticket1.TextoCentro("Su deuda: " + deuda);
        Ticket1.LineasGuion();
        Ticket1.TextoExtremo(agreement, mensual);
        Ticket1.TextoExtremo("Cuotas pagadas:", String.valueOf(cuotas));
        Ticket1.TextoExtremo("Pago de cuotas:", _mony + _format.decimal(pagosCuotas));
        Ticket1.TextoExtremo("Deuda anterior:", _mony + _format.decimal(deudaAnterior));
        Ticket1.TextoExtremo("Pago:", pago);
        Ticket1.TextoExtremo("Cambio:", cambio);
        Ticket1.TextoExtremo("Deuda actual:", saldo);
        Ticket1.TextoCentro("PDHN");
    }

    public void TicketDeuda() {
        switch (_seccion1) {
            case 1:
                if (_idHistorial == 0) {
                    JOptionPane.showMessageDialog(null, "Seleccione un historial de pago ");
                } else {
                    Ticket1.print();
                }
                break;
        }
    }

    public final void RestablecerReport() {
        _idReport = 0;
        _deudaActual = 0.0;
        _cambio = 0.0;
        _pago = 0.0;
        _mensual = 0.0;
        _label.get(6).setText("Nombre del proveedor");
        _label.get(7).setText(_mony + "0.00");
        _label.get(8).setText(_mony + "0.00");
        _label.get(9).setText("00000000000");
        _label.get(10).setText("--/--/--");
        _label.get(11).setText(_mony + "0.00");
        _label.get(12).setText("Ingrese el pago");
        _textField.get(4).setText("");
        SearchReportes("");
        if (!reporteFilter.isEmpty()) {
            _paginadorReportes = new Paginador<>(reporteFilter, _label.get(5), _reg_por_pagina);
        }
        var model = new SpinnerNumberModel(
                1.0, // Dato visualizado al inicio en el spinner 
                1.0, // Límite inferior 
                100.0, // Límite superior 
                1.0 // incremento-decremento 
        );
        _spinnerCuotas.setModel(model);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CODIGO DE FORMA DE PAGOS">
    private Double cuota;
    private boolean ejecutar;

    public void getCuotas() {
        if (_idReport == 0) {
            _label.get(22).setText("Seleccione un proveedor");
            _label.get(22).setForeground(Color.RED);
            ejecutar = false;
        } else {
            if (_deudaActual > 0) {
                var cuotas = 0;
                if (!_textField.get(5).getText().equals("")) {
                    cuota = _format.reconstruir(_textField.get(5).getText());
                    var valor1 = Math.ceil(_deudaActual / cuota);
                    cuotas = (int) Math.ceil(valor1);
                    _label.get(23).setText(String.valueOf(cuotas));
                    ejecutar = true;
                } else {
                    ejecutar = false;
                    _label.get(23).setText("0");
                }
                _label.get(22).setText("Cuotas");
            } else {
                ejecutar = false;
                _label.get(22).setText("El sistema no contiene deuda");
            }

        }
    }

    public void setCuotas() {
        if (ejecutar) {
            try {
                var qr = new QueryRunner(true);
                var forma = _radioButton2.isSelected() ? "M" : "Q";
                String query = "UPDATE treportes_proveedor SET Mensual = ?,"
                        + "FormaPago = ? WHERE IdReporte =" + _idReport;
                Object[] data = {
                    cuota,
                    forma};
                qr.update(getConn(), query, data);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
            restablecerFormaPago();
        }
    }

    public void restablecerFormaPago() {
        _radioButton1.setSelected(true);
        _radioButton2.setSelected(false);
        _textField.get(5).setText("");
        getCuotas();
    }
    // </editor-fold>

    public void Paginador(String metodo) {
        switch (metodo) {
            case "Primero":
                switch (seccion) {
                    case 0:
                        if (!proveedorFilter.isEmpty()) {
                            _num_pagina = _paginadorProveedor.primero();
                        }
                        break;
                    case 1:
                        switch (_seccion1) {
                            case 0:
                                if (!reporteFilter.isEmpty()) {
                                    _num_pagina = _paginadorReportes.primero();
                                }
                                break;
                            case 1:
                                if (!listPagos.isEmpty()) {
                                    _num_pagina = _paginadorPagos.primero();
                                }
                                break;
                        }
                        break;
                }
                break;
            case "Anterior":
                switch (seccion) {
                    case 0:
                        if (!proveedorFilter.isEmpty()) {
                            _num_pagina = _paginadorProveedor.anterior();
                        }
                        break;
                    case 1:
                        switch (_seccion1) {
                            case 0:
                                if (!reporteFilter.isEmpty()) {
                                    _num_pagina = _paginadorReportes.anterior();
                                }
                                break;
                            case 1:
                                if (!listPagos.isEmpty()) {
                                    _num_pagina = _paginadorPagos.anterior();
                                }
                                break;
                        }
                        break;
                }
                break;
            case "Siguiente":
                switch (seccion) {
                    case 0:
                        if (!proveedorFilter.isEmpty()) {
                            _num_pagina = _paginadorProveedor.siguiente();
                        }
                        break;
                    case 1:
                        switch (_seccion1) {
                            case 0:
                                if (!reporteFilter.isEmpty()) {
                                    _num_pagina = _paginadorReportes.siguiente();
                                }
                                break;
                            case 1:
                                if (!listPagos.isEmpty()) {
                                    _num_pagina = _paginadorPagos.siguiente();
                                }
                                break;
                        }
                        break;
                }
                break;
            case "Ultimo":
                switch (seccion) {
                    case 0:
                        if (!proveedorFilter.isEmpty()) {
                            _num_pagina = _paginadorReportes.ultimo();
                        }
                        break;
                    case 1:
                        switch (_seccion1) {
                            case 0:
                                if (!reporteFilter.isEmpty()) {
                                    _num_pagina = _paginadorProveedor.ultimo();
                                }
                                break;
                            case 1:
                                if (!listPagos.isEmpty()) {
                                    _num_pagina = _paginadorPagos.ultimo();
                                }
                                break;
                        }
                        break;
                }
                break;
        }
        switch (seccion) {
            case 0:
                SearchProveedores("");
                break;
            case 1:
                switch (_seccion1) {
                    case 0:
                        SearchReportes("");
                        break;
                    case 1:
                        historialPagos(false);
                        break;
                }
                break;
        }
    }

    public void Registro_Paginas() {
        _num_pagina = 1;
        Number value = (Number) _spinnerPaginas.getValue();
        _reg_por_pagina = value.intValue();
        switch (seccion) {
            case 0:
                if (!proveedorFilter.isEmpty()) {
                    _paginadorProveedor = new Paginador<>(proveedorFilter,
                            _label.get(5), _reg_por_pagina);
                }
                SearchProveedores("");
                break;
            case 1:
                switch (_seccion1) {
                    case 0:
                        if (!reporteFilter.isEmpty()) {
                            _paginadorReportes = new Paginador<>(reporteFilter,
                                    _label.get(5), _reg_por_pagina);
                        }
                        SearchReportes("");
                        break;
                    case 1:
                        if (!listPagos.isEmpty()) {
                            _paginadorPagos = new Paginador<>(listPagos,
                                    _label.get(5), _reg_por_pagina);
                        }
                        historialPagos(false);
                        break;
                }
                break;
        }
    }
}
