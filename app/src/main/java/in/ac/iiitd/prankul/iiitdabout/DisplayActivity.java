package in.ac.iiitd.prankul.iiitdabout;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DisplayActivity extends AppCompatActivity {

    TextView resultView;
    int len;
    boolean rotated = false;
    String resultData;
    Toolbar toolbar;
    ProgressBar pb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);


        resultView = (TextView) findViewById(R.id.result);
        pb = (ProgressBar) findViewById(R.id.progressBar);

        if(savedInstanceState!=null)
        {
            rotated = savedInstanceState.getBoolean("ROTATE",false);
            resultData = savedInstanceState.getString("RESULT","https://iiitd.ac.in/about/");

            if(rotated)
            {
                resultView.setText(resultData);
                getSupportActionBar().setTitle(savedInstanceState.getString("TITLE"));
                if(savedInstanceState.getBoolean("PB"))
                {
                    pb.setVisibility(View.VISIBLE);
                }
                else
                {
                    pb.setVisibility(View.INVISIBLE);
                }
            }
        }
        else
        {
            String stringUrl = "https://iiitd.ac.in/about/";
            len = getIntent().getIntExtra("LEN",500);
            stringUrl = getIntent().getStringExtra("URL");

            ConnectivityManager connectionMngr = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connectionMngr.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected())
            {
                new asinktask().execute(stringUrl);
                Toast.makeText(getApplicationContext(),"Getting data...",Toast.LENGTH_SHORT).show();
            }
            else
            {
                pb.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"Phone is not connected to internet. Kindly switch on WIFI or mobile data", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("ROTATE",true);
        outState.putString("RESULT", resultView.getText().toString());
        outState.putString("TITLE",toolbar.getTitle().toString());
        if(pb.getVisibility()==View.VISIBLE)
        {
            outState.putBoolean("PB",true);
        }
        else
        {
            outState.putBoolean("PB",false);
        }
    }

    private class asinktask extends AsyncTask<String, Void, String>
    {
        Document d;
        @Override
        protected String doInBackground(String... urls) {
            try
            {
                InputStream is = null;

                try
                {
                    URL url = new URL(urls[0]);
                    HttpURLConnection connect = (HttpURLConnection) url.openConnection();
                    connect.setReadTimeout(10000);
                    connect.setConnectTimeout(15000);
                    connect.setRequestMethod("GET");
                    connect.setDoInput(true);

                    connect.connect();
                    int response = connect.getResponseCode();
                    is = connect.getInputStream();

                    String contentAsString = readIt(is);

                    d = Jsoup.parse(contentAsString);

                    Log.i("HTML",contentAsString);

                    return contentAsString.substring(0,min(len,contentAsString.length()));

                }
                finally
                {
                    if (is != null)
                    {
                        is.close();
                    }
                }

            }
            catch (IOException e)
            {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        public String readIt(InputStream is) throws IOException {
            if (is != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                is.close();
                return sb.toString();
            }
            return "error aa gyi in reading from web... most probably size ki dikkat...";
        }

        @Override
        protected void onPostExecute(String result)
        {
            if(d!=null)
            {
                toolbar.setTitle(d.title());
            }
            resultView.setText("Title is parsed and written in activity toolbar\n\nHTML DATA :\n\n"
                    +result);
            resultData = result;
            pb.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(),"Data Set!",Toast.LENGTH_LONG).show();
        }
    }

    int min(int a, int b)
    {
        if(a<b) {return a;}
        else {return b;}
    }
}
