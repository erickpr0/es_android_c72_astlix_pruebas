package com.astlix.es_android_c72_astlixpruebas.fragments;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.astlix.es_android_c72_astlixpruebas.R;
import com.astlix.es_android_c72_astlixpruebas.adapter.RfidLecturasAdapter;

import java.util.ArrayList;
import java.util.Locale;

import io.github.muddz.styleabletoast.StyleableToast;

public class RfidLecturasFragment extends Fragment {
    private TextView cantidades;
    private RfidLecturasAdapter rfidLecturasAdapter;
    private final int indice;
    private final Activity activity;

    public RfidLecturasFragment(int indice, Activity activity) {
        this.indice = indice;
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.rfid_fragment, container, false);
        ListView listView = v.findViewById(R.id.lecturas);
        LinearLayout general = v.findViewById(R.id.general);
        LinearLayout header = v.findViewById(R.id.header);
        ImageView exportar = v.findViewById(R.id.exportarTags);
        View separador = v.findViewById(R.id.separador);

        /* Menú 1 */
        if (indice == 1) {
            general.setBackground(ContextCompat.getDrawable(activity, R.drawable.menu1_rectangle));
            header.setBackground(ContextCompat.getDrawable(activity, R.drawable.menu1_rectangle));
            separador.setBackground(ContextCompat.getDrawable(activity, R.drawable.menu1_rectangle));
        }
        rfidLecturasAdapter = new RfidLecturasAdapter(getActivity());
        listView.setAdapter(rfidLecturasAdapter);
        cantidades = v.findViewById(R.id.cantidad);

        exportar.setOnClickListener(view -> {
            if (listaisEmpty()) {
                if (getContext() != null) {
                    new StyleableToast.Builder(getContext())
                            .text("La lista está vacía").length(Toast.LENGTH_LONG)
                            .textColor(ContextCompat.getColor(getContext(), R.color.white))
                            .backgroundColor(ContextCompat.getColor(getContext(), R.color.faltante))
                            .textBold()
                            .show();
                }
            } else {
                new AlertDialog.Builder(activity)
                        .setCancelable(false)
                        .setTitle("¿Desea exportar?")
                        .setPositiveButton("Exportar", (dialogInterface, i) -> {
                            System.out.println("onclick");
                            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                Log.d(TAG, "onClick: if");
                            } else {
                                System.out.println("VA A EXPORTAR");
                                exportarTgas();
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.dismiss()).show();
            }
        });
        return v;
    }

    public boolean addTags(ArrayList<String> epcs, ArrayList<String> rssi) {
        Animation animBounce = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);

        if (rfidLecturasAdapter.addEpc(epcs, rssi)) {
            cantidades.startAnimation(animBounce);
            cantidades.setText(String.format(Locale.getDefault(), "%d", rfidLecturasAdapter.getCount()));
            return true;
        }
        return false;
    }

    public void clearList() {
        rfidLecturasAdapter.clearList();
        cantidades.setText(String.format(Locale.getDefault(), "%d", rfidLecturasAdapter.getCount()));
    }

    boolean listaisEmpty() {
        return rfidLecturasAdapter.lecturaVacia();
    }

    public void exportarTgas() {
        System.out.println("TAGS");
        rfidLecturasAdapter.exportarTags();
    }
}
