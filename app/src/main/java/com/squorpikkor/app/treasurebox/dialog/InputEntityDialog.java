package com.squorpikkor.app.treasurebox.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.squorpikkor.app.treasurebox.Entity;
import com.squorpikkor.app.treasurebox.R;
import com.squorpikkor.app.treasurebox.crypto.Encrypter2;

public class InputEntityDialog extends BaseDialog{

    private Entity entity;
    private EditText nameText, loginText, passText, emailText, addsText;

    public InputEntityDialog(Entity entity) {
        this.entity = entity;
    }

    public InputEntityDialog() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        initializeWithVM(R.layout.dialog_input);

        Button addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(v -> addEntityToDB());
        if (entity!=null)addButton.setText("Обновить");

        nameText = view.findViewById(R.id.name);
        loginText = view.findViewById(R.id.login);
        passText = view.findViewById(R.id.pass);
        emailText = view.findViewById(R.id.email);
        addsText = view.findViewById(R.id.adds);

        if (entity != null) {
            nameText.setText(entity.getName());
            loginText.setText(entity.getLogin());
            passText.setText(entity.getPass());
            emailText.setText(entity.getEmail());
            addsText.setText(entity.getAdds());
        }

        return dialog;
    }

    private void addEntityToDB() {
        String name = nameText.getText().toString();
        String login = loginText.getText().toString();
        String pass = passText.getText().toString();
        String email = emailText.getText().toString();
        String adds = addsText.getText().toString();
        if (entity==null) mViewModel.addEntityToDB(new Entity(name, login, pass, email, adds));
        else mViewModel.updateEntityToDB(new Entity(name, login, pass, email, adds, entity.getDocName()));
        dismiss();
    }
}
