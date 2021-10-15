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

    public static final String TAG = "TAG";

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
    }

    public String getLogin() {
        return login;
    }

    /*public String getEncryptedLogin() {
        return Encrypter2.encrypt(login, login);
    }*/

    public void setLogin(String login) {
        this.login = login;
    }

    public void clearStroke() {
        pass = "";
        passLine.setValue("");
    }

    public void clickButton(int i) {
        pass+=i;
        passLine.setValue(passLine.getValue()+"*");
//        passLine.setValue(passLine.getValue()+i);
    }

    public void openBox() {
        db.getEntities(Encrypter2.encrypt(login, login), Encrypter2.encrypt(login, pass));
//        pass = "";
//        passLine.setValue("");
    }

    public static final String KEY_LOGIN = "key_login";

    public void saveLogin(String login) {
        SaveLoad.save(KEY_LOGIN, login);
    }

    public String loadLogin() {
        return SaveLoad.getString(KEY_LOGIN);
    }

    /**Все поля объекта Entity сразу шифруются, затем этот шифрованный Entity передается в FireDBHelper*/
    public void addEntityToDB(String login, Entity entity) {
        Entity codedEntity = new Entity(
                Encrypter2.encrypt(login, entity.getName()),
                Encrypter2.encrypt(login, entity.getLogin()),
                Encrypter2.encrypt(login, entity.getPass()),
                Encrypter2.encrypt(login, entity.getEmail()),
                Encrypter2.encrypt(login, entity.getAdds())
        );
        db.addNewEventListener(Encrypter2.encrypt(login, login), Encrypter2.encrypt(login, pass));
        db.addUnitToDB(Encrypter2.encrypt(login, login), codedEntity);
    }

    /**Метод записывает в БД пароль. Если коллекции для этого пользователя ещё нет, она будет создана*/
    public void addPasswordAndLogin(String login) {
        db.addPassword(Encrypter2.encrypt(login, login), Encrypter2.encrypt(login, pass));
    }

    public MutableLiveData<ArrayList<Entity>> getEntitiesList() {
        return entitiesList;
    }
    public MutableLiveData<String> getPassLine() {
        return passLine;
    }

    public void closeBox() {
        pass = "";
        passLine.setValue("");
        entitiesList.setValue(new ArrayList<>());
    }

    public void deleteDocumentByName(String docName) {
        db.addNewEventListener(Encrypter2.encrypt(login, login), Encrypter2.encrypt(login, pass));
        db.deleteDocument(Encrypter2.encrypt(login, login), docName);
    }
}
