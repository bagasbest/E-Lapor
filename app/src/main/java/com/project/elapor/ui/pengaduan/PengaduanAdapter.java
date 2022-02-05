package com.project.elapor.ui.pengaduan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.elapor.R;
import com.project.elapor.ui.pengaduan.message.MessageActivity;

import java.util.ArrayList;

public class PengaduanAdapter extends RecyclerView.Adapter<PengaduanAdapter.ViewHolder> {



    private final ArrayList<PengaduanModel> listPengaduan = new ArrayList<>();

    String role;
    public PengaduanAdapter(String role) {
        this.role = role;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<PengaduanModel> items) {
        listPengaduan.clear();
        listPengaduan.addAll(items);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public PengaduanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pengaduan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PengaduanAdapter.ViewHolder holder, int position) {
        holder.bind(listPengaduan.get(position), role);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, unit, lastMessage, date;
        ImageView image;
        ConstraintLayout cv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            unit = itemView.findViewById(R.id.unit);
            lastMessage = itemView.findViewById(R.id.message);
            date = itemView.findViewById(R.id.date);
            image = itemView.findViewById(R.id.image);
            cv = itemView.findViewById(R.id.cv);
        }

        public void bind(PengaduanModel model, String role) {
            if(role.equals("user")){
                Glide.with(itemView.getContext())
                        .load(model.getAdminImage())
                        .into(image);
                name.setText(model.getAdminName());
                unit.setText(model.getAdminUnit());
            } else {
                Glide.with(itemView.getContext())
                        .load(model.getUserImage())
                        .into(image);
                name.setText(model.getUserName());
                unit.setText(model.getUserUnit());
            }
            lastMessage.setText(model.getMessage());
            date.setText(model.getDate());
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), MessageActivity.class);
                    intent.putExtra(MessageActivity.EXTRA_DATA, model);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
