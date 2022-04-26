/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewModels;

import Conexion.Consult;
import Library.Encriptar;
import Library.Objectos;
import Library.Paginador;
import Library.Render_CheckBox;
import Library.Uploadimage;
import Models.Caja.TCajas;
import Models.Usuario.TRoles;
import Models.Usuario.TUsuarios;
import java.awt.Color;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
 * @author AJPDHN
 */
public class UsuariosVM extends Consult {

    private static TUsuarios _dataUsuario;
    private static JLabel _nombrePerfil;
    private static JLabel _picturePerfil;
    private JLabel _imagePicture;
    private JLabel _labelCaja_Numero;
    private List<JTextField> _textField;
    private List<JLabel> _label;
    private JCheckBox _checkBoxState;
    private JComboBox _comboBoxRoles;
    private JTable _tableUser;
    private JSpinner _spinnerPaginas;
    private String _accion = "insert";
    private Uploadimage _uploadimage = new Uploadimage();
    private DefaultTableModel modelo1;
    private int _reg_por_pagina = 10;
    private int _num_pagina = 1;
    private int _seccion = 1;
    private static TCajas _cajaData;
    private Paginador<TUsuarios> _paginadorUsuarios;

    public UsuariosVM(TUsuarios dataUsuario, Object[] perfil, TCajas cajaData) {
        _cajaData = cajaData;
        _dataUsuario = dataUsuario;
        _nombrePerfil = (JLabel) perfil[0];
        _picturePerfil = (JLabel) perfil[1];
        _labelCaja_Numero = (JLabel) perfil[2];
        Perfil();
    }

    public UsuariosVM(Object[] objectos, List<JTextField> textField, List<JLabel> label) {
        _textField = textField;
        _label = label;
        _imagePicture = (JLabel) objectos[0];
        _checkBoxState = (JCheckBox) objectos[1];
        _tableUser = (JTable) objectos[2];
        _spinnerPaginas = (JSpinner) objectos[3];
        _comboBoxRoles = (JComboBox) objectos[4];
        Restablecer();
    }

    private void Perfil() {
        _labelCaja_Numero.setText("#" + _cajaData.getCaja());
        _nombrePerfil.setText(_dataUsuario.getNombre());
        if (null != _dataUsuario.getImagen()) {
            _uploadimage.byteImage(_picturePerfil, _dataUsuario.getImagen());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="CODIGO DE REGISTRAR USUARIOS">
    public void RegistrarUsuario() {
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
                    _label.get(2).setText("Ingrese el email");
                    _label.get(2).setForeground(Color.RED);
                    _textField.get(2).requestFocus();
                } else {
                    if (!Objectos.eventos.isEmail(_textField.get(2).getText())) {
                        _label.get(2).setText("Ingrese un email valido");
                        _label.get(2).setForeground(Color.RED);
                        _textField.get(2).requestFocus();
                    } else {
                        if (_textField.get(3).getText().equals("")) {
                            _label.get(3).setText("Ingrese el apellido");
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
                                    if (_textField.get(6).getText().equals("")) {
                                        _label.get(6).setText("Ingrese el usuario");
                                        _label.get(6).setForeground(Color.RED);
                                        _textField.get(6).requestFocus();
                                    } else {
                                        if (_textField.get(7).getText().equals("")) {
                                            _label.get(7).setText("Ingrese la contraseña");
                                            _label.get(7).setForeground(Color.RED);
                                            _textField.get(7).requestFocus();
                                        } else {
                                            int count;
                                            var listEmail = Usuarios().stream()
                                                    .filter(u -> u.getEmail().equals(_textField.get(2).getText()))
                                                    .collect(Collectors.toList());
                                            count = listEmail.size();
                                            var listNid = Usuarios().stream()
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
                                                                _label.get(2).setText("El email ya esta registrado");
                                                                _label.get(2).setForeground(Color.RED);
                                                                _textField.get(2).requestFocus();
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
                                                            if (listEmail.get(0).getIdUsuario() == _idUsuario
                                                                    && listNid.get(0).getIdUsuario() == _idUsuario) {
                                                                SaveData();
                                                            } else {
                                                                if (listNid.get(0).getIdUsuario() != _idUsuario) {
                                                                    _label.get(0).setText("El nid ya esta registrado");
                                                                    _label.get(0).setForeground(Color.RED);
                                                                    _textField.get(0).requestFocus();
                                                                }
                                                                if (listEmail.get(0).getIdUsuario() != _idUsuario) {
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
                                                                    if (listNid.get(0).getIdUsuario() == _idUsuario) {
                                                                        SaveData();
                                                                    } else {
                                                                        _label.get(0).setText("El nid ya esta registrado");
                                                                        _label.get(0).setForeground(Color.RED);
                                                                        _textField.get(0).requestFocus();
                                                                    }
                                                                }
                                                                if (!listEmail.isEmpty()) {
                                                                    if (listEmail.get(0).getIdUsuario() == _idUsuario) {
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
                                            } catch (Exception e) {
                                                JOptionPane.showMessageDialog(null, e);
                                            }
                                        }
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
                image = Objectos.uploadimage.getTransFoto(_imagePicture);
            }
            var role = (TRoles) _comboBoxRoles.getSelectedItem();
            switch (_accion) {
                case "insert":
                    String sqlUsuario1 = "INSERT INTO tusuarios(Nid,Nombre,Apellido,Direccion"
                            + ",Telefono,Email,Usuario,Password,Role,Imagen,Is_active"
                            + ",State,Fecha) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

                    Object[] dataUsuario1 = {
                        _textField.get(0).getText(),
                        _textField.get(1).getText(),
                        _textField.get(3).getText(),
                        _textField.get(5).getText(),
                        _textField.get(4).getText(),
                        _textField.get(2).getText(),
                        _textField.get(6).getText(),
                        Encriptar.encrypt(_textField.get(7).getText()),
                        role.getRole(),
                        image,
                        true,
                        _checkBoxState.isSelected(),//tynyint
                        new Date()
                    };
                    qr.insert(getConn(), sqlUsuario1, new ColumnListHandler(), dataUsuario1);
                    break;
                case "update":
                    Object[] dataUsuario2 = {
                        _textField.get(0).getText(),
                        _textField.get(1).getText(),
                        _textField.get(3).getText(),
                        _textField.get(2).getText(),
                        _textField.get(4).getText(),
                        _textField.get(5).getText(),
                        _textField.get(6).getText(),
                        _checkBoxState.isSelected(),
                        role.getRole(),
                        image
                    };
                    String sqlUsuario2 = "UPDATE tusuarios SET Nid = ?,Nombre = ?,"
                            + "Apellido = ?,Email = ?,Telefono = ?,Direccion = ?,"
                            + "Usuario = ?,State = ?,Role = ?,Imagen = ?"
                            + " WHERE IdUsuario =" + _idUsuario;
                    qr.update(getConn(), sqlUsuario2, dataUsuario2);
                    break;
            }
            getConn().commit();
            Restablecer();
        } catch (Exception e) {
            getConn().rollback();
            System.out.println(e.getMessage());
        }
    }

    public void SearchUsuarios(String campo) {
        List<TUsuarios> usuariosFilter;
        String[] titulos = {"Id", "Nid", "Nombre", "Apellido",
            "Email", "Direccion", "Telefono", "Usuario", "Role", "Estado", "Image"};
        modelo1 = new DefaultTableModel(null, titulos);
        int inicio = (_num_pagina - 1) * _reg_por_pagina;
        if (campo.equals("")) {
            usuariosFilter = Usuarios().stream()
                    .skip(inicio).limit(_reg_por_pagina)
                    .collect(Collectors.toList());
        } else {
            usuariosFilter = Usuarios().stream()
                    .filter(C -> C.getNid().startsWith(campo) || C.getNombre().startsWith(campo)
                    || C.getApellido().startsWith(campo))
                    .skip(inicio).limit(_reg_por_pagina)
                    .collect(Collectors.toList());
        }
        if (!usuariosFilter.isEmpty()) {
            usuariosFilter.forEach(item -> {
                Object[] registros = {
                    item.getIdUsuario(),
                    item.getNid(),
                    item.getNombre(),
                    item.getApellido(),
                    item.getEmail(),
                    item.getDireccion(),
                    item.getTelefono(),
                    item.getUsuario(),
                    item.getRole(),
                    item.isState(),
                    item.getImagen()
                };
                modelo1.addRow(registros);
            });
        }
        _tableUser.setModel(modelo1);
        _tableUser.setRowHeight(30);
        _tableUser.getColumnModel().getColumn(0).setMaxWidth(0);
        _tableUser.getColumnModel().getColumn(0).setMinWidth(0);
        _tableUser.getColumnModel().getColumn(0).setPreferredWidth(0);
        _tableUser.getColumnModel().getColumn(10).setMaxWidth(0);
        _tableUser.getColumnModel().getColumn(10).setMinWidth(0);
        _tableUser.getColumnModel().getColumn(10).setPreferredWidth(0);
        _tableUser.getColumnModel().getColumn(9).setCellRenderer(new Render_CheckBox());
    }
    private int _idUsuario = 0;

    public void GetUsuario() {
        _accion = "update";
        int filas = _tableUser.getSelectedRow();
        _idUsuario = (Integer) modelo1.getValueAt(filas, 0);
        _textField.get(0).setText((String) modelo1.getValueAt(filas, 1));
        _textField.get(1).setText((String) modelo1.getValueAt(filas, 2));
        _textField.get(3).setText((String) modelo1.getValueAt(filas, 3));
        _textField.get(2).setText((String) modelo1.getValueAt(filas, 4));
        _textField.get(5).setText((String) modelo1.getValueAt(filas, 5));
        _textField.get(4).setText((String) modelo1.getValueAt(filas, 6));
        _textField.get(6).setText((String) modelo1.getValueAt(filas, 7));
        _textField.get(7).setText("********");
        _textField.get(7).setEnabled(false);
        var model = new DefaultComboBoxModel();
        var role = new TRoles();
        var rol = (String) modelo1.getValueAt(filas, 8);
        role.setRole(rol);
        model.addElement(role);
        roles().forEach(item -> {
            if (!rol.equals(item.getRole())) {
                model.addElement(item);
            }
        });
        _comboBoxRoles.setModel(model);
        _checkBoxState.setSelected((Boolean) modelo1.getValueAt(filas, 9));
        byte[] image = (byte[]) modelo1.getValueAt(filas, 10);
        if (image != null) {
            Objectos.uploadimage.byteImage(_imagePicture, image);
        }
    }

    public final void Restablecer() {
        _seccion = 1;
        _accion = "insert";
        for (int i = 0; i < _textField.size(); i++) {
            _textField.get(i).setText("");
        }
        _checkBoxState.setSelected(false);
        _checkBoxState.setForeground(new Color(102, 102, 102));
        _label.get(0).setText("Nid");
        _label.get(0).setForeground(new Color(102, 102, 102));
        _label.get(1).setText("Nombre");
        _label.get(1).setForeground(new Color(102, 102, 102));
        _label.get(2).setText("Email");
        _label.get(2).setForeground(new Color(102, 102, 102));
        _label.get(3).setText("Apellido");
        _label.get(3).setForeground(new Color(102, 102, 102));
        _label.get(4).setText("Telefono");
        _label.get(4).setForeground(new Color(102, 102, 102));
        _label.get(5).setText("Direccion");
        _label.get(5).setForeground(new Color(102, 102, 102));
        _label.get(6).setText("Usuario");
        _label.get(6).setForeground(new Color(102, 102, 102));
        _label.get(7).setText("Contraseña");
        _label.get(7).setForeground(new Color(102, 102, 102));
        _imagePicture.setIcon(new ImageIcon(getClass().getClassLoader()
                .getResource("Resources/logo-google_1.png")));
        getRoles();
        listUsuarios = Usuarios();
        if (!listUsuarios.isEmpty()) {
            _paginadorUsuarios = new Paginador<>(listUsuarios,
                    _label.get(8), _reg_por_pagina);
        }
        SearchUsuarios("");
        SpinnerNumberModel model = new SpinnerNumberModel(
                10.0, // Dato visualizado al inicio en el spinner 
                1.0, // Límite inferior 
                100.0, // Límite superior 
                1.0 // incremento-decremento 
        );
        _spinnerPaginas.setModel(model);
    }

    public void getRoles() {
        var model = new DefaultComboBoxModel();
        roles().forEach(item -> {
            model.addElement(item);

        });
        _comboBoxRoles.setModel(model);
    }
    private List<TUsuarios> listUsuarios;

    public void Paginador(String metodo) {
        switch (metodo) {
            case "Primero":
                switch (_seccion) {
                    case 1:
                        if (!listUsuarios.isEmpty()) {
                            _num_pagina = _paginadorUsuarios.primero();
                        }
                        break;
                }
                break;
            case "Anterior":
                switch (_seccion) {
                    case 1:
                        if (!listUsuarios.isEmpty()) {
                            _num_pagina = _paginadorUsuarios.anterior();
                        }
                        break;
                }
                break;
            case "Siguiente":
                switch (_seccion) {
                    case 1:
                        if (!listUsuarios.isEmpty()) {
                            _num_pagina = _paginadorUsuarios.siguiente();
                        }
                        break;
                }
                break;
            case "Ultimo":
                switch (_seccion) {
                    case 1:
                        if (!listUsuarios.isEmpty()) {
                            _num_pagina = _paginadorUsuarios.ultimo();
                        }
                        break;
                }
                break;
        }
        switch (_seccion) {
            case 1:
                SearchUsuarios("");
                break;
        }
    }

    public void Registro_Paginas() {
        _num_pagina = 1;
        Number value = (Number) _spinnerPaginas.getValue();
        _reg_por_pagina = value.intValue();
        switch (_seccion) {
            case 1:
                if (!listUsuarios.isEmpty()) {
                    _paginadorUsuarios = new Paginador<>(listUsuarios,
                            _label.get(8), _reg_por_pagina);
                }
                SearchUsuarios("");
                break;
        }
    }
    // </editor-fold>
}
