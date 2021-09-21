package com.example.letsgro.Network;

import android.util.Log;
import android.widget.ListAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.letsgro.AppController;
import com.example.letsgro.Logic.IVolleyListener;
import com.example.letsgro.Logic.ToBuyLogic;
import com.example.letsgro.ToBuy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A serverRequest class to handle the request to server
 *
 * Example: ServerRequest sr = new ServerRequest();
 *
 * @author boshenglee
 * @version 1.0
 */
public class ServerRequest implements IServerRequest {

    private String tag_json_obj = "json_obj_req";
    private String tag_string = "string_req";
    private IVolleyListener i;

    /**
     * A method send the object to the server
     * @param obj json object to send to server
     * @param url the url to send the data
     */
    @Override
    public void sendToServer(JSONObject obj, String url,JSONArray jsonArray){


        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("POST",response.toString());
                i.onSuccesForAdd(response,jsonArray);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("POST",error.toString());
            }
        });

        AppController.getInstance().addToRequestQueue(req, tag_json_obj);
    }

    /**
     * A method to receive data from server
     * @param url the url to receive the data
     * @param jsonArray the json array of json object
     * @param list the array list of string
     */
    @Override
    public void getFromServer(String url, final JSONArray jsonArray, final ArrayList<String> list){

        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                i.onSuccess(response,jsonArray,list);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("GET",error.toString());
            }
        });

        AppController.getInstance().addToRequestQueue(req, tag_string);
    }

    /**
     * A method to remove all the data on the server
     * @param url the url to remove data
     */
    @Override
    public void removeFromServer(String url){
        StringRequest req = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DELETE", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("DELETE",error.toString());
            }
        });

        AppController.getInstance().addToRequestQueue(req, tag_string);
    }

    @Override
    public void sendToSeverWithoutResponse(JSONObject obj, String url) {

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("POST",response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("POST",error.toString());
            }
        });

        AppController.getInstance().addToRequestQueue(req, tag_json_obj);

    }

    /**
     * A method to add the vollyListener
     * @param logic the object of IVolleyListener
     */
    @Override
    public void addVolleyListener(IVolleyListener logic) {
        i = logic;
    }

}
