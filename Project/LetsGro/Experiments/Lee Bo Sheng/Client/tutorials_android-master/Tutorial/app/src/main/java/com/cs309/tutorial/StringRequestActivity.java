package com.cs309.tutorial;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class StringRequestActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "HELP";
    private Button btnGet, btnPost1, btnPost2;
    private TextView output;
    RequestQueue Queue;
    String URL, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_string_request);

        //Get username
        username = getIntent().getStringExtra("username");

        // Setup UI
        btnGet = (Button) findViewById(R.id.btnGet);
        btnPost1 = (Button) findViewById(R.id.btnPost1);
        btnPost2 = (Button) findViewById(R.id.btnPost2);
        output = (TextView) findViewById(R.id.output);

        btnGet.setOnClickListener(this);
        btnPost2.setOnClickListener(this);
        btnPost1.setOnClickListener(this);

        //Instantiate request queue to send requests
        Queue = Volley.newRequestQueue(this);

        //Set base url, makes it easier to change urls later
        //Stored my url in strings.xml(values/strings.xml)
        URL = getResources().getString(R.string.local);
    }

    /**
     * Volley String Get Request Example
     * @param url
     */
    public void httpGetTest(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // TODO SERVER RESPONSE HANDLING
                Log.d(TAG, "SERVER RESPONSE: " + response);

                //Print to a textview or do whatever you want with it
                output.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO ERROR HANDLING
                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    Log.d(TAG, responseBody);
                } catch (UnsupportedEncodingException e){
                    Log.d(TAG,e.toString());
                }
            }
        }){
            // THIS METHOD IS NOT CALLED ON A GET REQUEST! ONLY CALLED DURING POST OR PUT!
            // THIS METHOD IS MEANINGLESS
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<>(); // Can also pass in hashmap through method, etc.
                //TODO Add parameters to post request here
                //Example:
                params.put("username", username); //Key-Value pair that is very similar to how PostMan works
                return params;
            }
        };
        Queue.add(stringRequest);
    }

    /**
     * Volley String Post Request with Parameters Example
     * @param url
     */
    public void httpPostTest(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // TODO SERVER RESPONSE HANDLING
                //This code is executed if the server responds, whether or not the response contains data!!
                //The String 'response' contains the server's response.

                Log.d(TAG, "SERVER RESPONSE: " + response);

                //Print to a textview or do whatever you want with it
                output.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO ERROR HANDLING
                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    Log.d(TAG, responseBody);
                } catch (UnsupportedEncodingException e){
                    Log.d(TAG,e.toString());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<>(); // Can also pass in hashmap through method, etc.
                //TODO Add parameters to post request here
                //Example:
                params.put("username", username); //Key-Value pair that is very similar to how PostMan works
                return params;
            }
        };

        Queue.add(stringRequest);
    }

    /**
     * Volley String Post Request with RequestBody Example
     * @param URL
     * @param data
     */
    public void httpPostTest2(String URL, JSONObject data){
        //Convert passed in data into request body
        //JSONObject is VERY similar to hashmap formatting
        final String requestBody = data.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);

                //Print to a textview or do whatever you want with it
                output.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {

                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };

        //Add request to queue
        Queue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGet:

                httpGetTest(String.format("%s/getTest?username=%s", URL, username));
                break;
            case R.id.btnPost1:
                httpPostTest(URL + "/postTest1");
                break;
            case R.id.btnPost2:
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("message", username);
                    httpPostTest2(URL + "/postTest2", jsonBody);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
    }
