package com.example.fix_it.api;

public interface ServerResponseCallback {
    void onSuccess(String responseBody);
    void onFailure(int statusCode,String errorMessage);
}