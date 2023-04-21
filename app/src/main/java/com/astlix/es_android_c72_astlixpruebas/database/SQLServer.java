package com.astlix.es_android_c72_astlixpruebas.database;

import android.os.StrictMode;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLServer {
    private Connection connection = null;
    //private String msg = "Internet/DB_Credentials/Windows_FireeWall_TurnOn Error, See Android Monitor in the bottom For details!";
    private boolean connected = false;
    //private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

    public SQLServer(String ip, String dataBase, String user, String pass) {
        String dbString = "jdbc:jtds:sqlserver://" + ip + ";databaseName=" + dataBase + ";user=" + user + ";password=" + pass + ";";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(dbString);

            if (connection != null) {
                connected = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getConnected() {
        return connected;
    }

    /*public ArrayList<VistaDetOrdenes> getEntradasDetalle(int docEntry, int docNum) throws SQLException {
        ArrayList<VistaDetOrdenes> vistaDetOrdenesArrayList = new ArrayList<>();
        String query = "SELECT tbl1.ItemCode, tbl1.Medida, tbl1.Dscription, tbl1.Quantity, tbl1.WhsCode, tbl1.[No Proveedor], tbl1.Proveedor, tbl1.Propiedad, tbl1.[Status Linea]" + " FROM [DORMIMUNDO_PRODUCTIVA].[dbo].[Vista_Det_Ordenes] tbl1" + " WHERE tbl1.DocEntry = ?";
        PreparedStatement selectStatment = connection.prepareStatement(query);
        selectStatment.setInt(1, docEntry);
        ResultSet rs = selectStatment.executeQuery();
        if (rs != null) {
            while (rs.next()) {
                Log.d("ENTRADA_DETALLE", rs.getString("Medida") + " " + rs.getString("Dscription") + rs.getString("ItemCode") + rs.getInt("Quantity"));
                VistaDetOrdenes vistaDetOrdenes = new VistaDetOrdenes(docEntry, docNum, rs.getString("ItemCode"), rs.getString("Medida"), rs.getString("Dscription"), rs.getInt("Quantity"), rs.getString("WhsCode"), rs.getString("No Proveedor"), rs.getString("Proveedor"), rs.getString("Propiedad"), rs.getString("Status Linea"));
                vistaDetOrdenes.setSentido(1);
                vistaDetOrdenesArrayList.add(vistaDetOrdenes);
            }
            rs.close();
        }
        selectStatment.close();
        return vistaDetOrdenesArrayList;
    }

    public ArrayList<VistaCabGral> getEntradasCatalogo(String whsCode) throws SQLException {
        ArrayList<VistaCabGral> vistaCabOrdenesList = new ArrayList<>();
        String query = "SELECT DISTINCT tbl1.DocEntry, tbl1.DocNum, tbl1.DocDate, tbl1.DocDueDate, tbl1.CardCode, tbl1.CardName " + "FROM [DORMIMUNDO_PRODUCTIVA].[dbo].[Vista_Cab_Ordenes] tbl1 " + "JOIN [DORMIMUNDO_PRODUCTIVA].[dbo].[Vista_Det_Ordenes] tbl2  ON tbl1.DocEntry = tbl2.DocEntry " + "JOIN [DORMIMUNDO_RFID].[dbo].[tbl_provedores] tbl3 ON tbl1.CardCode = tbl3.clave " + "COLLATE SQL_Latin1_General_CP850_CI_AS " + "WHERE tbl2.WhsCode = ? AND tbl3.rfid = 1";
        PreparedStatement selectStatment = connection.prepareStatement(query);
        selectStatment.setString(1, whsCode);
        ResultSet rs = selectStatment.executeQuery();
        if (rs != null) {
            while (rs.next()) {
                Log.d("OBTENER", rs.getInt("DocEntry") + " " + rs.getInt("DocNum") + " " + rs.getString("DocDate") + " " + rs.getString("DocDueDate") + " " + rs.getString("CardCode") + " " + rs.getString("CardName"));
                vistaCabOrdenesList.add(new VistaCabGral(rs.getInt("DocEntry"), rs.getInt("DocNum"), rs.getString("DocDate"), rs.getString("CardCode"), rs.getString("CardName")));
            }
            rs.close();
        }
        selectStatment.close();
        return vistaCabOrdenesList;
    }

    public ArrayList<VistaCabGral> getSalidasCatalogo(String whsCode) throws SQLException {
        ArrayList<VistaCabGral> vistaCabOrdenesVentaList = new ArrayList<>();
        String query = "SELECT DISTINCT tbl1.DocEntry, tbl1.DocNum, tbl1.DocDate, tbl1.Origen, tbl1.CardCode, tbl1.CardName " + "FROM [DORMIMUNDO_PRODUCTIVA].[dbo].[Vista_Cab_OrdenVenta] tbl1 " + "JOIN [DORMIMUNDO_PRODUCTIVA].[dbo].[Vista_Det_OrdenVenta] tbl2  ON tbl1.DocEntry = tbl2.DocEntry " + "WHERE tbl2.WhsCode = ?";
        PreparedStatement selectStatment = connection.prepareStatement(query);
        selectStatment.setString(1, whsCode);
        ResultSet rs = selectStatment.executeQuery();
        if (rs != null) {
            while (rs.next()) {
                vistaCabOrdenesVentaList.add(new VistaCabGral(rs.getInt("DocEntry"), rs.getInt("DocNum"), rs.getString("DocDate"), rs.getString("CardCode"), rs.getString("CardName")));
            }
            rs.close();
        }
        selectStatment.close();
        return vistaCabOrdenesVentaList;
    }

    public List<VistaDetOrdenes> getSalidasDetalle(int docEntry, int docNum) throws SQLException {
        ArrayList<VistaDetOrdenes> vistaDetOrdenesArrayList = new ArrayList<>();
        String query = "SELECT tbl1.ItemCode, tbl1.Medida, tbl1.Dscription, tbl1.Quantity, tbl1.WhsCode, tbl1.[No Proveedor], tbl1.Proveedor, tbl1.Propiedad, tbl1.[Status Linea] FROM [DORMIMUNDO_PRODUCTIVA].[dbo].[Vista_Det_OrdenVenta] tbl1 WHERE tbl1.DocEntry = ?";
        PreparedStatement selectStatment = connection.prepareStatement(query);
        selectStatment.setInt(1, docEntry);
        ResultSet rs = selectStatment.executeQuery();
        if (rs != null) {
            while (rs.next()) {
                VistaDetOrdenes vistaDetOrdenes = new VistaDetOrdenes(docEntry, docNum, rs.getString("ItemCode"), rs.getString("Medida"), rs.getString("Dscription"), rs.getInt("Quantity"), rs.getString("WhsCode"), rs.getString("No Proveedor"), rs.getString("Proveedor"), rs.getString("Propiedad"), rs.getString("Status Linea"));
                vistaDetOrdenes.setSentido(1);
                vistaDetOrdenesArrayList.add(vistaDetOrdenes);
            }
            rs.close();
        }
        selectStatment.close();
        return vistaDetOrdenesArrayList;
    }

    public void procesarAlmacen(List<VistaDetOrdenes> vistaDetOrdenes, int status, String whsCode) throws SQLException {
        int enviar = 0;
        String query = "MERGE [DORMIMUNDO_RFID].[dbo].[tbl_almacen] AS t " + "USING (VALUES (?, ?, ?, ?)) " + "AS s (epc, sku, status, almacen) " + "ON t.epc = s.epc " + "WHEN MATCHED " + "THEN UPDATE SET " + "t.status = s.status, " + "t.almacen = s.almacen " + "WHEN NOT MATCHED " + "THEN INSERT (epc, sku, status, almacen) " + "VALUES (s.epc, s.sku, s.status, s.almacen);";
        PreparedStatement insertStmt = connection.prepareStatement(query);
        for (VistaDetOrdenes vdo : vistaDetOrdenes) {
            if (vdo.getLeidos() > 0) {
                for (String epc : vdo.getEpc()) {
                    insertStmt.setString(1, epc);
                    insertStmt.setString(2, vdo.getItemCode());
                    insertStmt.setInt(3, status);
                    insertStmt.setString(4, whsCode);
                    insertStmt.addBatch();
                    enviar++;

                    if (enviar >= 1000) {
                        enviar = 0;
                        insertStmt.executeBatch();
                    }
                }
            }
        }
        if (enviar > 0) {
            insertStmt.executeBatch();
        }
        insertStmt.close();
    }

    public void procesarMovimientos(List<VistaDetOrdenes> vistaDetOrdenes, ProcessType type, int id_puerta, String whsCode) throws SQLException {
        int enviar = 0;
        String query = "INSERT INTO [DORMIMUNDO_RFID].[dbo].[tbl_movimientos] (id_almacen, fecha, epc, docEntry, id_lector, transitionid) VALUES (?, GETDATE(), ?, ?, ?, ?);";
        PreparedStatement insertStmt = connection.prepareStatement(query);
        for (VistaDetOrdenes vdo : vistaDetOrdenes) {
            for (int i = 0; i < vdo.getEpc().size(); i++) {
                String epc = vdo.getEpc().get(i);
                insertStmt.setString(1, whsCode);
                insertStmt.setString(2, epc);
                insertStmt.setInt(3, vdo.getDocNum());
                insertStmt.setInt(4, id_puerta);
                if (vdo.getSentido() == 0) {
                    insertStmt.setInt(5, type.getWrong());
                } else {
                    if (vdo.getQuantity() > i) {
                        insertStmt.setInt(5, type.getTranID());
                    } else {
                        insertStmt.setInt(5, type.getSobrante());
                    }
                }

                insertStmt.addBatch();
                enviar++;

                if (enviar >= 1000) {
                    enviar = 0;
                    insertStmt.executeBatch();
                }
            }
        }
        if (enviar > 0) {
            insertStmt.executeBatch();
        }
        insertStmt.close();
    }*/
}
