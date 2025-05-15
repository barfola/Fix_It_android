package com.example.fix_it.api_dto;
import com.example.fix_it.helper.HashUtil;

import java.util.UUID;

public class User extends ApiDtoBase{

    private String userName;
    private String password;
    private int adminLevel;
    private String sessionID;
    private String hashPassword;

    public User(String userName, String password, int adminLevel, String sessionID) {
        super();
        this.userName = userName;
        this.password = password;
        this.adminLevel = adminLevel;
        this.sessionID = sessionID;
        this.hashPassword = HashUtil.hashString(password);
    }

    public User(String userName, String password) {
        super();
        this.userName = userName;
        this.password = password;
        this.adminLevel = 0;
        this.sessionID = null;
        this.hashPassword = HashUtil.hashString(password);

    }


//    public User() {
//        super();
//        this.userName = null;
//        this.password = null;
//        this.adminLevel = Integer.parseInt(null);
//        this.sessionID = null;
//    }

    //getters
    public String getUserName(){
        return this.userName;
    }
    public String getPassword(){
        return this.password;
    }
    public int getAdminLevel(){
        return this.adminLevel;
    }

    public String getSessionID(){
        return this.sessionID;
    }

    public String getHashPassword(){
        return this.hashPassword;
    }

    //setters
    public void setUserName(String userName){
        this.userName = userName;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setAdminLevel(int adminLevel){
        this.adminLevel = adminLevel;
    }

    public void setSessionID(String sessionID){
        this.sessionID = sessionID;
    }

    public void setHashPassword(String hashPassword){
        this.hashPassword = hashPassword;
    }




}
