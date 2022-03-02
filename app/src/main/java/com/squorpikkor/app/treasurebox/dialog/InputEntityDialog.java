package com.squorpikkor.app.treasurebox.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.squorpikkor.app.treasurebox.Entity;
import com.squorpikkor.app.treasurebox.R;

import java.util.ArrayList;

/**Диалог используется как для создания новых Entity, так и для обновления данных уже существующих
 *
 * Для вызова варианта создания, используется конструктор без параметроов
 * Для вызова варианта обновления, используется конструктор с параметром Entity. А значит, если
 * entity не равен null, то загружаем в TextView данные из полученного  entity
 * При addEntityToDB тоже проверчется entity: если null, то добавляем новый entity в БД; если
 * не null, обновляем существующий entity в БД
 * */
public class InputEntityDialog extends BaseDialog{

    private Entity entity;
    private EditText nameText, loginText, passText, emailText, addsText;
    private Spinner spinner;

    /**Диалог редактирования (обновления) уже существующей записи*/
    public InputEntityDialog(Entity entity) {
        this.entity = entity;
    }

    /**Диалог создания новой записи*/
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
        spinner = view.findViewById(R.id.spinner);
        updateCatList(mViewModel.getCatList().getValue());

        if (entity != null) {
            nameText.setText(entity.getName());
            loginText.setText(entity.getLogin());
            passText.setText(entity.getPass());
            emailText.setText(entity.getEmail());
            addsText.setText(entity.getAdds());
            spinner.setSelection(mViewModel.getCatPosition(entity.getCat()));
            //catText.setText(entity.getCat());
        }

        return dialog;
    }

    private void addEntityToDB() {
        String name = nameText.getText().toString();
        String login = loginText.getText().toString();
        String pass = passText.getText().toString();
        String email = emailText.getText().toString();
        String adds = addsText.getText().toString();
        //String cat = catText.getText().toString();
        String cat = mViewModel.getCatList().getValue().get(spinner.getSelectedItemPosition());
        if (entity==null) mViewModel.addEntityToDB(new Entity(name, login, pass, email, adds, cat));
        else mViewModel.updateEntityToDB(new Entity(name, login, pass, email, adds, cat, entity.getDocName()));
        dismiss();
    }

    private void updateCatList(ArrayList<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
