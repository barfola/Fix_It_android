package com.example.fix_it.api;

import android.content.Context;
import com.example.fix_it.db.db_utils;

public class ApiConfiguration {
    private static ApiConfiguration instance;

    private String serverIP;
    private String baseURL;
    private String userUUID;
    private String sessionId;

    private ApiConfiguration() {
        // Private constructor for singleton
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new ApiConfiguration();
            instance.loadConfigurationFromFiles(context);
        }
    }

    public static ApiConfiguration getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ApiConfiguration is not initialized. Call init(context) first.");
        }
        return instance;
    }

    private void loadConfigurationFromFiles(Context context) {
        this.serverIP = safeRead(context, "user.serverip");
        this.userUUID = safeRead(context, "user.uuid");
        this.sessionId = safeRead(context, "user.sessionId");
        updateBaseURL();
    }

    private String safeRead(Context context, String fileName) {
        String data = db_utils.readDataFromFile(context, fileName);
        return (data != null && !data.trim().isEmpty()) ? data.trim() : null;
    }

    public void setServerIP(Context context, String ip) {
        if (ip != null && !ip.trim().isEmpty()) {
            this.serverIP = ip.trim();
            db_utils.saveDataToFile(context, "user.serverip", this.serverIP);
            updateBaseURL();
        }
    }

    public void setUserUUID(Context context, String uuid) {
        if (uuid != null && !uuid.trim().isEmpty()) {
            this.userUUID = uuid.trim();
            db_utils.saveDataToFile(context, "user.uuid", this.userUUID);
        }
    }

    public void setSessionId(Context context, String sessionId) {
        if (sessionId != null && !sessionId.trim().isEmpty()) {
            this.sessionId = sessionId.trim();
            db_utils.saveDataToFile(context, "user.sessionId", this.sessionId);
        }
    }

    public boolean isServerIPSet() {
        return serverIP != null && !serverIP.trim().isEmpty();
    }

    public boolean isUserUUIDSet() {
        return userUUID != null && !userUUID.trim().isEmpty();
    }

    public boolean isSessionIdSet() {
        return sessionId != null && !sessionId.trim().isEmpty();
    }

    public String getServerIP() {
        return serverIP;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getBaseURL() {
        return baseURL;
    }

    private void updateBaseURL() {
        if (serverIP != null && !serverIP.trim().isEmpty()) {
            baseURL = "http://" + serverIP + ":5000";
        } else {
            baseURL = null; // baseURL not usable without serverIP
        }
    }

    // Endpoint methods
    public String getLoginUrl() {
        return baseURL + "/login";
    }

    public String getSignInUrl() {
        return baseURL + "/signin";
    }

    public String getProblemReportUrl() {
        return baseURL + "/problemReport";
    }

    public String getReportsUrl() {
        return baseURL + "/get_reports";
    }

    public String getAllReportsUrl() {
        return baseURL + "/get_all_reports";
    }

    public String getInitialCredentialsUrl() {
        return baseURL + "/initialCredentials";
    }

    public String getImageByUuidUrl() {
        return baseURL + "/get_image";
    }
}
