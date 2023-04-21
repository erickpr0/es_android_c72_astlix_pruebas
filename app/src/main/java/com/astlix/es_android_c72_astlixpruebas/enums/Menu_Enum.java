package com.astlix.es_android_c72_astlixpruebas.enums;

import com.astlix.es_android_c72_astlixpruebas.R;
import com.astlix.es_android_c72_astlixpruebas.model.Permisos;

public enum Menu_Enum {
    inicio(-1, "START", R.string.info_circle, "Start", 0, 0, false, MenuStyle_Enum.start, Permisos.todos, R.color.menu1to),
    validacion(0, "Validación", R.string.check_square, "Menu1ActivityValidacion", 13, 1, false, MenuStyle_Enum.button1, Permisos.entradas, R.color.menu1to),
    //busqueda(0, "Búsqueda", R.string.search, "Menu2ActivityBusqueda", 13, 1, false, MenuStyle_Enum.button1, Permisos.entradas, R.color.menu1to),
    rfid(2, "RFID", R.string.wifi, "Menu3RfidActivity", 0, 0, false, MenuStyle_Enum.button3, Permisos.todos, R.color.menu3to),
    barcode(2, "Código de Barras", R.string.barcode, "Menu3ActivityBarCode", 0, 0, false, MenuStyle_Enum.button3, Permisos.todos, R.color.menu3to),
    //server(3, "Servidor", R.string.cloud, "Menu4ConfigServidor", 0, 0, false, MenuStyle_Enum.button4, Permisos.administrador, R.color.menu4to),
    acerca(3, "Acerca", R.string.info_circle, "Menu4ActivityAcerca", 0, 0, false, MenuStyle_Enum.button4, Permisos.administrador, R.color.menu4to);

    private final String className;
    private final String texto;
    private final int icono;
    private final int id;
    private final int dir;
    private final int index;
    private final boolean verify;
    private final MenuStyle_Enum style_enum;
    private final Permisos permisos;
    private final int colortxt;

    Menu_Enum(int index, String texto, int icono, String className, int id, int dir, boolean verify, MenuStyle_Enum style_enum, Permisos permisos, int color) {
        this.index = index;
        this.icono = icono;
        this.texto = texto;
        this.className = className;
        this.id = id;
        this.dir = dir;
        this.verify = verify;
        this.style_enum = style_enum;
        this.permisos = permisos;
        this.colortxt = color;
    }

    public int getColortxt() {
        return colortxt;
    }

    public int checkPermisos(int rol) {
        return this.permisos.checkPermisos(rol);
    }

    public MenuStyle_Enum getStyle_enum() {
        return style_enum;
    }

    public boolean isVerify() {
        return verify;
    }

    public int getIndex() {
        return index;
    }

    public int getDir() {
        return dir;
    }

    public int getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public int getIcono() {
        return icono;
    }

    public String getTexto() {
        return this.texto;
    }

    public Permisos getPermisos() {
        return this.permisos;
    }
}
