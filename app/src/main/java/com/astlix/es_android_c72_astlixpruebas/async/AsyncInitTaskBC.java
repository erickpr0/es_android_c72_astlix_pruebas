package com.astlix.es_android_c72_astlixpruebas.async;

import android.app.Activity;
import android.os.AsyncTask;

import com.astlix.es_android_c72_astlixpruebas.controller.BarcodeControllActivity;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.lang.ref.WeakReference;

@SuppressWarnings("deprecation")
public class AsyncInitTaskBC extends AsyncTask<String, Integer, Boolean> {
    private final WeakReference<Activity> activityWeakReference;
    private Barcode2DWithSoft mBarcode;

    @SuppressWarnings("deprecation")
    public AsyncInitTaskBC(Activity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        boolean result = false;
        mBarcode = Barcode2DWithSoft.getInstance();
        if (mBarcode != null) {
            result = mBarcode.open(activityWeakReference.get());
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean) {
            mBarcode.setParameter(324, 1);
            mBarcode.setParameter(300, 0); //Snapshot Aiming
            mBarcode.setParameter(361, 0); //Image Capture Illuminaton

            // interleaved 2 of 5
            mBarcode.setParameter(6, 1);
            mBarcode.setParameter(22, 0);
            mBarcode.setParameter(23, 55);
            ((BarcodeControllActivity) activityWeakReference.get()).finalizarInit(mBarcode);
        } else {
            ((BarcodeControllActivity) activityWeakReference.get()).errordeBarcode();
        }
    }
}
