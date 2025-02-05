package com.example.chat_45;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    Socket socket = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        /*Button sendBtn = findViewById(R.id.sendBtn);
        EditText editText = findViewById(R.id.userMsg);
        TextView messageBox = findViewById(R.id.messageBox);
        RecyclerView messageList = findViewById(R.id.messageList);


        ArrayList<String> messages = new ArrayList<>();
        MessageAdapter messageAdapter = new MessageAdapter(messages);
        messageList.setAdapter(messageAdapter);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("192.168.1.9", 9123);
                    DataInputStream is = new DataInputStream(socket.getInputStream());
                    while (true){
                        String response = is.readUTF();
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messageAdapter.addItem(response);
                            }
                        });
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();

        sendBtn.setOnClickListener((e)->{
            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                        String msg = String.valueOf(editText.getText());
                        out.writeUTF(msg);
                    }catch (IOException exception){
                        exception.printStackTrace();
                    }
                }
            });
            thread1.start();
        });*/
    }


    /*public class MassageHolder extends RecyclerView.ViewHolder{
        TextView testText;
        public MassageHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.message, parent, false));
            testText = itemView.findViewById(R.id.testText);
        }
        public void bind(String text){
            testText.setText(text);
        }
    }
    public class MessageAdapter extends RecyclerView.Adapter<MassageHolder>{
        ArrayList<String> messages;
        public MessageAdapter(ArrayList<String> messages) {
            this.messages = messages;
        }

        @Override
        public MassageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
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
    }*/
}