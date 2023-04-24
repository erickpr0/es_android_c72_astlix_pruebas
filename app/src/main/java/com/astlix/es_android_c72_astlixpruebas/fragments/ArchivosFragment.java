package com.astlix.es_android_c72_astlixpruebas.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.astlix.es_android_c72_astlixpruebas.R;
import com.astlix.es_android_c72_astlixpruebas.adapter.SpinnerArchivosAdapter;
import com.astlix.es_android_c72_astlixpruebas.database.InterfazBD;
import com.astlix.es_android_c72_astlixpruebas.model.Archivo;

import java.util.ArrayList;

import io.github.muddz.styleabletoast.StyleableToast;

public class ArchivosFragment extends Fragment {
    ListView listView;
    SpinnerArchivosAdapter archivosAdapter;
    InterfazBD interfazBD;
    ArrayList<Archivo> archivos;

    public ArchivosFragment() {
        super(R.layout.archivos_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        interfazBD = new InterfazBD(requireActivity());
        listView = view.findViewById(R.id.listArchivos);
        archivos = interfazBD.obtenerArchivos();
        archivosAdapter = new SpinnerArchivosAdapter(requireActivity(), archivos);
        listView.setAdapter(archivosAdapter);

        listView.setOnItemLongClickListener((parent, view1, position, id) -> {
            Archivo archivo = (Archivo) parent.getItemAtPosition(position);
            AlertDialog alertDialog = new AlertDialog.Builder(requireActivity())
                    .setMessage("¿Eliminar Archivo?")
                    .setCancelable(false)
                    .setTitle("Eliminar")
                    .setPositiveButton("Eliminar", (dialog, which) ->  {
                        interfazBD.eliminarArchivo(archivo.getNombre());
                        interfazBD.eliminarTags(archivo.getNombre());
                        new StyleableToast.Builder(requireActivity())
                                .text("Archivo eliminado")
                                .backgroundColor(ContextCompat.getColor(requireActivity(), R.color.bien))
                                .textColor(ContextCompat.getColor(requireActivity(), R.color.white))
                                .textBold()
                                .length(Toast.LENGTH_LONG)
                                .show();
                        requireActivity().finish();
                        requireActivity().overridePendingTransition(0, 0);
                        startActivity(requireActivity().getIntent());
                        requireActivity().overridePendingTransition(0, 0);

                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                    .create();

            alertDialog.show();
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireActivity(), R.color.sobrante));
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireActivity(), R.color.bien));

            return true;
        });
    }
}
