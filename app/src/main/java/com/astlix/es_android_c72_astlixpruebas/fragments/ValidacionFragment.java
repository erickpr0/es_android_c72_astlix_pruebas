package com.astlix.es_android_c72_astlixpruebas.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.astlix.es_android_c72_astlixpruebas.R;
import com.astlix.es_android_c72_astlixpruebas.adapter.MatchAdapter;
import com.astlix.es_android_c72_astlixpruebas.adapter.SpinnerArchivosAdapter;
import com.astlix.es_android_c72_astlixpruebas.database.InterfazBD;
import com.astlix.es_android_c72_astlixpruebas.model.Archivo;
import com.astlix.es_android_c72_astlixpruebas.model.LecturasRfidCiega;
import com.astlix.es_android_c72_astlixpruebas.model.Resultado;

import java.util.ArrayList;

import io.github.muddz.styleabletoast.StyleableToast;

public class ValidacionFragment extends Fragment implements AdapterView.OnItemClickListener {
    ListView listView;
    InterfazBD interfazBD;
    ArrayList<LecturasRfidCiega> tags;
    MatchAdapter matchAdapter;
    TextView esperados;
    TextView leidos;
    Archivo archivo;

    public ValidacionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.validacion_fragment, container, false);
        esperados = v.findViewById(R.id.esperados);
        leidos = v.findViewById(R.id.leidos);

        interfazBD = new InterfazBD(getContext());
        listView = v.findViewById(R.id.listValidacion);

        showSpinner();

        return v;
    }

    private void showSpinner() {
        interfazBD = new InterfazBD(getContext());
        LayoutInflater li = LayoutInflater.from(ValidacionFragment.this.getContext());
        View promptsView = li.inflate(R.layout.archivos_prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ValidacionFragment.this.getContext());
        alertDialogBuilder.setView(promptsView);

        ArrayList<Archivo> archivos = interfazBD.obtenerArchivos();
        if (!archivos.isEmpty()){
            Spinner spinnerArchivo = promptsView.findViewById(R.id.spinnerArchivos);
            SpinnerArchivosAdapter spinnerArchivosAdapter = new SpinnerArchivosAdapter(getActivity(), archivos);
            spinnerArchivo.setAdapter(spinnerArchivosAdapter);

            alertDialogBuilder.setPositiveButton("Confirmar", (dialog, which) -> {
                archivo = (Archivo) spinnerArchivo.getSelectedItem();
                tags = interfazBD.obtenerTags(archivo.getNombre());
                for (int i = 0; i < tags.size(); i++) {
                    System.out.println(tags.get(i).getEpc());
                }
                matchAdapter = new MatchAdapter(getActivity(), tags);
                listView.setAdapter(matchAdapter);
                esperados.setText(String.valueOf(tags.size()));
            });

            alertDialogBuilder.setNegativeButton("Cancelar", (dialog, which) -> {
                dialog.dismiss();
                requireActivity().finish();
            });
            alertDialogBuilder.setCancelable(false);

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireActivity(), R.color.sobrante));
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireActivity(), R.color.bien));
        } else {
            new StyleableToast.Builder(requireActivity())
                    .text("No hay archivos")
                    .textColor(ContextCompat.getColor(requireActivity(), R.color.white))
                    .textBold()
                    .backgroundColor(ContextCompat.getColor(requireActivity(), R.color.faltante))
                    .length(Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void clearList() {
        matchAdapter.limpiar();
    }

    public Resultado addTags(ArrayList<String> epcs, ArrayList<String> rssis) {
        Animation animBounce = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);
        Resultado resultado = matchAdapter.addEpc(epcs, rssis);

        esperados.setText(String.valueOf(tags.size() - resultado.getLeidos()));

        if (tags.size() > resultado.getLeidos()) {
            esperados.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.faltante));
            leidos.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.faltante));
        }

        if (tags.size() == resultado.getLeidos()) {
            esperados.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bien));
            leidos.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bien));
        }

        leidos.setText(String.valueOf(resultado.getLeidos()));

        if (resultado.isNewtag()) {
            leidos.startAnimation(animBounce);
        }
        return resultado;
        //return matchAdapter.addEpc(epcs, rssis);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Archivo archivo = (Archivo) parent.getItemAtPosition(position);
        tags = interfazBD.obtenerTags(archivo.getNombre());
        matchAdapter = new MatchAdapter(getActivity(), tags);
        listView.setAdapter(matchAdapter);

        esperados.setText(String.valueOf(tags.size()));
    }
}
