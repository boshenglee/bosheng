package com.example.letsgro;

import com.example.letsgro.Logic.ToBuyLogic;
import com.example.letsgro.Network.ServerRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;

/**
 * A test class to test the ToBuyLogic loadItemList method
 */
@RunWith(MockitoJUnitRunner.class)
public class ToBuyLogicLoadItemListUnitTest {

    @Mock
    private ServerRequest sr;

    @Mock
    private ToBuy view;

    @Test
    public void loadItemListTest() throws JSONException {

        JSONArray jsonArray = new JSONArray();
        ArrayList<String> itemList = new ArrayList<String>();
        String url = "";
        String mockResponse = "[{\"id\":1,\"item\":\"Chocolate\"},{\"id\":2,\"item\":\"Egg\"},{\"id\":3,\"item\":\"Potato\"}]";

        final JSONArray expectedjsonArray = new JSONArray(mockResponse);

        ArrayList<String> expectedItemList = new ArrayList<String>();
        expectedItemList.add("Chocolate");
        expectedItemList.add("Egg");
        expectedItemList.add("Potato");


        ToBuyLogic tbl = new ToBuyLogic(view, sr);

        //mokcing getFromServer
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgumentAt(0, String.class);
            JSONArray arg1 = invocation.getArgumentAt(1, JSONArray.class);
            ArrayList<String> arg2 = invocation.getArgumentAt(2, ArrayList.class);

            tbl.onSuccess(mockResponse, arg1, arg2);
            JSONAssert.assertEquals(expectedjsonArray, arg1, true);
            assertEquals(expectedItemList, arg2);
            return null;
        }).when(sr).getFromServer(any(String.class), any(JSONArray.class), any(ArrayList.class));
//        sr.getFromServer(url,jsonArray,itemList);
        tbl.loadItemList(jsonArray, itemList);


    }

    @Test
    public void loadItemListTest2() throws JSONException {

        JSONArray jsonArray = new JSONArray();

        JSONObject obj = new JSONObject();
        obj.put("id", 1);
        obj.put("item", "Egg");

        jsonArray.put(obj);

        ArrayList<String> itemList = new ArrayList<String>();

        itemList.add("Egg");
        String url = "";
        String mockResponse = "[{\"id\":1,\"item\":\"Lamb\"},{\"id\":2,\"item\":\"Chicken\"},{\"id\":3,\"item\":\"Steak\"}]";

        final JSONArray expectedjsonArray = new JSONArray(mockResponse);

        ArrayList<String> expectedItemList = new ArrayList<String>();
        expectedItemList.add("Lamb");
        expectedItemList.add("Chicken");
        expectedItemList.add("Steak");


        ToBuyLogic tbl = new ToBuyLogic(view, sr);

//        doNothing().when(sr.getFromServer(url,jsonArray,itemList)).thenAnswer(x -> {
//           tbl.onSuccess(,jsonArray,itemList);
//        });

        //mokcing getFromServer
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgumentAt(0, String.class);
            JSONArray arg1 = invocation.getArgumentAt(1, JSONArray.class);
            ArrayList<String> arg2 = invocation.getArgumentAt(2, ArrayList.class);

            tbl.onSuccess(mockResponse, arg1, arg2);
            JSONAssert.assertEquals(expectedjsonArray, arg1, true);
            assertEquals(expectedItemList, arg2);
            return null;
        }).when(sr).getFromServer(any(String.class), any(JSONArray.class), any(ArrayList.class));
//        sr.getFromServer(url,jsonArray,itemList);
        tbl.loadItemList(jsonArray, itemList);

    }

}
