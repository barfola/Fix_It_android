package com.example.fix_it.api;

public  class ApiConfiguration {

    public static final String SERVER_IP = "10.100.102.12";
    public static final String BASE_URL = "http://10.100.102.12:5000";
    public static final String GET_REPORTS_USL = BASE_URL + "/get_reports";
    public static final String GET_ALL_REPORTS_URL = BASE_URL + "/get_all_reports";

    public static final String PROBLEM_REPORT_URL = BASE_URL + "/problemReport";
    public static final String INITIALS_CREDENTIALS_URL = BASE_URL + "/initialCredentials";
    public static final String LOGIN_URL = BASE_URL + "/login";
    public static final String SIGN_IN_URL = BASE_URL + "/signin";
    public static final String GET_IMAGE_BY_UUID_URL = BASE_URL + "/get_image";

    public static String userUUID;
    public static String sessionId;

    public void saveConfig()
    {

    }

    public void readConfig()
    {

    }

}
