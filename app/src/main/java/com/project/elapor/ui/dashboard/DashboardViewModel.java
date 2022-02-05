package com.project.elapor.ui.dashboard;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class DashboardViewModel extends ViewModel {

    /// KELAS VIEW MODEL BERFUNGSI UNTUK MENGAMBIL DATA DARI FIRESTORE KEMUDIAN MENERUSKANNYA KEPADA ACTIVITY YANG DI TUJU
    /// CONTOH KELAS Dashboard VIEW MODEL MENGAMBIL DATA DARI COLLECTION "users", KEMUDIAN SETELAH DI AMBIL, DATA DIMASUKKAN KEDALAM MODEL, SETELAH ITU DITERUSKAN KEPADA Fragment Dashboard, SEHINGGA Fragment DAPAT MENAMPILKAN DATA users

    private final MutableLiveData<ArrayList<DashboardModel>> listDashboard = new MutableLiveData<>();
    final ArrayList<DashboardModel> dashboardModelArrayList = new ArrayList<>();

    private static final String TAG = DashboardViewModel.class.getSimpleName();

    public void setListDashboardAdmin() {
        dashboardModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("users")
                    .whereEqualTo("role", "admin")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                DashboardModel model = new DashboardModel();

                                model.setEmail("" + document.get("email"));
                                model.setImage("" + document.get("image"));
                                model.setNip("" + document.get("nip"));
                                model.setPassword("" + document.get("password"));
                                model.setRole("" + document.get("role"));
                                model.setUid("" + document.get("uid"));
                                model.setUnit("" + document.get("unit"));
                                model.setName("" + document.get("name"));

                                dashboardModelArrayList.add(model);
                            }
                            listDashboard.postValue(dashboardModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public LiveData<ArrayList<DashboardModel>> getDashboard() {
        return listDashboard;
    }
}
