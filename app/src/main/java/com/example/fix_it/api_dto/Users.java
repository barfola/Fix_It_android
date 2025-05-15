package com.example.fix_it.api_dto;

import java.util.ArrayList;
import java.util.List;
import com.example.fix_it.api_dto.User;

import java.util.UUID;

public class Users {
    public static List<Users> appUsers = new ArrayList<>();
    public Users()
    {

    }

    public  void addUser(Users user){
        appUsers.add(user);
    }


    public List<Users> getAppUsers(){
        return appUsers;
    }

    public void setAppUsers(List<Users> updateAppUsers){
        appUsers = updateAppUsers;
    }



}
