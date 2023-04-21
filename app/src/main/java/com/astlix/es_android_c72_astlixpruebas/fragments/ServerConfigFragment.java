package com.astlix.es_android_c72_astlixpruebas.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.astlix.es_android_c72_astlixpruebas.R;
import com.astlix.es_android_c72_astlixpruebas.database.DatabaseConf;


public class ServerConfigFragment extends Fragment {
    private final EditText[] editTexts = new EditText[4];
    private ServerConfigFragmentListener listener;

    public ServerConfigFragment(DatabaseConf databaseConf) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("databaseConf", databaseConf);
        setArguments(bundle);
    }

    public ServerConfigFragment() {
    }

    public void addServerConfigFragmentListener(ServerConfigFragmentListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.configserver_fragment, container, false);

        Bundle bundle = getArguments();
        if(bundle!=null) {
            DatabaseConf databaseConf = bundle.getParcelable("databaseConf");
            editTexts[0] = v.findViewById(R.id.urlServer);
            editTexts[0].setText(databaseConf.getUrl());
            editTexts[1] = v.findViewById(R.id.baseServer);
            editTexts[1].setText(databaseConf.getDatabase());
            editTexts[2] = v.findViewById(R.id.userServer);
            editTexts[2].setText(databaseConf.getUser());
            editTexts[3] = v.findViewById(R.id.passServer);
            editTexts[3].setText(databaseConf.getPassword());
            Button button = v.findViewById(R.id.butGuardar);
            button.setOnClickListener(view -> {
                String[] strings = new String[4];
                for(int i=0; i<4; i++) {
                    strings[i] = editTexts[i].getText().toString();
                    if(strings[i]==null || strings[i].isEmpty()) {
                        if(listener!=null) {
                            listener.camposvacios(i);
                        }
                        return;
                    }
                }
                databaseConf.setUrl(strings[0]);
                databaseConf.setDatabase(strings[1]);
                databaseConf.setUser(strings[2]);
                databaseConf.setPassword(strings[3]);
                if(listener!=null) {
                    listener.modificarCampos(databaseConf);
                }
            });
        }
        return v;
    }
}
