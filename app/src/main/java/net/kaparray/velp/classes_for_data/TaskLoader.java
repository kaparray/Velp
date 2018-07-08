package net.kaparray.velp.classes_for_data;



public class TaskLoader {


    private String nameTask;
    private String valueTask;
    private String userUID;
    private String key;
    private String nameUser;
    private String uniqueIdentificator;
    private String accepted;
    private String userTakeUID;
    private String points;
    private double locationLatitude;
    private double locationLongitude;
    private String time;
    private String photo;
    private String done;
    private String helped;
    private String doublePoints;


    public TaskLoader(String taskName, String taskValue, String userUID, String key, String nameUser, String uniqueIdentificator, String points, String userTakeUID, String accepted, double locationLatitude, double locationLongitude, String time, String photo, String helped, String done, String doublePoints) {
        this.nameTask = taskName;
        this.valueTask = taskValue;
        this.userUID = userUID;
        this.key = key;
        this.nameUser = nameUser;
        this.uniqueIdentificator = uniqueIdentificator;
        this.points = points;
        this.userTakeUID = userTakeUID;
        this.accepted= accepted;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.time = time;
        this.photo = photo;
        this.helped = helped;
        this.done = done;
        this.doublePoints = doublePoints;
    }

    // For firebase! Without an empty constructor does not work :)
    public TaskLoader() {
    }



    public TaskLoader(String taskName, String taskValue,  String nameUser,  String points, String accepted, double locationLatitude, double locationLongitude, String time, String photo, String key) {
        this.nameTask = taskName;
        this.valueTask = taskValue;
        this.nameUser = nameUser;
        this.points = points;
        this.accepted= accepted;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.time = time;
        this.photo = photo;
        this.key = key;
    }


    public String getDoublePoints() {
        return doublePoints;
    }

    public void setDoublePoints(String doublePoints) {
        this.doublePoints = doublePoints;
    }


    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUniqueIdentificator() {
        return uniqueIdentificator;
    }

    public void setUniqueIdentificator(String uniqueIdentificator) {
        this.uniqueIdentificator = uniqueIdentificator;
    }


    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String value) {
        this.nameTask = value;
    }

    public String getValueTask() {
        return valueTask;
    }

    public void setValueTask(String value) {
        this.valueTask = value;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String value) {
        this.key = value;
    }
    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }


    public String getUserTakeUID() {
        return userTakeUID;
    }

    public void setUserTakeUID(String userTakeUID) {
        this.userTakeUID = userTakeUID;
    }

    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHelped() {
        return helped;
    }

    public void setHelped(String helped) {
        this.helped = helped;
    }


    public String getDone() {
        return done;
    }

    public void setDone(String done) {
        this.done = done;
    }
}


