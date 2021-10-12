package com.squorpikkor.app.treasurebox;

import android.util.Log;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.squorpikkor.app.treasurebox.MainViewModel.ENTITY_LOGIN;
import static com.squorpikkor.app.treasurebox.MainViewModel.ENTITY_NAME;
import static com.squorpikkor.app.treasurebox.MainViewModel.ENTITY_PASS;
import static com.squorpikkor.app.treasurebox.MainViewModel.PASS_DOCUMENT;
import static com.squorpikkor.app.treasurebox.MainViewModel.TAG;

class FireDBHelper {

    private final FirebaseFirestore db;

    private final MutableLiveData<ArrayList<Entity>> entitiesList;

    public FireDBHelper(MutableLiveData<ArrayList<Entity>> entitiesList) {
        db = FirebaseFirestore.getInstance();
        this.entitiesList = entitiesList;
    }

    /**Имя таблицы — это логин пользователя*/
    void addUnitToDB(String tableName, Entity entity) {
        Map<String, Object> data = new HashMap<>();
        data.put(ENTITY_NAME, entity.getName());
        data.put(ENTITY_LOGIN, entity.getLogin());
        data.put(ENTITY_PASS, entity.getPass());
        db.collection(tableName)
                .document()
                .set(data)
                .addOnSuccessListener(aVoid -> Log.e(TAG, "DocumentSnapshot successfully written!"))//todo toast
                .addOnFailureListener(e -> Log.e(TAG, "Error writing document", e));//todo toast
    }

    public void addPassword(String tableName, String newPass) {
        Map<String, Object> data = new HashMap<>();
        data.put(ENTITY_PASS, newPass);
        db.collection(tableName)
                .document(PASS_DOCUMENT)
                .set(data)
                .addOnSuccessListener(aVoid -> Log.e(TAG, "DocumentSnapshot successfully written!"))//todo toast
                .addOnFailureListener(e -> Log.e(TAG, "Error writing document", e));//todo toast
    }

    void getEntities(String login, String password) {
        Log.e(TAG, "getEntitiesByParam: password "+password);

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
                                        list.add(entity);
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
        return new Entity(name, login, pass);
    }

    /**Слушатель для новых событий*/
    void addNewEventListener(String login, String password) {
        db.collection(login).addSnapshotListener((queryDocumentSnapshots, error) -> getEntities(login, password));
    }

}
