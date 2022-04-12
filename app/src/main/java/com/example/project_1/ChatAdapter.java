package com.example.project_1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


    public class ChatAdapter extends RecyclerView.Adapter {
        ArrayList<MessageModel> messageModels;
        Context context;
        int SENDER_VIEW_TYPE = 1;
        int RECEIVER_VIEW_TYPE = 2;
        String recId;

        public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
            this.messageModels = messageModels;
            this.context = context;
        }

        public ChatAdapter(ArrayList<MessageModel> messageModels, Context context, String recId) {
            this.messageModels = messageModels;
            this.context = context;
            this.recId = recId;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == SENDER_VIEW_TYPE) {
                View view = LayoutInflater.from(context).inflate(R.layout.sender, parent, false);
                return new senderViewHolder(view);

            } else {
                View view = LayoutInflater.from(context).inflate(R.layout.receiver, parent, false);
                return new receiverViewHolder(view);
            }

        }

        @Override
        public int getItemViewType(int position) {
            if (messageModels.get(position).getMyid().equals(FirebaseAuth.getInstance().getUid()))
                return SENDER_VIEW_TYPE;
            else
                return RECEIVER_VIEW_TYPE;

        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            MessageModel messageModel = messageModels.get(position);

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle("Delete")
                            .setIcon(R.drawable.attention)
                            .setMessage("Are you sure to delete this message?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String sender=FirebaseAuth.getInstance().getUid()+recId;
                                    FirebaseDatabase.getInstance().getReference("chats").child(sender).child(messageModel.getMessageId()).setValue(null);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                }
                            })
                            .show();
                    return false;
                }
            });


            if (holder.getClass() == senderViewHolder.class) {
                ((senderViewHolder) holder).sMsg.setText(messageModel.getMsg());
                ((senderViewHolder) holder).sTime.setText(messageModel.getTime());
            } else {
                ((receiverViewHolder) holder).rMsg.setText(messageModel.getMsg());
                ((receiverViewHolder) holder).rTime.setText(messageModel.getTime());
            }
        }

        @Override
        public int getItemCount() {
            return messageModels.size();
        }

        public class receiverViewHolder extends RecyclerView.ViewHolder {
            TextView rMsg, rTime;

            public receiverViewHolder(@NonNull View itemView) {
                super(itemView);
                rMsg = itemView.findViewById(R.id.receivdmsg);
                rTime=itemView.findViewById(R.id.receivertime);
            }
        }

        public class senderViewHolder extends RecyclerView.ViewHolder {
            TextView sMsg, sTime;

            public senderViewHolder(@NonNull View itemView) {
                super(itemView);
                sMsg = itemView.findViewById(R.id.sentmsg);
                sTime=itemView.findViewById(R.id.sendertime);
            }
        }

    }
