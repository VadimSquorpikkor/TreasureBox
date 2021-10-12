package com.squorpikkor.app.treasurebox;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.squorpikkor.app.treasurebox.crypto.Encrypter2;

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

    /**Название поля для имени в БД*/
    public static final String ENTITY_NAME = "1";
    /**Название поля для пароля в БД*/
    public static final String ENTITY_PASS = "2";
    /**Название поля для логина в БД, не путать с логином пользователя приложения*/
    public static final String ENTITY_LOGIN = "3";
    /**Название документа с паролем в БД*/
    public static final String PASS_DOCUMENT = "0";

    public static final String TAG = "TAG";
    public static final int PRESS_CLEAR_BUTTON = 100;
    public static final int PRESS_OK_BUTTON = 101;

    private final FireDBHelper db;
    private final MutableLiveData<ArrayList<Entity>> entitiesList;
    private final MutableLiveData<String> passLine;

    private String login;
    private String pass;//todo объединить pass и passLine ()

    public MainViewModel() {
//        PASS_DOCUMENT = Encrypter2.encrypt("squorpikkor", "_password_key_");
        entitiesList = new MutableLiveData<>();
        db = new FireDBHelper(entitiesList);
        passLine = new MutableLiveData<>();
        pass = "";
        passLine.setValue("");
        proverochka();
//        addPassword();
    }

    public String getLogin() {
        return login;
    }

    private void proverochka() {
        login = "squorpikkor";
    }

    public void clearStroke() {
        pass = "";
        passLine.setValue("");
    }

    public void clickButton(int i) {
        pass+=i;
        passLine.setValue(passLine.getValue()+"*");
    }

    public void openBox() {
        db.getEntities(login, Encrypter2.encrypt(login, pass));
//        pass = "";
//        passLine.setValue("");
    }

    /**Все поля объекта Entity сразу шифруются, затем этот шифрованный Entity передается в FireDBHelper*/
    void addEntityToDB(String login, Entity entity) {
        Entity codedEntity = new Entity(
                Encrypter2.encrypt(login, entity.getName()),
                Encrypter2.encrypt(login, entity.getLogin()),
                Encrypter2.encrypt(login, entity.getPass())
        );
        db.addNewEventListener(login, Encrypter2.encrypt(login, pass));
        db.addUnitToDB(login, codedEntity);
    }

    /**Метод записывает в БД пароль. Если коллекции для этого пользователя ещё нет, она будет создана*/
    public void addPassword() {
        db.addPassword(login, Encrypter2.encrypt(login, "2985984"));
    }

    public MutableLiveData<ArrayList<Entity>> getEntitiesList() {
        return entitiesList;
    }
    public MutableLiveData<String> getPassLine() {
        return passLine;
    }

    public void addNewEventListener() {
        db.addNewEventListener(login, Encrypter2.encrypt(login, pass));
    }


    public void closeBox() {
        pass = "";
        passLine.setValue("");
        entitiesList.setValue(new ArrayList<>());
    }
}
