package com.idragonpro.andmagnus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.idragonpro.andmagnus.beans.SubscriptionModel;

public class UserModel {

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("mobileno")
    @Expose
    private String mobileno;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("sNoti")
    @Expose
    private String sNoti;
    @SerializedName("videoid")
    @Expose
    private String videoid;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("role_id")
    @Expose
    private String roleId;
    @SerializedName("daysdiff")
    @Expose
    private long daysdiff;
    @SerializedName("timediff")
    @Expose
    private long timediff;
    @SerializedName("subscriptions")
    @Expose
    private SubscriptionModel subscriptions;
    @SerializedName("daysdiff_web")
    @Expose
    private long daysdiff_web;
    @SerializedName("timediff_web")
    @Expose
    private long timediff_web;
    @SerializedName("subscriptions_web")
    @Expose
    private SubscriptionModel subscriptions_web;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("IsNotificationActive")
    @Expose
    private String isNotificationActive;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getsNoti() {
        return sNoti;
    }

    public void setsNoti(String sNoti) {
        this.sNoti = sNoti;
    }

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public long getDaysdiff() {
        return daysdiff;
    }

    public void setDaysdiff(long daysdiff) {
        this.daysdiff = daysdiff;
    }

    public SubscriptionModel getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(SubscriptionModel subscriptions) {
        this.subscriptions = subscriptions;
    }

    public long getTimediff() {
        return timediff;
    }

    public void setTimediff(long timediff) {
        this.timediff = timediff;
    }

    public long getDaysdiff_web() {
        return daysdiff_web;
    }

    public void setDaysdiff_web(long daysdiff_web) {
        this.daysdiff_web = daysdiff_web;
    }

    public long getTimediff_web() {
        return timediff_web;
    }

    public void setTimediff_web(long timediff_web) {
        this.timediff_web = timediff_web;
    }

    public SubscriptionModel getSubscriptions_web() {
        return subscriptions_web;
    }

    public void setSubscriptions_web(SubscriptionModel subscriptions_web) {
        this.subscriptions_web = subscriptions_web;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getIsNotificationActive() {
        return isNotificationActive;
    }

    public void setIsNotificationActive(String isNotificationActive) {
        this.isNotificationActive = isNotificationActive;
    }
}
