package com.moinuddinmulla100gmail.weatherforcasting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class city extends AppCompatActivity {
    Button btncity;
    EditText txtcity;
    TextView detailscity;
    private final String url="http://api.openweathermap.org/data/2.5/weather?q=";
    private final String api="6a097eb93beb52fe3fdb06d72e872149";
    DecimalFormat df=new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        btncity=findViewById(R.id.btncity);
        txtcity=findViewById(R.id.txtcity);
        detailscity=findViewById(R.id.detailscity);
        btncity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getweathergetails();
            }
        });
    }

    public void getweathergetails() {
        String tempurl="";
        String city=txtcity.getText().toString().trim();
        if(city.equals("")){
            Toast.makeText(getApplicationContext(),"Enter city name ,it is empty",Toast.LENGTH_SHORT).show();
        }
        else{
            tempurl=url + city + "&appid="+api;
            StringRequest stringrequest=new StringRequest(Request.Method.POST, tempurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //Log.d("response",response);
                    String output="";
                    try {
                        JSONObject jsonresponse=new JSONObject(response);
                        JSONArray jsonarr=jsonresponse.getJSONArray("weather");
                        JSONObject jsonweather=jsonarr.getJSONObject(0);
                        String description=jsonweather.getString("description");
                        JSONObject jsonmain=jsonresponse.getJSONObject("main");
                        double temp=jsonmain.getDouble("temp")-273.15;
                        double feels=jsonmain.getDouble("feels_like")-273.15;
                        int press=jsonmain.getInt("pressure");
                        int humidity=jsonmain.getInt("humidity");
                        JSONObject jsonwind=jsonresponse.getJSONObject("wind");
                        double speed=jsonwind.getDouble("speed");
                        JSONObject sys=jsonresponse.getJSONObject("sys");
                        String country=sys.getString("country");
                        String region=jsonresponse.getString("name");
                        JSONObject cloudj=jsonresponse.getJSONObject("clouds");
                        int cloud=cloudj.getInt("all");

                        output="Current weather of "+region+"("+country+")\n"+
                                "Tempreature: "+df.format(temp)+"°C"+
                                "\nFeels like: "+df.format(feels)+"°C"+
                                "\nDescription: "+description+
                                "\nPressure: "+press+"hPa"+
                                "\nHumidity: "+humidity+"%"+
                                "\nSpeed of wind: "+speed+" m/s"+
                                "\nCloudiness: "+cloud+"%";
                        detailscity.setText(output);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestqueue= Volley.newRequestQueue(getApplicationContext());
            requestqueue.add(stringrequest);
        }
    }
}