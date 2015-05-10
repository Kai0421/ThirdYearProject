package tour;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.third.year.project.smktpk.virtualcit.R;

public class CollegeTour extends ActionBarActivity {

    private Point p;
    private ImageButton button;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.college_tour);

        // Set the ActionBar background colour
        super.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d11748")));

        resources = getResources();

        // Rubicon Building
        ImageButton rubiconButton = (ImageButton) findViewById(R.id.rubicon_img_btn);
        rubiconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                //Open popup window
                if (p != null){
                    Drawable img = resources.getDrawable(R.drawable.rubicon);
                    showPopup(CollegeTour.this, p, img);
                }

            }
        });

        // Tourism & Hospitality building
        ImageButton sportsHall = (ImageButton) findViewById(R.id.gym_building);
        sportsHall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                //Open popup window
                if (p != null){
                    Drawable img = resources.getDrawable(R.drawable.gym_building);
                    showPopup(CollegeTour.this, p, img);
                }
            }
        });


        // Tourism & Hospitality building
        ImageButton centreCourt = (ImageButton) findViewById(R.id.courtyard_img_bth);
        centreCourt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                //Open popup window
                if (p != null){
                    Drawable img = resources.getDrawable(R.drawable.temp_cit);
                    showPopup(CollegeTour.this, p, img);
                }
            }
        });


        // Admin building
        ImageButton adminBuilding = (ImageButton) findViewById(R.id.adminbuilding_img_btn);
        adminBuilding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                //Open popup window
                if (p != null){
                    Drawable img = resources.getDrawable(R.drawable.admin_building);
                    showPopup(CollegeTour.this, p, img);
                }
            }
        });

        // Student Centre building
        ImageButton studentCentre = (ImageButton) findViewById(R.id.student_center_img_btn);
        studentCentre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                //Open popup window
                if (p != null){
                    Drawable img = resources.getDrawable(R.drawable.student_center);
                    showPopup(CollegeTour.this, p, img);
                }
            }
        });

        // Library
        ImageButton library = (ImageButton) findViewById(R.id.library_img_btn);
        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                //Open popup window
                if (p != null){
                    Drawable img = resources.getDrawable(R.drawable.library_building);
                    showPopup(CollegeTour.this, p, img);
                }
            }
        });

        // IT building
        ImageButton itBuilding = (ImageButton) findViewById(R.id.it_img_btn);
        itBuilding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                //Open popup window
                if (p != null){
                    Drawable img = resources.getDrawable(R.drawable.it_building);
                    showPopup(CollegeTour.this, p, img);
                }
            }
        });

        // Sports Field
        ImageButton sportsField = (ImageButton) findViewById(R.id.sports_field_img_btn);
        sportsField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                //Open popup window
                if (p != null){
                    Drawable img = resources.getDrawable(R.drawable.sports_field);
                    showPopup(CollegeTour.this, p, img);
                }
            }
        });


        // All weather pitch
        ImageButton allWeatherPitch = (ImageButton) findViewById(R.id.all_weather_sports_field_img_btn);
        allWeatherPitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                //Open popup window
                if (p != null){
                    Drawable img = resources.getDrawable(R.drawable.astra);
                    showPopup(CollegeTour.this, p, img);
                }
            }
        });





    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        int[] location = new int[2];
        button = (ImageButton) findViewById(R.id.rubicon_img_btn);

        // Get the x, y location and store it in the location[] array
        // location[0] = x, location[1] = y.
        button.getLocationOnScreen(location);

        //Initialize the Point with x, and y positions
        p = new Point();
        p.x = location[0];
        p.y = location[1];
    }

    private void showPopup(final Activity context, Point p, Drawable drawableImage) {
        int popupWidth = 800;
        int popupHeight = 800;

        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = 30;
        int OFFSET_Y = 30;

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);

        ImageView popupImage = (ImageView) layout.findViewById(R.id.image_placeholder_imgview);
        popupImage.setImageDrawable(drawableImage);

        // Getting a reference to Close button, and close the popup when clicked.
        Button close = (Button) layout.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
    }

}
