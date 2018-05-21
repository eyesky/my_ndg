package com.apptech.myndg.model;

/**
 * Created by raman on 2/20/17.
 */
public class CarInfo {

    private int id;
    private String name;
    private String status;
    private String dateTime;
    private String region;
    private String selectedLanguage;
    private int selectedCar;
    private boolean isSection;

    public CarInfo(int id, String name, String status, String dateTime, String region, String language, int selectedCar) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.dateTime = dateTime;
        this.region = region;
        this.selectedLanguage = language;
        this.selectedCar = selectedCar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    public int getSelectedCar() {
        return selectedCar;
    }

    public void setSelectedCar(int selectedCar) {
        this.selectedCar = selectedCar;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public boolean isSection() {
        return isSection;
    }

    public void setSection(boolean section) {
        isSection = section;
    }

    @Override
    public String toString() {
        return ""+getId()+", "+getName()+", "+getStatus()+" ,"+getRegion();
    }
}
