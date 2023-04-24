package com.astlix.es_android_c72_astlixpruebas.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textservice.TextInfo;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.astlix.es_android_c72_astlixpruebas.R;
import com.astlix.es_android_c72_astlixpruebas.model.Archivo;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ArchivosAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<Archivo> archivos;
    public ArchivosAdapter(Activity activity, ArrayList<Archivo> archivos) {
        this.activity = activity;
        this.archivos = archivos;
    }

    @Override
    public int getCount() {
        return archivos.size();
    }

    @Override
    public Object getItem(int position) {
        return archivos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private static class ViewHolder{
        TextView nombreArchivo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder;

        if (rowView == null){
            LayoutInflater inflater = activity.getLayoutInflater();
            //rowView = inflater.inflate(R.layout.list_ar, viewGroup, false);
        }
        return convertView;
    }
}
