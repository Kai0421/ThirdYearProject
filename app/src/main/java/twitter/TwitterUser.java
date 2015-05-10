package twitter;

import com.google.gson.annotations.SerializedName;


public class TwitterUser {

    @SerializedName("screen_name")
    private String screenName;

    @SerializedName("name")
    private String name;

    @SerializedName("favourites_count")
    private String favouritesCount;

    @SerializedName("friends_count")
    private String friendsCount;

    @SerializedName("followers_count")
    private String followersCount;

    @SerializedName("statuses_count")
    private String statusesCount;

    @SerializedName("listed_count")
    private String listedCount;

    @SerializedName("profile_image_url")
    private String profileImageUrl;

    @SerializedName("profile_background_image_url_https")
    private String profileBackgroundImageUrl;

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getFavouritesCount() {
        return favouritesCount;
    }
    public String getFollowersCount() {
        return followersCount;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }
    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getName() {
        return name;
    }

    public String getFriendsCount() {
        return friendsCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatusesCount() {
        return statusesCount;
    }

    public String getListedCount() {
        return listedCount;
    }
}
