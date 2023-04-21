package com.astlix.es_android_c72_astlixpruebas.gui;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.fragment.app.FragmentTransaction;

import com.astlix.es_android_c72_astlixpruebas.R;
import com.astlix.es_android_c72_astlixpruebas.fragments.AboutFragment;
import com.astlix.es_android_c72_astlixpruebas.fragments.HeaderFragment;

public class Menu4ActivityAcerca extends NoneRfidActivity {

    private FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_2);

        HeaderFragment headerFragment = new HeaderFragment(tituloActivity,
                style_enum.getColor(),
                style_enum.getColorTxt());
        headerFragment.addHeaderFragmentListner(this::onBackPressed);

        AboutFragment aboutFragment = new AboutFragment();

        RelativeLayout progreso = findViewById(R.id.progreso);

        if(transaction.isEmpty()) {
            transaction.add(R.id.headerFragment, headerFragment);
            transaction.add(R.id.contenidoFragment, aboutFragment).commit();
        } else {
            transaction = null;
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.headerFragment, headerFragment);
            transaction.replace(R.id.contenidoFragment, aboutFragment).commit();
        }

        progreso.setVisibility(View.GONE);
    }
}
