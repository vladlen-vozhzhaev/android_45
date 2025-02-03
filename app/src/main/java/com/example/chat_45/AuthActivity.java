package com.example.chat_45;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class AuthActivity extends AppCompatActivity {
    Socket socket = null;
    DataOutputStream out = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new AuthFragment())
                .commit();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("192.168.1.9", 9123);
                    DataInputStream is = new DataInputStream(socket.getInputStream());
                    out = new DataOutputStream(socket.getOutputStream());
                    Log.i("SERVER:", is.readUTF());
                    out.writeUTF("/login");
                    while (true){
                        String response = is.readUTF();
                        Log.i("SERVER:", response);
                        AuthActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(response.equals("success")){
                                    getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.main, new ChatFragment())
                                            .commit();
                                }else if(response.equals("error")){
                                    Toast.makeText(AuthActivity.this, "Неправильный логин или пароль", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();

    }
}