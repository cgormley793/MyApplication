package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FoodHygieneActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private TextView textView;
    private RequestQueue mQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_hygiene);

        textView = findViewById(R.id.textView_food);

        mQueue = Volley.newRequestQueue(this);


        jsonParse();

    }

    private void jsonParse() {

        final String url = "http://api.ratings.food.gov.uk/Establishments/basic";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(getBaseContext(), response.toString(), Toast.LENGTH_LONG).show();
                // here you can get the restaurants and rating details
                try {
                    JSONArray jsonArray = response.getJSONArray("establishments");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject estab = jsonArray.getJSONObject(i);

                        String businessName = estab.getString("BusinessName");
                        String businessValue = estab.getString("RatingValue");

                        textView.append(businessName + ", " + businessValue + "\n\n");
                    }
//                                String businessValue = establishment.getString("RatingValue");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("LOG", "An Error occured while making the request");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                //The api requires me to specify the api version
                params.put("x-api-version", "2");
                params.put("accept", "application/json");
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(jsonObjectRequest);
    }
    public void goMapsActivity (View view){
        startActivity(new Intent(FoodHygieneActivity.this, MapsActivity.class));
    }
}
