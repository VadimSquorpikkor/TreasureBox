package com.squorpikkor.app.treasurebox.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squorpikkor.app.treasurebox.Entity;
import com.squorpikkor.app.treasurebox.R;
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

    /**Лисенер, который будет возвращать позицию выбранного элемента*/
    private OnItemClickListener onItemClickListener;

    /**Лисенер, который будет возвращать объект Entity по позиции выбранного элемента*/
    private OnObjectClickListener onObjectClickListener;

    private OnLongClickListener onLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnObjectClickListener {
        void onObjectClick(Entity entity);
    }

    public interface OnLongClickListener {
//        void onLongClick(String docName);
        void onLongClick(int position);
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
    public void setOnObjectClickListener(OnObjectClickListener onObjectClickListener) {
        this.onObjectClickListener = onObjectClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entity, parent, false);
        return new AdapterViewHolder(view);
    }

    private void setView(String data, TextView titleView, TextView dataView) {
        if (data==null) {
            dataView.setVisibility(View.GONE);
            if (titleView!=null) titleView.setVisibility(View.GONE);
            return;
        }
        data = Encrypter2.decrypt(password, data);
        if (data.equals("null") || data.equals("")) {
            dataView.setVisibility(View.GONE);
            if (titleView!=null) titleView.setVisibility(View.GONE);
        } else {
            dataView.setVisibility(View.VISIBLE);
            if (titleView!=null) titleView.setVisibility(View.VISIBLE);
            dataView.setText(data);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        Entity entity = list.get(position);
        if (entity.getName().equals("null")) holder.name.setText("- - -");
        else holder.name.setText(Encrypter2.decrypt(password, entity.getName()));

        setView(entity.getLogin(), holder.loginTitle,   holder.login);
        setView(entity.getPass(),  holder.passTitle,    holder.pass);
        setView(entity.getEmail(), holder.emailTitle,   holder.email);
        setView(entity.getAdds(),  null,       holder.adds);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView pass;
        private final TextView passTitle;
        private final TextView login;
        private final TextView loginTitle;
        private final TextView email;
        private final TextView emailTitle;
        private final TextView adds;

        public AdapterViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            pass = view.findViewById(R.id.login);
            passTitle = view.findViewById(R.id.login_title);
            login = view.findViewById(R.id.pass);
            loginTitle = view.findViewById(R.id.pass_title);
            email = view.findViewById(R.id.email);
            emailTitle = view.findViewById(R.id.email_title);
            adds = view.findViewById(R.id.adds);

            /**Если задан ItemClickListener, то клик по пункту списка возвращает номер позиции;
             * если задан DeviceClickListener, то клик по пункту списка возвращает номер позиции возвращает объект BluetoothDevice соответствующий позиции в списке*/
            view.setOnClickListener(v -> {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(getAdapterPosition());
                if (onObjectClickListener != null)
                    onObjectClickListener.onObjectClick(list.get(getAdapterPosition()));
            });

            /**Если задан LongClickListener, то долгий клик по пункту списка возвращает имя документа в Firebase, в котором находятся данные выбранного Entity*/
            view.setOnLongClickListener(v -> {
                if (onLongClickListener != null)
//                    onLongClickListener.onLongClick(list.get(getAdapterPosition()).getDocName());
                    onLongClickListener.onLongClick(getAdapterPosition());
                return false;
            });

        }
    }
}
