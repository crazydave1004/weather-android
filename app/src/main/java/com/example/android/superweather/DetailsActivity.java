package com.example.android.superweather;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class DetailsActivity extends AppCompatActivity {

    public static String value;
    public static String city;
    public static String state;
    public static String degree;
    public static String tu;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("weather_JSON");
            city = extras.getString("city");
            state = extras.getString("state");
            degree = extras.getString("degree");
            tu = degree.equals("Fahrenheit") ? "\u2109" : "\u2103";
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String title = "Details for "+city+", "+state;
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent backIntent = new Intent(getApplicationContext(), ResultActivity.class);
            backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            backIntent.putExtra("weather_JSON",value);
            backIntent.putExtra("city",city);
            backIntent.putExtra("state",state);
            backIntent.putExtra("degree",degree);
            startActivity(backIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            switch(position){
                case 0: return PlaceholderFragment.newInstance(position + 1);
                case 1: return  MyFragment.newInstance();
            }
            return null;
        }

        public int getCount() {
            return 2;
        }

        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "NEXT 24 HOURS";
                case 1:
                    return "NEXT 7 DAYS";
            }
            return null;
        }
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        TextView[] hourArray = new TextView[48];
        TextView[] tempArray = new TextView[48];
        ImageView[] sumArray = new ImageView[48];
        TableRow[] rowArray = new TableRow[24];

        int[] hourArrayId = {R.id.h1,R.id.h2,R.id.h3,R.id.h4,R.id.h5,R.id.h6,R.id.h7,R.id.h8,R.id.h9,R.id.h10,
                R.id.h11,R.id.h12,R.id.h13,R.id.h14,R.id.h15,R.id.h16,R.id.h17,R.id.h18,R.id.h19,R.id.h20,
                R.id.h21,R.id.h22,R.id.h23,R.id.h24,R.id.h25,R.id.h26,R.id.h27,R.id.h28,R.id.h29,R.id.h30,
                R.id.h31,R.id.h32,R.id.h33,R.id.h34,R.id.h35,R.id.h36,R.id.h37,R.id.h38,R.id.h39,R.id.h40,
                R.id.h41,R.id.h42,R.id.h43,R.id.h44,R.id.h45,R.id.h46,R.id.h47,R.id.h48};
        int[] tempArrayId = {R.id.h1t,R.id.h2t,R.id.h3t,R.id.h4t,R.id.h5t,R.id.h6t,R.id.h7t,R.id.h8t,R.id.h9t,R.id.h10t,
                R.id.h11t,R.id.h12t,R.id.h13t,R.id.h14t,R.id.h15t,R.id.h16t,R.id.h17t,R.id.h18t,R.id.h19t,R.id.h20t,
                R.id.h21t,R.id.h22t,R.id.h23t,R.id.h24t,R.id.h25t,R.id.h26t,R.id.h27t,R.id.h28t,R.id.h29t,R.id.h30t,
                R.id.h31t,R.id.h32t,R.id.h33t,R.id.h34t,R.id.h35t,R.id.h36t,R.id.h37t,R.id.h38t,R.id.h39t,R.id.h40t,
                R.id.h41t,R.id.h42t,R.id.h43t,R.id.h44t,R.id.h45t,R.id.h46t,R.id.h47t,R.id.h48t};
        int[] sumArrayId = {R.id.h1i,R.id.h2i,R.id.h3i,R.id.h4i,R.id.h5i,R.id.h6i,R.id.h7i,R.id.h8i,R.id.h9i,R.id.h10i,
                R.id.h11i,R.id.h12i,R.id.h13i,R.id.h14i,R.id.h15i,R.id.h16i,R.id.h17i,R.id.h18i,R.id.h19i,R.id.h20i,
                R.id.h21i,R.id.h22i,R.id.h23i,R.id.h24i,R.id.h25i,R.id.h26i,R.id.h27i,R.id.h28i,R.id.h29i,R.id.h30i,
                R.id.h31i,R.id.h32i,R.id.h33i,R.id.h34i,R.id.h35i,R.id.h36i,R.id.h37i,R.id.h38i,R.id.h39i,R.id.h40i,
                R.id.h41i,R.id.h42i,R.id.h43i,R.id.h44i,R.id.h45i,R.id.h46i,R.id.h47i,R.id.h48i};
        int[] rowArrayId = {R.id.m1,R.id.m2,R.id.m3,R.id.m4,R.id.m5,R.id.m6,R.id.m7,R.id.m8,R.id.m9,R.id.m10,
                R.id.m11,R.id.m12,R.id.m13,R.id.m14,R.id.m15,R.id.m16,R.id.m17,R.id.m18,R.id.m19,R.id.m20,
                R.id.m21,R.id.m22,R.id.m23,R.id.m24};

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_details, container, false);
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
            TextView tempDegree = (TextView) rootView.findViewById(R.id.hDegree);
            tempDegree.setText("Temp("+tu+")");
            Button button1 = (Button) rootView.findViewById(R.id.show_more);

            try {
                JSONObject jsonObj = new JSONObject(value);
                String timeZone = jsonObj.getString("timezone");

                for (int i = 0; i < 48; i++) {
                    hourArray[i] = (TextView) rootView.findViewById(hourArrayId[i]);
                    sumArray[i] = (ImageView) rootView.findViewById(sumArrayId[i]);
                    tempArray[i] = (TextView) rootView.findViewById(tempArrayId[i]);
                    JSONObject hourly = jsonObj.getJSONObject("hourly").getJSONArray("data").getJSONObject(i);
                    String time = hourly.getString("time");
                    hourArray[i].setText(timeConvert(time,timeZone));
                    String icon = hourly.getString("icon");
                    sumArray[i].setImageResource(iconmap.get(icon));
                    String temp = String.valueOf(Math.round(hourly.getDouble("temperature"))) + "\u00B0";
                    tempArray[i].setText(temp);
                }

                button1.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v) {
                        TableRow r0 = (TableRow) rootView.findViewById(R.id.m0);
                        r0.setVisibility(View.GONE);

                        for (int i = 0; i < 24; i++) {
                            rowArray[i] = (TableRow) rootView.findViewById(rowArrayId[i]);
                            rowArray[i].setVisibility(View.VISIBLE);
                        }
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return rootView;
        }
    }
    public static class MyFragment extends Fragment {
        public static MyFragment newInstance() {
            MyFragment fragment = new MyFragment();
            return fragment;
        }
        public MyFragment() {
        }

        TextView[] dayArray = new TextView[7];
        TextView[] hlArray = new TextView[7];
        ImageView[] iconArray = new ImageView[7];

        int[] dayArrayId = {R.id.day1,R.id.day2,R.id.day3,R.id.day4,R.id.day5,R.id.day6,R.id.day7};
        int[] hlArrayId = {R.id.day1hl,R.id.day2hl,R.id.day3hl,R.id.day4hl,R.id.day5hl,R.id.day6hl,R.id.day7hl};
        int[] iconArrayId = {R.id.day1icon,R.id.day2icon,R.id.day3icon,R.id.day4icon,R.id.day5icon,R.id.day6icon,R.id.day7icon};
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.my_fragment, container, false);
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

            try {
                JSONObject jsonObj = new JSONObject(value);
                String timeZone = jsonObj.getString("timezone");
                for (int i = 0; i < 7; i++) {
                    dayArray[i] = (TextView) rootView.findViewById(dayArrayId[i]);
                    iconArray[i] = (ImageView) rootView.findViewById(iconArrayId[i]);
                    hlArray[i] = (TextView) rootView.findViewById(hlArrayId[i]);
                    JSONObject daily = jsonObj.getJSONObject("daily").getJSONArray("data").getJSONObject(i+1);
                    String time = daily.getString("time");
                    dayArray[i].setText(dayConvert(time,timeZone));
                    String icon = daily.getString("icon");
                    iconArray[i].setImageResource(iconmap.get(icon));
                    String low = String.valueOf(Math.round(daily.getDouble("temperatureMin"))) + tu;
                    String high = String.valueOf(Math.round(daily.getDouble("temperatureMax"))) + tu;
                    hlArray[i].setText("Min: "+low+" | Max: "+high);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return rootView;
        }
    }

    private static String dayConvert(String timestring, String timezone) {
        Date date = new Date(Long.parseLong(timestring)*1000);
        SimpleDateFormat timeFormat = new SimpleDateFormat("EEEE, MMM d");
        timeFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        return timeFormat.format(date);
    }

    private static String timeConvert(String timestring, String timezone) {
        Date date = new Date(Long.parseLong(timestring)*1000);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        timeFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        return timeFormat.format(date);
    }
}
