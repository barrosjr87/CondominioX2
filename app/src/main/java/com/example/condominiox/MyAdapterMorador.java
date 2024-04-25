package com.example.condominiox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
public class MyAdapterMorador extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Encomendas> userArrayList;

    public MyAdapterMorador(Context context, ArrayList<Encomendas> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item2,parent,false);

        return new MyAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {

        Encomendas encomendas = userArrayList.get(position);
        holder.Cep.setText(encomendas.Cep);
        holder.apto.setText(encomendas.apto);
        holder.data.setText(encomendas.data);
        holder.tipo.setText(encomendas.tipo);
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Cep, apto, data, tipo;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Cep = itemView.findViewById(R.id.tvCepLista);
            apto = itemView.findViewById(R.id.tvaptoLista);
            data = itemView.findViewById(R.id.tvdataLista);
            tipo = itemView.findViewById(R.id.tvtipoLista);
        }
    }
}
