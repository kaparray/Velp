package net.kaparray.velp.classes_for_data;

public class MarkerData {


    private double locationLatitude;
    private double locationLongitude;
    private String key;
    private String nameTask;

    public MarkerData(double locationLatitude, double locationLongitude, String key, String nameTask) {
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.key = key;
        this.nameTask = nameTask;
    }

    // For fire base
    public MarkerData() {
    }


    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
