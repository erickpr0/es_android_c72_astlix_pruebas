package com.astlix.es_android_c72_astlixpruebas.icons;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class FontAwesome extends TextView {
    public FontAwesome(Context context) {
        super(context);
        init();
    }

    public FontAwesome(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontAwesome(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public FontAwesome(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        //Font name should not contain "/".
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/fontawesome.otf");
        setTypeface(tf);
    }
}


