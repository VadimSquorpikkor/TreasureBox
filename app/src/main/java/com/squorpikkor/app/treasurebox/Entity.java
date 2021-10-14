package com.squorpikkor.app.treasurebox;

class Entity {
    private String name;
    private String login;
    private String pass;
    private String email;
    private String adds;

    public Entity(String name, String login, String pass, String email, String adds) {
        this.name = name;
        this.login = login;
        this.pass = pass;
        this.email = email;
        this.adds = adds;
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

    public String getEmail() {
        return email;
    }

    public String getAdds() {
        return adds;
    }
}
