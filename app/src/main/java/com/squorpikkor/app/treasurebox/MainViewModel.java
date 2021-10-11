package com.squorpikkor.app.treasurebox;

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
     * 3. Если коллекция существует, то в ней ищется документ _password_key_ .
     * В этом документе хранится пароль к коллекции. Если введенный пароль совпал с паролем в
     * коллекции, получаем данные из БД
     * 4. В коллекции каждый документ -- это отдельная сущность (имя+логин+пароль(возможно + ещё что-то))
     * 5. В документе поля могут иметь название: "title", "email", "login", "password" и любые другие,
     * количество может быть любым, название задается firebase автоматом, количество полей и их
     * имена задаются в приложении при создании (или редактировании) новой сущности (или админом
     * напрямую в firebase БД)
     * */

    public static final String TAG = "TAG";
    public static final int PRESS_CLEAR_BUTTON = 100;
    public static final int PRESS_OK_BUTTON = 101;

    private final FireDBHelper db;
    private final MutableLiveData<ArrayList<Entity>> entitiesList;
    private final MutableLiveData<String> passLine;

    private String login;
    private String pass;//todo объединить pass и passLine ()

    public MainViewModel() {
        entitiesList = new MutableLiveData<>();
        db = new FireDBHelper(entitiesList);
        passLine = new MutableLiveData<>();
        pass = "";
        passLine.setValue("");

        proverochka();
    }

    public String getLogin() {
        return login;
    }

    private void proverochka() {
        login = "squorpikkor";
    }

    public void clickButton(int i) {
        if (i == PRESS_CLEAR_BUTTON) {
            pass = "";
            passLine.setValue("");
        } else {
            pass+=i;
            passLine.setValue(passLine.getValue()+"*");
        }
    }

    public void openBox() {
        db.getEntitiesByParam(login, Encrypter.decodeMe(pass));
        pass = "";
        passLine.setValue("");
    }

    /**Все поля объекта Entity сразу шифруются, затем этот шифрованный Entity передается в FireDBHelper*/
    void addEntityToDB(String login, Entity entity) {
        Entity codedEntity = new Entity(
                Encrypter.decodeMe(entity.getName()),
                Encrypter.decodeMe(entity.getLogin()),
                Encrypter.decodeMe(entity.getPass())
        );
        db.addUnitToDB(login, codedEntity);
    }

    public void addPassword() {
        db.addPassword(login, Encrypter.decodeMe("2985984"));
    }

    public MutableLiveData<ArrayList<Entity>> getEntitiesList() {
        return entitiesList;
    }
    public MutableLiveData<String> getPassLine() {
        return passLine;
    }




}
