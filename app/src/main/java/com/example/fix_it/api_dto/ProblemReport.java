package com.example.fix_it.api_dto;

public class ProblemReport extends ApiDtoBase{
    public static enum Role {
        TEACHER,
        STUDENT,
        SCHOOL_CREW,
        OTHER;

    }
    public static enum Location {
        BATHROOM,
        YARD,
        CLASSROOM,
        TEACHERS_LOUNGE,
        HALLWAY;
    }
    public static enum ReportType {
        LOST_KEY,
        LAMP_BURNED_OUT,
        BATTERIES,
        AIR_CONDITIONER,
        PROJECTOR,
        BROKEN_OBJECT;
    }

    private String description;
    private Role role;
    private Location location;
    private ReportType reportType;
    private String image;
    private User user;




    public ProblemReport(String description, Role role, Location location, ReportType reportType , String image, User user){
        super();
        this.description = description;
        this.role = role;
        this.location = location;
        this.reportType = reportType;
        this.image = image;
        this.user = user;
    }

    public ProblemReport(String description, Role role, Location location, ReportType reportType, User user){
        super();
        this.description = description;
        this.role = role;
        this.location = location;
        this.reportType = reportType;
        this.image = null;
        this.user = user;
    }

    // Getters
    public String getDescription() {

        return description;
    }

    public Role getRole() {

        return role;
    }

    public Location getLocation() {

        return location;
    }

    public ReportType getReportType() {

        return reportType;
    }

    public String getImage() {

        return image;
    }

    public User getUser(){

        return this.user;
    }


    // Setters
    public void setDescription(String description) {

        this.description = description;
    }

    public void setRole(Role role) {

        this.role = role;
    }

    public void setLocation(Location location) {

        this.location = location;
    }

    public void setReportType(ReportType reportType) {

        this.reportType = reportType;
    }

    public void setImage(String image) {

        this.image = image;
    }

    public void setUser(User user){

            this.user = user;
    }

    public static Role getRoleFromString(String value) {
        switch (value.toLowerCase()) {
            case "teacher": return Role.TEACHER;
            case "student": return Role.STUDENT;
            case "school crew": return Role.SCHOOL_CREW;
            case "other": return Role.OTHER;
            default: return null;
        }
    }

    public static Location getLocationFromString(String value) {
        switch (value.toLowerCase()) {
            case "bathroom": return Location.BATHROOM;
            case "yard": return Location.YARD;
            case "classroom": return Location.CLASSROOM;
            case "teacher lounge": return Location.TEACHERS_LOUNGE;
            case "hallway": return Location.HALLWAY;
            default: return null;
        }
    }

    public static ReportType getReportTypeFromString(String value) {
        switch (value.toLowerCase()) {
            case "lost key": return ReportType.LOST_KEY;
            case "lamp burned out": return ReportType.LAMP_BURNED_OUT;
            case "batteries": return ReportType.BATTERIES;
            case "air conditioner": return ReportType.AIR_CONDITIONER;
            case "projector": return ReportType.PROJECTOR;
            case "broken object": return ReportType.BROKEN_OBJECT;
            default: return null;
        }
    }

    public static Role getRoleByOrdinal(int ordinal) {
        return Role.values()[ordinal];
    }

    public static Location getLocationByOrdinal(int ordinal) {
        return Location.values()[ordinal];
    }

    public static ReportType getReportTypeByOrdinal(int ordinal) {
        return ReportType.values()[ordinal];
    }




}
