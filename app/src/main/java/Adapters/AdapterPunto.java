package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.appreciclaje.R;

import java.util.List;

import Models.Punto;

public class AdapterPunto extends BaseAdapter {

    private final Context context;
    private final List<Punto> puntos;
    private static LayoutInflater inflater = null;

    public AdapterPunto(Context context, List<Punto> puntos) {
        this.context = context;
        this.puntos = puntos;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return puntos.size();
    }

    @Override
    public Object getItem(int position) {
        return puntos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vista = inflater.inflate(R.layout.ly_item_punto, null);

        TextView txtNomPunto = vista.findViewById(R.id.txt_NomPunto);
        TextView txtDirecPunto = vista.findViewById(R.id.txt_DirecPunto);
        TextView txtReferPunto = vista.findViewById(R.id.txt_ReferPunto);

        Punto punto = puntos.get(position);

        txtNomPunto.setText(punto.getNombre());
        txtDirecPunto.setText(punto.getDireccion());
        txtReferPunto.setText(punto.getReferencia());

        return vista;
    }
}