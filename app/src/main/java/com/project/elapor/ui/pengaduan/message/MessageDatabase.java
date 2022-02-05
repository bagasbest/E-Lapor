package com.project.elapor.ui.pengaduan.message;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MessageDatabase {

    public static void sendChat(String message, String format, String uid, String myUid, boolean isText) {


        Map<String, Object> logChat   = new HashMap<>();
        logChat.put("message", message);
        logChat.put("date", format);
        logChat.put("uid", myUid);
        logChat.put("isText", isText);

        // UPDATE LOG CHAT
        FirebaseFirestore
                .getInstance()
                .collection("report")
                .document(uid)
                .collection("message")
                .document(String.valueOf(System.currentTimeMillis()))
                .set(logChat)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Log.d("SENDER MSG", "success");
                    }else {
                        Log.d("SENDER MSG", task.toString());
                    }
                });


        Map<String, Object> updateMessage   = new HashMap<>();
        updateMessage.put("message", message);
        updateMessage.put("date", format);
        // UPDATE LAST MESSAGE
        FirebaseFirestore
                .getInstance()
                .collection("report")
                .document(uid)
                .update(updateMessage)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Log.d("MSG", "success update last message");
                    } else {
                        Log.d("MSG", task.toString());
                    }
                });
    }

}
