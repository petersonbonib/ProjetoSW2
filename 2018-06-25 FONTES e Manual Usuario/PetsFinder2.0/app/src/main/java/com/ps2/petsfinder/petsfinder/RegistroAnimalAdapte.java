package com.ps2.petsfinder.petsfinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class RegistroAnimalAdapte extends RecyclerView.Adapter {

    private List<RegistroAnimal> registroAnimal;

    public RegistroAnimalAdapte(List<RegistroAnimal> registroAnimal){
        this.registroAnimal = registroAnimal;
    }

    public RegistroAnimalAdapte() {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private class ViewHolderRegistroAnimal extends RecyclerView.ViewHolder{

        public String nome;

        public ViewHolderRegistroAnimal(View itemView) {
            super(itemView);
        }
    }
}
