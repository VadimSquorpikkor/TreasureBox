package com.squorpikkor.app.treasurebox;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squorpikkor.app.treasurebox.crypto.Encrypter2;

import java.util.ArrayList;

/**
 * Версия адаптера с двумя лисенерами.
 * Если задан ItemClickListener, то клик по пункту списка возвращает номер позиции;
 * если задан DeviceClickListener, то клик по пункту списка возвращает номер позиции возвращает
 * объект Entity соответствующий позиции в списке.
 */
public class EntitiesAdapter extends RecyclerView.Adapter<EntitiesAdapter.AdapterViewHolder> {

    String password = "";

    /**Конструктор. Пароль — это логин пользователя*/
    public EntitiesAdapter(String password) {
        this.password = password;
    }

    /**
     * Список объкетов Nuclide для отображения в Recycler
     */
    private ArrayList<Entity> list = new ArrayList<>();

    /**
     * Сеттер. После того, как список передан в адаптер, адаптер автоматом обновляет Recycler,
     * для отображения изменений
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setList(ArrayList<Entity> list) {
        if (list == null) list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    /**
     * Лисенер, который будет возвращать позицию выбранного элемента
     */
    private OnItemClickListener onItemClickListener;

    /**
     * Лисенер, который будет возвращать объект Entity по позиции выбранного элемента
     */
    private OnDeviceClickListener onObjectClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnDeviceClickListener {
        void onObjectClick(Entity nuclide);
    }

    /**
     * Сеттер на лисенер1
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Сеттер на лисенер2
     */
    public void setOnObjectClickListener(OnDeviceClickListener onNuclideClickListener) {
        this.onObjectClickListener = onNuclideClickListener;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entity, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        Entity entity = list.get(position);
        if (entity.getName().equals("null")) holder.name.setText("");
        else holder.name.setText(Encrypter2.decrypt(password, entity.getName()));

        if (entity.getLogin().equals("null")) holder.login.setText("");
        else holder.login.setText(Encrypter2.decrypt(password, entity.getLogin()));

        if (entity.getPass().equals("null")) holder.pass.setText("");
        else holder.pass.setText(Encrypter2.decrypt(password, entity.getPass()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView pass;
        private final TextView login;

        public AdapterViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            pass = view.findViewById(R.id.login);
            login = view.findViewById(R.id.pass);

            /**Если задан ItemClickListener, то клик по пункту списка возвращает номер позиции;
             * если задан DeviceClickListener, то клик по пункту списка возвращает номер позиции возвращает объект BluetoothDevice соответствующий позиции в списке*/
            view.setOnClickListener(v -> {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(getAdapterPosition());
                if (onObjectClickListener != null)
                    onObjectClickListener.onObjectClick(list.get(getAdapterPosition()));
            });

        }
    }
}
