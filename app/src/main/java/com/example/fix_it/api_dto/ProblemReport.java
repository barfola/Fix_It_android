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
    private byte[] image;
    private User user;




    public ProblemReport(String description, Role role, Location location, ReportType reportType , byte [] image, User user){
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

    public byte[] getImage() {
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

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setUser(User user){
        this.user = user;
    }


}
