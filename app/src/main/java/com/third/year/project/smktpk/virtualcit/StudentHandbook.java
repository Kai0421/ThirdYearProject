package com.third.year.project.smktpk.virtualcit;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;


public class StudentHandbook extends ActionBarActivity {
    private long enqueue;
    private DownloadManager dm;

    //Resources res = getResources();
   // int color = res.getColor(R.color.opaque_red);


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handbook_activity);

        // Set the ActionBar background colour
        super.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d11748")));

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(enqueue);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {

                            ImageView view = (ImageView) findViewById(R.id.button_cit_student_service);
                           // ImageView view2 = (ImageView) findViewById(R.id.button_download_student_sport);

                            String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            view.setImageURI(Uri.parse(uriString));
                        }
                    }
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }



    public void onClick(View view) {
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://third-year-project-smktpk.com/CITStudentServicesGuide.pdf"));


        request.setMimeType("application/pdf");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "CITStudentServicesGuide.pdf");
        enqueue = dm.enqueue(request);
        //openPdf();

    }


    public void onClick2(View view) {
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://third-year-project-smktpk.com/CIT-Sports.pdf"));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        enqueue = dm.enqueue(request);

    }


    public void showDownload(View view) {
        Intent i = new Intent();
        i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(i);
    }


    public void viewPicture(View view) {

        final ImageButton buttonCitService = (ImageButton) findViewById(R.id.button_cit_student_service);
        final ImageButton buttonCitSport = (ImageButton) findViewById(R.id.button_cit_sport);


        buttonCitService.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                WebView mWebView = new WebView(StudentHandbook.this);
                mWebView.getSettings().setJavaScriptEnabled(true);
                //mWebView.getSettings().setPluginsEnabled(true);
                mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + "http://third-year-project-smktpk.com/CITStudentServicesGuide.pdf");
                setContentView(mWebView);
            }
        });


        buttonCitSport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                WebView mWebView = new WebView(StudentHandbook.this);
                mWebView.getSettings().setJavaScriptEnabled(true);
                //mWebView.getSettings().setPluginsEnabled(true);
                mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + "http://third-year-project-smktpk.com/CIT-Sports.pdf");
                setContentView(mWebView);
            }
        });


        Intent i = new Intent();

    }

}






































/*




public class StudentHandbook extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student_handbook);

        final ImageButton buttonCitService = (ImageButton) findViewById(R.id.button_cit_student_service);
        final ImageButton buttonCitSport = (ImageButton) findViewById(R.id.button_cit_sport);


        buttonCitService.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                WebView mWebView = new WebView(StudentHandbook.this);
                mWebView.getSettings().setJavaScriptEnabled(true);
                //mWebView.getSettings().setPluginsEnabled(true);
                mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + "http://third-year-project-smktpk.com/CITStudentServicesGuide.pdf");
                setContentView(mWebView);
            }
        });


        buttonCitSport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                WebView mWebView = new WebView(StudentHandbook.this);
                mWebView.getSettings().setJavaScriptEnabled(true);
                //mWebView.getSettings().setPluginsEnabled(true);
                mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + "http://third-year-project-smktpk.com/CIT-Sports.pdf");
                setContentView(mWebView);
            }
        });
    }
}


   /*
        final String myHTTPurl = "http://third-year-project-smktpk.com/CITStudentServicesGuide.pdf";
        setContentView(R.layout.activity_student_handbook);
        final String nameOfFile = URLUtil.guessFileName(myHTTPurl,null, MimeTypeMap.getFileExtensionFromUrl(myHTTPurl));

        final ImageButton buttonCitService = (ImageButton) findViewById(R.id.button_cit_student_service);
        final ImageButton buttonCitSport =(ImageButton) findViewById(R.id.button_cit_sport);
        final Button button_download_s_service =(Button) findViewById(R.id.button_download_student_service);

        buttonCitService.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                WebView mWebView = new WebView(StudentHandbook.this);
                mWebView.getSettings().setJavaScriptEnabled(true);
                //mWebView.getSettings().setPluginsEnabled(true);
                mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + "http://third-year-project-smktpk.com/CITStudentServicesGuide.pdf");
                setContentView(mWebView);
            }
        });


        buttonCitSport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                WebView mWebView = new WebView(StudentHandbook.this);
                mWebView.getSettings().setJavaScriptEnabled(true);
                //mWebView.getSettings().setPluginsEnabled(true);
                mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + "http://third-year-project-smktpk.com/CIT-Sports.pdf");
                setContentView(mWebView);
            }
        });

        button_download_s_service.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(myHTTPurl));
                request.setTitle("File download.");
                request.setDescription("File is being downloated....");
                request.allowScanningByMediaScanner();//delete this later on
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nameOfFile);




                //DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

               // DownloadManager manager = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager manager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);

            }
        });

    }
}

*/

/*
public class StudentHandbook extends Activity {


    @Override
        public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_handbook);

        Button button = (Button) findViewById(R.id.button1);


        WebView mWebView = new WebView(StudentHandbook.this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        //mWebView.getSettings().setPluginsEnabled(true);
        mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + "http://third-year-project-smktpk.com/CIT-Sports.pdf");
        setContentView(mWebView);

    }

}

*/


/*
public class StudentHandbook extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_handbook);
    }
}
 */


/*
public class MyPdfViewActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    WebView mWebView=new WebView(MyPdfViewActivity.this);
    mWebView.getSettings().setJavaScriptEnabled(true);
    mWebView.getSettings().setPluginsEnabled(true);
    mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url="+LinkTo);
    setContentView(mWebView);
  }
}
 */

/*

  void openPdf() {

        //String sur_fil = surat_fi.getText().toString();
       // String baca_file = "/sdcard/Android/data/com.e_office/files/";

        String baca_file = "/sdcard/Android/DIRECTORY_DOWNLOADS/";

        String fs_baca = baca_file + "CITStudentServicesGuide.pdf";
        File pdfFile = new File(fs_baca);
        if (pdfFile.exists()) {
            Uri path = Uri.fromFile(pdfFile);
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(pdfIntent);
            } catch (ActivityNotFoundException e) {
                //Toast.makeText(SuratMasuk.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }

    }

*/

