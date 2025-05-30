package com.example.app_delitos.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_delitos.R;
import com.example.app_delitos.database.helpers.TipoDelitoDAO;
import com.example.app_delitos.database.models.Delito;
import com.example.app_delitos.database.models.TipoDelito;
import com.example.app_delitos.databinding.AdapterItemDelitoBinding;

import java.util.ArrayList;

public class ItemDelitoAdapter extends RecyclerView.Adapter<ItemDelitoAdapter.ItemDelitoHolder> implements View.OnClickListener{

    private ArrayList<Delito> delitoArrayList;
    private View.OnClickListener listener;

    public ItemDelitoAdapter() {
    }

    public ItemDelitoAdapter(ArrayList<Delito> itemRequestArrayList) {
        this.delitoArrayList = itemRequestArrayList;
    }

    @NonNull
    @Override
    public ItemDelitoAdapter.ItemDelitoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_delito, parent, false);
        view.setOnClickListener(this);
        return new ItemDelitoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemDelitoAdapter.ItemDelitoHolder holder, int position) {
        holder.setItem(this.delitoArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return delitoArrayList.size();
    }

    @Override
    public void onClick(View v) {
        if(this.listener != null) {
            this.listener.onClick(v);
        }
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public static class ItemDelitoHolder extends RecyclerView.ViewHolder {

        private final AdapterItemDelitoBinding binding;

        public ItemDelitoHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdapterItemDelitoBinding.bind(itemView);
        }

        public void setItem(Delito delito) {

            TipoDelitoDAO tipoDelitoDAO = new TipoDelitoDAO(binding.getRoot().getContext());
            TipoDelito tipoDelito = tipoDelitoDAO.getById(delito.getIdTipoDelito());

            binding.tipoDelito.setText(tipoDelito.getDescripcion());
            binding.fechaOcurrencia.setText(delito.getFechaCreacion());
        }
    }
}
