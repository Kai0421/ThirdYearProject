package twitter;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.third.year.project.smktpk.virtualcit.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TwitterMain extends ActionBarActivity {

    final static String SCREEN_NAME = "CIT_ie";
    private ArrayAdapter<Tweet> adapter;
    private ArrayList<Tweet> tweetList;
    private ListView twitterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_list);

        // Set the ActionBar background colour
        super.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d11748")));

        tweetList = new ArrayList<>();

        // Twitter tweets will be displayed in a ListView
        twitterList = (ListView) findViewById(R.id.twitter_list);

        // Tweets will be download in a background thread
        new DownloadCITTweets(TwitterMain.this).execute(SCREEN_NAME);
    }

    class DownloadCITTweets extends AsyncTask<String, Void, String> {

        // Tweet to be retrieved is shown by count variable
        private final static String COUNT = "50";

        // Secret key - customer key and tokens required to access data
        private final static String CONSUMER_KEY = "fwi8WmAICFhM5po3KWNYciN42";
        private final static String CONSUMER_SECRET = "9bokPH3ByKhUEwpevpq6CtEF4gLXYw2dUdctN6UCu1yewIWBfD";
        private final static String TwitterTokenURL = "https://api.twitter.com/oauth2/token";
        private final static String TwitterStreamURL = "https://api.twitter.com/1.1/statuses/user_timeline.json?include_rts=1&count=" + COUNT + "&screen_name=";
        private ProgressDialog dialog;
        private Context mContext;

        public DownloadCITTweets(Context context) {
            this.mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Display Dialog to the user
            dialog = new ProgressDialog(mContext);
            dialog.setTitle("Fetching Tweets");
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... screenNames) {
            String result = null;

            // Method that will get a String representation of the stream accessed
            result = getTwitterStream(screenNames[0]);
            return result;
        }

        private String getTwitterStream(String screenName) {

            String results = null;

            try {
                // Encode the Keys
                String urlApiKey = URLEncoder.encode(CONSUMER_KEY, "UTF-8");
                String urlApiSecret = URLEncoder.encode(CONSUMER_SECRET, "UTF-8");
                String combined = urlApiKey + ":" + urlApiSecret;
                String base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);

                // Retrieve authorization from twitter using the encode keys
                HttpPost httpPost = new HttpPost(TwitterTokenURL);
                httpPost.setHeader("Authorization", "Basic " + base64Encoded);
                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                httpPost.setEntity(new StringEntity("grant_type=client_credentials"));
                String rawAuthorization = getResponseBody(httpPost);

                // Get bearer token by authenticating keys
                Authenticated auth = jsonToAuthenticated(rawAuthorization);

                if (auth != null && auth.token_type.equals("bearer")) {

                    // Request Twitter stream
                    HttpGet httpGet = new HttpGet(TwitterStreamURL + screenName);

                    httpGet.setHeader("Authorization", "Bearer " + auth.access_token);
                    httpGet.setHeader("Content-Type", "application/json");

                    results = getResponseBody(httpGet);
                }

                return results;

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return null;
        }

        private Authenticated jsonToAuthenticated(String rawAuthorization) {

            Authenticated auth = null;
            if (rawAuthorization != null && rawAuthorization.length() > 0) {
                try {
                    Gson gson = new Gson();
                    auth = gson.fromJson(rawAuthorization, Authenticated.class);
                } catch (IllegalStateException ex) {
                    ex.printStackTrace();
                }
            }
            return auth;

        }

        private String getResponseBody(HttpRequestBase request) {

            StringBuilder sb = new StringBuilder();

            // Get the Twitter stream and return it to onPostExecute
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
                HttpResponse response = null;
                response = httpClient.execute(request);
                int statusCode = response.getStatusLine().getStatusCode();
                String reason = response.getStatusLine().getReasonPhrase();

                if (statusCode == 200) {

                    HttpEntity entity = response.getEntity();
                    InputStream inputStream = entity.getContent();

                    BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    String line = null;

                    int countIndex = 0;

                    while ((line = bReader.readLine()) != null) {
                        sb.append(line);
                    }
                } else {
                    sb.append(reason);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return sb.toString();
        }

        // Use GSON (Google JSON converter library) to
        // store retrieved JSON objects as Tweet objects
        private Twitter jsonToTwitter(String result) {
            Twitter twits = null;
            if (result != null && result.length() > 0) {
                try {
                    Gson gson = new Gson();
                    twits = gson.fromJson(result, Twitter.class);
                } catch (IllegalStateException ex) {
                    ex.printStackTrace();
                }
            }

            return twits;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();

            // Display the Tweets using a custom array adapter
            ArrayList<Tweet> tweetArrayList = jsonToTwitter(result);
            CustomTweetDisplayAdapter tweetCustomAdapter = new CustomTweetDisplayAdapter(mContext, R.id.twitter_list, tweetArrayList);
            twitterList.setAdapter(tweetCustomAdapter);

            super.onPostExecute(result);
        }
    }

    class CustomTweetDisplayAdapter extends ArrayAdapter<Tweet> {

        private Context context;
        private ArrayList<Tweet> tweets;

        public CustomTweetDisplayAdapter(Context context, int layoutResourceId, ArrayList<Tweet> tweets) {
            super(context, layoutResourceId, tweets);
            this.context = context;
            this.tweets = tweets;
        }

        // Infate a view for each row containing Tweet data
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = inflater.inflate(R.layout.twitter_list_tweet, parent, false);

            TextView citName = (TextView) row.findViewById(R.id.cit_name_tv);
            citName.setText(tweets.get(position).getUser().getName());

            TextView citScreenName = (TextView) row.findViewById(R.id.screen_name_tv);
            citScreenName.setText("@" + tweets.get(position).getUser().getScreenName());

            // Get and parse the Twitter date time
            TextView dayTime = (TextView) row.findViewById(R.id.cit_date_time_tv);
            String tweetTime = parseTwitterUTC(tweets.get(position).getDateCreated());
            dayTime.setText(tweetTime);

            TextView tweetMessage = (TextView) row.findViewById(R.id.tweet_message);
            String formattedTweetText = tweets.get(position).getText();
            formattedTweetText = formattedTweetText.replaceAll("&amp;", "&");
            tweetMessage.setText(formattedTweetText);

            ImageView embeddedImage = (ImageView) row.findViewById(R.id.tweet_embedded_image_iv);
            if (tweets.get(position).getEntities().geteMedia().size() > 0) {
                String url = tweets.get(position).getEntities().geteMedia().get(0).getMediaURL();
                url = (url + ":large");
                new DownloadImageTask(url, embeddedImage).execute();
            }

            TextView retweetCount = (TextView) row.findViewById(R.id.retweet_count_tv);
            retweetCount.setText(tweets.get(position).getRtCount());

            TextView favouriteCount = (TextView) row.findViewById(R.id.favourited_count_tv);
            favouriteCount.setText(tweets.get(position).getTweetFavourited());

            return row;
        }

    }

    // Class to download Tweet images
    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        private String urlImg;
        private ImageView imageView;

        public DownloadImageTask(String url, ImageView imageView) {
            this.urlImg = url;
            this.imageView = imageView;
        }

        // Store as Bitmap set the Imageview
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(urlImg);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
            super.onPostExecute(result);
        }
    }

    public Bitmap getBitmapFromURL(String srcURL) {

        try {
            URL url = new URL(srcURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    // Parse the Twitter date time format to usable format
    public static String parseTwitterUTC(String date) {
        try {

            String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
            SimpleDateFormat formatter = new SimpleDateFormat(twitterFormat);
            Date dateStr = formatter.parse(date);

            String displayFormat = "MMMM dd yyyy hh:mm a";
            String displayDateTimeCreated = new SimpleDateFormat(displayFormat).format(dateStr);

            return displayDateTimeCreated;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}


class Authenticated {
    String token_type;
    String access_token;

    public Authenticated() {
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }


}
