package com.example.chat_45;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AuthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuthFragment extends Fragment {
    EditText loginEditText;
    EditText passEditText;
    AppCompatButton authBtn;
    TextView openRegFragmentBtn;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AuthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AuthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AuthFragment newInstance(String param1, String param2) {
        AuthFragment fragment = new AuthFragment();
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
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle bundle){
        super.onViewCreated(view, bundle);
        loginEditText = view.findViewById(R.id.loginEditText);
        passEditText = view.findViewById(R.id.passEditText);
        authBtn = view.findViewById(R.id.authBtn);
        openRegFragmentBtn = view.findViewById(R.id.openRegFragmentBtn);

        openRegFragmentBtn.setOnClickListener((e)->{
            AuthActivity authActivity = (AuthActivity) requireActivity();
            authActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main, new RegFragment())
                    .commit();
        });

        authBtn.setOnClickListener((e)->{
            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        AuthActivity authActivity = (AuthActivity) requireActivity();
                        DataOutputStream out = authActivity.out;
                        DataInputStream is = authActivity.is;
                        while (true){
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("command", "login");
                            out.writeUTF(jsonObject.toString());
                            String response = is.readUTF();
                            jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                            if(jsonObject.get("command").equals("allow_login")){ // Сервер разрешил авторизоваться
                                JSONObject userData = new JSONObject();
                                String login = String.valueOf(loginEditText.getText());
                                String pass = String.valueOf(passEditText.getText());
                                userData.put("login", login);
                                userData.put("pass", pass);
                                jsonObject.put("user_data", userData);
                                out.writeUTF(jsonObject.toString());
                                response = is.readUTF();
                                jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                                if(jsonObject.get("command").equals("success")){
                                    Log.i("SERVER:", response);
                                    authActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            authActivity.getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .replace(R.id.main, new ContactsFragment())
                                                    .commit();
                                        }
                                    });
                                    break;
                                } else if (jsonObject.get("command").equals("error")) {
                                    Log.e("SERVER", "Ошибка авторизации");
                                }
                            }
                        }
                    }catch (IOException exception){
                        exception.printStackTrace();
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            thread1.start();
        });
    }
}