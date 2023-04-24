package com.astlix.es_android_c72_astlixpruebas.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.astlix.es_android_c72_astlixpruebas.R;
import com.astlix.es_android_c72_astlixpruebas.model.Archivo;

import java.util.ArrayList;

public class SpinnerArchivosAdapter extends BaseAdapter {
    ArrayList<Archivo> archivos;
    Activity activity;

    public SpinnerArchivosAdapter(Activity activity, ArrayList<Archivo> archivos) {
        this.archivos = archivos;
        this.activity = activity;
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

    private static class ViewHolder {
        TextView nombreTv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder;
        if (rowView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.item_archivo, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.nombreTv = rowView.findViewById(R.id.nombreTv);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.nombreTv.setText(String.valueOf(archivos.get(position).getNombre()));

        return rowView;
    }
}
