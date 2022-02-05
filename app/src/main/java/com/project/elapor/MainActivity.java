package com.project.elapor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.elapor.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        autoLogin();

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formValidation();
            }
        });

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

    }

    private void autoLogin() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, HomepageActivity.class));
        }
    }

    private void formValidation() {
        String nip = binding.nip.getText().toString().trim();
        String password = binding.password.getText().toString().trim();

        if (nip.isEmpty()) {
            Toast.makeText(this, "NIP tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Kata Sandi tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else {
            login(nip, password);
        }
    }

    private void login(String nip, String password) {

        binding.progressCircular.setVisibility(View.VISIBLE);

        FirebaseFirestore
                .getInstance()
                .collection("users")
                .whereEqualTo("nip", nip)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.getResult().size() == 0) {
                        /// jika tidak terdapat di database dan email serta password, maka tidak bisa login
                        binding.progressCircular.setVisibility(View.GONE);
                        showFailureDialog();
                        return;
                    }

                    /// jika terdaftar maka ambil email di database, kemudian lakukan autentikasi menggunakan email & password dari user
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                        String email = "" + snapshot.get("email");

                        /// fungsi untuk mengecek, apakah email yang di inputkan ketika login sudah terdaftar di database atau belum
                        FirebaseAuth
                                .getInstance()
                                .signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            /// jika terdapat di database dan email serta password sama, maka masuk ke homepage
                                            binding.progressCircular.setVisibility(View.GONE);
                                            startActivity(new Intent(MainActivity.this, HomepageActivity.class));
                                        } else {
                                            /// jika tidak terdapat di database dan email serta password, maka tidak bisa login
                                            binding.progressCircular.setVisibility(View.GONE);
                                            showFailureDialog();
                                        }
                                    }
                                });

                    }
                });
    }

    /// munculkan dialog ketika gagal login
    private void showFailureDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Gagal melakukan login")
                .setMessage("Silahkan periksa data diri maupun koneksi internet anda!")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}