package com.astlix.es_android_c72_astlixpruebas.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.astlix.es_android_c72_astlixpruebas.R;
import com.astlix.es_android_c72_astlixpruebas.enums.MenuStyle_Enum;
import com.astlix.es_android_c72_astlixpruebas.icons.FontAwesome;
import com.astlix.es_android_c72_astlixpruebas.icons.FontMatDesign;


@SuppressWarnings({"unused", "ConstantConditions"})
public class ControlsFragment extends Fragment {
    private final MenuStyle_Enum style_enum;
    private final String iconBt2;
    private final String textBt2;
    private ControlsFragmentListener listener;

    private LinearLayout button1;
    private LinearLayout button2;
    private FontMatDesign but1Icon;
    private FontAwesome but2Icon;
    private TextView but1Text;
    private TextView but2Text;
    private boolean secondfunction;

    public void addControlsFragmentAdapter(ControlsFragmentListener listener) {
        this.listener = listener;
    }

    public ControlsFragment(MenuStyle_Enum style_enum, String iconBt2, String textBt2) {
        this.style_enum = style_enum;
        this.iconBt2 = iconBt2;
        this.textBt2 = textBt2;
    }

    public ControlsFragment() {
        this.style_enum = MenuStyle_Enum.button1;
        this.iconBt2 = getContext().getResources().getString(R.string.check_circle);
        this.textBt2 = getContext().getResources().getString(R.string.butEnviar);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.controls, container, false);
        button1 = v.findViewById(R.id.button1);
        button1.setBackground(ContextCompat.getDrawable(getContext(), style_enum.getButDrawable()));
        but1Icon = v.findViewById(R.id.button1_icon);
        but1Text = v.findViewById(R.id.button1_text);
        button2 = v.findViewById(R.id.button2);
        button2.setBackground(ContextCompat.getDrawable(getContext(), style_enum.getButDrawable()));
        but2Icon = v.findViewById(R.id.button2_icon);
        but2Text = v.findViewById(R.id.button2_text);
        setButton2FirstFunction();

        button1.setOnClickListener(view -> {
            if (listener != null) {
                listener.lecturarToggleClicked();
            }
        });

        button2.setOnClickListener(view -> {
            if (listener != null) {
                if (!secondfunction) {
                    listener.button2Clicked();
                } else {
                    listener.button2ClickedSecond();
                }
            }
        });

        return v;
    }

    public void setButton1PRessed(boolean pressed) {
        if (pressed) {
            button2.setEnabled(false);
            but1Text.setText(getResources().getString(R.string.butFin));
            but1Icon.setText(getResources().getString(R.string.zmdi_portable_wifi_off));
            button1.setBackground(ContextCompat.getDrawable(getContext(), style_enum.getButDrawableAlt()));
        } else {
            button2.setEnabled(true);
            but1Text.setText(getResources().getString(R.string.butInit));
            but1Icon.setText(getResources().getString(R.string.zmdi_portable_wifi));
            button1.setBackground(ContextCompat.getDrawable(getContext(), style_enum.getButDrawable()));
        }
    }

    public void disableEnableRfid(boolean enable) {
        button1.setEnabled(enable);
    }

    public void setButton2FirstFunction() {
        but2Icon.setText(iconBt2);
        but2Text.setText(textBt2);
        secondfunction = false;
    }

    public void setButton2SecondFunction() {
        but2Icon.setText(getContext().getResources().getString(R.string.broom));
        but2Text.setText(getContext().getResources().getString(R.string.butClean));
        secondfunction = true;
    }

    public void button1Clicked() {
        button1.performClick();
    }

    public void button2Clicked() {
        button2.performClick();
    }
}

