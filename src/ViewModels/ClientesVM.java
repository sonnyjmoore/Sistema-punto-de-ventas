/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewModels;

import Conexion.Consult;
import Library.*;
import Models.Cliente.*;
import Models.TPagos_reportes_intereses_cliente;
import Models.Usuario.TUsuarios;
import datechooser.beans.DateChooserCombo;
import java.awt.Color;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

/**
 *
 * @author AJPDHN
 */
public class ClientesVM extends Consult {

    private String _accion = "insert", _mony;
    private ArrayList<JLabel> _label;
    private ArrayList<JTextField> _textField;
    private JCheckBox _checkBoxCredito;
    private JTable _tableCliente, _tableReporte, _tableReporteDeuda;
    private JTable _tablePagosCuotas, _tablePagosIntereses;
    private DefaultTableModel modelo1, modelo2, modelo3, modelo4, modelo5;
    private JSpinner _spinnerPaginas;
    private JRadioButton _radioCuotas, _radioInteres;
    private int _idCliente = 0;
    private int _reg_por_pagina = 10;
    private int _num_pagina = 1;
    public int seccion;
    private FormatDecimal _format;
    private Paginador<TClientes> _paginadorClientes;
    private Paginador<TClientes> _paginadorReportes;
    private Paginador<TReportes_clientes> _paginadorReportesDeuda;
    private Paginador<TPagos_clientes> _paginadorPagos;
    private Paginador<TPagos_reportes_intereses_cliente> _paginadorPagosIntereses;
    private Codigos _codigos;
    private SimpleDateFormat formateador;
    private DateChooserCombo _dateChooser, _dateChooser1, _dateChooser2;
    private JCheckBox _checkBox_Dia;
    private static TUsuarios _dataUsuario;
    Ticket Ticket1 = new Ticket();

    public ClientesVM() {
    }

    public ClientesVM(TUsuarios dataUsuario) {
        _dataUsuario = dataUsuario;
        formateador = new SimpleDateFormat("dd/MM/yyyy");
    }

    public ClientesVM(Object[] objects, ArrayList<JLabel> label, ArrayList<JTextField> textField) {
        _label = label;
        _textField = textField;
        _checkBoxCredito = (JCheckBox) objects[0];
        _tableCliente = (JTable) objects[1];
        _spinnerPaginas = (JSpinner) objects[2];
        _tableReporte = (JTable) objects[3];
        _radioCuotas = (JRadioButton) objects[4];
        _radioInteres = (JRadioButton) objects[5];
        _tableReporteDeuda = (JTable) objects[6];
        _dateChooser = (DateChooserCombo) objects[7];
        _checkBox_Dia = (JCheckBox) objects[8];
        _dateChooser1 = (DateChooserCombo) objects[9];
        _dateChooser2 = (DateChooserCombo) objects[10];
        _tablePagosCuotas = (JTable) objects[11];
        _tablePagosIntereses = (JTable) objects[12];
        _format = new FormatDecimal();
        _mony = ConfigurationVM.Mony;
        _codigos = new Codigos();
        formateador = new SimpleDateFormat("dd/MM/yyyy");
        restablecer();
        RestablecerReport();
        ResetReportDeudas();
    }

    // <editor-fold defaultstate="collapsed" desc="CODIGO DE REGISTRAR CLIENTE">
    public void RegistrarCliente() {
        if (_textField.get(0).getText().equals("")) {
            _label.get(0).setText("Ingrese el nid");
            _label.get(0).setForeground(Color.RED);
            _textField.get(0).requestFocus();
        } else {
            if (_textField.get(1).getText().equals("")) {
                _label.get(1).setText("Ingrese el nombre");
                _label.get(1).setForeground(Color.RED);
                _textField.get(1).requestFocus();
            } else {
                if (_textField.get(2).getText().equals("")) {
                    _label.get(2).setText("Ingrese el apellido");
                    _label.get(2).setForeground(Color.RED);
                    _textField.get(2).requestFocus();
                } else {
                    if (_textField.get(3).getText().equals("")) {
                        _label.get(3).setText("Ingrese el email");
                        _label.get(3).setForeground(Color.RED);
                        _textField.get(3).requestFocus();
                    } else {
                        if (!Objectos.eventos.isEmail(_textField.get(3).getText())) {
                            _label.get(3).setText("Ingrese un email valido");
                            _label.get(3).setForeground(Color.RED);
                            _textField.get(3).requestFocus();
                        } else {
                            if (_textField.get(4).getText().equals("")) {
                                _label.get(4).setText("Ingrese el telefono");
                                _label.get(4).setForeground(Color.RED);
                                _textField.get(4).requestFocus();
                            } else {
                                if (_textField.get(5).getText().equals("")) {
                                    _label.get(5).setText("Ingrese la direccion");
                                    _label.get(5).setForeground(Color.RED);
                                    _textField.get(5).requestFocus();
                                } else {
                                    int count;
                                    List<TClientes> listEmail = clientes().stream()
                                            .filter(u -> u.getEmail().equals(_textField.get(3).getText()))
                                            .collect(Collectors.toList());
                                    count = listEmail.size();
                                    List<TClientes> listNid = clientes().stream()
                                            .filter(u -> u.getNid().equals(_textField.get(0).getText()))
                                            .collect(Collectors.toList());
                                    count += listNid.size();
                                    try {
                                        switch (_accion) {
                                            case "insert":
                                                if (count == 0) {
                                                    SaveData();
                                                } else {
                                                    if (!listEmail.isEmpty()) {
                                                        _label.get(3).setText("El email ya esta registrado");
                                                        _label.get(3).setForeground(Color.RED);
                                                        _textField.get(3).requestFocus();
                                                    }
                                                    if (!listNid.isEmpty()) {
                                                        _label.get(0).setText("El nid ya esta registrado");
                                                        _label.get(0).setForeground(Color.RED);
                                                        _textField.get(0).requestFocus();
                                                    }
                                                }

                                                break;
                                            case "update":
                                                if (count == 2) {
                                                    if (listEmail.get(0).getID() == _idCliente
                                                            && listNid.get(0).getID() == _idCliente) {
                                                        SaveData();
                                                    } else {
                                                        if (listNid.get(0).getID() != _idCliente) {
                                                            _label.get(0).setText("El nid ya esta registrado");
                                                            _label.get(0).setForeground(Color.RED);
                                                            _textField.get(0).requestFocus();
                                                        }
                                                        if (listEmail.get(0).getID() != _idCliente) {
                                                            _label.get(3).setText("El email ya esta registrado");
                                                            _label.get(3).setForeground(Color.RED);
                                                            _textField.get(3).requestFocus();
                                                        }
                                                    }
                                                } else {
                                                    if (count == 0) {
                                                        SaveData();
                                                    } else {
                                                        if (!listNid.isEmpty()) {
                                                            if (listNid.get(0).getID() == _idCliente) {
                                                                SaveData();
                                                            } else {
                                                                _label.get(0).setText("El nid ya esta registrado");
                                                                _label.get(0).setForeground(Color.RED);
                                                                _textField.get(0).requestFocus();
                                                            }
                                                        }
                                                        if (!listEmail.isEmpty()) {
                                                            if (listEmail.get(0).getID() == _idCliente) {
                                                                SaveData();
                                                            } else {
                                                                _label.get(3).setText("El email ya esta registrado");
                                                                _label.get(3).setForeground(Color.RED);
                                                                _textField.get(3).requestFocus();
                                                            }
                                                        }
                                                    }
                                                }
                                                break;
                                        }
                                    } catch (SQLException ex) {
                                        JOptionPane.showMessageDialog(null, ex);
                                    }
                                }
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
                image = Objectos.uploadimage.getTransFoto(_label.get(6));
            }
            switch (_accion) {
                case "insert":
                    String sqlCliente1 = "INSERT INTO tclientes(Nid,Nombre, Apellido,Email,"
                            + " Telefono,Direccion,Credito,Fecha,Imagen) VALUES(?,?,?,?,?,?,?,?,?)";
                    Object[] dataCliente1 = {
                        _textField.get(0).getText(),
                        _textField.get(1).getText(),
                        _textField.get(2).getText(),
                        _textField.get(3).getText(),
                        _textField.get(4).getText(),
                        _textField.get(5).getText(),
                        _checkBoxCredito.isSelected(),//tynyint
                        new Calendario().getFecha(),
                        image,};
                    qr.insert(getConn(), sqlCliente1, new ColumnListHandler(), dataCliente1);
                    String sqlReport = "INSERT INTO treportes_clientes (Deuda,Mensual,Cambio,"
                            + "DeudaActual,FechaDeuda,UltimoPago,FechaPago,Ticket,FechaLimite,IdCliente)"
                            + " VALUES (?,?,?,?,?,?,?,?,?,?)";
                    List<TClientes> cliente = clientes();
                    Object[] dataReport = {
                        0,
                        0,
                        0,
                        0,
                        "--/--/--",
                        0,
                        "--/--/--",
                        "0000000000",
                        "--/--/--",
                        cliente.get(cliente.size() - 1).getID(),};
                    qr.insert(getConn(), sqlReport, new ColumnListHandler(), dataReport);

                    String sqlReportInteres = "INSERT INTO treportes_intereses_clientes "
                            + "(Intereses,Pago,Cambio,Cuotas,InteresFecha,TicketIntereses,"
                            + "IdCliente) VALUES (?,?,?,?,?,?,?)";
                    Object[] dataReportInteres = {
                        0,
                        0,
                        0,
                        0,
                        "--/--/--",
                        "0000000000",
                        cliente.get(cliente.size() - 1).getID()
                    };
                    qr.insert(getConn(), sqlReportInteres, new ColumnListHandler(), dataReportInteres);
                    break;
                case "update":
                    Object[] dataCliente2 = {
                        _textField.get(0).getText(),
                        _textField.get(1).getText(),
                        _textField.get(2).getText(),
                        _textField.get(3).getText(),
                        _textField.get(4).getText(),
                        _textField.get(5).getText(),
                        _checkBoxCredito.isSelected(),
                        image
                    };
                    String sqlCliente2 = "UPDATE tclientes SET Nid = ?,Nombre = ?,Apellido = ?,"
                            + "Email = ?,Telefono = ?,Direccion = ?,Credito = ?,"
                            + "Imagen = ? WHERE ID =" + _idCliente;
                    qr.update(getConn(), sqlCliente2, dataCliente2);
                    break;
            }

            getConn().commit();
            restablecer();
        } catch (SQLException ex) {
            getConn().rollback();
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void SearchClientes(String campo) {
        List<TClientes> clienteFilter;
        String[] titulos = {"Id", "Nid", "Nombre", "Apellido",
            "Email", "Direccion", "Telefono", "Credito", "Image"};
        modelo1 = new DefaultTableModel(null, titulos);
        int inicio = (_num_pagina - 1) * _reg_por_pagina;
        if (campo.equals("")) {
            clienteFilter = clientes().stream()
                    .skip(inicio).limit(_reg_por_pagina)
                    .collect(Collectors.toList());
        } else {
            clienteFilter = clientes().stream()
                    .filter(C -> C.getNid().startsWith(campo) || C.getNombre().startsWith(campo)
                    || C.getApellido().startsWith(campo))
                    .skip(inicio).limit(_reg_por_pagina)
                    .collect(Collectors.toList());
        }
        if (!clienteFilter.isEmpty()) {
            clienteFilter.forEach(item -> {
                Object[] registros = {
                    item.getID(),
                    item.getNid(),
                    item.getNombre(),
                    item.getApellido(),
                    item.getEmail(),
                    item.getDireccion(),
                    item.getTelefono(),
                    item.isCredito(),
                    item.getImagen()
                };
                modelo1.addRow(registros);
            });

        }
        _tableCliente.setModel(modelo1);
        _tableCliente.setRowHeight(30);
        _tableCliente.getColumnModel().getColumn(0).setMaxWidth(0);
        _tableCliente.getColumnModel().getColumn(0).setMinWidth(0);
        _tableCliente.getColumnModel().getColumn(0).setPreferredWidth(0);
        _tableCliente.getColumnModel().getColumn(8).setMaxWidth(0);
        _tableCliente.getColumnModel().getColumn(8).setMinWidth(0);
        _tableCliente.getColumnModel().getColumn(8).setPreferredWidth(0);
        _tableCliente.getColumnModel().getColumn(7).setCellRenderer(new Render_CheckBox());
    }

    public void GetCliente() {
        _accion = "update";
        int filas = _tableCliente.getSelectedRow();
        _idCliente = (Integer) modelo1.getValueAt(filas, 0);
        _textField.get(0).setText((String) modelo1.getValueAt(filas, 1));
        _textField.get(1).setText((String) modelo1.getValueAt(filas, 2));
        _textField.get(2).setText((String) modelo1.getValueAt(filas, 3));
        _textField.get(3).setText((String) modelo1.getValueAt(filas, 4));
        _textField.get(4).setText((String) modelo1.getValueAt(filas, 5));
        _textField.get(5).setText((String) modelo1.getValueAt(filas, 6));
        _checkBoxCredito.setSelected((Boolean) modelo1.getValueAt(filas, 7));
        Objectos.uploadimage.byteImage(_label.get(6), (byte[]) modelo1.getValueAt(filas, 8));

    }

    public final void restablecer() {
        seccion = 1;
        _accion = "insert";
        _textField.get(0).setText("");
        _textField.get(1).setText("");
        _textField.get(2).setText("");
        _textField.get(3).setText("");
        _textField.get(4).setText("");
        _textField.get(5).setText("");
        _checkBoxCredito.setSelected(false);
        _checkBoxCredito.setForeground(new Color(102, 102, 102));
        _label.get(0).setText("Nid");
        _label.get(0).setForeground(new Color(102, 102, 102));
        _label.get(1).setText("Nombre");
        _label.get(1).setForeground(new Color(102, 102, 102));
        _label.get(2).setText("Apellido");
        _label.get(2).setForeground(new Color(102, 102, 102));
        _label.get(3).setText("Email");
        _label.get(3).setForeground(new Color(102, 102, 102));
        _label.get(4).setText("Telefono");
        _label.get(4).setForeground(new Color(102, 102, 102));
        _label.get(5).setText("Direccion");
        _label.get(5).setForeground(new Color(102, 102, 102));
        _label.get(6).setIcon(new ImageIcon(getClass().getClassLoader()
                .getResource("Resources/logo-google_1.png")));
        listClientes = clientes();
        if (!listClientes.isEmpty()) {
            _paginadorClientes = new Paginador<>(listClientes,
                    _label.get(7), _reg_por_pagina);
        }
        SpinnerNumberModel model = new SpinnerNumberModel(
                10.0, // Dato visualizado al inicio en el spinner 
                1.0, // Límite inferior 
                100.0, // Límite superior 
                1.0 // incremento-decremento 
        );
        _spinnerPaginas.setModel(model);
        SearchClientes("");
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CODIGO DE PAGOS Y REPORTES">
    private int _interesCoutas = 0, _idReport, _idClienteRport, _idReportIntereses;
    private Double _intereses = 0.0, _deudaActual = 0.0, _interesPago = 0.0;
    private Double _interesesPagos = 0.0, _cambio = 0.0, _interesesCliente = 0.0;
    private Double _pago = 0.0, _mensual = 0.0, _deudaActualCliente = 0.0, _deuda;
    private String _ticketCuota, nameCliente, _ticketIntereses;
    private int coutas;
    public int _seccion1;
    private List<TIntereses_clientes> _listIntereses;

    public void SearchReportes(String valor) {
        List<TClientes> clienteFilter;
        String[] titulos = {"Id", "Nid", "Nombre", "Apellido",
            "Email", "Direccion", "Telefono"};
        modelo2 = new DefaultTableModel(null, titulos);
        int inicio = (_num_pagina - 1) * _reg_por_pagina;
        if (valor.equals("")) {
            clienteFilter = clientes().stream()
                    .skip(inicio).limit(_reg_por_pagina)
                    .collect(Collectors.toList());
        } else {
            clienteFilter = clientes().stream()
                    .filter(C -> C.getNid().startsWith(valor) || C.getNombre().startsWith(valor)
                    || C.getApellido().startsWith(valor))
                    .skip(inicio).limit(_reg_por_pagina)
                    .collect(Collectors.toList());
        }
        if (!clienteFilter.isEmpty()) {
            clienteFilter.forEach(item -> {
                Object[] registros = {
                    item.getID(),
                    item.getNid(),
                    item.getNombre(),
                    item.getApellido(),
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

    public void GetReportCliente() {
        int filas = _tableReporte.getSelectedRow();
        _idClienteRport = (Integer) modelo2.getValueAt(filas, 0);
        List<TReportes_clientes> clienteFilter = reportesClientes(_idClienteRport);
        if (!clienteFilter.isEmpty()) {
            TReportes_clientes cliente = clienteFilter.get(0);
            _idReport = cliente.getIdReporte();
            _idReportIntereses = cliente.getID();
            nameCliente = cliente.getNombre() + " " + cliente.getApellido();
            _label.get(8).setText(nameCliente);
            _deudaActual = (Double) cliente.getDeudaActual();
            _deuda = (Double) cliente.getDeuda();
            _label.get(9).setText(_mony + _format.decimal(_deudaActual));
            _label.get(10).setText(_mony + _format.decimal((Double) cliente.getUltimoPago()));
            _ticketCuota = cliente.getTicket();
            _label.get(11).setText(_ticketCuota);
            _label.get(12).setText(cliente.getFechaPago());
            _label.get(13).setText(_mony + _format.decimal((Double) cliente.getMensual()));
            _listIntereses = InteresesCliente().stream()
                    .filter(u -> u.getIdCliente() == _idClienteRport
                    && u.getCancelado() == false)
                    .collect(Collectors.toList());
            if (_listIntereses.isEmpty()) {
                _label.get(14).setText(_mony + "0.00");
                _label.get(15).setText("0");
                _label.get(16).setText("0000000000");
                _label.get(17).setText("--/--/--");
            } else {
                _interesCoutas = 0;
                _intereses = 0.0;
                _listIntereses.forEach(item -> {
                    _intereses += item.getIntereses();
                    _interesCoutas++;
                });
                _label.get(14).setText(_mony + _format.decimal(_intereses));
                _label.get(15).setText(String.valueOf(_interesCoutas));
                _ticketIntereses = cliente.getTicketIntereses();
                _label.get(16).setText(_ticketIntereses);
                _label.get(17).setText(cliente.getInteresFecha());
            }
            historialPagos(false);
            historialIntereses(false);
        }
    }

    public void Pagos() {
        if (!_textField.get(6).getText().isEmpty()) {
            _label.get(19).setText("Ingrese el pago");
            if (_idReport == 0) {
                _label.get(19).setText("Seleccione un cliente");
            } else {
                if (_radioInteres.isSelected()) {
                    if (!_textField.get(7).getText().isEmpty()) {
                        int cantCoutas = Integer.valueOf(_textField.get(7).getText());
                        if (cantCoutas <= _interesCoutas) {
                            if (!_textField.get(6).getText().isEmpty()) {
                                _interesPago = _format.reconstruir(_textField.get(6).getText());
                                if (_interesPago >= _interesesPagos) {
                                    _cambio = _interesPago - _interesesPagos;
                                    _label.get(19).setText("Cambio para el cliente " + _mony + _format.decimal(_cambio));
                                    _interesesCliente = _intereses - _interesesPagos;
                                    _label.get(14).setText(_mony + _format.decimal(_interesesCliente));
                                } else {
                                    _label.get(19).setText("El pago debe ser " + _mony + _format.decimal(_interesesPagos));
                                    _interesesCliente = _intereses - _interesesPagos;
                                    _label.get(14).setText(_mony + _format.decimal(_interesesCliente));
                                }
                            }
                        } else {
                            _label.get(19).setText("Cuotas inválida");
                        }
                    } else {
                        _label.get(19).setText("Ingrese el número de cuotas");
//                        _textField.get(7).requestFocus();
                    }
                } else if (_radioCuotas.isSelected()) {
                    if (!_textField.get(6).getText().isEmpty()) {
                        _pago = _format.reconstruir(_textField.get(6).getText());
                        TReportes_clientes dataReport = ReporteCliente().stream()
                                .filter(u -> u.getIdReporte() == _idReport)
                                .collect(Collectors.toList()).get(0);
                        _mensual = dataReport.getMensual();
                        if (_pago > _mensual) {
                            if (Objects.equals(_pago, _deudaActual) || _pago > _deudaActual) {
                                _cambio = _pago - _deudaActual;
                                _label.get(19).setText("Cambio para el cliente " + _mony
                                        + _format.decimal(_cambio));
                                _label.get(9).setText(_mony + "0.00");
                                _deudaActual = 0.0;
                                _deudaActualCliente = 0.0;
                            } else {
                                _cambio = _pago - _mensual;
                                _label.get(19).setText("Cambio para el cliente " + _mony
                                        + _format.decimal(_cambio));
                                _deudaActualCliente = _deudaActual - _mensual;
                                _label.get(9).setText(_mony + _format.decimal(_deudaActualCliente));
                            }
                        } else if (Objects.equals(_pago, _mensual)) {
                            _deudaActualCliente = _deudaActual - _mensual;
                            _label.get(9).setText(_mony + _format.decimal(_deudaActualCliente));
                        }
                    }
                }
            }
        } else {
            _label.get(19).setText("Ingresar el pago");
            _label.get(9).setText(_mony + _format.decimal(_deudaActual));
            _label.get(14).setText(_mony + _format.decimal(_intereses));
        }
    }

    public void CuotasIntereses() {

        if (Objects.equals(_idReport, 0)) {
            _label.get(19).setText("Seleccione un cliente");
        } else {
            if (_deudaActual > 0 || _intereses > 0) {
                _label.get(19).setText("Ingrese el pago");
                if (null != _textField.get(7)) {
                    if (_textField.get(7).getText().isEmpty()) {
                        _label.get(14).setText(_mony + _format.decimal(_intereses));
                        _label.get(15).setText(String.valueOf(_interesCoutas));
                        _label.get(18).setText(_mony + "0.00");
                        _label.get(19).setText("Ingrese el pago");
                    } else {
                        _label.get(18).setText(_mony + "0.00");
                        int cantCoutas = Integer.valueOf(_textField.get(7).getText());
                        if (cantCoutas <= _interesCoutas) {
                            _label.get(19).setText("Ingrese el pago");
                            if (!_listIntereses.isEmpty()) {
                                _interesesPagos = 0.0;
                                for (int i = 0; i < cantCoutas; i++) {
                                    _interesesPagos += _listIntereses.get(i).getIntereses();
                                }
                                coutas = _interesCoutas - cantCoutas;
                                double intereses = _intereses - _interesesPagos;
                                _label.get(14).setText(_mony + _format.decimal(intereses));
                                _label.get(15).setText(String.valueOf(coutas));
                                _label.get(18).setText(_mony + _format.decimal(_interesesPagos));
                            }
                        } else {
                            _label.get(19).setText("Cuotas inválida");
//                            _textField.get(7).requestFocus();
                        }
                    }
                }

                Pagos();
            } else {
                _label.get(19).setText("El cliente no contiene deuda");
            }
        }
    }

    public void EjecutarPago() throws SQLException {
        final QueryRunner qr = new QueryRunner(true);
        if (Objects.equals(_idReport, 0)) {
            _label.get(19).setText("Seleccione un cliente");
        } else {
            if (_textField.get(6).getText().isEmpty()) {
                _label.get(19).setText("Ingrese el pago");
                _textField.get(6).requestFocus();
            } else {
                String fecha = new Calendario().getFecha();
                //Realizar consulta al usuario que inicia sesión
                var usuario = _dataUsuario.getNombre() + " " + _dataUsuario.getApellido();
                if (_radioCuotas.isSelected()) {
                    if (!_deuda.equals(0) || !_deuda.equals(0.0)) {
                        if (_pago >= _mensual) {
                            try {
                                getConn().setAutoCommit(false);
                                String dateNow = new Calendario().addMes(1);
                                String _fechalimite = Objects.equals(_deudaActual, 0) ? new Calendario().getFecha() : dateNow;
                                String ticket = _codigos.codesTickets(_ticketCuota);

                                String query1 = "INSERT INTO tpagos_clientes(Deuda,Saldo, Pago,Cambio,"
                                        + "Fecha,FechaLimite,Ticket,IdUsuario,Usuario,IdCliente,FechaDeuda,Mensual)"
                                        + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
                                var dataReport = ReporteCliente().stream()
                                        .filter(u -> u.getIdReporte() == _idClienteRport)
                                        .collect(Collectors.toList()).get(0);

                                Object[] data1 = {
                                    _deuda,
                                    _deudaActualCliente,
                                    _pago,
                                    _cambio,
                                    formateador.parse(fecha),
                                    formateador.parse(_fechalimite),
                                    ticket,
                                    _dataUsuario.getIdUsuario(),
                                    usuario,
                                    _idClienteRport,
                                    formateador.parse(dataReport.getFechaDeuda()),
                                    dataReport.getMensual()
                                };
                                qr.insert(getConn(), query1, new ColumnListHandler(), data1);
                                //MODIFICAR

                                if (_deudaActualCliente.equals(0.0)) {
                                    String query2 = "UPDATE treportes_clientes SET Deuda = ?,"
                                            + ",Mensual = ?,FechaDeuda = ?,DeudaActual = ?,"
                                            + "UltimoPago = ?,Cambio = ?,FechaPago = ?,Ticket = ?,"
                                            + "FechaLimite = ? WHERE IdReporte =" + _idReport;
                                    Object[] data2 = {
                                        0.0,
                                        0.0,
                                        "--/--/--",
                                        0.0,
                                        0.0,
                                        0.0,
                                        "--/--/--",
                                        "0000000000",
                                        "--/--/--",};
                                    qr.update(getConn(), query2, data2);
                                } else {
                                    String query2 = "UPDATE treportes_clientes SET DeudaActual = ?,"
                                            + "UltimoPago = ?,Cambio = ?,FechaPago = ?,Ticket = ?,"
                                            + "FechaLimite = ? WHERE IdReporte =" + _idReport;
                                    Object[] data2 = {
                                        _deudaActualCliente, _pago,
                                        _cambio, fecha, ticket, _fechalimite,};
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
                                Ticket1.TextoIzquierda("Cliente: " + nameCliente);
                                Ticket1.TextoIzquierda("Fecha: " + fecha);
                                Ticket1.TextoIzquierda("Usuario: " + usuario);
                                Ticket1.LineasGuion();
                                Ticket1.TextoCentro("Su cretito: " + _mony + _format.decimal(_deuda));
                                Ticket1.TextoExtremo("Cuotas por 12 meses:", _mony + _format.decimal(_mensual));
                                Ticket1.TextoExtremo("Deuda anterior:", _mony + _format.decimal(_deudaActual));
                                Ticket1.TextoExtremo("Pago:", _mony + _format.decimal(_pago));
                                Ticket1.TextoExtremo("Cambio:", _mony + _format.decimal(_cambio));
                                Ticket1.TextoExtremo("Deuda actual:", _mony + _format.decimal(_deudaActualCliente));
                                Ticket1.TextoExtremo("Próximo pago:", _fechalimite);
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
                        _label.get(19).setText("El cliente no tiene deuda");
                    }
                } else if (_radioInteres.isSelected()) {
                    if (!_intereses.equals(0)) {
                        if (!_textField.get(7).getText().equals("")) {
                            try {
                                Integer cantCoutas = Integer.valueOf(_textField.get(7).getText());
                                if (cantCoutas <= _interesCoutas) {
                                    if (_interesPago >= _interesesPagos) {
                                        getConn().setAutoCommit(false);
                                        if (!_listIntereses.isEmpty()) {
                                            Object[] data1 = {true};
                                            for (int i = 0; i < cantCoutas; i++) {
                                                String query1 = "UPDATE tintereses_clientes SET Cancelado = ?"
                                                        + " WHERE Id =" + _listIntereses.get(i).getId() + " AND "
                                                        + " IdCliente=" + _idClienteRport;
                                                qr.update(getConn(), query1, data1);
                                            }

                                            String ticket = _codigos.codesTickets(_ticketIntereses);
                                            String query2 = "INSERT INTO tpagos_reportes_intereses_cliente(Intereses,Pago, Cambio,Cuotas,"
                                                    + "Fecha,Ticket,IdUsuario,Usuario,IdCliente) VALUES(?,?,?,?,?,?,?,?,?)";
                                            Object[] data2 = {
                                                _intereses, _interesPago, _cambio, cantCoutas,
                                                fecha, ticket, _dataUsuario.getIdUsuario(),
                                                usuario, _idClienteRport
                                            };
                                            qr.insert(getConn(), query2, new ColumnListHandler(), data2);

                                            String query3 = "UPDATE treportes_intereses_clientes SET Intereses = ?"
                                                    + ",Pago = ?,Cambio = ?,Cuotas = ?,InteresFecha = ? ,TicketIntereses = ?"
                                                    + " WHERE Id =" + _idReportIntereses + " AND "
                                                    + " IdCliente=" + _idClienteRport;

                                            if (coutas == 0) {
                                                Object[] data3 = {
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    0,
                                                    fecha,
                                                    "0000000000"
                                                };
                                                qr.update(getConn(), query3, data3);
                                            } else {
                                                Object[] data3 = {
                                                    _interesesCliente,
                                                    _interesPago,
                                                    _cambio,
                                                    cantCoutas,
                                                    fecha,
                                                    ticket
                                                };
                                                qr.update(getConn(), query3, data3);
                                            }
                                            Ticket1.TextoCentro("Sistema de ventas PDHN");
                                            Ticket1.TextoIzquierda("Direccion");
                                            Ticket1.TextoIzquierda("La Ceiba, Atlantidad");
                                            Ticket1.TextoIzquierda("Tel 658912146");
                                            Ticket1.LineasGuion();
                                            Ticket1.TextoCentro("FACTURA DE PAGOS DE INTERESES");
                                            Ticket1.LineasGuion();
                                            Ticket1.TextoIzquierda("Factura: " + ticket);
                                            Ticket1.TextoIzquierda("Cliente: " + nameCliente);
                                            Ticket1.TextoIzquierda("Fecha: " + fecha);
                                            Ticket1.TextoIzquierda("Usuario: " + usuario);
                                            Ticket1.LineasGuion();
                                            Ticket1.TextoCentro("Intereses " + _mony + _format.decimal(_intereses));
                                            Ticket1.TextoExtremo("Cuotas", cantCoutas.toString());
                                            Ticket1.TextoExtremo("Pago:", _mony + _format.decimal(_interesPago));
                                            Ticket1.TextoExtremo("Cambio:", _mony + _format.decimal(_cambio));
                                            Ticket1.TextoCentro("PDHN");
                                            Ticket1.print();
                                            getConn().commit();
                                            RestablecerReport();

                                        }
                                    } else {
                                        _label.get(19).setText("El pago debe ser " + _mony + _format.decimal(_interesesPagos));
                                    }
                                } else {
                                    _label.get(19).setText("Cuotas inválida");
                                }
                            } catch (Exception e) {
                                getConn().rollback();
                                JOptionPane.showMessageDialog(null, e);
                            }
                        } else {
                            _label.get(19).setText("Ingrese el número de cuotas");
                        }
                    } else {
                        _label.get(19).setText("El cliente no tiene deuda");
                    }
                }
            }
        }
    }

    public void historialPagos(boolean filtrar) {
        listPagos = new ArrayList<>();
        try {
            _dateChooser1.setFormat(3);
            _dateChooser2.setFormat(3);
            var cal = new Calendario();
            String[] titulos = {"Id", "Deuda", "Saldo", "Pago",
                "Cambio", "Fecha", "Ticket", "FechaDeuda", "Mensual", "FechaLimite"};
            modelo4 = new DefaultTableModel(null, titulos);
            var date1 = formateador.parse(_dateChooser1.getSelectedPeriodSet().toString());
            var date2 = formateador.parse(_dateChooser2.getSelectedPeriodSet().toString());
            if (filtrar) {
                if (date2.after(date1) || date1.equals(date2)) {
                    var listPagos1 = new ArrayList<TPagos_clientes>();
                    var pagos = Pagos_clientes().stream()
                            .filter(u -> u.getIdCliente() == _idClienteRport)
                            .collect(Collectors.toList());
                    formateador = new SimpleDateFormat("yyyy/MM/dd");
                    _dateChooser1.setFormat(3);
                    var date3 = _dateChooser1.getCurrent();
                    var date4 = formateador.parse(cal.getFecha(date3));
                    for (TPagos_clientes pago : pagos) {
                        var data = pago.getFecha().toString().replace('-', '/');

                        var date5 = formateador.parse(data);
                        if (date4.equals(date5) || date4.before(date5)) {
                            listPagos1.add(pago);
                        }
                    }
                    _dateChooser2.setFormat(3);
                    var date6 = _dateChooser2.getCurrent();
                    var date7 = formateador.parse(cal.getFecha(date6));
                    for (TPagos_clientes pago : listPagos1) {
                        var data = pago.getFecha().toString().replace('-', '/');

                        var date8 = formateador.parse(data);
                        if (date7.equals(date8) || date7.after(date8)) {
                            Object[] registros = {
                                pago.getId(),
                                _format.decimal(pago.getDeuda()),
                                _format.decimal(pago.getSaldo()),
                                _format.decimal(pago.getPago()),
                                _format.decimal(pago.getCambio()),
                                pago.getFecha(),
                                pago.getTicket(),
                                pago.getFechaDeuda(),
                                _format.decimal(pago.getMensual()),
                                pago.getFechaLimite(),};
                            modelo4.addRow(registros);
                            listPagos.add(pago);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "La fecha final debe ser mayor a la fecha inicial ");
                }
            } else {
                int inicio = (_num_pagina - 1) * _reg_por_pagina;
                var pagos = Pagos_clientes().stream()
                        .filter(u -> u.getIdCliente() == _idClienteRport)
                        .collect(Collectors.toList());
                listPagos = pagos;
                var data = pagos.stream()
                        .skip(inicio).limit(_reg_por_pagina)
                        .collect(Collectors.toList());
                Collections.reverse(listPagos);
                Collections.reverse(data);
                for (TPagos_clientes pago : data) {
                    Object[] registros = {
                        pago.getId(),
                        _format.decimal(pago.getDeuda()),
                        _format.decimal(pago.getSaldo()),
                        _format.decimal(pago.getPago()),
                        _format.decimal(pago.getCambio()),
                        pago.getFecha(),
                        pago.getTicket(),
                        pago.getFechaDeuda(),
                        _format.decimal(pago.getMensual()),
                        pago.getFechaLimite(),};
                    modelo4.addRow(registros);
                }
            }
        } catch (Exception ex) {

            var data = ex.getMessage();
        }
        _tablePagosCuotas.setModel(modelo4);
        _tablePagosCuotas.setRowHeight(30);
        _tablePagosCuotas.getColumnModel().getColumn(0).setMaxWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(0).setMinWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(0).setPreferredWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(7).setMaxWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(7).setMinWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(7).setPreferredWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(8).setMaxWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(8).setMinWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(8).setPreferredWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(9).setMaxWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(9).setMinWidth(0);
        _tablePagosCuotas.getColumnModel().getColumn(9).setPreferredWidth(0);
    }
    private int _idHistorial;

    public void GetHistorialPago() {
        formateador = new SimpleDateFormat("dd/MM/yyyy");
        int filas = _tablePagosCuotas.getSelectedRow();
        _idHistorial = (Integer) modelo4.getValueAt(filas, 0);
        var deuda = _mony + (String) modelo4.getValueAt(filas, 1);
        _label.get(22).setText(deuda);
        var saldo = _mony + (String) modelo4.getValueAt(filas, 2);
        _label.get(23).setText(saldo);
        var fechaDeuda = (Date) modelo4.getValueAt(filas, 7);
        _label.get(24).setText(fechaDeuda.toString());

        var ticket = (String) modelo4.getValueAt(filas, 6);
        _label.get(25).setText(ticket);
        var pago = _mony + (String) modelo4.getValueAt(filas, 3);
        _label.get(26).setText(pago);
        var mensual = _mony + (String) modelo4.getValueAt(filas, 8);
        _label.get(27).setText(mensual);
        var fechaPago = (Date) modelo4.getValueAt(filas, 5);
        _label.get(28).setText(fechaPago.toString());
        var fechaLimite = (Date) modelo4.getValueAt(filas, 9);
        _label.get(29).setText(fechaLimite.toString());
        var cambio = _mony + (String) modelo4.getValueAt(filas, 4);
        var usuario = _dataUsuario.getNombre() + " " + _dataUsuario.getApellido();
        Ticket1.TextoCentro("Sistema de ventas PDHN"); // imprime en el centro 
        Ticket1.TextoIzquierda("Direccion");
        Ticket1.TextoIzquierda("La Ceiba, Atlantidad");
        Ticket1.TextoIzquierda("Tel 658912146");
        Ticket1.LineasGuion();
        Ticket1.TextoCentro("FACTURA"); // imprime en el centro  
        Ticket1.LineasGuion();
        Ticket1.TextoIzquierda("Factura: " + ticket);
        Ticket1.TextoIzquierda("Cliente: " + nameCliente);
        Ticket1.TextoIzquierda("Fecha: " + fechaPago);
        Ticket1.TextoIzquierda("Usuario: " + usuario);
        Ticket1.LineasGuion();
        Ticket1.TextoCentro("Su cretito: " + deuda);
        Ticket1.TextoExtremo("Cuotas por mes:", mensual);
        Ticket1.TextoExtremo("Pago:", pago);
        Ticket1.TextoExtremo("Cambio:", cambio);
        Ticket1.TextoExtremo("Deuda actual:", saldo);
        Ticket1.TextoExtremo("Próximo pago:", fechaLimite.toString());
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
            case 2:
                if (_idHistorial1 == 0) {
                    JOptionPane.showMessageDialog(null, "Seleccione un historial de pago de intereses");
                } else {
                    Ticket1.print();
                }
                break;
        }
    }

    public void historialIntereses(boolean filtrar) {
        listPagosIntereses = new ArrayList<>();
        try {
            _dateChooser1.setFormat(3);
            _dateChooser2.setFormat(3);
            String[] titulos = {"Id", "Intereses", "Pago", "Cambio",
                "Cuotas", "Fecha", "Ticket"};
            modelo5 = new DefaultTableModel(null, titulos);
            var cal = new Calendario();
            var date1 = formateador.parse(_dateChooser1.getSelectedPeriodSet().toString());
            var date2 = formateador.parse(_dateChooser2.getSelectedPeriodSet().toString());
            if (filtrar) {
                if (date2.after(date1) || date1.equals(date2)) {
                    var listPagos1 = new ArrayList<TPagos_reportes_intereses_cliente>();
                    var pagos = Pagos_reportes_intereses_cliente().stream()
                            .filter(u -> u.getIdCliente() == _idClienteRport)
                            .collect(Collectors.toList());
                    formateador = new SimpleDateFormat("yyyy/MM/dd");
                    _dateChooser1.setFormat(3);
                    var date3 = _dateChooser1.getCurrent();
                    var date4 = formateador.parse(cal.getFecha(date3));
                    for (TPagos_reportes_intereses_cliente pago : pagos) {
                        var data = pago.getFecha().toString().replace('-', '/');
                        var array = data.split("/");
                        var date5 = formateador.parse(array[2] + "/" + array[1] + "/" + array[0]);
                        if (date4.equals(date5) || date4.before(date5)) {
                            listPagos1.add(pago);
                        }
                    }
                    _dateChooser2.setFormat(3);
                    var date6 = _dateChooser2.getCurrent();
                    var date7 = formateador.parse(cal.getFecha(date6));
                    for (TPagos_reportes_intereses_cliente pago : listPagos1) {

                        var data = pago.getFecha().toString().replace('-', '/');
                        var array = data.split("/");
                        var date8 = formateador.parse(array[2] + "/" + array[1] + "/" + array[0]);
                        if (date7.equals(date8) || date7.after(date8)) {
                            Object[] registros = {
                                pago.getId(),
                                _format.decimal(pago.getIntereses()),
                                _format.decimal(pago.getPago()),
                                _format.decimal(pago.getCambio()),
                                pago.getCuotas(),
                                pago.getFecha(),
                                pago.getTicket()};
                            modelo5.addRow(registros);
                            listPagosIntereses.add(pago);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "La fecha final debe ser mayor a la fecha inicial ");
                }
            } else {
                int inicio = (_num_pagina - 1) * _reg_por_pagina;
                var pagos = Pagos_reportes_intereses_cliente().stream()
                        .filter(u -> u.getIdCliente() == _idClienteRport)
                        .collect(Collectors.toList());
                listPagosIntereses = pagos;
                var data = pagos.stream()
                        .skip(inicio).limit(_reg_por_pagina)
                        .collect(Collectors.toList());
                Collections.reverse(listPagosIntereses);
                Collections.reverse(data);
                for (TPagos_reportes_intereses_cliente pago : data) {
                    Object[] registros = {
                        pago.getId(),
                        _format.decimal(pago.getIntereses()),
                        _format.decimal(pago.getPago()),
                        _format.decimal(pago.getCambio()),
                        pago.getCuotas(),
                        pago.getFecha(),
                        pago.getTicket()};
                    modelo5.addRow(registros);

                }
            }
        } catch (Exception ex) {

            var data = ex.getMessage();
        }
        _tablePagosIntereses.setModel(modelo5);
        _tablePagosIntereses.setRowHeight(30);
        _tablePagosIntereses.getColumnModel().getColumn(0).setMaxWidth(0);
        _tablePagosIntereses.getColumnModel().getColumn(0).setMinWidth(0);
        _tablePagosIntereses.getColumnModel().getColumn(0).setPreferredWidth(0);

    }
    private int _idHistorial1;

    public void GetHistorialIntereses() {
        int filas = _tablePagosIntereses.getSelectedRow();
        _idHistorial1 = (Integer) modelo5.getValueAt(filas, 0);
        var intereses = _mony + (String) modelo5.getValueAt(filas, 1);
        _label.get(30).setText(intereses);
        var ticket = (String) modelo5.getValueAt(filas, 6);
        _label.get(31).setText(ticket);
        var fecha = (String) modelo5.getValueAt(filas, 5);
        _label.get(32).setText(fecha);
        var cambio = _mony + (String) modelo5.getValueAt(filas, 3);
        _label.get(33).setText(cambio);
        var pago = _mony + (String) modelo5.getValueAt(filas, 2);
        _label.get(34).setText(pago);
        var cuota = (int) modelo5.getValueAt(filas, 4);
        _label.get(35).setText(cuota + "");

        var usuario = _dataUsuario.getNombre() + " " + _dataUsuario.getApellido();
        Ticket1.TextoCentro("Sistema de ventas PDHN"); // imprime en el centro 
        Ticket1.TextoIzquierda("Direccion");
        Ticket1.TextoIzquierda("La Ceiba, Atlantidad");
        Ticket1.TextoIzquierda("Tel 658912146");
        Ticket1.LineasGuion();
        Ticket1.TextoCentro("FACTURA DE PAGOS DE INTERESES"); // imprime en el centro  
        Ticket1.LineasGuion();
        Ticket1.TextoIzquierda("Factura: " + ticket);
        Ticket1.TextoIzquierda("Cliente: " + nameCliente);
        Ticket1.TextoIzquierda("Fecha: " + fecha);
        Ticket1.TextoIzquierda("Usuario: " + usuario);
        Ticket1.LineasGuion();
        Ticket1.TextoCentro("Intereses: " + intereses);
        Ticket1.TextoExtremo("Cuotas: ", cuota + "");
        Ticket1.TextoExtremo("Pago:", pago);
        Ticket1.TextoExtremo("Cambio:", cambio);
        Ticket1.TextoCentro("PDHN");
    }

    public final void RestablecerReport() {
        _idReport = 0;
        _interesCoutas = 0;
        _intereses = 0.0;
        _interesPago = 0.0;
        _deudaActual = 0.0;
        _interesesPagos = 0.0;
        _interesesCliente = 0.0;
        _cambio = 0.0;
        _pago = 0.0;
        _mensual = 0.0;
        _deudaActualCliente = 0.0;
        _ticketCuota = "0000000000";
//        idClienteRport = 0;
        _ticketIntereses = "0000000000";
        _deudaActualCliente = 0.0;
        _ticketCuota = "0000000000";
        _label.get(8).setText("Nombre del cliente");
        _label.get(9).setText(_mony + "0.00");
        _label.get(10).setText(_mony + "0.00");
        _label.get(11).setText("00000000000");
        _label.get(12).setText("--/--/--");
        _label.get(13).setText(_mony + "0.00");
        _label.get(14).setText(_mony + "0.00");
        _label.get(15).setText("0");
        _label.get(16).setText("0000000000");
        _label.get(17).setText("--/--/--");
        _label.get(18).setText(_mony + "0.00");
        _label.get(19).setText("Ingrese el pago");
        _textField.get(6).setText("");
        _textField.get(7).setText("");
        listReportesDeuda = clientes();
        if (!listReportesDeuda.isEmpty()) {
            _paginadorReportes = new Paginador<>(listReportesDeuda, _label.get(7), _reg_por_pagina);
        }
        SearchReportes("");
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CODIGO DE DEUDAS Y REPORTES">
    private List<TReportes_clientes> _list = new ArrayList<>();

    public void GetReportesDeudas(String valor) {
        _list = new ArrayList<>();
        String[] titulos = {"Id", "Nid", "Nombre", "Apellido",
            "Email", "Telefono", "IdReporte", "Fecha Limite"};
        modelo3 = new DefaultTableModel(null, titulos);
        var inicio = (_num_pagina - 1) * _reg_por_pagina;
        List<TReportes_clientes> list = new ArrayList<>();
        if (valor.equals("")) {
            list = Reportes_Clientes().stream()
                    .skip(inicio).limit(_reg_por_pagina)
                    .collect(Collectors.toList());
            if (!list.isEmpty()) {
                for (TReportes_clientes item : list) {
                    if (!item.getFechaLimite().equals("--/--/--")) {
                        try {
                            Date date1 = formateador.parse(item.getFechaLimite());
                            Date date2 = formateador.parse(new Calendario().getFecha());
                            long time = date1.getTime() - date2.getTime();
                            long days = time / (1000 * 60 * 60 * 24);
                            if (3 >= days) {
                                Object[] registros = {
                                    item.getID(),
                                    item.getNid(),
                                    item.getNombre(),
                                    item.getApellido(),
                                    item.getEmail(),
                                    item.getTelefono(),
                                    item.getIdReporte(),
                                    item.getFechaLimite(),
                                    item.getFechaPago(),
                                    item.getMensual(),
                                    item.getDeuda()
                                };
                                modelo3.addRow(registros);
                                if (0 > days) {
                                    InteresMora(registros, days);
                                }
                                _list.add(item);
                            }
                        } catch (ParseException ex) {
                        }
                    }
                }
            }
        } else {
            list = Reportes_Clientes().stream()
                    .filter(C -> C.getNid().startsWith(valor)
                    || C.getNombre().startsWith(valor)
                    || C.getApellido().startsWith(valor))
                    .skip(inicio).limit(_reg_por_pagina)
                    .collect(Collectors.toList());
            if (!list.isEmpty()) {
                for (TReportes_clientes item : list) {
                    if (!item.getFechaLimite().equals("--/--/--")) {
                        try {
                            Date date1 = formateador.parse(item.getFechaLimite());
                            Date date2 = formateador.parse(new Calendario().getFecha());
                            long time = date1.getTime() - date2.getTime();
                            long days = time / (1000 * 60 * 60 * 24);
                            if (3 >= days) {
                                Object[] registros = {
                                    item.getID(),
                                    item.getNid(),
                                    item.getNombre(),
                                    item.getApellido(),
                                    item.getEmail(),
                                    item.getTelefono(),
                                    item.getIdReporte(),
                                    item.getFechaLimite(),
                                    item.getFechaPago(),
                                    item.getMensual(),
                                    item.getDeuda()
                                };
                                modelo3.addRow(registros);
                                _list.add(item);
                            }
                        } catch (ParseException ex) {
                        }
                    }
                }
            }
        }
        if (_tableReporteDeuda != null) {
            _tableReporteDeuda.setModel(modelo3);
            _tableReporteDeuda.setRowHeight(30);
            _tableReporteDeuda.getColumnModel().getColumn(0).setMaxWidth(0);
            _tableReporteDeuda.getColumnModel().getColumn(0).setMinWidth(0);
            _tableReporteDeuda.getColumnModel().getColumn(0).setPreferredWidth(0);
            _tableReporteDeuda.getColumnModel().getColumn(6).setMaxWidth(0);
            _tableReporteDeuda.getColumnModel().getColumn(6).setMinWidth(0);
            _tableReporteDeuda.getColumnModel().getColumn(6).setPreferredWidth(0);
        }

    }
    private long diasMoras;
    private DefaultTableModel _selectedCliente;
    private Date _fechaLimite;
    private Integer _idReporte;

    public void GetReporteDeuda(DefaultTableModel selected, int fila) {
        if (selected != null) {
            Calendar calendar = Calendar.getInstance();
            try {
                var nombre = (String) selected.getValueAt(fila, 2);
                var apellido = (String) selected.getValueAt(fila, 3);
                _label.get(20).setText(nombre + " " + apellido);
                _idReporte = (Integer) selected.getValueAt(fila, 6);
                _fechaLimite = formateador.parse((String) selected.getValueAt(fila, 7));
                calendar.setTime(_fechaLimite);
                _dateChooser.setSelectedDate(calendar);
                Date date = formateador.parse(new Calendario().getFecha());
                long time = _fechaLimite.getTime() - date.getTime();
                diasMoras = time / (1000 * 60 * 60 * 24);
                if (0 < diasMoras) {
                    _label.get(21).setText(diasMoras + " dias restantes");
                } else {
                    _label.get(21).setText("Se ha sobrepasado" + Math.abs(diasMoras) + " dias");
                }
                _selectedCliente = selected;
            } catch (Exception e) {
            }
        }
    }

    public void ExtenderDias() {
        if (_selectedCliente != null) {
            if (0 <= diasMoras) {
                if (_checkBox_Dia.isSelected()) {
                    try {
                        _dateChooser.setFormat(3);
                        var date1 = formateador.parse(new Calendario().getFecha());
                        var date2 = _dateChooser.getSelectedDate().getTime();
                        if (date1.before(date2) && _fechaLimite.before(date2)) {
                            final QueryRunner qr = new QueryRunner(true);
                            String query = "UPDATE treportes_clientes SET FechaLimite = ?"
                                    + " WHERE IdReporte =" + _idReporte;
                            Object[] data = {formateador.format(date2)};

                            qr.update(getConn(), query, data);
                            ResetReportDeudas();
                        } else {
                            var fechaLimite = !date1.before(date2) ? new Calendario()
                                    .getFecha() : formateador.format(_fechaLimite);
                            JOptionPane.showConfirmDialog(null,
                                    "Seleccione una fecha mayor a la fecha " + fechaLimite
                                    + " \npara extender los dias de pago al cliente", "Fecha para extender días ",
                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        }
                        _dateChooser.setFormat(0);
                    } catch (Exception e) {
                    }

                } else {
                    JOptionPane.showConfirmDialog(null,
                            "Seleccione la casilla para verificar que va extender \n"
                            + "los dias de pago al cliente", "Extender días",
                            JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE);
                }
            } else {
                JOptionPane.showConfirmDialog(null,
                        "Al cliente no se le puede extender los días", "Extender días",
                        JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE);
            }
        } else {
            JOptionPane.showConfirmDialog(null,
                    "Seleccione un cliente de la lista", "Extender días",
                    JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE);
        }
    }

    private void InteresMora(Object[] cliente, long days) {
        var interests = ConfigurationVM.Interests;
        var id = (Integer) cliente[0];
        var fechaPago = (String) cliente[8];
        var mensual = (Double) cliente[9];
        if (!interests.equals(0.0)) {
            var clientesInteres1 = InteresesCliente().stream()
                    .filter(i -> i.getIdCliente() == id
                    && i.getFechaInicial().equals(fechaPago))
                    .collect(Collectors.toList());

            var clientesInteres2 = InteresesCliente().stream()
                    .filter(i -> i.getIdCliente() == id
                    && i.getFechaInicial().equals(fechaPago)
                    && i.getCancelado() == false)
                    .collect(Collectors.toList());
            long dias = Math.abs(days);
            Double porcentaje = interests / 100;
            Double moratorio = mensual * porcentaje;
            //Double moratorioDia = moratorioMensual / 30;
//            Double interes = moratorioDia * dias;
            int count1 = clientesInteres1.size();
            int count2 = clientesInteres2.size();
            int pos = count2;
            pos--;
            if (count2 == 0) {
                for (int i = 1; i <= dias; i++) {
                    insert(cliente, new TIntereses_clientes(), i, false, moratorio);
                }
            } else {
                if (count1 < dias) {
                    if (count2 <= dias) {
                        long interesDias = dias - count1;
                        for (int i = 1; i <= interesDias; i++) {
                            insert(cliente, clientesInteres2.get(pos), i, true, moratorio);
                        }
                    }
                }

            }
        }
    }

    private void insert(Object[] cliente, TIntereses_clientes clientesInteres,
            int day, boolean value, Double interes) {
        Date fecha = null;
        final QueryRunner qr = new QueryRunner(true);
        var id = (Integer) cliente[0];
        var fechaLimite = (String) cliente[7];
        var fechaPago = (String) cliente[8];
        var mensual = (Double) cliente[9];
        var deuda = (Double) cliente[10];
        try {
            getConn().setAutoCommit(false);
            if (value) {
                fecha = formateador.parse(clientesInteres.getFecha());
            } else {
                fecha = formateador.parse(fechaLimite);
            }
            var nowDate = new Calendario().addDay(fecha, day);
            String query = "INSERT INTO tintereses_clientes(IdCliente,FechaInicial,"
                    + " Deuda,Mensual,Intereses,Cancelado,Fecha)"
                    + " VALUES(?,?,?,?,?,?,?)";
            Object[] data = {
                id,
                fechaPago,
                deuda,
                mensual,
                interes,
                false,
                nowDate,};
            qr.insert(getConn(), query, new ColumnListHandler(), data);
            getConn().commit();
        } catch (Exception e) {
            try {
                getConn().rollback();
            } catch (SQLException ex) {
            }
        }
    }

    private void ResetReportDeudas() {
        diasMoras = 0;
        _label.get(20).setText("Cliente");
        _label.get(21).setText("Dias");
        Calendar c = new GregorianCalendar();
        _dateChooser.setSelectedDate(c);
        GetReportesDeudas("");
        if (!_list.isEmpty()) {
            _paginadorReportesDeuda = new Paginador<>(_list, _label.get(7), _reg_por_pagina);
        }
    }
    // </editor-fold>

    private List<TClientes> listClientes;
    private List<TClientes> listReportesDeuda;
    private List<TPagos_clientes> listPagos = new ArrayList<>();
    private List<TPagos_reportes_intereses_cliente> listPagosIntereses = new ArrayList<>();

    public void Paginador(String metodo) {
        switch (metodo) {
            case "Primero":
                switch (seccion) {
                    case 1:
                        if (!listClientes.isEmpty()) {
                            _num_pagina = _paginadorClientes.primero();
                        }
                        break;
                    case 2:
                        switch (_seccion1) {
                            case 0:
                                if (!listReportesDeuda.isEmpty()) {
                                    _num_pagina = _paginadorReportes.primero();
                                }
                                break;
                            case 1:
                                if (!listPagos.isEmpty()) {
                                    _num_pagina = _paginadorPagos.primero();
                                }
                                break;
                            case 2:
                                if (!listPagosIntereses.isEmpty()) {
                                    _num_pagina = _paginadorPagosIntereses.primero();
                                }
                                break;
                        }
                        break;
                    case 3:
                        if (!_list.isEmpty()) {
                            _num_pagina = _paginadorReportesDeuda.primero();
                        }
                        break;
                }
                break;
            case "Anterior":
                switch (seccion) {
                    case 1:
                        if (!listClientes.isEmpty()) {
                            _num_pagina = _paginadorClientes.anterior();
                        }
                        break;
                    case 2:
                        switch (_seccion1) {
                            case 0:
                                if (!listReportesDeuda.isEmpty()) {
                                    _num_pagina = _paginadorReportes.anterior();
                                }
                                break;
                            case 1:
                                if (!listPagos.isEmpty()) {
                                    _num_pagina = _paginadorPagos.anterior();
                                }
                                break;
                            case 2:
                                if (!listPagosIntereses.isEmpty()) {
                                    _num_pagina = _paginadorPagosIntereses.anterior();
                                }
                                break;
                        }
                        break;
                    case 3:
                        if (!_list.isEmpty()) {
                            _num_pagina = _paginadorReportesDeuda.anterior();
                        }
                        break;
                }
                break;
            case "Siguiente":
                switch (seccion) {
                    case 1:
                        if (!listClientes.isEmpty()) {
                            _num_pagina = _paginadorClientes.siguiente();
                        }
                        break;
                    case 2:
                        switch (_seccion1) {
                            case 0:
                                if (!listReportesDeuda.isEmpty()) {
                                    _num_pagina = _paginadorReportes.siguiente();
                                }
                                break;
                            case 1:
                                if (!listPagos.isEmpty()) {
                                    _num_pagina = _paginadorPagos.siguiente();
                                }
                                break;
                            case 2:
                                if (!listPagosIntereses.isEmpty()) {
                                    _num_pagina = _paginadorPagosIntereses.siguiente();
                                }
                                break;
                        }
                        break;
                    case 3:
                        if (!_list.isEmpty()) {
                            _num_pagina = _paginadorReportesDeuda.siguiente();
                        }
                        break;
                }
                break;
            case "Ultimo":
                switch (seccion) {
                    case 1:
                        if (!listClientes.isEmpty()) {
                            _num_pagina = _paginadorClientes.ultimo();
                        }
                        break;
                    case 2:
                        switch (_seccion1) {
                            case 0:
                                if (!listReportesDeuda.isEmpty()) {
                                    _num_pagina = _paginadorReportes.ultimo();
                                }
                                break;
                            case 1:
                                if (!listPagos.isEmpty()) {
                                    _num_pagina = _paginadorPagos.ultimo();
                                }
                                break;
                            case 2:
                                if (!listPagosIntereses.isEmpty()) {
                                    _num_pagina = _paginadorPagosIntereses.ultimo();
                                }
                                break;
                        }
                        break;
                    case 3:
                        if (!_list.isEmpty()) {
                            _num_pagina = _paginadorReportesDeuda.ultimo();
                        }
                        break;
                }
                break;
        }
        switch (seccion) {
            case 1:
                SearchClientes("");
                break;
            case 2:
                switch (_seccion1) {
                    case 0:
                        SearchReportes("");
                        break;
                    case 1:
                        historialPagos(false);
                        break;
                    case 2:
                        historialIntereses(false);
                        break;
                }

                break;
            case 3:
                GetReportesDeudas("");
                break;
        }
    }

    public void Registro_Paginas() {
        _num_pagina = 1;
        Number caja = (Number) _spinnerPaginas.getValue();
        _reg_por_pagina = caja.intValue();
        switch (seccion) {
            case 1:
                if (!listClientes.isEmpty()) {
                    _paginadorClientes = new Paginador<>(listClientes,
                            _label.get(7), _reg_por_pagina);
                }
                SearchClientes("");
                break;
            case 2:
                switch (_seccion1) {
                    case 0:
                        if (!listReportesDeuda.isEmpty()) {
                            _paginadorReportes = new Paginador<>(listReportesDeuda,
                                    _label.get(7), _reg_por_pagina);
                        }
                        SearchReportes("");
                        break;
                    case 1:
                        if (!listPagos.isEmpty()) {
                            _paginadorPagos = new Paginador<>(listPagos,
                                    _label.get(7), _reg_por_pagina);
                        }
                        historialPagos(false);
                        break;
                    case 2:
                        if (!listPagosIntereses.isEmpty()) {
                            _paginadorPagosIntereses = new Paginador<>(listPagosIntereses,
                                    _label.get(7), _reg_por_pagina);
                        }
                        historialIntereses(false);
                        break;
                }

                break;
            case 3:
                if (!_list.isEmpty()) {
                    _paginadorReportesDeuda = new Paginador<>(_list,
                            _label.get(7), _reg_por_pagina);
                }
                GetReportesDeudas("");
                break;
        }

    }
}
