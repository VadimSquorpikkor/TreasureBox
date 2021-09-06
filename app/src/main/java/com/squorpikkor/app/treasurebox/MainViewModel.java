package com.squorpikkor.app.treasurebox;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MainViewModel  extends ViewModel {

    /**1. Для авторизации используется логин и пароль (не аккаунт google: а) так при смене аккаунта не
     * надо думать, как восстановить сохраненные данные); б) можно сделать несколько учеток,
     * например для основных паролей и второстепенных)
     * 2. Каждому логину соответствует отдельная коллекция
     * После ввода логина и пароля приложение подключается к БД и ищет в ней коллекцию с именем
     * логина. Если такой коллекции в БД нет, выводится сообщение "Нет такого пользователя!"
     * 3. Если коллекция существует, то в ней ищется документ _password_to_collection (название ещё
     * подумаю). В этом документе хранится пароль к коллекции. Пароль хранится не как значение ключа,
     * а как имя field, т.е. в документе ищется field (value не используется) и если не находится,
     * то выводится "Неправильный пароль!". Если хранить как значение (value), то получается при
     * проверке пароля будет из БД браться правильный пароль и сравниваться с введенным пользователем,
     * т.е. некто будет всегда получать правильный пароль, даже не зная его. Как-то не камильфо. Если
     * же хранить пароль как имя поля (field), то на запрос о проверке пароля может быть только 2
     * варианта: найден (правильный) и не найден (неправильный)
     * 4. В коллекции каждый документ -- это отдельная сущность (имя+логин+пароль(возможно + ещё что-то))
     * 5. В документе поля могут иметь название: "title", "email", "login", "password" и любые другие,
     * количество может быть любым, название задается firebase автоматом, количество полей и их
     * имена задаются в приложении при создании (или редактировании) новой сущности (или админом
     * напрямую в firebase БД)
     * */

    public static final String TAG = "TAG";
    private static final String RIGHT_PASS = "2985984";
    public static final int PRESS_CLEAR_BUTTON = 100;
    public static final int PRESS_OK_BUTTON = 101;

    private FireDBHelper db;
    private MutableLiveData<ArrayList<Entity>> entitiesList;
    private MutableLiveData<String> passLine;

    private String login;
    private String password;
    private String pass;

    public MainViewModel() {
        db = new FireDBHelper();
        entitiesList = new MutableLiveData<>();
        passLine = new MutableLiveData<>();
        pass = "";
        passLine.setValue("");


        proverochka();
    }

    private void proverochka() {
        login = "squorpikkor";
        password = "2985984";
    }

    public void clickButton(int i) {
        if (i == PRESS_CLEAR_BUTTON) {
            pass = "";
            passLine.setValue("");
        } else if (i == PRESS_OK_BUTTON) {
            openBox();
        } else {
            pass+=i;
            passLine.setValue(passLine.getValue()+"*");
        }
    }

    private void openBox() {
        if (pass.equals(RIGHT_PASS)) getEntitiesFromDB();
        else Log.e(TAG, "wrong: "+pass);
    }

    public MutableLiveData<ArrayList<Entity>> getEntitiesList() {
        return entitiesList;
    }
    public MutableLiveData<String> getPassLine() {
        return passLine;
    }

    void getEntitiesFromDB() {
        db.getEntitiesByParam(entitiesList, login, password);
    }



}
