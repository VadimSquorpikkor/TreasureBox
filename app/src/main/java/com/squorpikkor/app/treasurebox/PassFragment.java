package com.squorpikkor.app.treasurebox;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class PassFragment extends Fragment {

    public static PassFragment newInstance() {
        return new PassFragment();
    }
    private MainViewModel mViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pass, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        TextView passLine = view.findViewById(R.id.password_line);
        TextView loginLine = view.findViewById(R.id.login_line);

        loginLine.setText(mViewModel.loadLogin());

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

        view.findViewById(R.id.button_cl).setOnClickListener(v -> mViewModel.clearStroke());
        view.findViewById(R.id.button_ok).setOnClickListener(v -> openListFragment(loginLine.getText().toString()));//todo перед открытием проверять пароль (сейчас: если пароль неправильный, то список не загружается)
        view.findViewById(R.id.button_ok).setOnLongClickListener(v -> {
            if (view.findViewById(R.id.button_cl).isPressed() && view.findViewById(R.id.button1).isPressed() ) {
                Toast.makeText(requireActivity(), "Добавление нового аккаунта", Toast.LENGTH_SHORT).show();
                mViewModel.addPasswordAndLogin(loginLine.getText().toString());
            }
            return false;
        });

        mViewModel.closeBox();

        return view;
    }

    void openListFragment(String login) {
        mViewModel.setLogin(login);
        mViewModel.saveLogin(login);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ListFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }
}