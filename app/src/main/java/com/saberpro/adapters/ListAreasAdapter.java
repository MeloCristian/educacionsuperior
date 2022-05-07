package com.saberpro.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.saberpro.funciones.Funciones;
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
        setListeners(holder.cardView, areaList.get(position));
        LayerDrawable layerDrawable = (LayerDrawable) ContextCompat.getDrawable(view.getContext(), R.drawable.img_card_area);
        String url = Funciones.generateUrl(areaList.get(position).getImg());
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(view.getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        Glide.with(view.getContext())
                .asBitmap()
                .load(url)
                .placeholder(circularProgressDrawable)
                .into(new CustomTarget<Bitmap>() {

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Drawable drawable = new BitmapDrawable(view.getResources(),  Bitmap.createScaledBitmap(resource, 600, 600, true));
                        layerDrawable.setDrawableByLayerId(R.id.img_respuesta,drawable);
                        holder.title.setCompoundDrawablesWithIntrinsicBounds(layerDrawable, null,null,null);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return areaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_card_area);
            cardView = itemView.findViewById(R.id.card_area);
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
}
