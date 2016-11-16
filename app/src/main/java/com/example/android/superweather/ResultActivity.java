package com.example.android.superweather;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class ResultActivity extends AppCompatActivity {
    String value;
    String city;
    String state;
    String degree;
    Double lat;
    Double lon;
    String postSum;
    String temperature;
    String icon;
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        HashMap<String,Integer> iconmap = new HashMap<>();
        iconmap.put("clear-day",R.drawable.clear_day);
        iconmap.put("clear-night",R.drawable.clear_night);
        iconmap.put("partly-cloudy-day",R.drawable.cloud_day);
        iconmap.put("partly-cloudy-night",R.drawable.clear_night);
        iconmap.put("cloudy",R.drawable.cloudy);
        iconmap.put("fog",R.drawable.fog);
        iconmap.put("rain",R.drawable.rain);
        iconmap.put("sleet",R.drawable.sleet);
        iconmap.put("snow",R.drawable.snow);
        iconmap.put("wind",R.drawable.wind);

        ImageView generalIcon = (ImageView) findViewById(R.id.g_icon);
        TextView generalSum = (TextView) findViewById(R.id.g_sum);
        TextView generalTemp = (TextView) findViewById(R.id.g_temp);
        TextView generalHighLow = (TextView) findViewById(R.id.g_highlow);
        TextView generalPrep = (TextView) findViewById(R.id.g_prep);
        TextView generalRain = (TextView) findViewById(R.id.g_rain);
        TextView generalWind = (TextView) findViewById(R.id.g_wind);
        TextView generalDew = (TextView) findViewById(R.id.g_dew);
        TextView generalHum = (TextView) findViewById(R.id.g_hum);
        TextView generalVis = (TextView) findViewById(R.id.g_vis);
        TextView generalRise = (TextView) findViewById(R.id.g_rise);
        TextView generalSet = (TextView) findViewById(R.id.g_set);
        final TextView testText = (TextView) findViewById(R.id.test1);
        Button button1 = (Button) findViewById(R.id.detail_bt);
        Button button2 = (Button) findViewById(R.id.map_bt);
        ImageButton button3 = (ImageButton) findViewById(R.id.fb_bt);
        final LoginButton fbLogin = (LoginButton) findViewById(R.id.authButton);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("weather_JSON");
            city = extras.getString("city");
            state = extras.getString("state");
            degree = extras.getString("degree");
            try {
                final JSONObject jsonObj = new JSONObject(value);
                JSONObject current = jsonObj.getJSONObject("currently");
                icon = current.getString("icon");
                generalIcon.setImageResource(iconmap.get(icon));
                postSum = current.getString("summary");
                String summary = current.getString("summary") + " in " + city + ", " + state;
                generalSum.setText(summary);
                String tu;
                String wu;
                String vu;
                if (degree.equals("Fahrenheit")) {
                    tu = "\u2109";
                    wu = " mph";
                    vu = " mi";
                } else {
                    tu = "\u2103";
                    wu = " m/s";
                    vu = " km";
                }
                temperature = String.valueOf(Math.round(current.getDouble("temperature"))) + tu;
                generalTemp.setText(temperature);
                JSONObject daily = jsonObj.getJSONObject("daily").getJSONArray("data").getJSONObject(0);
                String low = String.valueOf(Math.round(daily.getDouble("temperatureMin"))) + "\u00B0";
                String high = String.valueOf(Math.round(daily.getDouble("temperatureMax"))) + "\u00b0";
                String highlow = "L:" + low + "| " + "H:" + high;
                generalHighLow.setText(highlow);
                generalPrep.setText(precipitationConvert(current.getDouble("precipIntensity")));
                String rainchance = String.valueOf(Math.round(current.getDouble("precipProbability") * 100)) + "%";
                generalRain.setText(rainchance);
                String windspeed = String.valueOf(current.getDouble("windSpeed")) + wu;
                generalWind.setText(windspeed);
                String dewpoint = String.valueOf(Math.round(current.getDouble("dewPoint"))) + tu;
                generalDew.setText(dewpoint);
                String humidity = String.valueOf(Math.round(current.getDouble("humidity") * 100)) + "%";
                generalHum.setText(humidity);
                String visibility = String.valueOf(current.getDouble("visibility")) + vu;
                generalVis.setText(visibility);
                String sunRise = daily.getString("sunriseTime");
                String sunSet = daily.getString("sunsetTime");
                String timeZone = jsonObj.getString("timezone");
                generalRise.setText(timeConvert(sunRise,timeZone));
                generalSet.setText(timeConvert(sunSet,timeZone));
                lat = jsonObj.getDouble("latitude");
                lon = jsonObj.getDouble("longitude");

                button1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent1 = new Intent(getBaseContext(), DetailsActivity.class);
                        intent1.putExtra("weather_JSON", value);
                        intent1.putExtra("city", city);
                        intent1.putExtra("state", state);
                        intent1.putExtra("degree", degree);
                        startActivity(intent1);
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent2 = new Intent(getBaseContext(), MapActivity.class);
                        intent2.putExtra("latitude", lat);
                        intent2.putExtra("longitude",lon);
                        startActivity(intent2);
                    }
                });

                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isLoggedIn()){
                            popDialog();;
                        } else {
                            fbLogin.performClick();
                        }
                    }
                });

                fbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    public void onSuccess(LoginResult loginResult) {
                        popDialog();
                    }

                    public void onCancel() {
                        testText.setText("Canceled!");
                    }

                    public void onError(FacebookException e) {
                        testText.setText("Failed!");
                    }
                });

                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

                    public void onSuccess(Sharer.Result loginResult) {
                        Toast.makeText(getApplicationContext(), "Facebook Post Successful",
                                Toast.LENGTH_LONG).show();
                    }

                    public void onCancel() {
                        Toast.makeText(getApplicationContext(), "Post Canceled",
                                Toast.LENGTH_LONG).show();
                    }

                    public void onError(FacebookException e) {
                        Toast.makeText(getApplicationContext(), "Post Failed",
                                Toast.LENGTH_LONG).show();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            testText.setText("Error: No Data.");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Current Weather");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public String precipitationConvert(Double prein) {
        String preinV = "";
        if (prein >= 0 && prein < 0.002) {
            preinV = "None";
        } else if (prein >= 0.002 && prein < 0.017) {
            preinV = "Very Light";
        } else if (prein >= 0.017 && prein < 0.1) {
            preinV = "Light";
        } else if (prein >= 0.1 && prein < 0.4) {
            preinV = "Moderate";
        } else if (prein >= 0.4) {
            preinV = "Heavy";
        }
        return preinV;
    }

    public String timeConvert(String timestring, String timezone) {
        Date date = new Date(Long.parseLong(timestring)*1000);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        timeFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        return timeFormat.format(date);
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public void popDialog(){
        String is;
        if (icon.equals("clear-day")){
            is="clear.png";
        }else if (icon.equals("clear-night")){
            is="clear_night.png";
        }else if (icon.equals("partly-cloudy-day")) {
            is = "cloud_day.png";
        }else if (icon.equals("partly-cloudy-night")) {
            is = "cloud_night.png";
        }else{
            is = icon+".png";
        }
        String imageUrl = "http://cs-server.usc.edu:45678/hw/hw8/images/"+is;
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Current Weather in "+city+", "+state)
                    .setContentDescription(
                            postSum+", "+temperature)
                    .setContentUrl(Uri.parse("http://forecast.io"))
                    .setImageUrl(Uri.parse(imageUrl))
                    .build();
            shareDialog.show(linkContent);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
