package api.retrofit.nasa.helpers;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vakk on 3/16/16.
 */

/**
 * Server response, contain variables from parsed json
 */
public class Data {
    @SerializedName("copyright")
    String photographer;
    String date;
    String explanation;
    String title;
    @SerializedName("url")
    String imageUrl;
    public Data(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPhotographer() {
        return photographer;
    }
}
