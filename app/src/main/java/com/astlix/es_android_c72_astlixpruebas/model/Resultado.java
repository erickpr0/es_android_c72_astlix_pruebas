package com.astlix.es_android_c72_astlixpruebas.model;

public class Resultado {
    private int esperados;
    private int leidos;
    private boolean newtag;

    public Resultado(int esperados, int leidos, boolean newtag) {
        this.esperados = esperados;
        this.leidos = leidos;
        this.newtag = newtag;
    }

    public int getEsperados() {
        return esperados;
    }

    public void setEsperados(int esperados) {
        this.esperados = esperados;
    }

    public int getLeidos() {
        return leidos;
    }

    public void setLeidos(int leidos) {
        this.leidos = leidos;
    }

    public boolean isNewtag() {
        return newtag;
    }

    public void setNewtag(boolean newtag) {
        this.newtag = newtag;
    }
}
