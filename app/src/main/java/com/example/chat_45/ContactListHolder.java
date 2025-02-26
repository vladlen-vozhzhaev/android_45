package com.example.chat_45;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class ContactListHolder extends RecyclerView.ViewHolder{
    TextView nameTextView;
    TextView loginTextView;
    int id;
    public ContactListHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.contact, parent, false));
        nameTextView = itemView.findViewById(R.id.contactNameTextView);
        loginTextView = itemView.findViewById(R.id.contactLoginTextView);

        itemView.setOnClickListener(e->{
            Context context = itemView.getContext();
            if (context instanceof Activity) {
                AppCompatActivity activity = (AppCompatActivity) context;
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main, new ChatFragment(this.id)) // Открываем чат
                        .commit();
            }
        });
    }
    public void bind(String username, String login, int id ){
        nameTextView.setText(username);
        loginTextView.setText(login);
        this.id = id;
    }

}
