package com.example.letsgro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Yen Wang
 * @version 1.0
 * The activity for the user to create an account
 */
public class Create_Account extends AppCompatActivity {

    /**
     * the button to submit the username, password, and email
     */
    private Button submit;
    /**
     * the username user input
     */
    private EditText Viewusername;
    /**
     * the password user input
     */
    private EditText Viewpassword;
    /**
     * the password user reinput
     */
    private EditText Viewrepass;
    /**
     * the email user input
     */
    private EditText Viewemail;
    /**
     *the request queue use to store the requested item
     */
    private RequestQueue mQueue;
    /**
     * The id of the user
     */
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__account);

        submit = (Button) findViewById(R.id.submit);
        Viewusername = (EditText) findViewById(R.id.Cuserbox);
        Viewpassword = (EditText) findViewById(R.id.Cpassbox);
        Viewrepass = (EditText) findViewById(R.id.CRepassbox);
        Viewemail = (EditText) findViewById(R.id.CEmailbox);

        mQueue = Volley.newRequestQueue(this);
        JSONObject object = new JSONObject();


        //add
        submit.setOnClickListener(new View.OnClickListener() {
            /**
             * the function for the user to submit the data
             * @param view
             */
            @Override
            public void onClick(View view) {
                String username = Viewusername.getText().toString();
                String password = Viewpassword.getText().toString();
                String repass = Viewrepass.getText().toString();
                String email = Viewemail.getText().toString();

                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("user", username);
                    if (password.equals(repass)) {
                        jsonObject.put("password", password);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Create_Account.this);
                        builder.setMessage("Passwords do not match")
                                .setPositiveButton("OK", null)
                                .create();
                        builder.show();
                        return;
                    }
                    jsonObject.put("email", email);
                    postObject(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


    }


    //post is to request and update data from server
    //Post

    /**
     * use to request if the user exist in the database or not
     * @param object
     */
    public void postObject(JSONObject object) {
        String url = "http://10.24.226.91:8080/account";
        JsonObjectRequest postReq = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response postObject", response.toString());
                if (!response.isNull("user")) {
                    try {
                        id = response.getString("accountId");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(Create_Account.this, MainActivity.class);
                    intent.putExtra("accountID", id);
                    startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Create_Account.this);
                    builder.setMessage("Username or email already exists." + "Please enter again")
                            .setPositiveButton("OK", null)
                            .create();
                    builder.show();

                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error", error.toString());
                    }
                }
        );
        mQueue.add(postReq);
    }
}
