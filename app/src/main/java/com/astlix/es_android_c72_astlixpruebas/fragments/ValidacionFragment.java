package com.astlix.es_android_c72_astlixpruebas.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.astlix.es_android_c72_astlixpruebas.R;
import com.astlix.es_android_c72_astlixpruebas.adapter.MatchAdapter;
import com.astlix.es_android_c72_astlixpruebas.database.InterfazBD;
import com.astlix.es_android_c72_astlixpruebas.model.LecturasRfidCiega;
import com.astlix.es_android_c72_astlixpruebas.model.Resultado;

import java.util.ArrayList;

public class ValidacionFragment extends Fragment {
    ListView listView;
    InterfazBD interfazBD;
    ArrayList<LecturasRfidCiega> tags;
    MatchAdapter matchAdapter;
    TextView esperados;
    TextView leidos;

    public ValidacionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.validacion_fragment, container, false);

        esperados = v.findViewById(R.id.esperados);
        leidos = v.findViewById(R.id.leidos);

        interfazBD = new InterfazBD(getContext());
        tags = interfazBD.obtenerTags();
        System.out.println(tags.size());
        listView = v.findViewById(R.id.listValidacion);
        matchAdapter = new MatchAdapter(getActivity(), tags);
        listView.setAdapter(matchAdapter);

        esperados.setText(String.valueOf(tags.size()));

        return v;
    }

    public void clearList() {
        matchAdapter.limpiar();
    }

    public Resultado addTags(ArrayList<String> epcs, ArrayList<String> rssis) {
        Animation animBounce = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);
        Resultado resultado = matchAdapter.addEpc(epcs, rssis);

        esperados.setText(String.valueOf(tags.size() - resultado.getLeidos()));

        if (tags.size() > resultado.getLeidos()){
            esperados.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.faltante));
            leidos.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.faltante));
        }

        if (tags.size() == resultado.getLeidos()){
            esperados.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bien));
            leidos.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bien));
        }

        leidos.setText(String.valueOf(resultado.getLeidos()));

        if (resultado.isNewtag()){
            leidos.startAnimation(animBounce);
        }
        return resultado;
        //return matchAdapter.addEpc(epcs, rssis);
    }
}
