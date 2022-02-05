package com.project.elapor.ui.pengaduan.message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.elapor.R;
import com.project.elapor.databinding.ActivityMessageBinding;
import com.project.elapor.ui.pengaduan.PengaduanModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageActivity extends AppCompatActivity {

    public static final String EXTRA_DATA = "data";
    public static final String ROLE = "role";
    private ActivityMessageBinding binding;
    private PengaduanModel model;

    private static final int REQUEST_IMAGE_FROM_GALLERY = 1002;
    private String imageText;
    private MessageAdapter adapter;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = getIntent().getParcelableExtra(EXTRA_DATA);
        role = getIntent().getStringExtra(ROLE);
        if (role.equals("user")) {
            if(model.getStatus().equals("not finish")){
                binding.finish.setVisibility(View.VISIBLE);
            }
            Glide.with(this)
                    .load(model.getAdminImage())
                    .into(binding.image);
            binding.name.setText(model.getAdminName());
        } else {
            Glide.with(this)
                    .load(model.getUserImage())
                    .into(binding.image);
            binding.name.setText(model.getUserName());
        }

        // LOAD CHAT HISTORY
        initRecyclerView();
        initViewModel();

        /// kirim pesan
        binding.send.setOnClickListener(view -> {
            String message = binding.messageEt.getText().toString().trim();
            if (message.isEmpty()) {
                Toast.makeText(MessageActivity.this, "Pesan tidak boleh kosong", Toast.LENGTH_SHORT).show();
            } else {
                sendMessage(message, false);
            }
        });

        // KLIK BERKAS
        binding.attach.setOnClickListener(view -> {
            ImagePicker.with(MessageActivity.this)
                    .galleryOnly()
                    .compress(1024)
                    .start(REQUEST_IMAGE_FROM_GALLERY);
        });

        binding.finish.setOnClickListener(view -> new AlertDialog.Builder(MessageActivity.this)
                .setTitle("Konfirmasi Menyelesaikan Laporan")
                .setMessage("Apakah anda yakin ingin menyelesaikan laporan ini ?")
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setPositiveButton("YA", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    finishReport();
                })
                .setNegativeButton("TIDAK", null)
                .show());
    }

    private void finishReport() {
        FirebaseFirestore
                .getInstance()
                .collection("report")
                .document(model.getUid())
                .update("status", "finish")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            binding.finish.setVisibility(View.GONE);
                            Toast.makeText(MessageActivity.this, "Laporan ini sukses diselesaikan!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void sendMessage(String message, boolean isText) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat getDate = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss");
        String format = getDate.format(new Date());

        if(role.equals("user")) {
            MessageDatabase.sendChat(message, format, model.getUid(), model.getUserUid(), isText);
        } else {
            MessageDatabase.sendChat(message, format, model.getUid(), model.getAdminUid(), isText);
        }

        binding.messageEt.getText().clear();
        imageText = null;

        // LOAD CHAT HISTORY
        initRecyclerView();
        initViewModel();
    }

    private void initRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        binding.chatRv.setLayoutManager(linearLayoutManager);

        if (role.equals("user")) {
            adapter = new MessageAdapter(model.getUserUid());
        } else {
            adapter = new MessageAdapter(model.getAdminUid());
        }
        binding.chatRv.setAdapter(adapter);

    }

    private void initViewModel() {

        MessageViewModel viewModel = new ViewModelProvider(this).get(MessageViewModel.class);

        viewModel.setListMessage(model.getUid());
        viewModel.getMessage().observe(this, messageList -> {
            if (messageList != null) {
                adapter.setData(messageList);
            }
            binding.progressBar.setVisibility(View.GONE);
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_FROM_GALLERY) {
                uploadPicture(data.getData());
            }
        }
    }

    private void uploadPicture(Uri data) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        String imageFileName = "message/data_" + System.currentTimeMillis() + ".png";

        mStorageRef.child(imageFileName).putFile(data)
                .addOnSuccessListener(taskSnapshot ->
                        mStorageRef.child(imageFileName).getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    mProgressDialog.dismiss();
                                    imageText = uri.toString();
                                    sendMessage(imageText, true);
                                })
                                .addOnFailureListener(e -> {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(MessageActivity.this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
                                    Log.d("imageDp: ", e.toString());
                                }))
                .addOnFailureListener(e -> {
                    mProgressDialog.dismiss();
                    Toast.makeText(MessageActivity.this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
                    Log.d("imageDp: ", e.toString());
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}