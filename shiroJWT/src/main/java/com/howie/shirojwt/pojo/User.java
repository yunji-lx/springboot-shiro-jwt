package com.howie.shirojwt.pojo;

import lombok.Data;

@Data
public class User {

    private int id;

    private String username;

    private String password;

    private String role;

    private String permission;

    private int ban;

    public User(){}



}
