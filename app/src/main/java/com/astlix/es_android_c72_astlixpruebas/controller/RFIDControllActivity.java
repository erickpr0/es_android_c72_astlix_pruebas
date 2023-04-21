package com.astlix.es_android_c72_astlixpruebas.controller;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.astlix.es_android_c72_astlixpruebas.R;
import com.astlix.es_android_c72_astlixpruebas.enums.MenuStyle_Enum;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("unused")
public abstract class RFIDControllActivity extends AppCompatActivity implements RFIDControllActivityListener {
    private boolean dialogShowing = false;
    private boolean triggerPressed = false;
    public final AtomicBoolean isRfidReady = new AtomicBoolean(false);
    private AlertDialog dialog;
    private DiscreteSeekBar discreteSeekBar1;
    public boolean toggleBut = false;
    private Context context;
    public MenuStyle_Enum style_enum;

    public RfidThread myThread;
    public RFIDActivityHandler rfidActivityHandler;

    private ToneGenerator tonG;

    public boolean notBusy = true;

    public String tituloActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
            tituloActivity = bundle.getString("texto");
            int styleCode = bundle.getInt("styleCode", 1);
            style_enum = MenuStyle_Enum.button1;
            for (MenuStyle_Enum m : MenuStyle_Enum.values()) {
                if (m.getCode() == styleCode) {
                    style_enum = m;
                    break;
                }
            }


        Log.d("enum", "enum: "+ MenuStyle_Enum.button1);
        Log.d("enum", "enum: "+ style_enum);
        Log.d("enum", "enum: "+ style_enum.getButUp());

        getBundleValues(bundle);

        /*Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, style_enum.getColor()));*/

        context = this;

        tonG = new ToneGenerator(AudioManager.STREAM_ALARM, ToneGenerator.MAX_VOLUME);

        rfidActivityHandler = new RFIDActivityHandler();
        setAdapterHandler();
    }

    public void getBundleValues(Bundle bundle) {

    }

    public void setAdapterHandler() {

    }


    public void soundPowerChanged() {
        tonG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
    }

    public void soundTagRead() {
        tonG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
    }

    public void soundError() {
        tonG.startTone(ToneGenerator.TONE_CDMA_CONFIRM, 500);
    }

    public void soundNear() {
        tonG.startTone(ToneGenerator.TONE_CDMA_HIGH_L, 90);
    }

    public void soundMed() {
        tonG.startTone(ToneGenerator.TONE_CDMA_MED_L, 90);
    }

    public void soundFar() {
        tonG.startTone(ToneGenerator.TONE_CDMA_LOW_L, 90);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myThread = new RfidThread(rfidActivityHandler, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        myThread.finalizarRfid();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private int createDialogPotencia(int pot) {
        if (isRfidReady.get()) {
            if (!toggleBut && !triggerPressed) {
                if (!dialogShowing) {
                    dialogShowing = true;
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle(getResources().getString(R.string.potTit));
                    alertDialog.setMessage(getResources().getString(R.string.potMsg));
                    View dialogView = View.inflate(context, R.layout.seek_bar_layout, null);
                    alertDialog.setView(dialogView);
                    discreteSeekBar1 = dialogView.findViewById(R.id.discrete1);

                    discreteSeekBar1.setRippleColor(context.getColor(R.color.menu1p));
                    discreteSeekBar1.setScrubberColor(context.getColor(R.color.menu1p));
                    discreteSeekBar1.setThumbColor(context.getColor(R.color.menu1p), context.getColor(R.color.menutw));

                    discreteSeekBar1.setProgress(pot);
                    discreteSeekBar1.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
                        @Override
                        public int transform(int value) {
                            return value;
                        }
                    });
                    dialog = alertDialog.create();
                    dialog.setOnShowListener(arg0 -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.menutw)));
                    dialog.setOnCancelListener(dialogInterface -> {
                        dialogShowing = false;
                        int pot2 = discreteSeekBar1.getProgress();
                        onModificarPotencia(pot2);
                        myThread.setPotencia(pot2);
                    });
                    dialog.setOnKeyListener((dialogInterface, i, keyEvent) -> {
                        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN || keyEvent.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
                            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                                int pot1 = onObtenerPotencia();
                                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                                    if (pot1 - 1 > 0) {
                                        pot1--;
                                    }
                                } else {
                                    if (pot1 + 1 < 31) {
                                        pot1++;
                                    }
                                }
                                onModificarPotencia(pot1);
                                switch (createDialogPotencia(pot1)) {
                                    case -1:
                                    case 1:
                                        break;
                                    case 0:
                                        discreteSeekBar1.setProgress(pot1);
                                        break;
                                }
                            }
                            return true;
                        }
                        return false;
                    });

                    dialog.show();
                    return 1;
                }
                return 0;
            }
        }
        return -1;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("Key", keyCode + "");
        if (keyCode == 139 || keyCode == 294) {
            if (isRfidReady.get() && notBusy) {
                if (!toggleBut && !triggerPressed) {
                    triggerPressed = true;
                    toggleBut = true;
                    startLectura();
                }
            }
            return true;
        }
        if (keyCode == 82) {
            createDialogPotencia(onObtenerPotencia());
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                int pot = onObtenerPotencia();
                if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                    if (pot - 1 > 0) {
                        pot--;
                    }
                } else {
                    if (pot + 1 < 31) {
                        pot++;
                    }
                }
                onModificarPotencia(pot);
                switch (createDialogPotencia(pot)) {
                    case -1:
                    case 1:
                        break;
                    case 0:
                        discreteSeekBar1.setProgress(pot);
                        break;
                }
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 139 || keyCode == 294) {
            if (notBusy) {
                if (toggleBut) {
                    toggleBut = false;
                    stopLectura();
                }
                triggerPressed = false;
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);

    }

    public void startLectura() {
    }

    public void stopLectura() {
    }
}
