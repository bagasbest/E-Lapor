package com.project.elapor.ui.pengaduan;

import android.os.Parcel;
import android.os.Parcelable;

public class PengaduanModel implements Parcelable {


    private String uid;
    private String userUid;
    private String userImage;
    private String userUnit;
    private String userName;
    private String adminUid;
    private String adminImage;
    private String adminUnit;
    private String adminName;
    private String date;
    private String message;
    private String status;

    public PengaduanModel(){}

    protected PengaduanModel(Parcel in) {
        uid = in.readString();
        userUid = in.readString();
        userImage = in.readString();
        userUnit = in.readString();
        userName = in.readString();
        adminUid = in.readString();
        adminImage = in.readString();
        adminUnit = in.readString();
        adminName = in.readString();
        date = in.readString();
        message = in.readString();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(userUid);
        dest.writeString(userImage);
        dest.writeString(userUnit);
        dest.writeString(userName);
        dest.writeString(adminUid);
        dest.writeString(adminImage);
        dest.writeString(adminUnit);
        dest.writeString(adminName);
        dest.writeString(date);
        dest.writeString(message);
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PengaduanModel> CREATOR = new Creator<PengaduanModel>() {
        @Override
        public PengaduanModel createFromParcel(Parcel in) {
            return new PengaduanModel(in);
        }

        @Override
        public PengaduanModel[] newArray(int size) {
            return new PengaduanModel[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserUnit() {
        return userUnit;
    }

    public void setUserUnit(String userUnit) {
        this.userUnit = userUnit;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAdminUid() {
        return adminUid;
    }

    public void setAdminUid(String adminUid) {
        this.adminUid = adminUid;
    }

    public String getAdminImage() {
        return adminImage;
    }

    public void setAdminImage(String adminImage) {
        this.adminImage = adminImage;
    }

    public String getAdminUnit() {
        return adminUnit;
    }

    public void setAdminUnit(String adminUnit) {
        this.adminUnit = adminUnit;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
