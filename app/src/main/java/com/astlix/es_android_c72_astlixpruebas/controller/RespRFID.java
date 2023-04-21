package com.astlix.es_android_c72_astlixpruebas.controller;

public enum RespRFID {
    error(-1),
    neutro(0),
    success(1);

    private final int code;

    RespRFID(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
