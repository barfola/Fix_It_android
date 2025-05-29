package com.example.fix_it.api_dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ProblemReport extends ApiDtoBase implements Parcelable {
    protected ProblemReport(Parcel in) {
        description = in.readString();
        image = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        int roleOrdinal = in.readInt();
        int locationOrdinal = in.readInt();
        int reportTypeOrdinal = in.readInt();

        role = roleOrdinal != -1 ? Role.values()[roleOrdinal] : null;
        location = locationOrdinal != -1 ? Location.values()[locationOrdinal] : null;
        reportType = reportTypeOrdinal != -1 ? ReportType.values()[reportTypeOrdinal] : null;
        uuid = in.readString();
    }



    public static final Creator<ProblemReport> CREATOR = new Creator<ProblemReport>() {
        @Override
        public ProblemReport createFromParcel(Parcel in) {
            return new ProblemReport(in);
        }

        @Override
        public ProblemReport[] newArray(int size) {
            return new ProblemReport[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(image);
        dest.writeParcelable(user, flags);
        dest.writeInt(role != null ? role.ordinal() : -1);
        dest.writeInt(location != null ? location.ordinal() : -1);
        dest.writeInt(reportType != null ? reportType.ordinal() : -1);
        dest.writeString(uuid);
    }

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

    public boolean isReportChanged(String desc, String role, String location, String reportType) {
        return desc.equals(this.getDescription()) &&
                role.equals(this.getRole().name()) &&
                location.equals(this.getLocation().name()) &&
                reportType.equals(this.getReportType().name());
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
