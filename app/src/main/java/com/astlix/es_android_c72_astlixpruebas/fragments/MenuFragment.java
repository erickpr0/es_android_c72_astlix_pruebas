package com.astlix.es_android_c72_astlixpruebas.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.astlix.es_android_c72_astlixpruebas.R;
import com.astlix.es_android_c72_astlixpruebas.enums.Menu_Enum;
import com.astlix.es_android_c72_astlixpruebas.icons.FontAwesome;

public class MenuFragment extends Fragment {
    int layoutid;
    boolean emptyFragment;

    public MenuFragment(int layoutid) {
        emptyFragment = false;
        this.layoutid = layoutid;
    }

    public MenuFragment() {
        emptyFragment = true;
        this.layoutid = R.layout.menu1_fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(layoutid, container, false);
        if (!emptyFragment) {
            LinearLayout parent = v.findViewById(R.id.parentContainer);

            int index = Integer.parseInt((String) v.getTag());
            for (Menu_Enum m : Menu_Enum.values()) {
                if (m.getIndex() == index) {
                    @SuppressLint("InflateParams") View h = inflater.inflate(R.layout.submenu_button, null, false);
                    LinearLayout ll = h.findViewById(R.id.submenu);
                    ll.setTag(m);
                    ll.setBackground(ContextCompat.getDrawable(getContext(), m.getStyle_enum().getButDrawable()));
                    FontAwesome icono = h.findViewById(R.id.icono);
                    icono.setText(getResources().getString(m.getIcono()));
                    TextView texto = h.findViewById(R.id.texto);
                    texto.setText(m.getTexto());
                    parent.addView(h);
                }
            }
        }
        return v;
    }
}
