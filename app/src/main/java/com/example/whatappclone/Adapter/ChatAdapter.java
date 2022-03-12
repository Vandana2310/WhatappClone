package com.example.whatappclone.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatappclone.Model.MessagesModel;
import com.example.whatappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {

    ArrayList<MessagesModel> messagesModels;
    Context context;
    String receiverId;

    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;


    public ChatAdapter(ArrayList<MessagesModel> messagesModels, Context context) {
        this.messagesModels = messagesModels;
        this.context = context;
    }


    public ChatAdapter(ArrayList<MessagesModel> messagesModels, Context context, String receiverId) {
        this.messagesModels = messagesModels;
        this.context = context;
        this.receiverId = receiverId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         if(viewType==SENDER_VIEW_TYPE){
             View view= LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
             return  new SenderViewHolder(view);
         }
        else {
             View view= LayoutInflater.from(context).inflate(R.layout.sample_receiver,parent,false);
             return  new ReceiverViewHolder(view);
         }
    }

    @Override
    public int getItemViewType(int position) {

        if(messagesModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }else{
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessagesModel messagesModel=messagesModels.get(position);

        //for deleting messages
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this message")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               // actual delete code are here
                                FirebaseDatabase database=FirebaseDatabase.getInstance();
                                String senderRoom =FirebaseAuth.getInstance().getUid()+ receiverId;

                                database.getReference().child("chats").child(senderRoom)
                                        .child(messagesModel.getMessageId())
                                        .setValue(null);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        }).show();

                return false;
            }
        });
        if(holder.getClass()==SenderViewHolder.class){
            ((SenderViewHolder)holder).senderMessage.setText(messagesModel.getMessages());
        }else {
            ((ReceiverViewHolder)holder).receiverMessage.setText(messagesModel.getMessages());
        }
    }

    @Override
    public int getItemCount() {
        return messagesModels.size();
    }

    public  class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView receiverMessage ,receiverTime;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            receiverMessage =itemView.findViewById(R.id.receiverTxt);
            receiverTime =itemView.findViewById(R.id.receiverTime);

        }
    }

    public  class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView senderMessage ,senderTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMessage =itemView.findViewById(R.id.senderTxt);
            senderTime =itemView.findViewById(R.id.senderTime);

        }
    }
}
