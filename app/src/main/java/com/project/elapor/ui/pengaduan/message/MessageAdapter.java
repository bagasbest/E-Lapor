package com.project.elapor.ui.pengaduan.message;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.elapor.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    private final ArrayList<MessageModel> messageList = new ArrayList<>();
    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<MessageModel> items) {
        messageList.clear();
        messageList.addAll(items);
        notifyDataSetChanged();
    }

    private final String uid;
    public MessageAdapter(String uid) {
        this.uid = uid;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_right, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_left, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(messageList.get(position));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //get currently signed user
        if(messageList.get(position).getUid().equals(uid)) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageText;
        TextView message, time;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.messageTv);
            time = itemView.findViewById(R.id.timeTv);
            imageText = itemView.findViewById(R.id.imageText);
        }


        public void bind(MessageModel model) {
            if(model.isText()) {
                message.setVisibility(View.GONE);
                imageText.setVisibility(View.VISIBLE);
                Glide.with(itemView.getContext())
                        .load(model.getMessage())
                        .into(imageText);

                imageText.setOnClickListener(view -> {
                    Dialog dialog;
                    ImageView imageView;
                    dialog = new Dialog(itemView.getContext());

                    dialog.setContentView(R.layout.popup_image);
                    imageView = dialog.findViewById(R.id.image);

                    Glide.with(itemView.getContext())
                            .load(model.getMessage())
                            .into(imageView);


                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                });

            } else {
                imageText.setVisibility(View.GONE);
                message.setVisibility(View.VISIBLE);
                message.setText(model.getMessage());
            }
            time.setText(model.getDate());
        }
    }
}
