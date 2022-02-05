package com.project.elapor.ui.pengaduan;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class PengaduanViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<PengaduanModel>> listPengaduan = new MutableLiveData<>();
    final ArrayList<PengaduanModel> pengaduanModelArrayList = new ArrayList<>();

    private static final String TAG = PengaduanViewModel.class.getSimpleName();

    public void setListPengaduanUserByUid(String uid) {
        pengaduanModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("report")
                    .whereEqualTo("userUid", uid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                PengaduanModel model = new PengaduanModel();

                                model.setUid("" + document.get("uid"));
                                model.setAdminImage("" + document.get("adminImage"));
                                model.setAdminUid("" + document.get("adminUid"));
                                model.setAdminName("" + document.get("adminName"));
                                model.setAdminUnit("" + document.get("adminUnit"));
                                model.setDate("" + document.get("date"));
                                model.setMessage("" + document.get("message"));
                                model.setUserImage("" + document.get("userImage"));
                                model.setUserName("" + document.get("userName"));
                                model.setUserUid("" + document.get("userUid"));
                                model.setUserUnit("" + document.get("userUnit"));


                                pengaduanModelArrayList.add(model);
                            }
                            listPengaduan.postValue(pengaduanModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void setListPengaduanAdminByUid(String uid) {
        pengaduanModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("report")
                    .whereEqualTo("adminUid", uid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                PengaduanModel model = new PengaduanModel();

                                model.setUid("" + document.get("uid"));
                                model.setAdminImage("" + document.get("adminImage"));
                                model.setAdminUid("" + document.get("adminUid"));
                                model.setAdminName("" + document.get("adminName"));
                                model.setAdminUnit("" + document.get("adminUnit"));
                                model.setDate("" + document.get("date"));
                                model.setMessage("" + document.get("message"));
                                model.setUserImage("" + document.get("userImage"));
                                model.setUserName("" + document.get("userName"));
                                model.setUserUid("" + document.get("userUid"));
                                model.setUserUnit("" + document.get("userUnit"));


                                pengaduanModelArrayList.add(model);
                            }
                            listPengaduan.postValue(pengaduanModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }


    public LiveData<ArrayList<PengaduanModel>> getPengaduan() {
        return listPengaduan;
    }

}
