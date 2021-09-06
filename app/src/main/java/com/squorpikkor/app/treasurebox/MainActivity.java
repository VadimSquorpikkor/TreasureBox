package com.squorpikkor.app.treasurebox;

import static com.squorpikkor.app.treasurebox.MainViewModel.PRESS_CLEAR_BUTTON;
import static com.squorpikkor.app.treasurebox.MainViewModel.PRESS_OK_BUTTON;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mViewModel;
    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        TextView passLine = findViewById(R.id.password_line);

        mViewModel.getEntitiesList().observe(this, this::updateTreasureList);
        mViewModel.getPassLine().observe(this, passLine::setText);

        findViewById(R.id.button1).setOnClickListener(v -> mViewModel.clickButton(1));
        findViewById(R.id.button2).setOnClickListener(v -> mViewModel.clickButton(2));
        findViewById(R.id.button3).setOnClickListener(v -> mViewModel.clickButton(3));
        findViewById(R.id.button4).setOnClickListener(v -> mViewModel.clickButton(4));
        findViewById(R.id.button5).setOnClickListener(v -> mViewModel.clickButton(5));
        findViewById(R.id.button6).setOnClickListener(v -> mViewModel.clickButton(6));
        findViewById(R.id.button7).setOnClickListener(v -> mViewModel.clickButton(7));
        findViewById(R.id.button8).setOnClickListener(v -> mViewModel.clickButton(8));
        findViewById(R.id.button9).setOnClickListener(v -> mViewModel.clickButton(9));
        findViewById(R.id.button0).setOnClickListener(v -> mViewModel.clickButton(0));

        findViewById(R.id.button_cl).setOnClickListener(v -> mViewModel.clickButton(PRESS_CLEAR_BUTTON));
        findViewById(R.id.button_ok).setOnClickListener(v -> mViewModel.clickButton(PRESS_OK_BUTTON));
    }

    private void updateTreasureList(ArrayList<Entity> entities) {
        if (entities==null||entities.size()==0)return;
        Log.e(TAG, "updateTreasureList: "+entities.get(0).getName());
        Toast.makeText(this, entities.get(0).getName(), Toast.LENGTH_SHORT).show();
    }

}