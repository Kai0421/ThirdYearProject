package twitter;

import com.google.gson.annotations.SerializedName;

public class EntityMedia {

    @SerializedName("media_url")
    private String mediaURL;

    @SerializedName("id")
    private String mediaID;

    @SerializedName("media_url_https")
    private String mediaURLHTTPS;

    @SerializedName("display_url")
    private String displayURL;

    public String getMediaURL() {
        return mediaURL;
    }

    public String getMediaURLHTTPS() {
        return mediaURLHTTPS;
    }

    public String getDisplayURL() {
        return displayURL;
    }

    public String getMediaID() {
        return mediaID;
    }
}
