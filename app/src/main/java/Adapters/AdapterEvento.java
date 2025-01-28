package Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.appreciclaje.ActividadDetalleEvento;
import com.example.appreciclaje.R;

import java.util.List;

import Models.Evento;

public class AdapterEvento extends BaseAdapter {

    private final Context context;
    private final List<Evento> eventos;
    private static LayoutInflater inflater = null;

    public AdapterEvento(Context context, List<Evento> eventos) {
        this.context = context;
        this.eventos = eventos;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return eventos.size();
    }

    @Override
    public Object getItem(int position) {
        return eventos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vista = inflater.inflate(R.layout.ly_item_evento, null);

        TextView titulo = vista.findViewById(R.id.lb_tituloEvento);
        TextView fecha = vista.findViewById(R.id.lb_horarioEvento);
        TextView lugar = vista.findViewById(R.id.lb_lugarEvento);

        Evento evento = eventos.get(position);

        titulo.setText(evento.getTitulo());
        fecha.setText(evento.getFecha());
        lugar.setText(evento.getLugar());


        vista.setOnClickListener(v->{
            Intent oIntento = new Intent(context, ActividadDetalleEvento.class);
            oIntento.putExtra("titulo", evento.getTitulo());
            oIntento.putExtra("descripcion", evento.getDescripcion());
            oIntento.putExtra("fecha", evento.getFecha());
            oIntento.putExtra("lugar", evento.getLugar());
            oIntento.putExtra("organizador", evento.getOrganizador());
            context.startActivity(oIntento);
        });

        return vista;
    }
}
