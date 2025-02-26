package com.example.chat_45;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MassageHolder>{

    ArrayList<String> messages;
    public MessageAdapter(ArrayList<String> messages) {
        this.messages = messages;
    }

    @Override
    public MassageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MassageHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(MassageHolder holder, int position) {
        holder.bind(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    // Добавление нового элемента в список RecyclerView
    public void addItem(String item){
        messages.add(item);
        notifyItemInserted(messages.size()-1);
    }
}
