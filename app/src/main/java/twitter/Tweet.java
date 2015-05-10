package twitter;

import com.google.gson.annotations.SerializedName;

public class Tweet {

    @SerializedName("created_at")
    private String DateCreated;

    @SerializedName("id")
    private String Id;

    @SerializedName("retweet_count")
    private String rtCount;

    @SerializedName("text")
    private String Text;

    @SerializedName("favorited")
    private String bFavorited;

    @SerializedName("favorite_count")
    private String tweetFavourited;

    @SerializedName("in_reply_to_status_id")
    private String InReplyToStatusId;

    @SerializedName("in_reply_to_user_id")
    private String InReplyToUserId;

    @SerializedName("in_reply_to_screen_name")
    private String InReplyToScreenName;

    @SerializedName("user")
    private TwitterUser User;

    @SerializedName("entities")
    private Entities Entities;

    @SerializedName("description")
    private TwitterUser description;








    public String getDateCreated() {
        return DateCreated;
    }

    public String getId() {
        return Id;
    }

    public String getInReplyToScreenName() {
        return InReplyToScreenName;
    }

    public String getInReplyToStatusId() {
        return InReplyToStatusId;
    }

    public String getInReplyToUserId() {
        return InReplyToUserId;
    }

    public String getText() {
        return Text;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setInReplyToScreenName(String inReplyToScreenName) {
        InReplyToScreenName = inReplyToScreenName;
    }

    public void setInReplyToStatusId(String inReplyToStatusId) {
        InReplyToStatusId = inReplyToStatusId;
    }

    public String getRtCount() {
        return rtCount;
    }

    public TwitterUser getUser() {
        return User;
    }

    @Override
    public String  toString(){
        return getText();
    }


    public TwitterUser getDescription() {
        return description;
    }

    public String getTweetFavourited() {
        return tweetFavourited;
    }

    public Entities getEntities() {
        return Entities;
    }




    /*
    public String getEntity() {
        return entity;
    }

    public String getHashtags() {
        return hashtags;
    }
    */
}
