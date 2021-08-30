package com.squorpikkor.app.treasurebox;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    private static final String RIGHT_PASS = "2985984";
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


    private void clickButton(int i) {
        pass+=i;
        if (pass.equals(RIGHT_PASS)) openBox();
        else Log.e(TAG, "wrong: "+pass);
    }

    private void openBox() {
        Log.e(TAG, "openBox: ");
    }


}