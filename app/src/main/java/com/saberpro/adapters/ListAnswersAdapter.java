package com.saberpro.adapters;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.saberpro.funciones.Funciones;
import com.saberpro.icfes.R;
import com.saberpro.models.Respuesta;

import java.util.List;

public class ListAnswersAdapter extends RecyclerView.Adapter<ListAnswersAdapter.ViewHolder> {

    private List<Respuesta> respuestaList;
    private int selectedPosition = -1;
    private View view;
    private ItemClickListener listener;

    public interface ItemClickListener {
        void onClick(int position);
    }

    public ListAnswersAdapter(List<Respuesta> respuestaList, ItemClickListener listener) {
        this.respuestaList = respuestaList;
        this.listener = listener;
    }

    public Respuesta getAnswer() {
        if (selectedPosition != -1)
            return respuestaList.get(selectedPosition);
        return null;
    }

    @NonNull
    @Override
    public ListAnswersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.response_item, parent, false);
        return new ListAnswersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAnswersAdapter.ViewHolder holder, int position) {
        if (respuestaList.get(position).getDescripcion() != null){
            holder.answer.setText(respuestaList.get(position).getDescripcion());
            holder.answer.setCompoundDrawablesRelative(null, null,null,null);
        }
        else{
            LayerDrawable layerDrawable = (LayerDrawable) ContextCompat.getDrawable(view.getContext(), R.drawable.img_radio_button);
            String url = Funciones.generateUrl(respuestaList.get(position).getImg());
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
                            holder.answer.setCompoundDrawablesWithIntrinsicBounds(null, null,null,layerDrawable);
                            holder.answer.setText("");
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }

        holder.answer.setChecked(position == selectedPosition);
        holder.answer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    selectedPosition = holder.getAdapterPosition();
                    listener.onClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return respuestaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton answer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            answer = itemView.findViewById(R.id.rb_respuesta);
        }
    }
}
