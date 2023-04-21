package com.astlix.es_android_c72_astlixpruebas.controller;

public enum InstRFID {
    instance(0),
    iniciar(1),
    changePower(2),
    inventoryFilter(3),
    ciega(4),
    finalizar(5),
    none(6),
    tags(7),
    ready(8),
    grabar(9),
    busquedaFilter(10),
    inventario(11),
    search(12);

    private final int code;

    InstRFID(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
