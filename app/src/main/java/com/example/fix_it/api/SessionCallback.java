package com.example.fix_it.api;

public interface SessionCallback {
    void onSessionReceived(String sessionId);
    void onSessionError(String errorMessage);
}

