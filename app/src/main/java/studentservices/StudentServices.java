package studentservices;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.third.year.project.smktpk.virtualcit.R;

public class StudentServices extends ActionBarActivity {

    String[] student_services_list_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_services_list);

        Resources res = this.getResources();

        // Set the ActionBar background colour
        super.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d11748")));

        student_services_list_items = res.getStringArray(R.array.student_services_items);

        ListView list = (ListView) findViewById(R.id.student_service_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.student_services_content, student_services_list_items);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClick, int position, long id) {

                Intent intent;

                String[] urls = new String[6];

                urls[0] = "http://third-year-project-smktpk.com/";
                urls[1] = "http://third-year-project-smktpk.com/sport";
                urls[2] = "http://third-year-project-smktpk.com/students-union";
                urls[3] = "http://third-year-project-smktpk.com/chaplaincy";
                urls[4] = "http://third-year-project-smktpk.com/medical-centre";
                urls[5] = "http://third-year-project-smktpk.com/academic-learning-centre";

                switch (position) {
                    case 0:
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(urls[0]));
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(urls[1]));
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(urls[2]));
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(urls[3]));
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(urls[4]));
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(urls[5]));
                        startActivity(intent);
                        break;
                }
            }
        });


    }


}
