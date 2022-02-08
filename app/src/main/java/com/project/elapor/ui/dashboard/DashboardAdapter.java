package com.project.elapor.ui.dashboard;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.elapor.R;
import com.project.elapor.ui.pengaduan.PengaduanModel;
import com.project.elapor.ui.pengaduan.message.MessageActivity;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    /// INISIASI ARRAY LIST SEBAGAI PENAMPUNG LIST DATA ADMIN / CLIENT
    private final ArrayList<DashboardModel> listUser = new ArrayList<>();
    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<DashboardModel> items) {
        listUser.clear();
        listUser.addAll(items);
        notifyDataSetChanged();
    }

    public String myUid, myImage, myName, myUnit;
    public DashboardAdapter(String myUid, String myImage, String myName, String myUnit) {
        this.myUid = myUid;
        this.myImage = myImage;
        this.myName = myName;
        this.myUnit = myUnit;
    }



    /// CASTING LAYOUT KE item_booking SUPAYA LIST BOOKING DAPAT DI TAMPILKAN BERBENTUK URUTAN
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(listUser.get(position), myUid, myImage, myName, myUnit);
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    //// FUNGSI UNTUK MEMASUKKAN DATA DARI ARRAY LIST DIATAS KEDALAM ATRIBUT, SEHINGGA TERLIHAT KODE TRANSAKSI DLL PADA LIST
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, nip, unit;
        ImageView image;
        ConstraintLayout cl;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            nip = itemView.findViewById(R.id.nip);
            unit = itemView.findViewById(R.id.unit);
            image = itemView.findViewById(R.id.image);
            cl = itemView.findViewById(R.id.cv);
        }

        @SuppressLint("SetTextI18n")
        public void bind(DashboardModel model, String myUid, String myImage, String myName, String myUnit) {
            Glide.with(itemView.getContext())
                    .load(model.getImage())
                    .into(image);
            name.setText(model.getName());
            nip.setText("NIP: " + model.getNip());
            unit.setText("Unit: " + model.getUnit());

            cl.setOnClickListener(view -> {

                String uid = String.valueOf(System.currentTimeMillis());

                ProgressDialog mProgressDialog = new ProgressDialog(itemView.getContext());

                mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                SimpleDateFormat getDate = new SimpleDateFormat("dd MMM yyyy, hh:mm:ss", Locale.getDefault());
                String format = getDate.format(new Date());


                    Map<String, Object> data = new HashMap<>();
                    data.put("uid", uid);
                    data.put("userUid", myUid);
                    data.put("userImage", myImage);
                    data.put("userUnit", myUnit);
                    data.put("userName", myName);
                    data.put("adminUid", model.getUid());
                    data.put("adminImage", model.getImage());
                    data.put("adminUnit", model.getUnit());
                    data.put("adminName", model.getName());
                    data.put("status", "not finish");
                    data.put("date", format);
                    data.put("message", "Silahkan ajukan laporan anda!");

                    PengaduanModel pengaduanModel = new PengaduanModel();
                    pengaduanModel.setUid(uid);
                    pengaduanModel.setAdminImage(model.getImage());
                    pengaduanModel.setAdminName(model.getName());
                    pengaduanModel.setAdminUid(model.getUid());
                    pengaduanModel.setAdminUnit(model.getUnit());
                    pengaduanModel.setUserUid(myUid);
                    pengaduanModel.setUserName(myName);
                    pengaduanModel.setUserUnit(myUnit);
                    pengaduanModel.setUserImage(myImage);
                    pengaduanModel.setStatus("not finish");


                    FirebaseFirestore
                            .getInstance()
                            .collection("report")
                            .document(uid)
                            .set(data)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    mProgressDialog.dismiss();
                                    Intent intent = new Intent(itemView.getContext(), MessageActivity.class);
                                    intent.putExtra(MessageActivity.EXTRA_DATA, pengaduanModel);
                                    intent.putExtra(MessageActivity.ROLE, "user");
                                    itemView.getContext().startActivity(intent);
                                } else {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(itemView.getContext(), "Gagal menuju halaman chat", Toast.LENGTH_SHORT).show();
                                    Log.e("Error Transaction", task.toString());
                                }
                            });
            });

        }
    }
}