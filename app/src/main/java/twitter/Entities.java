package twitter;

        import com.google.gson.annotations.SerializedName;

        import java.util.ArrayList;

public class Entities {

    @SerializedName("media")
    private ArrayList<EntityMedia> eMedia = new ArrayList<EntityMedia>();

    public ArrayList<EntityMedia> geteMedia() {
        return eMedia;
    }
}
