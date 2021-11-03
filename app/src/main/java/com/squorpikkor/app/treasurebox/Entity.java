package com.squorpikkor.app.treasurebox;

public class Entity {
    private final String name;
    private final String login;
    private final String pass;
    private final String email;
    private final String adds;
    private String docName;//имя документа в Firebase. Нужно для удаления по имени документа
    private final String cat;

    public Entity(String name, String login, String pass, String email, String adds, String cat, String docName) {
        this.name = name;
        this.login = login;
        this.pass = pass;
        this.email = email;
        this.adds = adds;
        this.cat = cat;
        this.docName = docName;
    }

    public Entity(String name, String login, String pass, String email, String adds, String cat) {
        this.name = name;
        this.login = login;
        this.pass = pass;
        this.email = email;
        this.adds = adds;
        this.cat = cat;
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

    public String getCat() {
        return cat;
    }
}
