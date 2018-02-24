package net.kaparray.velp.classes;


import com.google.firebase.database.IgnoreExtraProperties;
//@IgnoreExtraProperties

public class TaskLoader {


    private String nameTask;
    private String valueTask;
    private String userTask;
    private String key;

    public TaskLoader(String taskName, String taskValue, String userUID, String key) {
        this.nameTask = taskName;
        this.valueTask = taskValue;
        this.userTask = userUID;
        this.key = key;
    }

    public TaskLoader() {
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

    public String getUserTask() {
        return userTask;
    }

    public void setUserTask(String value) {
        this.userTask = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String value) {
        this.key = value;
    }
}


