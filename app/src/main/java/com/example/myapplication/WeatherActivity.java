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
import java.util.ArrayList;
import java.util.List;

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
        String temp = "";
        @Override
        protected String doInBackground(String... strings){
            String weather = "UNDEFINED";
            String pressure = "UNDEFINED";
            String humidity = "UNDEFINED";
            List<String> weather_list = new ArrayList<>();
            String listString ="";
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
                weather_list.add(String.valueOf(main.getDouble("temp")));
                weather_list.add(String.valueOf(main.getDouble("pressure")));
                weather_list.add(String.valueOf(main.getDouble("humidity")));

                JSONObject wind = toplevel.optJSONObject("wind");
                weather_list.add(String.valueOf(wind.getDouble("speed")));
                weather_list.add(String.valueOf(wind.getDouble("deg")));
                for (String s : weather_list)
                {
                    listString += s + "\t";
                }

                urlConnection.disconnect();
            } catch(IOException | JSONException e){
                e.printStackTrace();
            }
            temp = listString;
            return listString;
        }

        @Override
        protected void onPostExecute(String weather_list){
            String[] tempA = temp.split("\t");
            if (tempA.length > 1)
            {
                txtView.setText("Current Temperature : " + tempA[0] + "\n" + "Curremt Pressure : " + tempA[1] + "\n" +
                        "Current Humidity : " + tempA[2] + "\n" +
                        "Current Speed : " + tempA[3] + "mph" + "\n" +
                        "Current Degrees: " + tempA[4]);
            }

        }


    }



    public void goFoodHygiene (View view){
        startActivity(new Intent(WeatherActivity.this, FoodHygieneActivity.class));
    }
    public void goMainActivity (View view){
        startActivity(new Intent(WeatherActivity.this, MainActivity.class));
    }

}
