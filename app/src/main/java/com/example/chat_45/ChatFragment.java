package com.example.chat_45;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    RecyclerView messageList;
    EditText userMsg;
    Button sendBtn;
    ArrayList<String> messages = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle bundle){
        super.onViewCreated(view, bundle);
        sendBtn = view.findViewById(R.id.sendBtn);
        userMsg = view.findViewById(R.id.userMsg);
        messageList = view.findViewById(R.id.messageList);
        MessageAdapter messageAdapter = new MessageAdapter(messages);
        messageList.setAdapter(messageAdapter);
        AuthActivity authActivity = (AuthActivity) requireActivity();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DataInputStream is = authActivity.is;
                    while (true){
                        String response = is.readUTF();
                        authActivity.runOnUiThread(new Runnable() {
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
                        DataOutputStream out = authActivity.out;
                        String msg = String.valueOf(userMsg.getText());
                        authActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                userMsg.setText("");
                                messageAdapter.addItem(msg);
                            }
                        });
                        out.writeUTF(msg);
                    }catch (IOException exception){
                        exception.printStackTrace();
                    }
                }
            });
            thread1.start();
        });
    }
}