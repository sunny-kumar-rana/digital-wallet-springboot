package com.wallet.model;

public class User {
    private long id;
    private String name;
    private String email;
    private String password;

    public User(){}
    public User(long id, String name, String email, String password){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public long getId(){
        return this.id;
    }
    public void setId(long id){
        this.id = id;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getPassword(){
        return this.password;
    }
    public void setPassword(String password){
        this.password = password;
    }
}
