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

public class ListFragment extends Fragment {

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    private EntitiesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        MainViewModel mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        adapter = new EntitiesAdapter(mViewModel.getLogin());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(adapter);
        mViewModel.getEntitiesList().observe(requireActivity(), entities -> adapter.setList(entities));

        mViewModel.openBox();

        view.findViewById(R.id.floatingActionButton).setOnClickListener(v -> openInputDialog());

        return view;
    }

    private void openInputDialog() {
        InputEntityDialog dialog = new InputEntityDialog();
        dialog.show(getParentFragmentManager(), null);
    }
}
