package com.example.chat_45;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListHolder>{
    ArrayList<User> users;
    public ContactListAdapter(ArrayList<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public ContactListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ContactListHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(ContactListHolder holder, int position) {
        String username = users.get(position).getName()+" "+users.get(position).getLastname();
        String login = users.get(position).getLogin();
        int id = users.get(position).getId();
        holder.bind(username, login, id);
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }
}
