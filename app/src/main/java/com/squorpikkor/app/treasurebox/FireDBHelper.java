package com.squorpikkor.app.treasurebox;

import android.util.Log;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;

import static com.squorpikkor.app.treasurebox.MainViewModel.TAG;

class FireDBHelper {

    public static final String ENTITY_NAME = "name";
    public static final String ENTITY_PASS = "pass";
    public static final String ENTITY_LOGIN = "login";

    private final FirebaseFirestore db;

    public FireDBHelper() {
        db = FirebaseFirestore.getInstance();
    }

    void getEntitiesByParam(MutableLiveData<ArrayList<Entity>> entitiesList, String login, String password) {
        Query query = db.collection(login);//Логин -- это название коллекции. Другими словами у
        // каждого пользователя пароли храняться в отдельной коллекции, название которой -- это имя пользоватеоля

        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot == null) return;
                        ArrayList<Entity> list = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Entity entity = getEntityFromSnapshot(document);
                            list.add(entity);
                        }
                        entitiesList.setValue(list);
                    } else {
                        Log.e(TAG, "Error getting documents: ", task.getException());
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
