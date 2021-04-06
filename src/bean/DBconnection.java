/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author janer
 */
public class DBconnection {

    Connection connection;
    String url = "jdbc:postgresql://localhost:5432/Turnero";
    String user = "postgres";
    String password = "123";

    public String login(String usuario, String contrasena) {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            Statement st = connection.createStatement();
            ResultSet result = st.executeQuery("select rol,id_login,usuario from login where login.usuario='" + usuario + "' and login.contrasena='" + contrasena + "' ");
            String valor = "2", id_login = "", login_usuario = "";
            id_caja llenado = new id_caja();
            id_login llenado_login = new id_login();
            int id_cajera;
            if (result.next()) {
                valor = result.getString("rol");
                id_login = result.getString("id_login");
                llenado_login.setIdlogin(Integer.parseInt(id_login));
                llenado_login.setUsuario(result.getString("usuario"));

                if (valor.equals("0")) {
                    ResultSet resultcaja = st.executeQuery("select id_caja from caja where caja.id_login=" + id_login);
                    if (resultcaja.next()) {
                        id_cajera = resultcaja.getInt("id_caja");
                        llenado.setId_cajera(id_cajera);
                    }
                }
            }

            connection.close();
            st.close();
            return valor;
        } catch (Exception e) {
        }
        return "2";
    }

    public boolean CrearCaja(String user1, String clave, String nombre, String apellido, int caja) {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            Statement st = connection.createStatement();
            st.executeQuery("select crearcaja('" + user1 + "','" + clave + "','0','" + nombre + "','" + apellido + "'," + caja + ")");
            connection.close();
            st.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean actualizarclave(String pass) {
        try {
            id_login datos = new id_login();
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            Statement st = connection.createStatement();
            int result = st.executeUpdate(" update login set contrasena='" + pass + "' where id_login= " + datos.getIdlogin());
            connection.close();
            st.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String recordarcontra(String usuario, String nombre, int caja) {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            Statement st = connection.createStatement();
            ResultSet resultSet = st.executeQuery("select recordarclave('" + usuario + "','" + nombre + "'," + caja + ")");
            String valor = "";
            if (resultSet.next()) {
                valor = resultSet.getString("recordarclave");
            }
            connection.close();
            st.close();
            return valor;
        } catch (Exception e) {
            return "error";
        }
    }

    public Map<Integer, String> combocajera() {
        Map<Integer, String> map = new HashMap<>();
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            Statement st = connection.createStatement();
            ResultSet resultSet = st.executeQuery("select id_caja,concat(n_caja,' - ', nombre) as cajita from caja");
            while (resultSet.next()) {
                map.put(Integer.parseInt(resultSet.getString("id_caja")), resultSet.getString("cajita"));
            }
            connection.close();
            st.close();
            return map;
        } catch (Exception e) {
            return map;
        }
    }

    public boolean turndiscapacidad(String fecha_h) {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            Statement st = connection.createStatement();
            id_caja cajita = new id_caja();
            ResultSet resultSet= st.executeQuery("select inturnespe('" + fecha_h + "'," + cajita.getId_cajera() + ")");           
            connection.close();
            st.close();
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
