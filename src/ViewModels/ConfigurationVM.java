/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewModels;

import Conexion.Consult;
import Library.FormatDecimal;
import Models.TConfiguration;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

/**
 *
 * @author AJPDHN
 */
public class ConfigurationVM extends Consult {

    public static String Mony;
    public static Double Interests;
    private static List<JRadioButton> _radio;
    private List<JTextField> _textField;
    private List<JLabel> _label;
    private FormatDecimal _format;

    public ConfigurationVM() {
        TypeMoney();
    }

    public ConfigurationVM(List<JRadioButton> radio) {
        _radio = radio;
        RadioEvent();
        TypeMoney();
         Restablecer();
    }

    public ConfigurationVM(List<JRadioButton> radio,
            List<JTextField> textField,
            List<JLabel> label) {
        _radio = radio;
        _label = label;
        _textField = textField;
        _format = new FormatDecimal();
        RadioEvent();
        TypeMoney();
         Restablecer();
    }

    private void RadioEvent() {
        _radio.get(0).addActionListener((ActionEvent e) -> {
            TypeMoney("MX.", _radio.get(0).isSelected());
        });
        _radio.get(1).addActionListener((ActionEvent e) -> {
            TypeMoney("DLL", _radio.get(1).isSelected());
        });
    }
    private String sqlConfig;

    private void TypeMoney() {
        sqlConfig = "INSERT INTO tconfiguration(TypeMoney) VALUES(?)";

        List<TConfiguration> config = config();
        final QueryRunner qr = new QueryRunner(true);

        if (config.isEmpty()) {
            Mony = "MX.";
            Object[] dataConfig = {Mony};
            try {
                qr.insert(getConn(), sqlConfig, new ColumnListHandler(), dataConfig);
            } catch (SQLException ex) {
            }
        } else {
            TConfiguration data = config.get(0);
            Mony = data.getTypeMoney();
            switch (Mony) {
                case "MX.":
                    _radio.get(0).setSelected(true);
                    break;
                case "DLL":
                    _radio.get(1).setSelected(true);
                    break;
            }
        }
    }

    private void TypeMoney(String typeMoney, boolean valor) {
        final QueryRunner qr = new QueryRunner(true);
        if (valor) {
            try {
                List<TConfiguration> config = config();
                if (config.isEmpty()) {
                    sqlConfig = "INSERT INTO tconfiguration(TypeMoney) VALUES(?)";
                    Mony = typeMoney;
                    Object[] dataConfig = {Mony};

                    qr.insert(getConn(), sqlConfig, new ColumnListHandler(), dataConfig);

                } else {
                    TConfiguration data = config.get(0);
                    sqlConfig = "UPDATE tconfiguration SET TypeMoney = ? WHERE ID =" + data.getID();

                    if (data.getTypeMoney().equals(typeMoney)) {
                        Mony = typeMoney;
                    } else {
                        Mony = typeMoney;
                        Object[] dataConfig = {Mony};
                        qr.update(getConn(), sqlConfig, dataConfig);
                    }
                }
            } catch (SQLException e) {
            }
        }
    }

    public void RegistrarIntereses() {
        if (_textField.get(0).getText().equals("")) {
            _label.get(0).setText("Ingrese el interés");
            _label.get(0).setForeground(Color.WHITE);
            _textField.get(0).requestFocus();
        } else {
            if (_radio.get(2).isSelected()) {
                try {
                    final QueryRunner qr = new QueryRunner(true);
                    var _tconfiguration = config();
                    if (_tconfiguration.isEmpty()) {
                        var sqlConfig = "INSERT INTO tconfiguration(TypeMoney,Interests) VALUES(?,?)";
                        Object[] dataConfig = {"L.", _format.reconstruir(_textField.get(0).getText())};
                        qr.insert(getConn(), sqlConfig, new ColumnListHandler(), dataConfig);
                    } else {
                        var data = _tconfiguration.get(0);
                        var sqlConfig = "UPDATE tconfiguration SET TypeMoney = ?,"
                                + "Interests = ? WHERE ID =" + data.getID();
                        Object[] dataConfig = {
                            data.getTypeMoney(),
                            _format.reconstruir(_textField.get(0).getText())
                        };
                        qr.update(getConn(), sqlConfig, dataConfig);
                    }
                    Restablecer();
                } catch (Exception e) {
                }
            } else {
                _label.get(0).setText("Seleccione la opcion intereses");
                _label.get(0).setForeground(Color.WHITE);
            }
        }
    }

    private void Restablecer() {
        var _tconfiguration = config();
        if (!_tconfiguration.isEmpty()) {
            var data = _tconfiguration.get(0);
            Interests = data.getInterests();
            if (_label != null) {
                var interest = data.getInterests() == 0.0 ? "0.0%" : data.getInterests() + "%";
                _label.get(1).setText(interest);
                _textField.get(0).setText("");
                _label.get(0).setText("");
                _radio.get(0).setSelected(false);
            }
        }
    }
}
