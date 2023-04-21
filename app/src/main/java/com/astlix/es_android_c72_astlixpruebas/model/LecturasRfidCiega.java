package com.astlix.es_android_c72_astlixpruebas.model;

public class LecturasRfidCiega {
    private final String epc;
    private String rssi;
    private int cant;
    private String ascii;
    private boolean correcto;

    public LecturasRfidCiega(String epc, String rssi) {
        this.epc = epc;
        this.rssi = rssi;
        this.cant = 1;
    }

    public LecturasRfidCiega(String epc, String rssi, String ascii) {
        this.epc = epc;
        this.rssi = rssi;
        this.cant = 1;
        this.ascii = ascii;
    }

    public String getAscii() {
        return ascii;
    }

    public void setAscii(String ascii) {
        this.ascii = ascii;
    }

    public int getCant() {
        return cant;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
        this.cant++;
    }

    public String getEpc() {
        return epc;
    }

    public int compareTo(String epc) {
        return this.epc.compareTo(epc);
    }

    public void setCorrecto(boolean correcto) {
        this.correcto = correcto;
    }

    public boolean isCorrecto() {
        return correcto;
    }
}
