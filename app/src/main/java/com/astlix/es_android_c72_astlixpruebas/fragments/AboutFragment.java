package com.astlix.es_android_c72_astlixpruebas.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.astlix.es_android_c72_astlixpruebas.BuildConfig;
import com.astlix.es_android_c72_astlixpruebas.R;

import java.util.Locale;

public class AboutFragment extends Fragment {

    public AboutFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.about_fragment, container, false);
        TextView textView = v.findViewById(R.id.versionTv);
        textView.setText(String.format(Locale.getDefault(), "Versión %s", BuildConfig.VERSION_NAME));
        return v;
    }
}
