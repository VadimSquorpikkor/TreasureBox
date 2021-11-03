package com.squorpikkor.app.treasurebox.data;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squorpikkor.app.treasurebox.Entity;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.squorpikkor.app.treasurebox.MainViewModel.TAG;

class FireDBHelper {

    /**Название поля для имени в БД*/
    public static final String ENTITY_NAME = "1";
    /**Название поля для пароля в БД*/
    public static final String ENTITY_PASS = "2";
    /**Название поля для логина в БД, не путать с логином пользователя приложения*/
    public static final String ENTITY_LOGIN = "3";

    public static final String ENTITY_EMAIL = "4";

    public static final String ENTITY_ADDS = "5";

    public static final String ENTITY_CAT = "0";
    /**Название документа с паролем в БД*/
    public static final String PASS_DOCUMENT = "0";

    private final FirebaseFirestore db;

    private final MutableLiveData<ArrayList<Entity>> entitiesList;

    public FireDBHelper(MutableLiveData<ArrayList<Entity>> entitiesList) {
        db = FirebaseFirestore.getInstance();
        this.entitiesList = entitiesList;
    }

    /**Имя таблицы — это логин пользователя*/
    void addEntityToDB(String tableName, Entity entity) {
        Map<String, Object> data = new HashMap<>();
        data.put(ENTITY_NAME, entity.getName());
        data.put(ENTITY_LOGIN, entity.getLogin());
        data.put(ENTITY_PASS, entity.getPass());
        data.put(ENTITY_EMAIL, entity.getEmail());
        data.put(ENTITY_ADDS, entity.getAdds());
        data.put(ENTITY_CAT, entity.getCat());
        db.collection(tableName)
                .document()
                .set(data)
                .addOnSuccessListener(aVoid -> Log.e(TAG, "DocumentSnapshot successfully written!"))//todo toast
                .addOnFailureListener(e -> Log.e(TAG, "Error writing document", e));//todo toast
    }

    void updateEntityToDB(String tableName, Entity entity, String docName) {
        Map<String, Object> data = new HashMap<>();
        data.put(ENTITY_NAME, entity.getName());
        data.put(ENTITY_LOGIN, entity.getLogin());
        data.put(ENTITY_PASS, entity.getPass());
        data.put(ENTITY_EMAIL, entity.getEmail());
        data.put(ENTITY_ADDS, entity.getAdds());
        Log.e(TAG, "updateEntityToDB: "+entity.getCat());
        data.put(ENTITY_CAT, entity.getCat());
        db.collection(tableName)
                .document(docName)
                .set(data)
                .addOnSuccessListener(aVoid -> Log.e(TAG, "DocumentSnapshot successfully written!"))//todo toast
                .addOnFailureListener(e -> Log.e(TAG, "Error writing document", e));//todo toast
    }

    void addPassword(String tableName, String newPass) {
        if (tableName.equals("")||newPass.equals("")) return;

        db.collection(tableName).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot == null||querySnapshot.isEmpty()) {
                            Log.e(TAG, "НЕТ ТАКОГО ПОЛЬЗОВАТЕЛЯ!3!");
                            Map<String, Object> data = new HashMap<>();
                            data.put(ENTITY_PASS, newPass);
                            db.collection(tableName)
                                    .document(PASS_DOCUMENT)
                                    .set(data)
                                    .addOnSuccessListener(aVoid -> Log.e(TAG, "DocumentSnapshot successfully written!"))//todo toast
                                    .addOnFailureListener(e -> Log.e(TAG, "Error writing document", e));//todo toast
                        } else Log.e(TAG, "ТАКОЙ ПОЛЬЗОВАТЕЛЬ УЖЕ ЕСТЬ!!!");
                    }
                });
    }

    void getEntities(String login, String password) {
        if (login.equals("")||password.equals("")) return;

        Query query = db.collection(login);//Логин -- это название коллекции. Другими словами у
        // каждого пользователя пароли храняться в отдельной коллекции, название которой -- это имя пользоватеоля

        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot == null) return;
                        if (querySnapshot.isEmpty()) {
                            Log.e(TAG, "НЕТ ТАКОГО ПОЛЬЗОВАТЕЛЯ!3!");
                            return;
                        }
                        //Если пользователь есть — проверяем пароль
                        DocumentReference docRef = db.collection(login).document(PASS_DOCUMENT);
                        docRef.get().addOnCompleteListener(task2 -> {
                            if (task2.isSuccessful()) {
                                DocumentSnapshot document = task2.getResult();
                                if (document == null) return;
                                if (querySnapshot.isEmpty()) return;
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    if (!Objects.equals(document.get(ENTITY_PASS), password)) {
                                        Log.e(TAG, "НЕ ПРАВИЛЬНЫЙ ПАРОЛЬ!");
                                        return;
                                    }
                                    //Если пароль правильный, грузим данные

                                    ArrayList<Entity> list = new ArrayList<>();
                                    for (DocumentSnapshot document2 : task.getResult()) {
                                        Entity entity = getEntityFromSnapshot(document2);
                                        if (!entity.getDocName().equals(PASS_DOCUMENT)) list.add(entity);//Документ с паролем не грузим
                                    }
                                    entitiesList.setValue(list);
//                                    entitiesList.setValue(entitiesList.getValue());
                                }
                            }
                        });
                    }
                });
    }

    private Entity getEntityFromSnapshot(DocumentSnapshot document) {
        String name = String.valueOf(document.get(ENTITY_NAME));
        String pass = String.valueOf(document.get(ENTITY_PASS));
        String login = String.valueOf(document.get(ENTITY_LOGIN));
        String email = String.valueOf(document.get(ENTITY_EMAIL));
        String adds = String.valueOf(document.get(ENTITY_ADDS));
        String cat = String.valueOf(document.get(ENTITY_CAT));
        return new Entity(name, login, pass, email, adds, cat, document.getId());
    }

    /**Слушатель для новых событий*/
    void addNewEventListener(String login, String password) {
        db.collection(login).addSnapshotListener((queryDocumentSnapshots, error) -> getEntities(login, password));
    }

    void deleteDocument(String login, String docName) {
        db.collection(login).document(docName)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully deleted!"));
    }


}