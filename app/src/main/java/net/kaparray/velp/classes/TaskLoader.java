package net.kaparray.velp.classes;



public class TaskLoader {


    private String nameTask;
    private String valueTask;
    private String userUID;
    private String key;
    private String nameUser;

    public TaskLoader(String taskName, String taskValue, String userUID, String key, String nameUser) {
        this.nameTask = taskName;
        this.valueTask = taskValue;
        this.userUID = userUID;
        this.key = key;
        this.nameUser = nameUser;
    }

    // For firebase! Without an empty constructor does not work :)
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


