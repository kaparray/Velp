package net.kaparray.velp.classes_for_data;



public class RatingData {

    private String nameRating;
    private String valueRating;
    private String key;

    public RatingData(String nameRating, String valueRating, String key) {
        this.nameRating = nameRating;
        this.valueRating = valueRating;
        this.key = key;
    }

    // For firebase
    public RatingData() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNameRating() {
        return nameRating;
    }

    public void setNameRating(String nameRating) {
        this.nameRating = nameRating;
    }

    public String getValueRating() {
        return valueRating;
    }

    public void setValueRating(String valueRating) {
        this.valueRating = valueRating;
    }
}
