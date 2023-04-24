package com.astlix.es_android_c72_astlixpruebas.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.astlix.es_android_c72_astlixpruebas.model.Archivo;
import com.astlix.es_android_c72_astlixpruebas.model.LecturasRfidCiega;

import java.util.ArrayList;

public class InterfazBD {
    private static final String TAG = "InterfazBD";
    private final ConexionBd con;
    private SQLiteDatabase db;

    public InterfazBD(Context context) {
        con = new ConexionBd(context);
        inicializaDB();
    }


    public void open() throws SQLiteException {
        db = con.getWritableDatabase();
    }

    public void close() throws SQLiteException {
        con.close();
    }

    public void inicializaDB() {
        int numRegistros;

        ContentValues content;
        open();

        numRegistros = traeRegistro("configuracion");
        if (numRegistros == 0) {
            content = new ContentValues();
            content.put("ip", "192.168.100.141");
            content.put("base", "DORMIMUNDO_RFID");
            content.put("usuario", "test");
            //content.put("contrasena", "SAPB1Admin");
            content.put("contrasena", "sa");
            content.put("potencia", 30);
            db.insert("configuracion", null, content);
        }
    }

    public String[] obtenerServidor() {
        open();

        String consulta = "select ip, base, usuario, contrasena from configuracion where _id = 1;";

        Cursor c = db.rawQuery(consulta, null);
        c.moveToFirst();
        String[] res; //=new String[c.getString()];
        if (c.getCount() != 0) {
            res = new String[c.getColumnCount()];
            for (int i = 0; i < c.getColumnCount(); ++i) {
                res[i] = c.getString(i);
            }
        } else {
            return null;
        }
        return res;
    }

    @SuppressWarnings("SameParameterValue")
    public void truncarTabla(String tabla) {
        String query = "DELETE FROM " + tabla + ";";
        db.execSQL(query);
        String query2 = "delete from sqlite_sequence where name='" + tabla + "';";
        db.execSQL(query2);
    }

    public int traeRegistro(String tabla) {
        int ret;
        String consulta = "select _id from " + tabla + ";";
        Cursor c = db.rawQuery(consulta, null);
        ret = c.getCount();
        c.close();
        return ret;
    }

    public int obtenerPotencia() {
        open();

        String consulta = "select potencia from configuracion where _id = 1;";
        int res;
        Cursor c = db.rawQuery(consulta, null);
        c.moveToFirst();
        if (c.getCount() != 0) {
            res = c.getInt(0);
        } else {
            res = -1;
        }
        c.close();
        return res;
    }

    public void modificarPotencia(int potencia) {
        ContentValues content;
        open();
        content = new ContentValues();
        content.put("potencia", potencia);
        db.update("configuracion", content, "_id=1", null);
    }

    public DatabaseConf obtenerConfiguracionDatabase() {
        open();
        DatabaseConf configuracion = null;

        String consulta = "select ip, base, usuario, contrasena from configuracion where _id = 1;";
        Cursor c = db.rawQuery(consulta, null);
        c.moveToFirst();
        if (c.getCount() != 0) {
            configuracion = new DatabaseConf(c.getString(0), c.getString(1), c.getString(2), c.getString(3));
        }
        c.close();
        return configuracion;
    }

    public void modificarDatabase(DatabaseConf databaseConf) {
        ContentValues content;
        open();
        content = new ContentValues();
        content.put("ip", databaseConf.getUrl());
        content.put("base", databaseConf.getDatabase());
        content.put("usuario", databaseConf.getUser());
        content.put("contrasena", databaseConf.getPassword());
        db.update("configuracion", content, "_id=1", null);
    }

    public void insertarTags(LecturasRfidCiega lecturasRfidCiega, String ascii, String nombreArchivo) {
        ContentValues content;
        open();

        content = new ContentValues();
        content.put("tag", lecturasRfidCiega.getEpc());
        content.put("rssi", lecturasRfidCiega.getRssi());
        content.put("cantidad", lecturasRfidCiega.getCant());
        content.put("ascii", ascii);
        content.put("archivo", nombreArchivo);

        db.insert("tags", null, content);
    }

    public ArrayList<LecturasRfidCiega> obtenerTags(String nombre) {
        System.out.println("nombre en interfaz " + nombre);
        ArrayList<LecturasRfidCiega> tags = new ArrayList<>();
        open();

        String consulta = "select tag, rssi, ascii from tags where archivo = '" + nombre + "';";

        try {
            Cursor c = db.rawQuery(consulta, null);
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    tags.add(new LecturasRfidCiega(c.getString(0), c.getString(1), c.getString(2)));
                    c.moveToNext();
                }
            }
            c.close();
        } catch (SQLiteException e) {
            Log.d("Denver", e.getMessage());
        }
        return tags;
    }

    public ArrayList<Archivo> obtenerArchivos() {
        ArrayList<Archivo> archivos = new ArrayList<>();
        open();

        String consulta = "select * from archivos;";

        try {
            Cursor c = db.rawQuery(consulta, null);
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    archivos.add(new Archivo(c.getInt(0), c.getString(1)));
                    c.moveToNext();
                }
            }
            c.close();
        } catch (SQLiteException e) {
            Log.d("Denver", e.getMessage());
        }
        return archivos;
    }

    public void insertarArchivo(String nombreArchivoExportar) {
        ContentValues content;
        open();

        content = new ContentValues();
        content.put("nombre", nombreArchivoExportar);

        db.insert("archivos", null, content);
    }

    public void eliminarArchivo(String nombre) {
        String query = "DELETE FROM archivos where nombre = '"+ nombre +"';";
        db.execSQL(query);
    }

    public void eliminarTags(String nombre) {
        String query = "DELETE FROM tags where archivo = '"+ nombre +"';";
        db.execSQL(query);
    }
}