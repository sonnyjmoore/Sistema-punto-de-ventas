/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewModels;

import Library.Calendario;
import Library.FormatDecimal;
import Library.Objectos;
import Models.Caja.TCajas_ingresos;
import Models.Usuario.TUsuarios;
import java.awt.Color;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

/**
 *
 * @author ajpag
 */
public class CajasVM extends Objectos {

    private List<JTextField> _textField;
    private List<JLabel> _label;
    private static TUsuarios _dataUsuario;
    private JSpinner _spinnerCaja;
    private JCheckBox _checkBoxEstado, _checkBoxIngresos;
    private DefaultTableModel modelo1, modelo2;
    private FormatDecimal _format;
    private JTable _table_ListaCajas, _table_Ingresos;
    private String _accion = "insert", _mony;
    private Calendario _cal;

    public CajasVM(TUsuarios usuario) {
        _dataUsuario = usuario;
    }

    public CajasVM(Object[] objectos,
            List<JTextField> textField,
            List<JLabel> label) {
        _textField = textField;
        _label = label;
        _spinnerCaja = (JSpinner) objectos[0];
        _checkBoxEstado = (JCheckBox) objectos[1];
        _checkBoxIngresos = (JCheckBox) objectos[2];
        _table_ListaCajas = (JTable) objectos[3];
        _format = new FormatDecimal();
        _mony = ConfigurationVM.Mony;
        _cal = new Calendario();
        restablecer();
    }

    public void registrarCajas() throws SQLException {
        try {
            var qr = new QueryRunner(true);
            getConn().setAutoCommit(false);
            var value = (Number) _spinnerCaja.getValue();
            var caja = value.intValue();
            var cajas = Cajas().stream()
                    .filter(p -> p.getCaja() == caja)
                    .collect(Collectors.toList());
            switch (_accion) {
                case "insert":
                    if (!_checkBoxIngresos.isSelected()) {
                        if (caja > 0) {
                            if (cajas.isEmpty()) {
                                var sqlCaja = "INSERT INTO tcajas(Caja,Estado, Fecha)"
                                        + " VALUES(?,?,?)";
                                Object[] dataCaja = {
                                    caja,
                                    _checkBoxEstado.isSelected(),
                                    new Date()
                                };
                                var data = (List<? extends Number>) qr.insert(getConn(),
                                        sqlCaja, new ColumnListHandler(), dataCaja);
                                var idCaja = data.get(0).intValue();
                                String sqlIngresos = "INSERT INTO tcajas_ingresos"
                                        + "(IdCaja,IdUsuario, Billete,Moneda,"
                                        + "Ingreso,Fecha) VALUES(?,?,?,?,?,?)";
                                Object[] dataIngresos = {
                                    idCaja,
                                    0,
                                    0.0,
                                    0.0,
                                    0.0,
                                    new Date()
                                };
                                qr.insert(getConn(), sqlIngresos, new ColumnListHandler(), dataIngresos);
                                restablecer();
                            } else {
                                _label.get(0).setText("El numero de caja ya esta regitrado");
                                _label.get(0).setForeground(Color.red);
                            }
                        } else {
                            _label.get(0).setText("Ingrese un numero de caja");
                            _label.get(0).setForeground(Color.red);
                        }
                    }
                    break;
                case "update":
                    var sqlCaja = "UPDATE tcajas SET Estado = ?"
                            + " WHERE IdCaja =" + idCaja;
                    Object[] dataCaja = {
                        _checkBoxEstado.isSelected()};
                    qr.update(getConn(), sqlCaja, dataCaja);
                    restablecer();
                    break;
            }
            getConn().commit();
        } catch (Exception e) {
            getConn().rollback();
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void SearchCajas(int caja) {
        String[] titulos = {"IdCaja", "Caja", "Estado",
            "Billete", "Moneda", "Ingreso"};
        modelo1 = new DefaultTableModel(null, titulos) {
            public Class<?> getColumnClass(int column) {
                return column == 2 ? Boolean.class : String.class;
            }
        };
        var list = caja == 0 ? CajaIngreso()
                : CajaIngreso().stream()
                        .filter(p -> p.getCaja() == caja)
                        .collect(Collectors.toList());
        if (!list.isEmpty()) {
            list.forEach(item -> {
                Object[] registros = {
                    item.getIdCaja(),
                    item.getCaja(),
                    item.isEstado(),
                    _mony + _format.decimal(item.getBillete()),
                    _mony + _format.decimal(item.getMoneda()),
                    _mony + _format.decimal(item.getIngreso())
                };
                modelo1.addRow(registros);
            });
        }
        _table_ListaCajas.setModel(modelo1);
        _table_ListaCajas.setRowHeight(30);
        _table_ListaCajas.getColumnModel().getColumn(0).setMaxWidth(0);
        _table_ListaCajas.getColumnModel().getColumn(0).setMinWidth(0);
        _table_ListaCajas.getColumnModel().getColumn(0).setPreferredWidth(0);
    }
    private int idCaja, numCaja;
    private double _ingresos;

    public void dataCaja() {
        switch (_seccion) {
            case 0:

                break;
            case 1:
                _accion = "update";
                var filas1 = _table_ListaCajas.getSelectedRow();
                idCaja = (Integer) modelo1.getValueAt(filas1, 0);
                numCaja = (Integer) modelo1.getValueAt(filas1, 1);
                _label.get(1).setText("#" + numCaja);
                var estado = (boolean) modelo1.getValueAt(filas1, 2);
                var color = estado ? new Color(0, 153, 51) : new Color(102, 102, 102);
                _checkBoxEstado.setSelected(estado);
                _checkBoxEstado.setForeground(color);
                _spinnerCaja.setValue(numCaja);
                break;
        }
    }

    public void asignarIngresos() throws SQLException {
        if (idCaja == 0) {
            JOptionPane.showConfirmDialog(null, "Seleccione un numero de caja", "Caja",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        } else {
            if (_textField.get(0).getText().equals("")) {
                _label.get(2).setText("Ingrese los billetes");
                _label.get(2).setForeground(Color.RED);
                _textField.get(0).requestFocus();
            } else if (_textField.get(1).getText().equals("")) {
                _label.get(3).setText("Ingrese las monedas equivalente a " + _mony);
                _label.get(3).setForeground(Color.RED);
                _textField.get(1).requestFocus();
            } else {
                try {
                    var qr = new QueryRunner(true);
                    getConn().setAutoCommit(false);
                    var billetes1 = _format.reconstruir(_textField.get(0).getText());
                    var monedas1 = _format.reconstruir(_textField.get(1).getText());
                    var ingresos1 = billetes1 + monedas1;
                    
                    var ingresosData = CajaIngreso().stream()
                            .filter(c -> c.getIdCaja() == idCaja && 
                                    _cal.getFecha(c.getFecha())
                                            .equals(_cal.getFecha(new Date())))
                            .collect(Collectors.toList());
                    var ingresoData = 0 < ingresosData.size() ? ingresosData.get(0) 
                            : new TCajas_ingresos();
                    var billetes2 = billetes1 + ingresoData.getBillete();
                    var monedas2 = monedas1 + ingresoData.getMoneda();
                    var ingresos2 = ingresoData.getMoneda() + ingresoData.getBillete()+ ingresos1;
                    String sqlIngresos = "UPDATE tcajas_ingresos SET Billete = ?,"
                            + "Moneda = ?,Ingreso = ?,Fecha = ? WHERE IdCaja =" + idCaja;
                    Object[] dataIngresos = {
                        billetes2,
                        monedas2,
                        ingresos2,
                        new Date()
                    };
                    qr.update(getConn(), sqlIngresos, dataIngresos);
                    var cajasData = Cajas_registros().stream()
                            .filter(c -> c.getIdCaja() == idCaja
                            && c.isEstado() == true).collect(Collectors.toList());
                    var idUsuario = 0 < cajasData.size() ? cajasData.get(0).getIdUsuario() : 0;
                    String sqlReportes = "INSERT INTO tcajas_reportes(IdCaja,IdUsuario,"
                            + "Billete,Moneda,Ingreso,TipoIngreso,Fecha)"
                            + " VALUES(?,?,?,?,?,?,?)";
                    Object[] dataReportes = {
                        idCaja,
                        idUsuario,
                        billetes1,
                        monedas1,
                        ingresos1,
                        "inicial",
                        new Date()
                    };
                    qr.insert(getConn(), sqlReportes, new ColumnListHandler(), dataReportes);
                    getConn().commit();
                    restablecer();
                } catch (Exception ex) {
                    getConn().rollback();
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        }
    }

    public void restablecer() {
        var model = new SpinnerNumberModel(
                10.0, // Dato visualizado al inicio en el spinner 
                1.0, // Límite inferior 
                24.0, // Límite superior 
                1.0 // incremento-decremento 
        );
        _spinnerCaja.setModel(model);
        _label.get(0).setText("Numero de caja");
        _label.get(0).setForeground(new Color(102, 102, 102));
        _label.get(1).setText("#" + 0);
        _checkBoxEstado.setSelected(false);
        _checkBoxEstado.setForeground(new Color(102, 102, 102));
        _checkBoxIngresos.setSelected(false);
        _checkBoxIngresos.setForeground(new Color(102, 102, 102));
        _textField.get(0).setEnabled(false);
        _textField.get(1).setEnabled(false);
        _spinnerCaja.setValue(1);
        _label.get(2).setText("Billetes");
        _label.get(2).setForeground(new Color(102, 102, 102));
        _label.get(3).setText("Monedas");
        _label.get(3).setForeground(new Color(102, 102, 102));
        _textField.get(0).setText("");
        _textField.get(1).setText("");
        var numCaja = _textField.get(2).getText().equals("") ? 0
                : Integer.valueOf(_textField.get(2).getText());
        SearchCajas(numCaja);
    }
    public int _seccion;
}
