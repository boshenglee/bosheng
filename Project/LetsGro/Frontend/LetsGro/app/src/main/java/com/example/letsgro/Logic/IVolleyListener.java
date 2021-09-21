package com.example.letsgro.Logic;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * IVolleyListener interface
 * @author boshenglee
 * @version 1.0
 */
public interface IVolleyListener {

    /**
     * A method to handle response when user successfully receive response from server
     * @param response response receive for the server
     * @param jsonArray jsonarray that store the JSON object
     * @param list array list that store the item or image location
     */
    public void onSuccess(String response, JSONArray jsonArray, ArrayList<String> list);

    public void onSuccesForAdd(JSONObject response, JSONArray jsonArray);
}
