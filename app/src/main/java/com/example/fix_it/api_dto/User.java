package com.example.fix_it.api_dto;
import com.example.fix_it.helper.HashUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class User extends ApiDtoBase{

    private enum Fields{
        sessionId,
        userName,
        uuid,
        password,
        hashPassword,
        adminLevel,
        error,
        message
    }

    private String userName;
    private String password;
    private int adminLevel;
    private String sessionID;
    private String hashPassword;
    private String message;
    private String error;


    public User()   {  }

    public User(String userName, String password, int adminLevel, String sessionID) {
        super();
        this.userName = userName;
        this.password = password;
        this.adminLevel = adminLevel;
        this.sessionID = sessionID;
        this.hashPassword = HashUtil.hashString(password);
        this.message = null;
        this.error = null;

    }

    public User(String userName, String password) {
        super();
        this.userName = userName;
        this.password = password;
        this.adminLevel = 0;
        this.sessionID = null;
        this.hashPassword = HashUtil.hashString(password);
        this.message = null;
        this.error = null;

    }

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

    public String getMessage() {return this.message;}
    public String getError() {return this.error;}


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

    public void setMessage(String message) {this.message = message;}

    public void setError(String error) {this.error = error;}


    public void mapJson(String json) throws JSONException {
        JSONObject jsonObj = new JSONObject(json);

        this.sessionID=jsonObj.isNull(Fields.sessionId.toString()) ? "" : jsonObj.getString(Fields.sessionId.toString());
        this.userName=jsonObj.isNull(Fields.userName.toString()) ? "" : jsonObj.getString(Fields.userName.toString());
        this.password=jsonObj.isNull(Fields.password.toString()) ? "" : jsonObj.getString(Fields.password.toString());
        this.hashPassword=jsonObj.isNull(Fields.hashPassword.toString()) ? "" : jsonObj.getString(Fields.hashPassword.toString());
        this.uuid=jsonObj.isNull(Fields.uuid.toString()) ? "" : jsonObj.getString(Fields.uuid.toString());
        this.adminLevel=jsonObj.isNull(Fields.adminLevel.toString()) ? 0 : jsonObj.getInt(Fields.adminLevel.toString());
        this.message=jsonObj.isNull(Fields.message.toString()) ? "" : jsonObj.getString(Fields.message.toString());
        this.error=jsonObj.isNull(Fields.error.toString()) ? "" : jsonObj.getString(Fields.error.toString());


    }



}
