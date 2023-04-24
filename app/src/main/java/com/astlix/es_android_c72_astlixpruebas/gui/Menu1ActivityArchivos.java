package com.astlix.es_android_c72_astlixpruebas.gui;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.astlix.es_android_c72_astlixpruebas.R;
import com.astlix.es_android_c72_astlixpruebas.fragments.ArchivosFragment;
import com.astlix.es_android_c72_astlixpruebas.fragments.HeaderFragment;

public class Menu1ActivityArchivos extends AppCompatActivity {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    HeaderFragment headerFragment;
    ArchivosFragment archivosFragment;
    RelativeLayout progreso;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic);

        progreso = findViewById(R.id.progreso);
        progreso.setVisibility(View.GONE);

        headerFragment = new HeaderFragment("Archivos", R.color.menu1p, R.color.white);
        headerFragment.addHeaderFragmentListner(this::onBackPressed);
        archivosFragment = new ArchivosFragment();

        if (transaction.isEmpty()) {
            transaction.add(R.id.headerFragment, headerFragment);
            transaction.add(R.id.contenidoFragment, archivosFragment).commit();
        } else {
            transaction = null;
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.headerFragment, headerFragment);
            transaction.replace(R.id.contenidoFragment, archivosFragment).commit();
        }
    }
}
