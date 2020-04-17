package org.sergio.users;

public class User {

    protected String name;
    protected String password;
    protected boolean boss;

    protected User(String name, String password, boolean boss){
        this.name = name;
        this.password = password;
        this.boss = boss;
    }

    // GETTERS

    public String getName(){
        return this.name;
    }

    public String getPassword(){
        return this.password;
    }

    public boolean getBoss() {
        return this.boss;
    }

}
