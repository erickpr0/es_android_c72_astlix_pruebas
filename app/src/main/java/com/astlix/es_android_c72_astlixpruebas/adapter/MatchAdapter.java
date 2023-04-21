package com.astlix.es_android_c72_astlixpruebas.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.astlix.es_android_c72_astlixpruebas.R;
import com.astlix.es_android_c72_astlixpruebas.icons.FontAwesome;
import com.astlix.es_android_c72_astlixpruebas.model.LecturasRfidCiega;
import com.astlix.es_android_c72_astlixpruebas.model.Resultado;

import java.util.ArrayList;

public class MatchAdapter extends BaseAdapter {
    private final Activity activity;
    private final ArrayList<LecturasRfidCiega> tags;
    private int esperados;
    private int leidos = 0;

    public MatchAdapter(Activity activity, ArrayList<LecturasRfidCiega> tags) {
        this.activity = activity;
        this.tags = tags;
    }

    @Override
    public int getCount() {
        return tags.size();
    }

    @Override
    public Object getItem(int position) {
        return tags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public Resultado addEpc(ArrayList<String> epcs, ArrayList<String> rssis) {

        boolean newTag = false;
        for (int j = 0; j < epcs.size(); j++) {
            boolean found = false;
            for (LecturasRfidCiega lfc : tags) {
                if (lfc.compareTo(epcs.get(j)) == 0) {
                    found = true;
                    if (!lfc.isCorrecto()){
                        lfc.setCorrecto(true);
                        //esperados--;
                        leidos++;
                        newTag = true;
                    }
                    break;
                }
            }
            if (!found) {
                Log.d("SOUND", "addEpc: NO FOUND");
            }
        }
        notifyDataSetChanged();
        Log.d("SOUND", "addEpc: " + newTag);
        Resultado resultado = new Resultado(esperados, leidos, newTag);
        return resultado;
    }

    public void limpiar() {
        for (LecturasRfidCiega tags : tags) {
            tags.setCorrecto(false);
        }
    }

    private static class ViewHolder {
        TextView tag;
        TextView ascii;
        FontAwesome found;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        View rowView = view;
        ViewHolder viewHolder;

        if (rowView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_validacion, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tag = rowView.findViewById(R.id.tagTv);
            viewHolder.ascii = rowView.findViewById(R.id.asciiTv);
            viewHolder.found = rowView.findViewById(R.id.foundTv);

            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tag.setText(tags.get(i).getEpc());
        viewHolder.ascii.setText(tags.get(i).getAscii());

        if (tags.get(i).isCorrecto()) {
            viewHolder.found.setTextColor(ContextCompat.getColor(activity, R.color.bien));
        } else {
            viewHolder.found.setTextColor(ContextCompat.getColor(activity, R.color.faltante));
        }

        return rowView;
    }
}
