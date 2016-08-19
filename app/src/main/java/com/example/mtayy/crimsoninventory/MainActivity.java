package com.example.mtayy.crimsoninventory;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
ArrayList<String> catagories = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadData invent = new DownloadData();
        invent.execute("http://www.crimsonrobotics.com/inventory-2/");
    }

    public class DownloadData extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
           String result ="";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data != -1){
                    char current = (char)data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String[] splitter = s.split("<table class=\"wpinventory_loop wpinventory_loop_all wpinventory_loop_all_table\">");
            String partOne = splitter[0];
            String[] splitTwo  = partOne.split("<span class=\"categories\"><select name=\"inventory_category_id\">");
            String catagorie = splitTwo[1];
            Pattern p = Pattern.compile("\">(.*?)</option>");
            Matcher m = p.matcher(catagorie);
                while(m.find()){
                    catagories.add(m.group(1));
                    Log.i("Catagorie", m.group(1));
                }
            super.onPostExecute(s);
        }
    }

}
