package com.squorpikkor.app.treasurebox.fragment;

import static com.squorpikkor.app.treasurebox.MainViewModel.VIBE_TIME;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.squorpikkor.app.treasurebox.MainViewModel;
import com.squorpikkor.app.treasurebox.R;

public class PassFragment extends Fragment {

    public static PassFragment newInstance() {
        return new PassFragment();
    }
    private MainViewModel mViewModel;
    Vibrator vibe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pass, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        vibe = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);

        TextView passLine = view.findViewById(R.id.password_line);
        TextView loginLine = view.findViewById(R.id.login_line);

        loginLine.setText(mViewModel.loadLogin());

        mViewModel.getPassLine().observe(requireActivity(), passLine::setText);

        view.findViewById(R.id.button1).setOnClickListener(v -> mViewModel.clickButton(1, vibe));
        view.findViewById(R.id.button2).setOnClickListener(v -> mViewModel.clickButton(2, vibe));
        view.findViewById(R.id.button3).setOnClickListener(v -> mViewModel.clickButton(3, vibe));
        view.findViewById(R.id.button4).setOnClickListener(v -> mViewModel.clickButton(4, vibe));
        view.findViewById(R.id.button5).setOnClickListener(v -> mViewModel.clickButton(5, vibe));
        view.findViewById(R.id.button6).setOnClickListener(v -> mViewModel.clickButton(6, vibe));
        view.findViewById(R.id.button7).setOnClickListener(v -> mViewModel.clickButton(7, vibe));
        view.findViewById(R.id.button8).setOnClickListener(v -> mViewModel.clickButton(8, vibe));
        view.findViewById(R.id.button9).setOnClickListener(v -> mViewModel.clickButton(9, vibe));
        view.findViewById(R.id.button0).setOnClickListener(v -> mViewModel.clickButton(0, vibe));

        view.findViewById(R.id.button_cl).setOnClickListener(v -> {
            vibe.vibrate(VIBE_TIME);
            mViewModel.clearStroke();
        });
        view.findViewById(R.id.button_ok).setOnClickListener(v -> {
            vibe.vibrate(VIBE_TIME);
            mViewModel.setLoginAndPassword(loginLine.getText().toString());
            openListFragment();
        });//todo перед открытием проверять пароль (сейчас: если пароль неправильный, то список не загружается)
        view.findViewById(R.id.button_ok).setOnLongClickListener(v -> {
            if (view.findViewById(R.id.button_cl).isPressed() && view.findViewById(R.id.button1).isPressed() ) {
                vibe.vibrate(VIBE_TIME);
                Toast.makeText(requireActivity(), "Добавление нового аккаунта", Toast.LENGTH_SHORT).show();
                mViewModel.setLoginAndPassword(loginLine.getText().toString());
                mViewModel.addPasswordAndLogin();
            }
            return false;
        });

        mViewModel.closeBox();

        return view;
    }

    void openListFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ListFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }
}