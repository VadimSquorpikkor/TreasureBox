package com.squorpikkor.app.treasurebox;

class Entity {
    private String name;
    private String login;
    private String pass;

    public Entity(String name, String login, String pass) {
        this.name = name;
        this.login = login;
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }
}
