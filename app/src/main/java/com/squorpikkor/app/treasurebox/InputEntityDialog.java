package com.squorpikkor.app.treasurebox;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

public class InputEntityDialog extends BaseDialog{

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        initializeWithVM(R.layout.dialog_input);

        Button addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(v -> addEntityToDB());


        return dialog;
    }

    private void addEntityToDB() {
        EditText nameText = view.findViewById(R.id.name);
        EditText loginText = view.findViewById(R.id.login);
        EditText passText = view.findViewById(R.id.pass);
        EditText emailText = view.findViewById(R.id.email);
        EditText addsText = view.findViewById(R.id.adds);
        String name = nameText.getText().toString();
        String login = loginText.getText().toString();
        String pass = passText.getText().toString();
        String email = emailText.getText().toString();
        String adds = addsText.getText().toString();
        mViewModel.addEntityToDB(mViewModel.getLogin(), new Entity(name, login, pass, email, adds));
        dismiss();
    }
}
