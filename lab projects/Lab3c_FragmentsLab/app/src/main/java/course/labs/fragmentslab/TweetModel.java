package course.labs.fragmentslab;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Matei Alexandru on 30.06.2016.
 */
public class TweetModel {
    @SerializedName("name")
    String user;
    String text;
    String created_at;

    public String getUser() {
        return user;
    }


    public String getText() {
        return text;
    }

}
