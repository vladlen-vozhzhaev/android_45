package com.example.chat_45;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chat_45.database.DatabaseHelper;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment {
    EditText contactLogin;
    AppCompatButton addContactBtn;
    RecyclerView contactListRecyclerView;
    ArrayList<User> users = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ContactsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactsFragment newInstance(String param1, String param2) {
        ContactsFragment fragment = new ContactsFragment();
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
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle bundle){
        DatabaseHelper databaseHelper = new DatabaseHelper(this.getContext());
        contactLogin = view.findViewById(R.id.contactLogin);
        addContactBtn = view.findViewById(R.id.addContactBtn);
        contactListRecyclerView = view.findViewById(R.id.contactListRecyclerView);


        SQLiteDatabase db1 = databaseHelper.getReadableDatabase();
        Cursor cursor = db1.query("users", new String[]{"_id", "name", "lastname", "login", "user_id"}, null, null, null, null,null);
        while (cursor.moveToNext()){
            long _id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String lastname = cursor.getString(cursor.getColumnIndexOrThrow("lastname"));
            String login = cursor.getString(cursor.getColumnIndexOrThrow("login"));
            int user_id = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
            Log.i("SQLite", _id+" "+name+" "+lastname+" "+login+" "+user_id);
            User user = new User(name, lastname, login, user_id, _id);
            users.add(user);
        }
        cursor.close();
        db1.close();
        ContactListAdapter contactListAdapter = new ContactListAdapter(users);
        contactListRecyclerView.setAdapter(contactListAdapter);


        addContactBtn.setOnClickListener(e->{
            String login = contactLogin.getText().toString();
            AuthActivity authActivity = (AuthActivity) requireActivity();
            DataOutputStream out = authActivity.out;
            DataInputStream is = authActivity.is;
            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Подготавливаем данные для поиска пользователя на сервере
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("command", "find_user");
                        jsonObject.put("error", "");
                        jsonObject.put("login", login);
                        out.writeUTF(jsonObject.toString());
                        String response = is.readUTF();
                        jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                        if(jsonObject.getString("error").equals("not_exist")){
                            Log.e("SERVER", "NOT EXIST");
                        }else{
                            String name = jsonObject.getString("name");
                            String lastname = jsonObject.getString("lastname");
                            int id = jsonObject.getInt("id");
                            Log.i("SERVER", name+" "+lastname+" "+id+" "+login);
                            SQLiteDatabase db = databaseHelper.getWritableDatabase();
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("name", name);
                            contentValues.put("lastname", lastname);
                            contentValues.put("login", login);
                            contentValues.put("user_id", id);
                            long rowId = db.insert("users", null, contentValues);
                            if(rowId != -1){
                                Log.i("SQLite:", "Строка успешно добавлена");
                            }else{
                                Log.e("SQLite", "Ошибка добавления записи");
                            }
                            db.close();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });
            thread1.start();
        });
    }
}