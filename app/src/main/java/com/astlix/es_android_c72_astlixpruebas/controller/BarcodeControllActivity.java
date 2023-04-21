package com.astlix.es_android_c72_astlixpruebas.controller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.astlix.es_android_c72_astlixpruebas.R;
import com.astlix.es_android_c72_astlixpruebas.async.AsyncInitTaskBC;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.util.concurrent.atomic.AtomicBoolean;

public class BarcodeControllActivity extends GenericActivity {
    private Barcode2DWithSoft mBarcode = null;
    public boolean barcodeReady = false;
    private final AtomicBoolean barcodeReading = new AtomicBoolean(true);

    private ToneGenerator tonG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBarcode = null;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        tonG = new ToneGenerator(AudioManager.STREAM_ALARM, ToneGenerator.MAX_VOLUME);

        if (mBarcode == null){
            new AsyncInitTaskBC(this).execute();
        }

    }

    public void soundTagRead() {
        tonG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
    }

    public void soundError() {
        tonG.startTone(ToneGenerator.TONE_CDMA_CONFIRM, 500);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mBarcode != null) {
            mBarcode.stopScan();
            mBarcode.close();
        }
        super.onDestroy();
    }

    public void closeBarcode() {
        if (mBarcode != null) {
            mBarcode.close();
            mBarcode = null;
        }
    }

    public void restartBarcode() {
        new AsyncInitTaskBC(this).execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println(keyCode);
        if (keyCode == 291 || keyCode == 293 || keyCode == 294) {
            startLectura();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 291 || keyCode == 293 || keyCode == 294) {
            stopLectura();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void ScanBarcode() {
        mBarcode.scan();
        mBarcode.setScanCallback(ScanBack);
    }

    private void StopScan() {
        mBarcode.stopScan();
    }

    public Barcode2DWithSoft.ScanCallback ScanBack = new Barcode2DWithSoft.ScanCallback() {
        @Override
        public void onScanComplete(int i, int length, byte[] bytes) {
            if (length > 0) {
                System.out.println("length mayor a 0");
                String barCode = new String(bytes, 0, length);
                onBarcodeRead(barCode);
                tonG.startTone(ToneGenerator.TONE_PROP_ACK, 100);
            } else {
                tonG.startTone(ToneGenerator.TONE_CDMA_CONFIRM, 500);
            }
            if (barcodeReading.get()) {
                barcodeReading.set(false);
                onBarcodeStarted(false);
                StopScan();
            }
        }
    };

    public void onBarcodeRead(String barCode) {
    }

    public void onBarcodeStarted(boolean b) {
    }

    public void startLectura() {
        if (mBarcode != null && barcodeReady) {
            if (!barcodeReading.get()) {
                barcodeReading.set(true);
                onBarcodeStarted(true);
                ScanBarcode();
            }
        }
    }

    public void stopLectura() {
        if (mBarcode != null && barcodeReady) {
            if (barcodeReading.get()) {
                barcodeReading.set(false);
                onBarcodeStarted(false);
                StopScan();
            }
        }
    }

    public void finalizarInit(Barcode2DWithSoft mBarcode) {
        this.mBarcode = mBarcode;
        barcodeReady = true;
        soundTagRead();
    }

    public void errordeBarcode() {
        Toast.makeText(this, getResources().getString(R.string.errInitBC), Toast.LENGTH_SHORT).show();
        soundError();
    }
}
