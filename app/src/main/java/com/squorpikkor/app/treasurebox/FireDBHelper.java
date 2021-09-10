package com.squorpikkor.app.treasurebox;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Objects;

import static com.squorpikkor.app.treasurebox.MainViewModel.TAG;

class FireDBHelper {

    public static final String ENTITY_NAME = "name";
    public static final String ENTITY_PASS = "pass";
    public static final String ENTITY_LOGIN = "login";

    private final FirebaseFirestore db;

    private final MutableLiveData<ArrayList<Entity>> entitiesList;

    public FireDBHelper(MutableLiveData<ArrayList<Entity>> entitiesList) {
        db = FirebaseFirestore.getInstance();
        this.entitiesList = entitiesList;
    }

    void getEntitiesByParam(String login, String password) {

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
                        DocumentReference docRef = db.collection(login).document("_password_key_");
                        docRef.get().addOnCompleteListener(task2 -> {
                            if (task2.isSuccessful()) {
                                DocumentSnapshot document = task2.getResult();
                                if (document == null) return;
                                if (querySnapshot.isEmpty()) return;
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    if (!Objects.equals(document.get("pass"), password)) {
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

}
