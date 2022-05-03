package com.saberpro.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.saberpro.icfes.Preguntas;
import com.saberpro.icfes.R;
import com.saberpro.models.Area;

import java.util.List;

public class ListAreasAdapter extends RecyclerView.Adapter<ListAreasAdapter.ViewHolder> {

    private View view;
    private List<Area> areaList;

    public ListAreasAdapter(List<Area> areaList) {
        this.areaList = areaList;
    }

    @NonNull
    @Override
    public ListAreasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_area, parent, false);
        return new ListAreasAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAreasAdapter.ViewHolder holder, int position) {
        holder.title.setText(areaList.get(position).getNombre());
        holder.descripcion.setText(areaList.get(position).getDescripcion());
        setListeners(holder.button, areaList.get(position));
        String url = generateUrl(areaList.get(position).getImg());
        Glide.with(view.getContext()).load(url).priority(Priority.IMMEDIATE).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return areaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, descripcion;
        Button button;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_card_area);
            descripcion = itemView.findViewById(R.id.descripcion_area);
            button = itemView.findViewById(R.id.btn_acceder_area);
            imageView = itemView.findViewById(R.id.img_card_area);
        }
    }

    private void setListeners(View view2, Area area) {
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), Preguntas.class);
                i.putExtra("id_area", area.getId_area());
                i.putExtra("nombre_area", area.getNombre());
                view.getContext().startActivity(i);
                Activity activity = (Activity) view.getContext();
                activity.finish();
            }
        });
    }

    private String generateUrl(String s) {
        String[] p = s.split("/");
        String imageLink = "https://drive.google.com/uc?export=download&id=" + p[5];
        return imageLink;
    }
}
