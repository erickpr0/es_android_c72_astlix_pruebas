package com.astlix.es_android_c72_astlixpruebas.gui;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.astlix.es_android_c72_astlixpruebas.R;
import com.astlix.es_android_c72_astlixpruebas.controller.BCRead;
import com.astlix.es_android_c72_astlixpruebas.icons.FontAwesome;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import io.github.muddz.styleabletoast.StyleableToast;


public class Menu3ActivityBarCode extends AppCompatActivity {
    private Barcode2DWithSoft mBarcode = null;
    private boolean toggleBut = false;
    private LinearLayout butToggle;
    private static ArrayList<BCRead> bcReadArr = null;
    private MySimpleArrayAdapterBC mAdapter1;
    private RelativeLayout progreso;
    private FontAwesome slashIcon;
    private TextView textoInicio;
    private boolean keypressed = false;
    private RelativeLayout titulo;
    private ToneGenerator tonG;
    private LinearLayout btnExportar;
    private Context context;
    boolean vacio = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu3_activity_barcode);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.menu3p));

        mBarcode = null;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        /*if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        }*/
        context = this;
        titulo = findViewById(R.id.menu3TituloBarCode);
        butToggle = findViewById(R.id.menu3IniciarBarCode);
        progreso = findViewById(R.id.menu3progress);
        Log.d("Barcode", "progreso");
        textoInicio = findViewById(R.id.menu3TextoIniciarBarCode);
        slashIcon = findViewById(R.id.menu3SlashBarCode);
        bcReadArr = new ArrayList<>();
        ListView mList = findViewById(R.id.menu3ListBarCode);
        mAdapter1 = new MySimpleArrayAdapterBC(this, bcReadArr);
        mList.setAdapter(mAdapter1);


        tonG = new ToneGenerator(AudioManager.STREAM_ALARM, ToneGenerator.MAX_VOLUME);

        if (mBarcode == null) {
            mBarcode = Barcode2DWithSoft.getInstance();
            new InitTaskBC(this, titulo, mBarcode, progreso).execute();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //progreso.setVisibility(RelativeLayout.VISIBLE);
        titulo.setElevation((float) 0.0);
    }


    @Override
    protected void onDestroy() {
        if (mBarcode != null) {
            mBarcode.stopScan();
            mBarcode.close();
        }
        super.onDestroy();
    }


    public void SalirBarCodeHerramientas(View view) {
        Menu3ActivityBarCode.this.finish();
    }


    public static class InitTaskBC extends AsyncTask<String, Integer, Boolean> {
        private final WeakReference<Context> context;
        private final WeakReference<Barcode2DWithSoft> mBarcode;
        private final WeakReference<RelativeLayout> progreso;
        private final WeakReference<RelativeLayout> titulo;

        @SuppressWarnings("deprecation")
        InitTaskBC(Context context, RelativeLayout titulo, Barcode2DWithSoft mBarcode, RelativeLayout progreso) {
            this.context = new WeakReference<>(context);
            this.mBarcode = new WeakReference<>(mBarcode);
            this.progreso = new WeakReference<>(progreso);
            this.titulo = new WeakReference<>(titulo);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            System.out.println("do in background");
            boolean result = false;
            if (mBarcode.get() != null) {
                result = mBarcode.get().open(context.get());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                mBarcode.get().setParameter(324, 1);
                mBarcode.get().setParameter(300, 0); //Snapshot Aiming
                mBarcode.get().setParameter(361, 0); //Image Capture Illuminaton

                // interleaved 2 of 5
                mBarcode.get().setParameter(6, 1);
                mBarcode.get().setParameter(22, 0);
                mBarcode.get().setParameter(23, 55);
            } else {
                //Toast.makeText(context.get(), context.get().getResources().getString(R.string.errInitBC), Toast.LENGTH_SHORT).show();
            }
            progreso.get().setVisibility(RelativeLayout.GONE);
            titulo.get().setElevation((float) 54.0);
        }
    }

    private void StopScan() {
        if (mBarcode != null) {
            progreso.setVisibility(RelativeLayout.GONE);
            titulo.setElevation((float) 54.0);
            toggleBut = false;
            mBarcode.stopScan();
            final int sdk = Build.VERSION.SDK_INT;
            if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
                butToggle.setBackground(ContextCompat.getDrawable(this, R.drawable.menu3_button));
            }
            textoInicio.setText(getResources().getString(R.string.butInit));
            slashIcon.setVisibility(View.GONE);
        }
    }

    private void ScanBarcode() {
        if (mBarcode != null) {
            progreso.setVisibility(RelativeLayout.VISIBLE);
            titulo.setElevation((float) 0.0);
            toggleBut = true;
            mBarcode.scan();
            mBarcode.setScanCallback(ScanBack);
            final int sdk = Build.VERSION.SDK_INT;
            if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
                butToggle.setBackground(ContextCompat.getDrawable(this, R.drawable.menu3_button_alt));
            }
            textoInicio.setText(getResources().getString(R.string.butFin));
            slashIcon.setVisibility(View.VISIBLE);
        }
        System.out.println("scanBarcode");
    }

    public void StartStopLecturaBC(View view) {
        System.out.println("Desde menu 3");
        if (!toggleBut) {
            ScanBarcode();
        } else {
            StopScan();
        }
    }

    public void LimpiarLecturaBC(View view) {
        bcReadArr.clear();
        mAdapter1.notifyDataSetChanged();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //System.out.println("on key down de menu3");
        if (!toggleBut) {
            Log.d("TECLA", "" + keyCode);
            if (keyCode == 291 || keyCode == 294 || keyCode == 293) {
                keypressed = true;
                ScanBarcode();
                return true;
            } else {
                super.onBackPressed();
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (toggleBut) {
            if (keyCode == 291 || keyCode == 294 || keyCode == 293) {
                keypressed = false;
                StopScan();
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    public Barcode2DWithSoft.ScanCallback ScanBack = new Barcode2DWithSoft.ScanCallback() {
        @Override
        public void onScanComplete(int i, int length, byte[] bytes) {
            System.out.println("onScan");
            if (length > 0) {
                String barCode = new String(bytes, 0, length);

                boolean found = false;
                for (BCRead bc : bcReadArr) {
                    if (bc.getBc().compareTo(barCode) == 0) {
                        found = true;

                        new StyleableToast
                                .Builder(context)
                                .text("CÓDIGO REPETIDO!")
                                .textColor(Color.WHITE)
                                .backgroundColor(Color.RED)
                                .textBold()
                                .show();
                        tonG.startTone(ToneGenerator.TONE_SUP_ERROR, 100);
                        //new AlertDialog.Builder(context).setTitle("Código repetido").setPositiveButton("Ok" , null).show();
                        break;
                    }
                }
                if (!found) {
                    bcReadArr.add(new BCRead(barCode));
                    mAdapter1.notifyDataSetChanged();
                    tonG.startTone(ToneGenerator.TONE_PROP_ACK, 100);
                }

                System.out.println("BcReadArr SIZE: " + bcReadArr.size());

            } else {
                tonG.startTone(ToneGenerator.TONE_CDMA_CONFIRM, 500);
            }
            if (!keypressed) StopScan();
        }
    };

    public class MySimpleArrayAdapterBC extends BaseAdapter {

        private final ArrayList<BCRead> barCodes;
        private ArrayList<BCRead> visible;

        public MySimpleArrayAdapterBC(Context context, ArrayList<BCRead> bcRead) {
            this.barCodes = bcRead;
        }

        private class ViewHolder {
            TextView bcText;
            LinearLayout container;
            int[] colors = context.getResources().getIntArray(R.array.adapter);
        }

        @Override
        public int getCount() {
            return barCodes.size();
        }

        @Override
        public Object getItem(int position) {
            return barCodes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View rowView = convertView;
            ViewHolder viewHolder;
            if (rowView == null) {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.list_barcode_adapter, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.bcText = rowView.findViewById(R.id.bcText);
                viewHolder.container = rowView.findViewById(R.id.item);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), viewHolder.colors[0], viewHolder.colors[1]);
            colorAnimator.setDuration(2700);
            colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                    if (position == barCodes.size() - 1){
                        viewHolder.container.setBackgroundColor((int) animation.getAnimatedValue());
                        viewHolder.bcText.setText(barCodes.get(position).getBc());
                    }
                }
            });
            colorAnimator.start();

            return rowView;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
