package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.BreakIterator;

public class WeatherActivity extends AppCompatActivity {

    private static final String OPEN_WEATHER_MAP_API_KEY ="52976fc6f9cf66a062dce0ec8f79fde8";
    TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        txtView = (TextView) findViewById(R.id.textView_weat_info);
        //co ordinates for Derry
        double lat =55 , lon = -7.31;
        String units = "metric";
        String url = String.format("https://openweathermap.org/data/2.5/weather?q=Derry,uk&appid=b6907d289e10d714a6e88b30761fae22",
                lat, lon, units, OPEN_WEATHER_MAP_API_KEY);
        new GetWeatherTask().execute(url);
    }

    private class GetWeatherTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings){
            String weather = "UNDEFINED";
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null){
                    builder.append(inputString);
                }
                JSONObject toplevel = new JSONObject(builder.toString());
                JSONObject main = toplevel.getJSONObject("main");
                weather = String.valueOf(main.getDouble("temp"));


                urlConnection.disconnect();
            } catch(IOException | JSONException e){
                e.printStackTrace();
            }
            return weather;
        }

        @Override
        protected void onPostExecute(String temp){
            txtView.setText("Current Temperature : " + temp);

        }


    }



    public void goFoodHygiene (View view){
        startActivity(new Intent(WeatherActivity.this, FoodHygieneActivity.class));
    }
    public void goMainActivity (View view){
        startActivity(new Intent(WeatherActivity.this, MainActivity.class));
    }

}
