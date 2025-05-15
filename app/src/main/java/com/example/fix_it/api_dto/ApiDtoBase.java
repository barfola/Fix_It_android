package com.example.fix_it.api_dto;
import java.util.UUID;


public abstract class ApiDtoBase {
    public String uuid;
    public ApiDtoBase()
    {
        this.uuid = UUID.randomUUID().toString();
    }

    public String getUuid(){
        return this.uuid;
    }

    public void setUuid(String uuid){
        this.uuid = uuid;
    }


}
