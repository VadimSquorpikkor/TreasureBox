package com.squorpikkor.app.treasurebox.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squorpikkor.app.treasurebox.dialog.DeleteDialog;
import com.squorpikkor.app.treasurebox.dialog.InputEntityDialog;
import com.squorpikkor.app.treasurebox.MainViewModel;
import com.squorpikkor.app.treasurebox.R;
import com.squorpikkor.app.treasurebox.adapter.EntitiesAdapter;

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
        adapter.setOnLongClickListener(this::openDeleteDialog);

        mViewModel.openBox();

        view.findViewById(R.id.floatingActionButton).setOnClickListener(v -> openInputDialog());

        return view;
    }

    private void openInputDialog() {
        InputEntityDialog dialog = new InputEntityDialog();
        dialog.show(getParentFragmentManager(), null);
    }

    public void openDeleteDialog(int position) {
        DeleteDialog dialog = new DeleteDialog(position);
        dialog.show(getParentFragmentManager(), null);
    }
}
