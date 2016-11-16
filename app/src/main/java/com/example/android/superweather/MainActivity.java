package com.example.android.superweather;

import android.content.Context;
import android.content.Intent;
import android.net.*;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.widget.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    private TextView form_val;
    private TextView form_feedback;
    private EditText city_name;
    private Spinner s;
    private RadioButton degree_radio;
    private HashMap<String,String> state_map;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] keys = this.getResources().getStringArray(R.array.states_array);
        String[] values = this.getResources().getStringArray(R.array.states_ab_array);
        state_map = new HashMap<String, String>();
        for (int i = 0; i < Math.min(keys.length, values.length); ++i) {
            state_map.put(keys[i], values[i]);
        }

        final EditText street_address = (EditText) findViewById(R.id.text_1);
        city_name = (EditText) findViewById(R.id.text_2);

        s = (Spinner) findViewById(R.id.state_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.states_array,R.layout.spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        s.setAdapter(adapter);

        final RadioGroup degree_group = (RadioGroup) findViewById(R.id.radio_degree);
        form_feedback = (TextView) findViewById(R.id.feedback_text);
        form_val = (TextView) findViewById(R.id.formv_text);

        Button button = (Button) findViewById(R.id.button_send);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String street = street_address.getText().toString();
                String city = city_name.getText().toString();
                String state = s.getSelectedItem().toString();
                int selectedId = degree_group.getCheckedRadioButtonId();
                degree_radio = (RadioButton) findViewById(selectedId);
                String degree = degree_radio.getText().toString();
                if (formValidator(street, city, state)){
                    String url_street = url_convert(street);
                    String url_city = url_convert(city);
                    String url_state = state_map.get(state);
                    String url_degree = degree.equals("Fahrenheit") ? "us" : "si";
                    String url = "http://superweather.elasticbeanstalk.com/?streetAddress="+url_street+"&cities="+url_city+"&states="+url_state+"&degree="+url_degree;

                    ConnectivityManager connMgr = (ConnectivityManager)
                            getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        new JSONTask().execute(url);
                    } else {
                        form_feedback.setText("No network connection available.");
                    }
                } else{
                    form_feedback.setText("Error in form.");
                }
            }
        });
        Button button2 = (Button) findViewById(R.id.button_reset);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                street_address.setText("");
                city_name.setText("");
                s.setSelection(0);
                degree_group.clearCheck();
                RadioButton fa = (RadioButton) findViewById(R.id.f_radio);
                fa.setChecked(true);
                form_val.setText("");
            }
        });

        ImageView sponsor = (ImageView) findViewById(R.id.sponsor);
        sponsor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent3 = new Intent();
                intent3.setAction(Intent.ACTION_VIEW);
                intent3.addCategory(Intent.CATEGORY_BROWSABLE);
                intent3.setData(Uri.parse("http://forecast.io"));
                startActivity(intent3);
            }
        });

        Button button3= (Button) findViewById(R.id.button_about);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getBaseContext(), AboutActivity.class);
                startActivity(intent2);
            }
        });
    }

    private class JSONTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    Intent intent = new Intent(getBaseContext(), ResultActivity.class);
                    intent.putExtra("weather_JSON",result);
                    intent.putExtra("city",city_name.getText().toString());
                    intent.putExtra("state",state_map.get(s.getSelectedItem().toString()));
                    intent.putExtra("degree",degree_radio.getText().toString());
                    startActivity(intent);
                } catch (Exception e) {
                    form_feedback.setText("Something wrong.");
                    e.printStackTrace();
                }
            } else {
                form_feedback.setText("Couldn't get any data from the url");
            }
        }
    }

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(100000);
            conn.setConnectTimeout(150000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
            String contentAsString = readIt(is);
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null)
        {
            sb.append(line + "\n");
        }
        return sb.toString();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static String url_convert(String str){
        int start = 0;
        String sub = "";
        while(str.indexOf(" ",start)>0){
            int end = str.indexOf(" ",start);
            sub += str.substring(start,end) + "+";
            start = end+1;
        }
        sub += str.substring(start);
        return sub;
    }

    public boolean formValidator(String sa, String ct, String st){
        if (sa.trim().equals("")){
            form_val.setText("Please enter a street address.");
            return false;
        } else if (ct.trim().equals("")){
            form_val.setText("Please enter a city.");
            return false;
        } else if (st.equals("Select...")){
            form_val.setText("Please select a state.");
            return false;
        }
        return true;
    }

}
