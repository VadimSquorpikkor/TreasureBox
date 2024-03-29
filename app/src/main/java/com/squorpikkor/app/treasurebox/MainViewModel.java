package com.squorpikkor.app.treasurebox;

import android.os.Vibrator;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.squorpikkor.app.treasurebox.data.Bridge;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

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
     *
     * Важно: теперь ни один класс приложения (ни адаптер, ни helper, ни viewModel, ни диалоги)
     * ничего не знают о наличии шифрования. Шифрование и дешифрование происходит в классе Bridge.
     * Этот клас является "прослойкой" между DBHelper (который получает из БД данные в зашифрованном
     * виде) и ViewModel, которая работает с данными в расшифрованном виде. ViewModel и DBHelper
     * обмениваются данными через Bridge, который является по сути переводчиком, и не в курсе, что
     * общаются друг с другом на разных языках. Соответственно данные в ViewModel можно сортировать
     * и искать, так как они уже на этом этапе расшифрованы
     *
     * Впрочем, ViewModel и BDHelper ничего не знают друг о друге
     * */

    public static final String TAG = "TAG";
    public static final int VIBE_TIME = 50;

//    private final FireDBHelper db;
    private final Bridge bridge;
    private final MutableLiveData<ArrayList<Entity>> entitiesList;

    private final MutableLiveData<String> passLine;

    private final MutableLiveData<ArrayList<String>> catList;

    private String login;
    private String pass;//todo объединить pass и passLine ()

    //Текущая выбранная категория
    private String catNow;
    public static final String ALL = "all";
    public static final String FINANCE = "finance";

    public MainViewModel() {
        entitiesList = new MutableLiveData<>();
        bridge = new Bridge(entitiesList);
        passLine = new MutableLiveData<>();
        pass = "";
        passLine.setValue("");
        catNow = ALL;
        ArrayList<String> list = new ArrayList<>();
        list.add(ALL);
        list.add("Жена");
        catList = new MutableLiveData<>(list);
    }

    /**Несмотря на название, метод передает (и сохраняет) только логин, пароль не передается, так
     * как он изначально записывается посимвольно в viewModel; очевиднее было бы в фрагменте
     * передавать и пароль и логин (и размещать всю логику click кнопок и вывода строки со
     * звездочками и не засорять viewModel), но пока так, потому что просто решаю сохранение
     * состояния UI после поворота устройства. Потом перенесу логику кнопок в фрагмент, но пока так*/
    public void setLoginAndPassword(String login/*, String password*/) {
        this.login = login;
        saveLogin(login);
        bridge.createMainKey(this.pass, this.login);
    }

    public void clearStroke() {
        pass = "";
        passLine.setValue("");
    }

    public void clickButton(int i, Vibrator vibe) {
        vibe.vibrate(VIBE_TIME);
        pass+=i;
        passLine.setValue(passLine.getValue()+"*");
    }

    public void openBox() {
        bridge.getEntities(login);
    }

    public static final String KEY_LOGIN = "key_login";

    public void saveLogin(String login) {
        SaveLoad.save(KEY_LOGIN, login);
    }

    public String loadLogin() {
        return SaveLoad.getString(KEY_LOGIN);
    }

    /**Все поля объекта Entity сразу шифруются, затем этот шифрованный Entity передается в FireDBHelper*/
    public void addEntityToDB(Entity entity) {
        bridge.addNewEventListener(login);
        bridge.addEntityToDB(login, entity);
    }

    public void updateEntityToDB(Entity entity) {
        bridge.addNewEventListener(login);
        bridge.updateEntityToDB(login, entity);
    }

    /**Метод записывает в БД пароль. Если коллекции для этого пользователя ещё нет, она будет создана*/
    public void addPasswordAndLogin() {
        bridge.createMainKey(this.pass, this.login);
        bridge.addPassword(login);
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
        bridge.addNewEventListener(login);
        bridge.deleteDocument(login, docName);
    }

    /**Сортировка списка в алфавитном порядке. Сортируются в не зависимости от регистра*/
    public void sortList(ArrayList<Entity> list) {
        //noinspection ComparatorCombinators
        Collections.sort(list, (o1, o2) -> o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase()));
    }

    public ArrayList<Entity> getFilteredByCategory(ArrayList<Entity> list) {
        sortList(list);
        if (catNow.equals(ALL)) return list;
        ArrayList<Entity> filteredList = new ArrayList<>();
        for (Entity entity:list) {
            if (entity.getCat().equals(catNow)) filteredList.add(entity);
        }
        return filteredList;
    }

    public MutableLiveData<ArrayList<String>> getCatList() {
        return catList;
    }

    /**
     *
     * Получить в списке категорий номер позиции этой категории. На эту позицию переключаю спиннер.
     * Если такой категории не найдено (была удалена или переименована), спинер переключится в 0-ю
     * позицию (первую по счету)*/
    public int getCatPosition(String s) {
        if (s.equals("")) return 0;
        if (catList.getValue().size()==1) return 0;
        for (int i = 0; i < catList.getValue().size(); i++) {
            if (catList.getValue().get(i).equals(s)) return i;
        }
        return 0;//если такой категории нет, то самая первая категория (ALL)
    }

    public void setCategory(String cat) {
        this.catNow = cat;
    }

    /**Поиск в именах заметок по совпадению со строкой. Если строка содержит 2 и меньее символов,
     * такой поиск игнорируется, список не фильтруется. При поиске игнорируется регистр, поиск и по
     * "EBAY", и по "ebay" покажет то же самое*/
    public ArrayList<Entity> getMatchedList(ArrayList<Entity> list, String s) {
        sortList(list);
        if (s.length()<2) return list;
        ArrayList<Entity> filteredList = new ArrayList<>();
        for (Entity entity:list) {
            if (entity.getName().toLowerCase().contains(s.toLowerCase(Locale.ROOT))) filteredList.add(entity);
        }
        return filteredList;
    }
}
