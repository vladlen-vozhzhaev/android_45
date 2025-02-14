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
 * Use the {@link RegFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegFragment extends Fragment {
    TextView openAuthFragmentBtn;
    EditText nameRegEditText;
    EditText lastnameRegEditText;
    EditText loginRegEditText;
    EditText passRegEditText;
    AppCompatButton regBtn;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegFragment newInstance(String param1, String param2) {
        RegFragment fragment = new RegFragment();
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
        return inflater.inflate(R.layout.fragment_reg, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle bundle){
        openAuthFragmentBtn = view.findViewById(R.id.openAuthFragmentBtn);
        nameRegEditText = view.findViewById(R.id.nameRegEditText);
        lastnameRegEditText = view.findViewById(R.id.lastnameRegEditText);
        loginRegEditText = view.findViewById(R.id.loginRegEditText);
        passRegEditText = view.findViewById(R.id.passRegEditText);
        regBtn = view.findViewById(R.id.regBtn);
        // Если пользователь нажал на НАДПИСЬ авторизоваться
        openAuthFragmentBtn.setOnClickListener((e)->{
            AuthActivity authActivity = (AuthActivity) requireActivity();
            authActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main, new AuthFragment())
                    .commit();
        });
        // Если пользователь нажал на КНОПКУ зарегистрироваться
        regBtn.setOnClickListener((e)->{
            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        AuthActivity authActivity = (AuthActivity) requireActivity();
                        DataOutputStream out = authActivity.out;
                        DataInputStream is = authActivity.is;
                        while (true){
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("command", "reg");
                            out.writeUTF(jsonObject.toString()); // Говорим, что хотим регистрироваться
                            String response = is.readUTF();
                            jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                            if(jsonObject.get("command").equals("allow_reg")){ // Сервер разрешил регистрироваться
                                JSONObject userData = new JSONObject();
                                // Собираем данные с полей
                                String name = String.valueOf(nameRegEditText.getText());
                                String lastname = String.valueOf(lastnameRegEditText.getText());
                                String login = String.valueOf(loginRegEditText.getText());
                                String pass = String.valueOf(passRegEditText.getText());
                                // Упаковываем их в JSON
                                userData.put("name", name);
                                userData.put("lastname", lastname);
                                userData.put("login", login);
                                userData.put("pass", pass);
                                jsonObject.put("user_data", userData);
                                // Отправили на сервер данные с формы
                                out.writeUTF(jsonObject.toString());
                                response = is.readUTF(); // Ждём, что на это скажет сервер
                                jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                                if(jsonObject.get("command").equals("success")){ // Если сервер сказал, что всё окей
                                    Log.i("SERVER:", response);
                                    authActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            authActivity.getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .replace(R.id.main, new ContactsFragment()) // Открываем список контактов
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