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
        DownloadData inventory = new DownloadData();
        inventory.execute("http://www.crimsonrobotics.com/inventory-2/");
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
            getCategories(s,"<table class=\"wpinventory_loop wpinventory_loop_all wpinventory_loop_all_table\">","<span class=\"categories\"><select name=\"inventory_category_id\">");
            super.onPostExecute(s);
        }

        public void getCategories(String htmlData, String firstCut, String secondCut){
           String partOne = splitString(htmlData, firstCut, 0);
           String categorie = splitString(partOne, secondCut,1);
            Pattern p = Pattern.compile("\">(.*?)</option>");
            Matcher m = p.matcher(categorie);
            while(m.find()){
                catagories.add(m.group(1));
                Log.i("Categorie", m.group(1));
            }
        } //Method cuts downloaded HTML data in order to retrieve the categories in which the items are separated into.


    }

        public String splitString(String string,String cut, int part){
            String[] splitter = string.split(cut);
            String selected = splitter[part];
            return selected;
    } //Created in order to simplify the HTML splitting process and returns only one of the parts.
}
