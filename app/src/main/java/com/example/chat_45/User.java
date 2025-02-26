package com.example.chat_45;

public class User {
    private String name;
    private String lastname;
    private String login;
    private int id; // ID из MySQL (серверная)
    private long _id; // ID из SQLite (локальная)

    public User(String name, String lastname, String login, int id, long _id) {
        this.name = name;
        this.lastname = lastname;
        this.login = login;
        this.id = id;
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getLogin() {
        return login;
    }

    public int getId() {
        return id;
    }

    public long get_id() {
        return _id;
    }
}
