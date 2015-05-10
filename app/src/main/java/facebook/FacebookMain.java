package facebook;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.third.year.project.smktpk.virtualcit.R;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FacebookMain extends ActionBarActivity {

    private static final String LIMIT = "20";
    private ArrayAdapter<FacebookPost> adapter;
    private ArrayList<FacebookPost> facebookPostArrayList;
    private ListView facebookListView;
    private String access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facebook_list);

        // Set the ActionBar background colour
        super.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d11748")));

        // Facebook posts will be displayed in a custom ListView - facebookListView
        facebookListView = (ListView) findViewById(R.id.facebook_lv);

        // Downloading of the post will be done in a background thread
        new DownloadFacebookJSON(FacebookMain.this).execute();
    }

    class DownloadFacebookJSON extends AsyncTask<Void, Void, String> {

        Dialog dialog;
        Context mContext;
        String baseURL = "https://graph.facebook.com/oauth/access_token?client_id=";
        String app_id = "1562322934038797";
        String clientSecretQuery = "&client_secret=";
        String APP_SECRET = "2ec919b22c5510653f76f68fc8d97c7b";
        String tailURL = "&grant_type=client_credentials";
        HttpClient client;
        HttpGet getRequest;
        ResponseHandler<String> responseHandler;
        String responseBody = null;

        public DownloadFacebookJSON(Context context) {

            this.mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Display a Dialog to the user while retrieving posts
            dialog = new ProgressDialog(mContext);
            dialog.setTitle("Downloading Posts");
            dialog.show();
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... params) {

            String OWNER_OF_FEED = "mycit";
            client = new DefaultHttpClient();

            // The url containing the app id and secret key which will be used
            // to get an access token to access Facebook resources
            String url = (baseURL + app_id + clientSecretQuery + APP_SECRET + tailURL);

            try {

                // Use Apachhe HTTP classes/methods to get the access token
                getRequest = new HttpGet(url);
                responseHandler = new BasicResponseHandler();
                access_token = client.execute(getRequest, responseHandler);

                // Create URL and replace the pipe '|' symbol in the access token
                String uri = "https://graph.facebook.com/" + OWNER_OF_FEED + "/feed?limit=" + LIMIT + "&"
                        + access_token.replace("|", "%7C");

                // Using the uri which points to the mycit facebook feeds - number specified by Limit variable
                // Retrieve the string contents of that page
                getRequest = new HttpGet(uri);
                responseBody = client.execute(getRequest, responseHandler);

            } catch (IOException e) {
                e.printStackTrace();
            }

            // Return the string of the JSON data to onPostExecute
            return responseBody;
        }


        @Override
        protected void onPostExecute(String result) {

            facebookPostArrayList = new ArrayList<>();
            FacebookPost fbPost;

            dialog.dismiss();

            JSONObject jsonObject = null;
            JSONArray facebookJSONArray = null;

            StringBuilder stringBuilder = new StringBuilder();

            try {
                // Create a JSON object using the results obtained from the background task
                jsonObject = new JSONObject(result);
                facebookJSONArray = jsonObject.getJSONArray("data");

                // Loop through JSON array
                // Populate a list of FacebookPost objects
                // Set their values from json fields
                for (int x = 0; x < facebookJSONArray.length(); x++) {
                    JSONObject facebookObject = facebookJSONArray.getJSONObject(x);

                    fbPost = new FacebookPost();

                    if (facebookObject.has("message")) {
                        fbPost.setMessage(facebookObject.getString("message"));
                    }

                    // The top of the facebook post uses the name field
                    // Not all facebook post have a name field
                    // If no name field exist use the first 10
                    // characters of the message field
                    if (facebookObject.has("name")) {
                        fbPost.setName(facebookObject.getString("name"));
                    } else {
                        if (facebookObject.has("message")) {
                            String message = facebookObject.getString("message");

                            String firstWords[] = message.split("\\s");
                            String newName = "";

                            for (int i = 0; i < 8; i++) {
                                newName = newName + firstWords[i] + " ";
                            }

                            newName = newName + " ...";
                            fbPost.setName(newName);
                        }
                    }

                    if (facebookObject.has("description")) {
                        fbPost.setDescription(facebookObject.getString("description"));
                    }

                    if (facebookObject.has("link")) {
                        fbPost.setLink(facebookObject.getString("link"));
                    }

                    if (facebookObject.has("object_id")) {
                        fbPost.setObject_id(facebookObject.getString("object_id"));
                    }

                    fbPost.setPostID(facebookObject.getString("id"));
                    fbPost.setDateCreated(facebookObject.getString("created_time"));
                    fbPost.setType(facebookObject.getString("type"));

                    facebookPostArrayList.add(fbPost);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // The Facebook likes per post are not available on the feed page
            // A separate request is required to get the number of likes
            new GetFacebookLikes(facebookPostArrayList).execute();

            // Create a new custom adapter to display in the ListView
            CustomFacebookDisplayAdapter facebookDisplayAdapter = new CustomFacebookDisplayAdapter(mContext, R.id.facebook_lv, facebookPostArrayList);
            facebookListView.setAdapter(facebookDisplayAdapter);
            super.onPostExecute(result);
        }

    }

    // Get Facebook Post number of likes
    class GetFacebookLikes extends AsyncTask<Void, Void, String> {

        private ArrayList<FacebookPost> facebookPostArrayLIst;

        public GetFacebookLikes(ArrayList<FacebookPost> facebookPostsList) {
            this.facebookPostArrayLIst = facebookPostsList;
        }

        @Override
        protected String doInBackground(Void... params) {

            String base = "https://graph.facebook.com/";
            String tail = "/likes?summary=true";

            // Loop through all the posts in the Facebook ArrayList
            for (int x = 0; x < facebookPostArrayLIst.size(); x++) {

                try {

                    // Create the URL to access Facebook likes page
                    // Extract JSON and set the value in the appropriate post
                    String url = (base + facebookPostArrayLIst.get(x).getPostID() + tail + "&" + access_token.replace("|", "%7C"));
                    HttpClient client = new DefaultHttpClient();
                    HttpGet getRequest = new HttpGet(url);
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    String responseBody = client.execute(getRequest, responseHandler);

                    JSONObject jsonImageObject = new JSONObject(responseBody);
                    JSONObject jsonObject = jsonImageObject.getJSONObject("summary");

                    facebookPostArrayLIst.get(x).setNumberOfLikes(jsonObject.getString("total_count"));

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(String facebookPosts) {
            super.onPostExecute(facebookPosts);
        }
    }

    // The array adapter to be displayed in the ListView
    class CustomFacebookDisplayAdapter extends ArrayAdapter<FacebookPost> {

        private Context context;
        private ArrayList<FacebookPost> facebookPosts;

        public CustomFacebookDisplayAdapter(Context context, int layoutResourceId, ArrayList<FacebookPost> facebookPosts) {
            super(context, layoutResourceId, facebookPosts);
            this.context = context;
            this.facebookPosts = facebookPosts;
        }

        // The view that is inflated for each row in the table
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = inflater.inflate(R.layout.facebook_list_post, parent, false);

            // Get all widgets in the facebook_list_post and set their values
            TextView nameTextView = (TextView) row.findViewById(R.id.facebook_name_tv);
            nameTextView.setText(facebookPosts.get(position).getName());

            // Facebook store the create_at object in ISO8601 format
            // Method converts to Irish format
            String formattedDateTimeString = convertISO8601ToDateTimeString(facebookPosts.get(position).getDateCreated());

            TextView dateTimeTextView = (TextView) row.findViewById(R.id.facebook_dt_tv);
            dateTimeTextView.setText(formattedDateTimeString);

            if (facebookPosts.get(position).getLink() != null) {
                TextView linkTextView = (TextView) row.findViewById(R.id.link_tv);
                linkTextView.setText(facebookPosts.get(position).getLink());
            }

            if (facebookPosts.get(position).getMessage() != null) {
                TextView fbMessage = (TextView) row.findViewById(R.id.facebook_message_tv);
                fbMessage.setText(facebookPosts.get(position).getMessage());
            }

            ImageView fbPostImage = (ImageView) row.findViewById(R.id.post_image_tv);
            String url = "https://fbcdn-sphotos-h-a.akamaihd.net/hphotos-ak-xfp1/v/t1.0-9/s720x720/10422920_822847217784728_6118314520404487017_n.jpg?oh=00ce020692759888c41ea252485604a2&oe=557D9D20&__gda__=1433651984_2fc616165da5fb573150c179d9c05797";
            new DownloadImageTask(facebookPosts.get(position).getPostID(), fbPostImage).execute();

            TextView likeCount = (TextView) row.findViewById(R.id.like_count_tv);
            likeCount.setText(facebookPosts.get(position).getNumberOfLikes());

            return row;
        }

        // Convert ISO8601 to Irish format
        public String convertISO8601ToDateTimeString(String iso8601Format) {

            String dateTime[] = iso8601Format.split("T");

            String dateFragment[] = dateTime[0].split("-");

            String sYear = dateFragment[0];
            String sMonth = dateFragment[1];
            String sDay = dateFragment[2];

            dateTime[1] = dateTime[1].replace("+0000", "");

            String timeFragment[] = dateTime[1].split(":");
            String hour = timeFragment[0];
            String minute = timeFragment[1];

            String[] months = {"Jan", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

            int iMonth = Integer.parseInt(sMonth) - 1;
            sMonth = months[iMonth];

            String formattedDateTime = (sDay + " " + sMonth + " " + sYear + "  " + hour + ":" + minute);

            return formattedDateTime;
        }
    }

    // Full sized images are on a separate URL
    // Class used to download them
    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        private String urlImg;
        private ImageView imageView;
        private String imageLocation;
        private String baseURL;
        private String query;

        public DownloadImageTask(String id, ImageView imageView) {
            this.imageView = imageView;
            this.imageLocation = id;
            this.baseURL = "https://graph.facebook.com/";
            this.query = "fields=attachments%7Bmedia%7D";
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                // Get the URL
                // Replace illegal characters
                String urlToImage = (baseURL + imageLocation + "?" + query + "&" + access_token.replace("|", "%7C"));
                urlToImage = urlToImage.replaceAll("\\/", "/");
                urlToImage = urlToImage.replaceAll("\\u0025", "%");

                // Get an HTTP request/response
                HttpClient client = new DefaultHttpClient();
                HttpGet getRequest = new HttpGet(urlToImage);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String responseBody = client.execute(getRequest, responseHandler);

                // Extract the data using JSON
                JSONObject jsonImageObject = new JSONObject(responseBody);
                JSONObject jsonObject = jsonImageObject.getJSONObject("attachments");
                JSONArray imageJSONArray = jsonObject.getJSONArray("data");
                JSONObject jsonDataObj = imageJSONArray.getJSONObject(0).getJSONObject("media");
                JSONObject jsonImageObj = jsonDataObj.getJSONObject("image");
                String srcImg = jsonImageObj.getString("src");

                // Get the Image return the Bitmap
                URL url = new URL(srcImg);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                return bitmap;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Set the retrieved Bitmap image in the array adapter
            imageView.setImageBitmap(result);
            super.onPostExecute(result);
        }
    }

}
