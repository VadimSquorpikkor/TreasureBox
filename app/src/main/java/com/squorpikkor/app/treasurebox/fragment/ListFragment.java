package com.squorpikkor.app.treasurebox.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squorpikkor.app.treasurebox.Entity;
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
    MainViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        EditText searchText = view.findViewById(R.id.search_line);

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.setList(mViewModel.getMatchedList(mViewModel.getEntitiesList().getValue(), s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        adapter = new EntitiesAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(adapter);
        mViewModel.getEntitiesList().observe(requireActivity(), entities -> adapter.setList(mViewModel.getFilteredByCategory(entities)));
        adapter.setOnLongClickListener(this::openDeleteDialog);
        adapter.setOnObjectClickListener(this::openRenameDialog);

        mViewModel.openBox();

        view.findViewById(R.id.addNewNoteButton).setOnClickListener(v -> openInputDialog());

        EditText catText = view.findViewById(R.id.cat_line);


        catText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mViewModel.setCategory(s.toString());
                mViewModel.getEntitiesList().setValue(mViewModel.getEntitiesList().getValue());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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

    void openRenameDialog(Entity entity) {
        InputEntityDialog dialog = new InputEntityDialog(entity);
        dialog.show(getParentFragmentManager(), null);
    }
}
