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
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;


/**
 * A test class to test the ToBuyLogic addItem method
 */
@RunWith(MockitoJUnitRunner.class)
public class ToBuyLogicAddItemUnitTest {

    @Mock
    private ServerRequest sr;

    @Mock
    private ToBuy view;

    @Test
    public void addItemTest() throws JSONException {

        JSONArray jsonArray = new JSONArray();
        ArrayList<String> itemList = new ArrayList<String>();
        String url = "";

        String mockName = "Potato";

        ArrayList<String> expectedItemList = new ArrayList<String>();
        expectedItemList.add("Potato");

        JSONObject expectedItemObject = new JSONObject();
        try {
            expectedItemObject.put("item", "Potato");
            expectedItemObject.put("id", 4);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray expectedJsonArray = new JSONArray();
        expectedJsonArray.put(expectedItemObject);


        JSONObject mockResponse = new JSONObject();
        mockResponse.put("item", "Potato");
        mockResponse.put("id", 4);

        ToBuyLogic tbl = new ToBuyLogic(view, sr);


        doAnswer(invocation -> {
            JSONObject arg0 = invocation.getArgumentAt(0, JSONObject.class);
            String arg1 = invocation.getArgumentAt(1, String.class);
            JSONArray arg2 = invocation.getArgumentAt(2, JSONArray.class);

            tbl.onSuccesForAdd(mockResponse, arg2);
            JSONAssert.assertEquals(expectedJsonArray, arg2, true);
            return null;
        }).when(sr).sendToServer(any(JSONObject.class), any(String.class), any(JSONArray.class));
        tbl.addItem(mockName, jsonArray, itemList);


        assertEquals(expectedItemList, itemList);

    }
}
