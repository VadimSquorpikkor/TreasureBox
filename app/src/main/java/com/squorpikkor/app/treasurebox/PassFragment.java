package com.squorpikkor.app.treasurebox;

import static com.squorpikkor.app.treasurebox.MainViewModel.PRESS_CLEAR_BUTTON;
import static com.squorpikkor.app.treasurebox.MainViewModel.PRESS_OK_BUTTON;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PassFragment extends Fragment {

    public static PassFragment newInstance() {
        return new PassFragment();
    }


    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pass, container, false);
        MainViewModel mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        TextView passLine = view.findViewById(R.id.password_line);


        mViewModel.getPassLine().observe(requireActivity(), passLine::setText);

        view.findViewById(R.id.button1).setOnClickListener(v -> mViewModel.clickButton(1));
        view.findViewById(R.id.button2).setOnClickListener(v -> mViewModel.clickButton(2));
        view.findViewById(R.id.button3).setOnClickListener(v -> mViewModel.clickButton(3));
        view.findViewById(R.id.button4).setOnClickListener(v -> mViewModel.clickButton(4));
        view.findViewById(R.id.button5).setOnClickListener(v -> mViewModel.clickButton(5));
        view.findViewById(R.id.button6).setOnClickListener(v -> mViewModel.clickButton(6));
        view.findViewById(R.id.button7).setOnClickListener(v -> mViewModel.clickButton(7));
        view.findViewById(R.id.button8).setOnClickListener(v -> mViewModel.clickButton(8));
        view.findViewById(R.id.button9).setOnClickListener(v -> mViewModel.clickButton(9));
        view.findViewById(R.id.button0).setOnClickListener(v -> mViewModel.clickButton(0));

        view.findViewById(R.id.button_cl).setOnClickListener(v -> mViewModel.clickButton(PRESS_CLEAR_BUTTON));
        view.findViewById(R.id.button_ok).setOnClickListener(v -> openListFragment());

//        mViewModel.addPassword();

        return view;
    }

    void openListFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ListFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }





}