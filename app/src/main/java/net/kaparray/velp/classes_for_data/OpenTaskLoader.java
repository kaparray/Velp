package net.kaparray.velp.classes_for_data;

public class OpenTaskLoader {


    private String userUID;

    public OpenTaskLoader(String userUID) {
        this.userUID = userUID;
    }

    public OpenTaskLoader() {
    }


    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }


}
