package net.kaparray.velp.classes_for_data;



public class RatingData {

    private String nameRating;
    private String valueRating;

    public RatingData(String nameRating, String valueRating) {
        this.nameRating = nameRating;
        this.valueRating = valueRating;
    }

    // For firebase
    public RatingData() {
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
