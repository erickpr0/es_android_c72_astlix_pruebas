package com.astlix.es_android_c72_astlixpruebas.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.astlix.es_android_c72_astlixpruebas.R;
import com.astlix.es_android_c72_astlixpruebas.adapter.BarcodeSimpleAdapter;


public class BarcodeFragment extends Fragment {
    private BarcodeSimpleAdapter barcodeSimpleAdapter;

    public BarcodeFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_barcode, container, false);
        ListView listView = v.findViewById(R.id.listview);
        barcodeSimpleAdapter = new BarcodeSimpleAdapter(getActivity());
        listView.setAdapter(barcodeSimpleAdapter);
        return v;
    }

    public boolean addBarcode(String bc) {
        return barcodeSimpleAdapter.addBarCode(bc);
    }

    public void clearList() {
        barcodeSimpleAdapter.clearList();
    }
}
