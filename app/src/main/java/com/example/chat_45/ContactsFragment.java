package com.example.chat_45;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chat_45.database.DatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment {
    EditText contactName;
    EditText contactLastname;
    EditText contactLogin;
    EditText contactId;
    AppCompatButton addContactBtn;
    TextView contactTextView;

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
        contactName = view.findViewById(R.id.contactName);
        contactLastname = view.findViewById(R.id.contactLastname);
        contactLogin = view.findViewById(R.id.contactLogin);
        contactId = view.findViewById(R.id.contactId);
        addContactBtn = view.findViewById(R.id.addContactBtn);
        contactTextView = view.findViewById(R.id.contactTextView);

        SQLiteDatabase db1 = databaseHelper.getReadableDatabase();
        Cursor cursor = db1.query("users", new String[]{"_id", "name", "lastname", "login", "user_id"}, null, null, null, null,null);
        while (cursor.moveToNext()){
            long _id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String lastname = cursor.getString(cursor.getColumnIndexOrThrow("lastname"));
            String login = cursor.getString(cursor.getColumnIndexOrThrow("login"));
            int user_id = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
            Log.i("SQLite", _id+" "+name+" "+lastname+" "+login+" "+user_id);
        }
        cursor.close();
        db1.close();


        addContactBtn.setOnClickListener(e->{
            String name = contactName.getText().toString();
            String lastname = contactLastname.getText().toString();
            String login = contactLogin.getText().toString();
            int id = Integer.parseInt(contactId.getText().toString());
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
                Log.e("SQLlite", "Ошибка добавления записи");
            }
            db.close();
        });
    }
}