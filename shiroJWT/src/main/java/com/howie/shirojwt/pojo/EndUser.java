package com.howie.shirojwt.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class EndUser implements Serializable {

    private int id;

    private String e_name;

    private String e_pwd;

    private int ban;

    public EndUser(){}

}
