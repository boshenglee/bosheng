package com.example.letsgro.Network;

import com.example.letsgro.Logic.IVolleyListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * An interface class, IServerRequest
 */
public interface IServerRequest {

    /**
     * A method send the object to the server
     * @param obj json object to send to the server
     * @param url the url to send the data
     * @param jsonArray
     */
    public void sendToServer(JSONObject obj, String url, JSONArray jsonArray);

    /**
     * A method to add the vollyListener
     * @param logic the volley listener object
     */
    public void addVolleyListener(IVolleyListener logic);
    /**
     * A method to receive data from server
     * @param url the url to get the data
     * @param jsonArray the json array of the JSON object
     * @param itemList the array list of string
     */
    public void getFromServer(String url, final JSONArray jsonArray, final ArrayList<String> itemList);
    /**
     * A method to remove all the data on the server
     * @param url the url to remove from server
     */
    public void removeFromServer(String url);

    public void sendToSeverWithoutResponse(JSONObject obj, String url);

}
