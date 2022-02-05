package com.project.elapor.ui.pengaduan.message;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MessageViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<MessageModel>> listMessage = new MutableLiveData<>();
    final ArrayList<MessageModel> messageModelArrayList = new ArrayList<>();

    private static final String TAG = MessageViewModel.class.getSimpleName();

    public void setListMessage(String uid) {
        messageModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("report")
                    .document(uid)
                    .collection("message")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                MessageModel model = new MessageModel();

                                model.setDate("" + document.get("date"));
                                model.setMessage("" + document.get("message"));
                                model.setText(document.getBoolean("isText"));
                                model.setUid("" + document.get("uid"));

                                messageModelArrayList.add(model);
                            }
                            listMessage.postValue(messageModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public LiveData<ArrayList<MessageModel>> getMessage() {
        return listMessage;
    }

}
