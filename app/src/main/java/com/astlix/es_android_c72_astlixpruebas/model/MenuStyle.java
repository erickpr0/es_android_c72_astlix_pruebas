package com.astlix.es_android_c72_astlixpruebas.model;

import com.astlix.es_android_c72_astlixpruebas.enums.MenuStyle_Enum;

public class MenuStyle {
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
    private final String whsCode;

    public MenuStyle(String className, String texto, int icono, int id, int dir, int index, boolean verify, MenuStyle_Enum style_enum, Permisos permisos, int colortxt, String whsCode) {
        this.className = className;
        this.texto = texto;
        this.icono = icono;
        this.id = id;
        this.dir = dir;
        this.index = index;
        this.verify = verify;
        this.style_enum = style_enum;
        this.permisos = permisos;
        this.colortxt = colortxt;
        this.whsCode = whsCode;
    }

    public String getWhsCode() {
        return whsCode;
    }

    public String getClassName() {
        return className;
    }

    public String getTexto() {
        return texto;
    }

    public int getIcono() {
        return icono;
    }

    public int getId() {
        return id;
    }

    public int getDir() {
        return dir;
    }

    public int getIndex() {
        return index;
    }

    public boolean isVerify() {
        return verify;
    }

    public MenuStyle_Enum getStyle_enum() {
        return style_enum;
    }

    public Permisos getPermisos() {
        return permisos;
    }

    public int getColortxt() {
        return colortxt;
    }

    public int checkPermisos(int rol) {
        return this.permisos.checkPermisos(rol);
    }
}
