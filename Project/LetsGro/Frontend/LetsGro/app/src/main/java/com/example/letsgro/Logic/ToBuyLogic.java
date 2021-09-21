package com.example.letsgro.Logic;


import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.letsgro.AppController;
import com.example.letsgro.IView;
import com.example.letsgro.Network.IServerRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A ToBuyLogic class to handle the logic of ToBuy fragment
 *
 * Example: ToBuyLogic tbl = new ToBuyLogic(view, sr);
 * @author boshenglee
 * @version 1.0
 */
public class ToBuyLogic implements IVolleyListener {

    IView v;
    IServerRequest sr;
    int accountID;

    /**
     * Constructor of ToBuyLogic class to set the value of view and server request
     * @param v view
     * @param sr server request
     */
    public ToBuyLogic(IView v,IServerRequest sr) {
        // Required empty public constructor
        this.v = v;
        this.sr = sr;
        accountID = -1;
        sr.addVolleyListener(this);
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    /**
     * A method that compute an jsonObject to send to server
     * @param name naem of the item
     * @param jsonArray the json array of item object
     * @param itemList the array list of the item
     */
    public void addItem(String name,JSONArray jsonArray,ArrayList<String> itemList){

        final String url = "http://10.24.226.91:8080/toBuyItem/account/"+accountID;
        itemList.add(name);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",itemList.size());
            jsonObject.put("item",name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sr.sendToServer(jsonObject,url,jsonArray);
    }

    /**
     * A method that load item from server
     * @param jsonArray the json array of the item object
     * @param itemList the array list of the item
     */
    public void loadItemList(JSONArray jsonArray,ArrayList<String> itemList){

            final String url = "http://10.24.226.91:8080/toBuyItems/account/"+accountID;
            sr.getFromServer(url,jsonArray,itemList);
    }

    /**
     * A method to delete item on the server
     * @param position the position of the itme on the listview
     * @param itemList the array list of the tiem
     * @param jsonArray the jsonarray of the item object
     */
    public void deleteItem(int position,ArrayList<String> itemList,JSONArray jsonArray){

        String url = "http://10.24.226.91:8080/toBuyItem";
        itemList.remove(position);
        JSONObject object = null;
        try {
            object = jsonArray.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String id = object.optString("id");
        url = url + "/"+id;
        jsonArray.remove(position);
        sr.removeFromServer(url);

    }

    /**
     * A method to delete all data on server
     */
    public void deleteAllItem(){

        final String url = "http://10.24.226.91:8080/toBuyItem/account/"+accountID;
        sr.removeFromServer(url);
    }

    /**
     * A method to handle the response when the user receive a response successfully
     * @param response the response from the server
     * @param jsonArray the json array of the item object
     * @param itemList the array list of the item
     */
    @Override
    public void onSuccess(String response,JSONArray jsonArray,ArrayList<String> itemList) {
        List<String> itemList1 = new ArrayList<>();

        while(jsonArray.length()>0)
        {
            jsonArray.remove(0);
        }

        JSONArray item = null;
        try {
            item = new JSONArray(response);
            for(int i=0;i<item.length();i++){
                // Get current json object

                Log.d("ToBuy", response);


                // Get the current student (json object) data
                JSONObject object = item.getJSONObject(i);
                String itemName = object.getString("item");

                jsonArray.put(object);
                itemList1.add(itemName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        itemList.clear();
        itemList.addAll(itemList1);
        v.setData(itemList);
    }

    @Override
    public void onSuccesForAdd(JSONObject response, JSONArray jsonArray) {
        JSONObject obj = response;
        jsonArray.put(obj);
    }
}