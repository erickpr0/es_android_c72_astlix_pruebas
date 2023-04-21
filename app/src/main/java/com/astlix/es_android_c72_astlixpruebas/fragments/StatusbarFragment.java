package com.astlix.es_android_c72_astlixpruebas.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.astlix.es_android_c72_astlixpruebas.R;
import com.astlix.es_android_c72_astlixpruebas.icons.FontAwesome;


@SuppressWarnings("unused")
public class StatusbarFragment extends Fragment {
    private final int drawable;

    private TextView mensajeTv = null;
    private FontAwesome statusFa = null;

    private Context context;

    public StatusbarFragment(int drawable) {
        this.drawable = drawable;
    }

    public StatusbarFragment() {
        this.drawable = R.drawable.menu1_button_up;
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.statusbar, container, false);
        mensajeTv = v.findViewById(R.id.textoMsg);
        mensajeTv.setBackground(ContextCompat.getDrawable(getContext(), drawable));
        statusFa = v.findViewById(R.id.iconoStatus);
        statusFa.setBackground(ContextCompat.getDrawable(getContext(), drawable));
        context = getContext();
        return v;
    }

    @Override
    public void onDestroyView() {
        mensajeTv = null;
        statusFa = null;
        super.onDestroyView();
    }

    public void setTextMessage(String msg) {
        Animation animBlink = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        if (mensajeTv != null) {
            if (msg.equals("Iniciando Lector RFID")){
                mensajeTv.setTextColor(ContextCompat.getColor(context, R.color.faltante));
            } else if (msg.equals("RFID Conectado")){
                mensajeTv.setTextColor(ContextCompat.getColor(context, R.color.bien));
            }
            mensajeTv.startAnimation(animBlink);
            mensajeTv.setText(msg);
        }
    }

    public void setStatusIcon(int code) {
        if (statusFa != null) {
            switch (code) {
                case 0:
                    statusFa.setText(context.getResources().getString(R.string.exclamation_circle));
                    statusFa.setTextColor(ContextCompat.getColor(context, R.color.faltante));
                    break;
                case 1:
                    statusFa.setText(context.getResources().getString(R.string.check_circle));
                    statusFa.setTextColor(ContextCompat.getColor(context, R.color.bien));
                    break;
                default:
                    statusFa.setText(context.getResources().getString(R.string.times_circle));
                    statusFa.setTextColor(ContextCompat.getColor(context, R.color.white));
                    break;
            }
        }
    }
}
