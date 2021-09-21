package com.example.letsgro.Logic;

import android.util.Log;
import com.example.letsgro.IView;
import com.example.letsgro.Network.IServerRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *  A ReceiptLogic class to handle the logic of Receipt Fragment
 *
 *  Example: ReceiptLogic rl = new ReceiptLogic(sr,v);
 *
 * @author boshenglee
 * @version 1.0
 */
public class ReceiptLogic implements IVolleyListener{

    IServerRequest sr;
    IView v;
    private int accountID;

    /**
     * A receiptLogic class constructor to set the value of server request and view
     * @param sr server request
     * @param v view
     */
    public ReceiptLogic(IServerRequest sr, IView v) {
        // Required empty public constructor
        this.sr = sr;
        this.v = v;
        accountID = -1;
        sr.addVolleyListener(this);
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    /**
     * A method to handle the logic when user select done to send data and image
     * @param data OCR data to send to the server
     * @param imageLoc image location of the receipt
     * @param imageList the array list of image location
     * @param jsonArray jsonarray storing the image object
     */
    public void sendDataAndImage (String data, String imageLoc, ArrayList<String> imageList, JSONArray jsonArray){

        JSONObject receiptObj = new JSONObject();
        JSONObject dataObj = new JSONObject();
        try {
            if(imageLoc != null && !imageLoc.equals("")) {
                receiptObj.put("id",imageList.size() + 1);
                receiptObj.put("imgLocation", imageLoc);
                imageList.add(imageLoc);
//                jsonArray.put(receiptObj);
            }
            dataObj.put("data",data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlImage = "http://10.24.226.91:8080/receipt/account/"+accountID;
        String urlData = "http://10.24.226.91:8080/receipt/ocrText/account/"+accountID;
        if(imageLoc != null && !imageLoc.equals("")) {

            sr.sendToServer(receiptObj,urlImage, jsonArray);
        }
        sr.sendToSeverWithoutResponse(dataObj,urlData);
    }

    /**
     * A method to remove the image from database
     * @param imageList the array list of image location
     * @param jsonArray the json array of image object
     * @param position the position of the image in the gridview
     */
    public void deleteImage(ArrayList<String> imageList,JSONArray jsonArray, int position){

        imageList.remove(position);

        JSONObject obj = null;
        try {
            obj = jsonArray.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "http://10.24.226.91:8080/receipt/";
        String id = obj.optString("id");
        url = url + id;

        jsonArray.remove(position);

        sr.removeFromServer(url);
    }

    /**
     * A method to load image from database
     * @param imageList the array list of the image location
     * @param jsonArray the jsonarray of the image object
     */
    public void loadImage(ArrayList<String> imageList, JSONArray jsonArray) {

        String url = "http://10.24.226.91:8080/receipts/account/"+accountID;
        sr.getFromServer(url,jsonArray,imageList);
    }

    /**
     * A method to handle the response when user successfully receive response from server
     * @param response the response from the server
     * @param jsonArray the json array of the image object
     * @param imageList the array list of image location
     */
    @Override
    public void onSuccess(String response, JSONArray jsonArray, ArrayList<String> imageList) {
        List<String> imageList1 = new ArrayList<>();

        while(jsonArray.length()>0)
        {
            jsonArray.remove(0);
        }
        JSONArray receipt = null;

        try {
            receipt = new JSONArray(response);
            for(int i=0;i<receipt.length();i++){
                // Get current json object



                Log.d("Receipt", response);


                // Get the current student (json object) data
                JSONObject object = receipt.getJSONObject(i);
                String imgLocation = object.getString("imgLocation");

                jsonArray.put(object);
                imageList1.add(imgLocation);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        imageList.clear();
        imageList.addAll(imageList1);
        v.setData(imageList);
    }

    @Override
    public void onSuccesForAdd(JSONObject response, JSONArray jsonArray) {
        JSONObject obj = response;
        jsonArray.put(obj);
    }
}
