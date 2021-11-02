package com.squorpikkor.app.treasurebox.data;

import androidx.lifecycle.MutableLiveData;

import com.squorpikkor.app.treasurebox.Entity;
import com.squorpikkor.app.treasurebox.crypto.Encrypter2;

import java.util.ArrayList;

/**Подправлена архитектура: теперь ни один класс приложения (ни адаптер, ни helper, ни viewModel,
 * ни диалоги) ничего не знают о наличии шифрования. Шифрование и дешифрование происходит в классе
 * Bridge. Этот клас является "прослойкой" между DBHelper (который получает из БД данные в
 * зашифрованном виде) и ViewModel, которая работает с данными в расшифрованном виде. ViewModel и
 * DBHelper обмениваются данными через Bridge, который является по сути переводчиком, и не в курсе,
 * что общаются друг с другом на разных языках. Соответственно теперь данные в ViewModel можно будет
 * и сортировать и искать, так как теперь они уже на этом этапе расшифрованы*/
public class Bridge {

    private final MutableLiveData<ArrayList<Entity>> entitiesList;
    private final FireDBHelper fireDBHelper;
    private String main_key;//ключ шифрования

    private final MutableLiveData<ArrayList<Entity>> encodedEntitiesList;

    public void createMainKey(String pass, String login) {
        main_key = pass+login;
    }

    public Bridge(MutableLiveData<ArrayList<Entity>> entitiesList) {
        this.entitiesList = entitiesList;
        this.encodedEntitiesList = new MutableLiveData<>();
        this.fireDBHelper = new FireDBHelper(encodedEntitiesList);
        encodedEntitiesList.observeForever(this::decodeList);
    }

    private void decodeList(ArrayList<Entity> list) {
        ArrayList<Entity> decodedList = new ArrayList<>();
        for (Entity entity:list) {
            Entity decodedEntity = new Entity(
                    Encrypter2.decrypt(main_key, entity.getName()),
                    Encrypter2.decrypt(main_key, entity.getLogin()),
                    Encrypter2.decrypt(main_key, entity.getPass()),
                    Encrypter2.decrypt(main_key, entity.getEmail()),
                    Encrypter2.decrypt(main_key, entity.getAdds()),
                    entity.getDocName()
            );
            decodedList.add(decodedEntity);
        }
        entitiesList.setValue(decodedList);
    }

    public void addEntityToDB(String tableName, Entity entity) {
        Entity codedEntity = new Entity(
                Encrypter2.encrypt(main_key, entity.getName()),
                Encrypter2.encrypt(main_key, entity.getLogin()),
                Encrypter2.encrypt(main_key, entity.getPass()),
                Encrypter2.encrypt(main_key, entity.getEmail()),
                Encrypter2.encrypt(main_key, entity.getAdds())
        );
        fireDBHelper.addEntityToDB(tableName, codedEntity);
    }

    public void updateEntityToDB(String login, Entity entity) {
        Entity codedEntity = new Entity(
                Encrypter2.encrypt(main_key, entity.getName()),
                Encrypter2.encrypt(main_key, entity.getLogin()),
                Encrypter2.encrypt(main_key, entity.getPass()),
                Encrypter2.encrypt(main_key, entity.getEmail()),
                Encrypter2.encrypt(main_key, entity.getAdds())
        );
        fireDBHelper.updateEntityToDB(login, codedEntity, entity.getDocName());
    }

    public void addNewEventListener(String login) {
        fireDBHelper.addNewEventListener(login, Encrypter2.encrypt(main_key, main_key));
    }

    public void addPassword(String login) {
        fireDBHelper.addPassword(login, Encrypter2.encrypt(main_key, main_key));
    }

    public void deleteDocument(String login, String docName) {
        fireDBHelper.deleteDocument(login, docName);
    }

    public void getEntities(String login) {
        fireDBHelper.getEntities(login, Encrypter2.encrypt(main_key, main_key));
    }
}
