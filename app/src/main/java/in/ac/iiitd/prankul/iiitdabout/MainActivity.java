package in.ac.iiitd.prankul.iiitdabout;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText picker;
    AutoCompleteTextView website;
    int len;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        picker = (EditText) findViewById(R.id.picker);
        website = (AutoCompleteTextView) findViewById(R.id.website);

        String[] list = {"https://www.iiitd.ac.in/about/",
                "https://www.iiitd.ac.in/",
                "https://www.facebook.com/",
                "https://www.google.com/",
                "http://foobar.iiitd.edu.in/",
                "https://library.iiitd.edu.in/",
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,list);
        website.setAdapter(adapter);

    }

    public void onFetchDataClick(View view) {

        try
        {
            len = Integer.parseInt(picker.getText().toString());
        }
        catch(Exception e)
        {
            len = 500;
            Toast.makeText(getApplicationContext(),"Unable to get size... using 500...",Toast.LENGTH_SHORT).show();
        }
        Intent i = new Intent(this,DisplayActivity.class);
        i.putExtra("LEN",len);
        i.putExtra("URL",website.getText().toString());
        startActivity(i);


    }
}