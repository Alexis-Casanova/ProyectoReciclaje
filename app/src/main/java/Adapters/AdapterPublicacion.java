package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appreciclaje.R;

import java.util.List;
import java.util.zip.Inflater;

import Models.Publicacion;

public class AdapterPublicacion extends BaseAdapter {
    private final Context context;
    private final List<Publicacion> publicaciones;
    private static LayoutInflater inflater =  null;

    public AdapterPublicacion(Context context, List<Publicacion> publicaciones) {
        this.context = context;
        this.publicaciones = publicaciones;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return publicaciones.size();
    }

    @Override
    public Object getItem(int position) {
        return publicaciones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vista = inflater.inflate(R.layout.item, null);


        ImageView imgUsuario = vista.findViewById(R.id.img_usuario);
        TextView lbUsuario = vista.findViewById(R.id.lb_usuario);
        TextView lbFecha = vista.findViewById(R.id.lb_fecha);
        TextView lbDescripcion = vista.findViewById(R.id.lb_descripcion);
        ImageView imgPost = vista.findViewById(R.id.img_post);
        TextView lbMoneda = vista.findViewById(R.id.lb_moneda);

        Publicacion publicacion = publicaciones.get(position);
        if (publicacion.getUsuario() != null) {
            lbUsuario.setText(publicacion.getUsuario().getNombre());
            lbMoneda.setText(String.valueOf(publicacion.getUsuario().getRecompensa()));

            Glide.with(context)
                    .load(publicacion.getUsuario().getRutaUsuario())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(imgUsuario);
        } else {
            lbUsuario.setText("Usuario desconocido");
            lbMoneda.setText("0");
            imgUsuario.setImageResource(R.drawable.placeholder_image); // Imagen predeterminada
        }
        lbFecha.setText(publicacion.getFecha());
        lbDescripcion.setText(publicacion.getContenido());
        Glide.with(context)
                .load(publicacion.getRutaPublicacion())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(imgPost);

        return vista;

    }
}
