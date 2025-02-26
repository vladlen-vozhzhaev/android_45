package com.example.chat_45;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MassageHolder extends RecyclerView.ViewHolder{
    TextView messageText;
    public MassageHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.message, parent, false));
        messageText = itemView.findViewById(R.id.messageText);
    }
    public void bind(String text){ // Сюда бы передать ID отправителя)))
        // Сделать так, что бы в зависимости от кого сообщение, оно отображалось (слева или справа)
        messageText.setText(text);
    }
}