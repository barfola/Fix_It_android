package com.example.fix_it.api_dto;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.fix_it.helper.HashUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class User extends ApiDtoBase implements Parcelable {

    protected User(Parcel in) {
        userName = in.readString();
        password = in.readString();
        adminLevel = in.readInt();
        sessionID = in.readString();
        hashPassword = in.readString();
        message = in.readString();
        error = in.readString();
        uuid = in.readString();

    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(password);
        dest.writeInt(adminLevel);
        dest.writeString(sessionID);
        dest.writeString(hashPassword);
        dest.writeString(message);
        dest.writeString(error);
        dest.writeString(uuid);

    }

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

        this.setSessionID(jsonObj.isNull(Fields.sessionId.toString()) ? "" : jsonObj.getString(Fields.sessionId.toString()));
        this.setUserName(jsonObj.isNull(Fields.userName.toString()) ? "" : jsonObj.getString(Fields.userName.toString()));
        this.setPassword(jsonObj.isNull(Fields.password.toString()) ? "" : jsonObj.getString(Fields.password.toString()));
        this.setHashPassword(jsonObj.isNull(Fields.hashPassword.toString()) ? "" : jsonObj.getString(Fields.hashPassword.toString()));
        this.setUuid(jsonObj.isNull(Fields.uuid.toString()) ? "" : jsonObj.getString(Fields.uuid.toString()));
        this.setAdminLevel(jsonObj.isNull(Fields.adminLevel.toString()) ? 0 : jsonObj.getInt(Fields.adminLevel.toString()));
        this.setMessage(jsonObj.isNull(Fields.message.toString()) ? "" : jsonObj.getString(Fields.message.toString()));
        this.setError(jsonObj.isNull(Fields.error.toString()) ? "" : jsonObj.getString(Fields.error.toString()));


    }



}
