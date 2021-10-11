package com.squorpikkor.app.treasurebox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    View view;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        MainViewModel mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        recyclerView = view.findViewById(R.id.recycler);

        mViewModel.getEntitiesList().observe(requireActivity(), this::updateTreasureList);
        mViewModel.openBox();

        view.findViewById(R.id.floatingActionButton).setOnClickListener(v -> openRecognizeDialog());

        return view;
    }

    private void openRecognizeDialog() {
        InputEntityDialog dialog = new InputEntityDialog();
        dialog.show(getParentFragmentManager(), null);
    }

    private void updateTreasureList(ArrayList<Entity> entities) {
//        if (entities==null||entities.size()==0)return;
//        Log.e(TAG, "updateTreasureList: "+entities.get(0).getName());
//        Toast.makeText(this, entities.get(0).getName(), Toast.LENGTH_SHORT).show();

        if (entities==null || entities.size()==0) return;
        /*view.findViewById(R.id.button_layout).setVisibility(View.GONE);
        view.findViewById(R.id.password_line).setVisibility(View.GONE);
        view.findViewById(R.id.recycler).setVisibility(View.VISIBLE);*/
        EntitiesAdapter adapter = new EntitiesAdapter(entities);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(adapter);


    }
}
