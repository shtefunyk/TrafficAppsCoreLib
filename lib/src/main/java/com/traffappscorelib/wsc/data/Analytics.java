package com.traffappscorelib.wsc.data;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;

public class Analytics {

    public static class Data implements Serializable {
        public String url;
    }

    public static void sendAppsflyerData(String url) {
        Data data = new Data();
        data.url = url;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("af_deeplinks")
            .add(data)
            .addOnCompleteListener(task -> {})
            .addOnFailureListener(e -> {});
    }

    public static void sendIDFAData(String url) {
        Data data = new Data();
        data.url = url;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("idfa_deeplinks")
            .add(data)
            .addOnCompleteListener(task -> {})
            .addOnFailureListener(e -> {});
    }
}
