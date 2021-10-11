package com.squorpikkor.app.treasurebox;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EntitiesAdapter extends RecyclerView.Adapter<EntitiesAdapter.AdapterViewHolder>{

    ArrayList<Entity> deviceList;

    //TODO переделать адаптер

    public EntitiesAdapter(ArrayList<Entity> deviceList) {
        this.deviceList = deviceList;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entity, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        Entity entity = deviceList.get(position);
        if (entity.getName().equals("null")) holder.name.setText("");
        else holder.name.setText(Encrypter.decodeMe(entity.getName()));

        if (entity.getLogin().equals("null")) holder.login.setText("");
        else holder.login.setText(Encrypter.decodeMe(entity.getLogin()));

        if (entity.getPass().equals("null")) holder.pass.setText("");
        else holder.pass.setText(Encrypter.decodeMe(entity.getPass()));
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView pass;
        private final TextView login;

        public AdapterViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            pass = view.findViewById(R.id.login);
            login = view.findViewById(R.id.pass);
        }
    }
}
