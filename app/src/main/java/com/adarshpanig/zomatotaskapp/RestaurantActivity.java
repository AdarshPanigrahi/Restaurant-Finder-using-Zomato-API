package com.adarshpanig.zomatotaskapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RestaurantActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RestaurantAdapter mRestaurantAdapter;
    private ArrayList<RestaurantItem> mRestaurantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        mRecyclerView = findViewById(R.id.recyclerView2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRestaurantList = new ArrayList<>();

        Intent intent = getIntent();
        String result=intent.getStringExtra("value");

        String query="https://developers.zomato.com/api/v2.1/search?q="+result;
        new RestaurantActivity.ApiTask().execute(query);

    }

    class ApiTask extends AsyncTask<String, String,String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection=null;
            BufferedReader reader=null;
            String API_KEY="1b3c8b37ea96785391fa55c288ac385c";

            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("user-key",API_KEY);
                urlConnection.connect();
                InputStream in = urlConnection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(in));
                StringBuffer buffer= new StringBuffer();
                String line="";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finalJson= buffer.toString();
                String MyStr="";
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray( "restaurants");
                for(int i=0; i<parentArray.length();i++ )
                {
                    JSONObject finalObject =parentArray.getJSONObject(i);
                    JSONObject ultraFinal = finalObject.getJSONObject("restaurant");
                    String name= ultraFinal.getString("name");

                    JSONObject promiseLast = ultraFinal.getJSONObject("location");
                    String address=promiseLast.getString("address");
                    String cuisines= ultraFinal.getString("cuisines");

                    mRestaurantList.add(new RestaurantItem(name,address,cuisines));

                }

                return MyStr;

            }catch(MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(urlConnection!=null) {
                    urlConnection.disconnect();
                }
                try {
                    if(reader!=null){
                        reader.close();
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(mRestaurantList!=null) {
                mRestaurantAdapter = new RestaurantAdapter(RestaurantActivity.this, mRestaurantList);
                mRecyclerView.setAdapter(mRestaurantAdapter);
            }
        }
    }
}
