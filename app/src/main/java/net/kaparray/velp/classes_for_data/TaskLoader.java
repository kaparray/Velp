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


    public TaskLoader(String taskName, String taskValue, String userUID, String key, String nameUser, String uniqueIdentificator, String points, String userTakeUID, String accepted) {
        this.nameTask = taskName;
        this.valueTask = taskValue;
        this.userUID = userUID;
        this.key = key;
        this.nameUser = nameUser;
        this.uniqueIdentificator = uniqueIdentificator;
        this.points = points;
        this.userTakeUID = userTakeUID;
        this.accepted= accepted;
    }

    // For firebase! Without an empty constructor does not work :)
    public TaskLoader() {
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
}


