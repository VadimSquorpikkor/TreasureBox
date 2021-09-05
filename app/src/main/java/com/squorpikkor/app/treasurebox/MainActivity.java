package com.squorpikkor.app.treasurebox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private MainViewModel mViewModel;
    private static final String TAG = "TAG";
    private static final String RIGHT_PASS = "2985984";
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        mViewModel.getEntitiesList().observe(this, new Observer<ArrayList<Entity>>() {
            @Override
            public void onChanged(ArrayList<Entity> entities) {
                updateTreasureList(entities);
            }
        });

        pass = "";

        findViewById(R.id.button1).setOnClickListener(v -> clickButton(1));
        findViewById(R.id.button2).setOnClickListener(v -> clickButton(2));
        findViewById(R.id.button3).setOnClickListener(v -> clickButton(3));
        findViewById(R.id.button4).setOnClickListener(v -> clickButton(4));
        findViewById(R.id.button5).setOnClickListener(v -> clickButton(5));
        findViewById(R.id.button6).setOnClickListener(v -> clickButton(6));
        findViewById(R.id.button7).setOnClickListener(v -> clickButton(7));
        findViewById(R.id.button8).setOnClickListener(v -> clickButton(8));
        findViewById(R.id.button9).setOnClickListener(v -> clickButton(9));
        findViewById(R.id.button0).setOnClickListener(v -> clickButton(0));
    }

    private void updateTreasureList(ArrayList<Entity> entities) {
        if (entities==null||entities.size()==0)return;
        Log.e(TAG, "updateTreasureList: "+entities.get(0).getName());
        Toast.makeText(this, entities.get(0).getName(), Toast.LENGTH_SHORT).show();
    }

    private void clickButton(int i) {
        mViewModel.getEntitiesFromDB();
        pass+=i;
        if (pass.equals(RIGHT_PASS)) openBox();
        else Log.e(TAG, "wrong: "+pass);
    }

    private void openBox() {
        Log.e(TAG, "openBox: ");
        mViewModel.getEntitiesFromDB();
        Toast.makeText(this, "Правильно", Toast.LENGTH_SHORT).show();
    }


}