package com.astlix.es_android_c72_astlixpruebas.gui;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.astlix.es_android_c72_astlixpruebas.enums.MenuStyle_Enum;

public class NoneRfidActivity extends AppCompatActivity {
    public MenuStyle_Enum style_enum;

    public String tituloActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        int styleCode = bundle.getInt("styleCode", 1);
        style_enum = MenuStyle_Enum.button1;
        for (MenuStyle_Enum m : MenuStyle_Enum.values()) {
            if (m.getCode() == styleCode) {
                style_enum = m;
                break;
            }
        }
        getBundleValues(bundle);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, style_enum.getColor()));

        tituloActivity = bundle.getString("texto");
    }

    public void getBundleValues(Bundle bundle) {

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
    protected void onStop() {
        super.onStop();
    }
}
