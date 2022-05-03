package com.saberpro.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

    @NonNull
    @Override
    public ListAnswersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.response_item, parent, false);
        return new ListAnswersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAnswersAdapter.ViewHolder holder, int position) {
        holder.answer.setText(respuestaList.get(position).getDescripcion());
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

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton answer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            answer = itemView.findViewById(R.id.rb_respuesta);
        }
    }
}
