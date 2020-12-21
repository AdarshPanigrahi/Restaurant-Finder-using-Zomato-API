package com.adarshpanig.zomatotaskapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

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


public class MainActivity extends AppCompatActivity implements LocationListener {

    private TextView tv;
    private RecyclerView mRecyclerView;
    private NearbyAdapter mNearbyAdapter;
    private ArrayList<NearbyItem> mNearbyList;
    private SearchView searchView;
    LocationManager locationManager;
    LocationListener locationListener;
    public double clat ;
    public double clong;
    private static final int REQUEST_LOCATION = 1;

// wooooo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.Zomato);
        mRecyclerView = findViewById(R.id.recyclerView1);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNearbyList = new ArrayList<>();
        searchView = findViewById(R.id.search_bar1);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);

        }

        getLocation();


         searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
             @Override
             public boolean onQueryTextSubmit(String query) {
                 Intent intent= new Intent(MainActivity.this,RestaurantActivity.class);
                 intent.putExtra("value",query);
                 startActivity(intent);
                 return false;
             }

             @Override
             public boolean onQueryTextChange(String newText) {
                 return false;
             }
         });

    }

    private void OnGps()
    {
        final AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }


    private void getLocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            OnGps();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, MainActivity.this);
    }

    @Override
    public void onLocationChanged(Location location) {
        clat=location.getLatitude();
        clong=location.getLongitude();

        String result = "https://developers.zomato.com/api/v2.1/geocode?lat=" + clat + "&lon=" + clong;
        new ApiTask().execute(result);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    class ApiTask extends AsyncTask<String, String,String>{

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
                JSONArray parentArray = parentObject.getJSONArray( "nearby_restaurants");
                   for(int i=0; i<parentArray.length();i++ )
                   {
                          JSONObject finalObject =parentArray.getJSONObject(i);
                          JSONObject ultraFinal = finalObject.getJSONObject("restaurant");
                          String name= ultraFinal.getString("name");

                          JSONObject promiseLast = ultraFinal.getJSONObject("location");
                          String address=promiseLast.getString("address");
                          String cuisines= ultraFinal.getString("cuisines");

                          mNearbyList.add(new NearbyItem(name,address,cuisines));

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
            if(mNearbyList!=null) {
                mNearbyAdapter = new NearbyAdapter(MainActivity.this, mNearbyList);
                mRecyclerView.setAdapter(mNearbyAdapter);
            }
        }
    }


}

