package com.squorpikkor.app.treasurebox;

public class Entity {
    private String name;
    private String login;
    private String pass;
    private String email;
    private String adds;
    private String docName;//имя документа в Firebase. Нужно для удаления по имени документа

    public Entity() {
    }

    public Entity(String name, String login, String pass, String email, String adds, String docName) {
        this.name = name;
        this.login = login;
        this.pass = pass;
        this.email = email;
        this.adds = adds;
        this.docName = docName;
    }

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

    public String getDocName() {
        return docName;
    }
}
