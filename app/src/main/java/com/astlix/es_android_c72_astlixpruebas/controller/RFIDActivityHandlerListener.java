package com.astlix.es_android_c72_astlixpruebas.controller;

import java.util.ArrayList;

public interface RFIDActivityHandlerListener {
    void setError(String msg);

    void iniciarSuccess();

    void instanceSuccess();

    void changePowerSuccess();

    void lecturaIniciada();

    void lecturaFinalizada();

    void threadReady();

    void finalizar();

    void tagsLecturaInventario(ArrayList<String> stringArrayList);

    void tagsLecturaCiega(ArrayList<String> stringArrayList, ArrayList<String> stringArrayList1);

    void lecturaInventario(ArrayList<String> stringArrayList, ArrayList<Integer> integerArrayList);

    void grabarSuccess();

    void enviarRssi(double aDouble);

    void buscar(double rssi);
}
