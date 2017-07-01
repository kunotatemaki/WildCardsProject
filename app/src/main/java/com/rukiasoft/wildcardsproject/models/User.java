package com.rukiasoft.wildcardsproject.models;

/**
 * Created by roll on 26/06/17.
 */
public class User {

    // region Member Variables
    private String pictureUrl;
    private String userName;
    private int userAge;
    private String city;
    private String profession;
    private String smokingAttitude;
    private String wishOfChildren;
    private long dateModified;
    // endregion

    // region Constructors
    public User(){

    }
    // endregion

    // region Getters

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserAge() {
        return userAge;
    }

    public String getCity() {
        return city;
    }

    public String getProfession() {
        return profession;
    }

    public String getSmokingAttitude() {
        return smokingAttitude;
    }

    public String getWishOfChildren() {
        return wishOfChildren;
    }

    public long getDateModified() {
        return dateModified;
    }
    // endregion

    // region Setters

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setSmokingAttitude(String smokingAttitude) {
        this.smokingAttitude = smokingAttitude;
    }

    public void setWishOfChildren(String wishOfChildren) {
        this.wishOfChildren = wishOfChildren;
    }

    public void setDateModified(long dateModified) {
        this.dateModified = dateModified;
    }

    // endregion

}
