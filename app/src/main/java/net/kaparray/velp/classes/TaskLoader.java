package net.kaparray.velp.classes;



public class TaskLoader {


    private String nameTask;
    private String valueTask;
    private String userUID;
    private String key;
    private String nameUser;
    private String photoUser;



    public TaskLoader(String taskName, String taskValue, String userUID, String key, String nameUser, String photoUser) {
        this.nameTask = taskName;
        this.valueTask = taskValue;
        this.userUID = userUID;
        this.key = key;
        this.nameUser = nameUser;
        this.photoUser = photoUser;
    }

    // For firebase! Without an empty constructor does not work :)
    public TaskLoader() {
    }

    public String getPhotoUser() {
        return photoUser;
    }

    public void setPhotoUser(String photoUser) {
        this.photoUser = photoUser;
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



}


