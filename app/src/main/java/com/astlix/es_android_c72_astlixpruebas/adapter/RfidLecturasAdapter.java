package com.astlix.es_android_c72_astlixpruebas.adapter;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.astlix.es_android_c72_astlixpruebas.R;
import com.astlix.es_android_c72_astlixpruebas.database.InterfazBD;
import com.astlix.es_android_c72_astlixpruebas.model.LecturasRfidCiega;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class RfidLecturasAdapter extends BaseAdapter {
    private final List<LecturasRfidCiega> lecturasRfidCiegas;
    private final Activity activity;

    public RfidLecturasAdapter(Activity activity) {
        this.activity = activity;
        lecturasRfidCiegas = new LinkedList<>();
    }

    public boolean addEpc(ArrayList<String> epcs, ArrayList<String> rssi) {
        boolean newTag = false;
        for (int j = 0; j < epcs.size(); j++) {
            boolean found = false;
            for (LecturasRfidCiega lfc : lecturasRfidCiegas) {
                if (lfc.compareTo(epcs.get(j)) == 0) {
                    found = true;
                    lfc.setRssi(rssi.get(j));
                    break;
                }
            }
            if (!found) {
                newTag = true;
                lecturasRfidCiegas.add(0, new LecturasRfidCiega(epcs.get(j), rssi.get(j)));
            }
        }
        notifyDataSetChanged();
        return newTag;
    }

    @Override
    public int getCount() {
        return lecturasRfidCiegas.size();
    }

    @Override
    public Object getItem(int i) {
        return lecturasRfidCiegas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void clearList() {
        lecturasRfidCiegas.clear();
        notifyDataSetChanged();
    }

    public boolean lecturaVacia() {
        return lecturasRfidCiegas.size() == 0;
    }

    public void exportarTags() {
        InterfazBD interfazBD = new InterfazBD(activity);
        int cont = 0;
        Log.d("EXPORTAR", "exportarTags: ");
        String[] header = {"", "EPC", "ASCII", "RSSI"};
        String[] blank = {""};
        String nombre = "test";
        try {
            //String csv = System.getenv("EXTERNAL_STORAGE") + "/Documents/Tags/" + nombre + ".csv";
            String csv = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Tags/" + nombre + ".csv";
            //String csv = "Este equipo\\C72\\Almacenamiento interno\\Documents\\Tags\\" + nombre + ".csv";

            System.out.println(csv);
            CSVWriter csvWriter = new CSVWriter(new FileWriter(csv), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

            csvWriter.writeNext(header);
            csvWriter.writeNext(blank);

            for (LecturasRfidCiega tag : lecturasRfidCiegas) {
                System.out.println(tag.getEpc());
                cont++;
                String[] tags = {String.valueOf(cont), String.valueOf(tag.getEpc()), hexToAscii(tag.getEpc()), String.valueOf(tag.getRssi())};
                csvWriter.writeNext(tags);
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            System.out.println("catch");
            e.printStackTrace();
        }

        new AlertDialog.Builder(activity).setTitle("Exportación exitosa").setMessage("Se han exportado los códigos correctamente")
                .setIcon(ContextCompat.getDrawable(activity, R.drawable.export_check))
                .setCancelable(false)
                .setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss()).show();

        interfazBD.truncarTabla("tags");

        for (int i = 0; i < lecturasRfidCiegas.size(); i++) {
            interfazBD.insertarTags(lecturasRfidCiegas.get(i), hexToAscii(lecturasRfidCiegas.get(i).getEpc()));
        }
    }

    private static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder();
        String asc = "";

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        if (!output.toString().startsWith("0")) {
            if (output.toString().matches("\\A\\p{ASCII}*\\z") && !output.toString().startsWith("0")) {
                asc = output.toString();
            } else {
                asc = "";
            }
        } else {
            System.out.println("NO VALIDO" + output);
        }

        return asc;
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    private class ViewHolder {
        int[] colors = activity.getResources().getIntArray(R.array.adapter);
        TextView epc;
        TextView sku;
        TextView rssi;
        TextView cant;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        ViewHolder viewHolder;

        if (rowView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_lecturas_rfid, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.epc = rowView.findViewById(R.id.epcTv);
            viewHolder.sku = rowView.findViewById(R.id.skuTv);
            viewHolder.rssi = rowView.findViewById(R.id.rssiTv);
            viewHolder.cant = rowView.findViewById(R.id.cantTv);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        @SuppressLint("Recycle") ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), viewHolder.colors[0], viewHolder.colors[1]);
        colorAnimator.setDuration(500);

        viewHolder.epc.setText(lecturasRfidCiegas.get(i).getEpc());

        viewHolder.rssi.setText(lecturasRfidCiegas.get(i).getRssi());
        viewHolder.cant.setText(String.format(Locale.getDefault(), "%d", lecturasRfidCiegas.get(i).getCant()));

        return rowView;
    }
}
