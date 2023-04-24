package com.astlix.es_android_c72_astlixpruebas.gui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.astlix.es_android_c72_astlixpruebas.R;
import com.astlix.es_android_c72_astlixpruebas.controller.RFIDActivityHandlerListener;
import com.astlix.es_android_c72_astlixpruebas.controller.RFIDControllActivity;
import com.astlix.es_android_c72_astlixpruebas.database.InterfazBD;
import com.astlix.es_android_c72_astlixpruebas.fragments.ControlsFragment;
import com.astlix.es_android_c72_astlixpruebas.fragments.ControlsFragmentListener;
import com.astlix.es_android_c72_astlixpruebas.fragments.HeaderFragment;
import com.astlix.es_android_c72_astlixpruebas.fragments.StatusbarFragment;
import com.astlix.es_android_c72_astlixpruebas.fragments.ValidacionFragment;
import com.astlix.es_android_c72_astlixpruebas.model.Resultado;

import java.util.ArrayList;

import io.github.muddz.styleabletoast.StyleableToast;

public class Menu1ActivityValidacion extends RFIDControllActivity {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    ValidacionFragment validacionFragment;
    ControlsFragment controlsFragment;
    HeaderFragment headerFragment;
    StatusbarFragment statusbarFragment;
    RelativeLayout progreso;
    InterfazBD interfazBD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic);

        interfazBD = new InterfazBD(this);

        progreso = findViewById(R.id.progreso);

        headerFragment = new HeaderFragment(tituloActivity, style_enum.getColor(), style_enum.getColorTxt());
        headerFragment.addHeaderFragmentListner(this::onBackPressed);

        controlsFragment = new ControlsFragment(style_enum, getResources().getString(R.string.broom), getResources().getString(R.string.butClean));
        controlsFragment.addControlsFragmentAdapter(new ControlsFragmentListener() {
            @Override
            public void lecturarToggleClicked() {
                if (isRfidReady.get()) {
                    if (!toggleBut) {
                        toggleBut = true;
                        startLectura();
                    } else {
                        stopLectura();
                        toggleBut = false;
                    }
                }
            }

            @Override
            public void button2Clicked() {
                if (validacionFragment != null) {
                    validacionFragment.clearList();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                    System.out.println("LIMPIAR");
                }
            }

            @Override
            public void button2ClickedSecond() {

            }
        });

        statusbarFragment = new StatusbarFragment(style_enum.getButUp());
        validacionFragment = new ValidacionFragment();

        if (transaction.isEmpty()) {
            transaction.add(R.id.headerFragment, headerFragment);
            if (!interfazBD.obtenerArchivos().isEmpty()){
                transaction.add(R.id.contenidoFragment, validacionFragment);
                transaction.add(R.id.controlsFragment, controlsFragment);
                transaction.add(R.id.statusbarFragment, statusbarFragment).commit();
            } else {
                new StyleableToast.Builder(this)
                        .text("No hay archivos")
                        .textColor(ContextCompat.getColor(this, R.color.white))
                        .textBold()
                        .backgroundColor(ContextCompat.getColor(this, R.color.faltante))
                        .length(Toast.LENGTH_LONG)
                        .show();
                finish();
            }
        } else {
            transaction = null;
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.headerFragment, headerFragment);
            transaction.replace(R.id.contenidoFragment, validacionFragment);
            transaction.replace(R.id.controlsFragment, controlsFragment);
            transaction.replace(R.id.statusbarFragment, statusbarFragment).commit();
        }
    }

    @Override
    public int onObtenerPotencia() {
        return 0;
    }

    @Override
    public void onModificarPotencia(int pot) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        progreso.setVisibility(RelativeLayout.VISIBLE);
        headerFragment.elevateMainBlock((float) 0.0);
    }

    @Override
    public void startLectura() {
        myThread.startLecturaCiega();
        controlsFragment.setButton1PRessed(true);
    }

    @Override
    public void stopLectura() {
        myThread.stopReading();
        controlsFragment.setButton1PRessed(false);
    }

    @Override
    public void setAdapterHandler() {
        rfidActivityHandler.addRFIDActivityHandlerAdapter(new RFIDActivityHandlerListener() {
            @Override
            public void setError(String msg) {
                headerFragment.elevateMainBlock((float) 54.0);
                isRfidReady.set(false);
                progreso.setVisibility(View.GONE);
                statusbarFragment.setStatusIcon(-1);
                statusbarFragment.setTextMessage(msg);
            }

            @Override
            public void iniciarSuccess() {
                myThread.setPotencia(interfazBD.obtenerPotencia());
                statusbarFragment.setStatusIcon(1);
                statusbarFragment.setTextMessage("RFID Conectado");
            }

            @Override
            public void instanceSuccess() {
                headerFragment.elevateMainBlock((float) 0.0);
                progreso.setVisibility(View.VISIBLE);
                myThread.start();
            }

            @Override
            public void changePowerSuccess() {
                isRfidReady.set(true);
                headerFragment.elevateMainBlock((float) 54.0);
                progreso.setVisibility(View.GONE);
                soundPowerChanged();
            }

            @Override
            public void lecturaIniciada() {
                statusbarFragment.setTextMessage("RFID leyendo");
            }

            @Override
            public void lecturaFinalizada() {
                statusbarFragment.setTextMessage("RFID Conectado");
            }

            @Override
            public void threadReady() {
                myThread.iniciarRfid();
            }

            @Override
            public void finalizar() {
                isRfidReady.set(false);
                myThread.myThreadHandler.getLooper().quit();
                statusbarFragment.setTextMessage("Conectando a RFID");
                statusbarFragment.setStatusIcon(0);
            }

            @Override
            public void tagsLecturaInventario(ArrayList<String> stringArrayList) {
            }

            @Override
            public void tagsLecturaCiega(ArrayList<String> epcs, ArrayList<String> rssis) {
                if (validacionFragment != null) {
                    Resultado resultado = validacionFragment.addTags(epcs, rssis);
                    if (resultado.isNewtag()){
                        soundTagRead();
                    }
                    /*if (validacionFragment.addTags(epcs, rssis)) {
                        soundTagRead();
                    }*/
                }
            }

            @Override
            public void lecturaInventario(ArrayList<String> stringArrayList, ArrayList<Integer> integerArrayList) {

            }

            @Override
            public void grabarSuccess() {
            }

            @Override
            public void enviarRssi(double aDouble) {

            }

            @Override
            public void buscar(double rssi) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!toggleBut) {
            if (keyCode == 139 || keyCode == 280 || keyCode == 293) {
                toggleBut = true;
                startLectura();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (toggleBut) {
            if (keyCode == 139 || keyCode == 280 || keyCode == 293) {
                toggleBut = false;
                stopLectura();
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
