package com.astlix.es_android_c72_astlixpruebas.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConexionBd extends SQLiteOpenHelper {
    public ConexionBd(Context context) {
        super(context, "configura.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String cadena = "create table if not exists configuracion (_id integer primary key autoincrement, ip text not null, base text not null, usuario text not null, contrasena text not null, potencia integer not null);";
        String tags = "create table if not exists tags (_id integer primary key autoincrement, tag text not null,  rssi integer, cantidad integer, ascii text, archivo text);";
        String archivos = "create table if not exists archivos (_id integer primary key autoincrement, nombre text not null);";

        db.execSQL(cadena);
        db.execSQL(tags);
        db.execSQL(archivos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String cadenaUpdate = "drop table if exists configuracion;";
        String tagsUpdate = "drop table if exists tags;";
        String archivosUpdate = "drop table if exists archivos;";

        db.execSQL(cadenaUpdate);
        db.execSQL(tagsUpdate);
        db.execSQL(archivosUpdate);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.setVersion(oldVersion);
    }
}