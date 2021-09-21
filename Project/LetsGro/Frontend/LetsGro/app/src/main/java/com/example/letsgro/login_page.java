package com.example.letsgro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Yen Wang
 * @version 1.0
 * the activity is where the user login into the app
 */
public class login_page extends AppCompatActivity {

    /**
     * the button to click after inputting the username and password
     */
    private Button button;
    /**
     * the request queue use to store the requested item
     */
    private RequestQueue mQueue;
    /**
     * username that the user input
     */
    private EditText Viewusername;
    /**
     * password that the user input
     */
    private EditText Viewpassword;
    /**
     * id of the user
     */
    private String id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        mQueue = Volley.newRequestQueue(this);

        button = (Button)findViewById(R.id.login);

        Viewusername = (EditText) findViewById(R.id.userbox);
        Viewpassword = (EditText)findViewById(R.id.passbox);

        button.setOnClickListener(new View.OnClickListener() {
            /**
             * the function when the user click on the button
             * @param view
             */
            @Override
            public void onClick(View view) {
                String username = Viewusername.getText().toString();
                String password = Viewpassword.getText().toString();
                JSONObject jsonObject = new JSONObject();

                try{
                    jsonObject.put("user", username);
                    jsonObject.put("password", password);
                    postObject(jsonObject);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    /**
     * use to request if the user exist in the database or not
     * @param object
     */
    public void postObject(JSONObject object){
        String url = "http://10.24.226.91:8080/account/check";
        JsonObjectRequest postReq = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                if (!response.isNull("user")){
                    try{
                        id = response.getString("accountId");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(login_page.this, MainActivity.class);
                    intent.putExtra("accountID", id);
                    startActivity(intent);
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(login_page.this);
                    builder.setMessage("Wrong username or password")
                            .setPositiveButton("OK", null)
                            .create();
                    builder.show();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error nononono", error.toString());
                    }
                }
        );
        mQueue.add(postReq);
    }


    /**
     * open create account when the user click on the button
     * @param view
     */
    public void open_createAccount(View view){
        Intent intent = new Intent(this, Create_Account.class);
        startActivity(intent);
    }




}
