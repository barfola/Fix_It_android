package com.example.fix_it.api_dto;

import java.util.ArrayList;
import java.util.List;

public class Reports {
    public static List<ProblemReport> appProblemReports = new ArrayList<>();


    public Reports() {

    }

    public  void addReports(ProblemReport user){
        appProblemReports.add(user);
    }


    public List<ProblemReport> getAppReports(){
        return appProblemReports;
    }

    public void setAppReport(List<ProblemReport> updateAppProblemReports){
        appProblemReports = updateAppProblemReports;
    }


}
