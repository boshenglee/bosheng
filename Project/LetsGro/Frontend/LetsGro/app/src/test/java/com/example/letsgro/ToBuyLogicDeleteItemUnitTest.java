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
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;

/**
 * A test class to test the ToBuyLogic deleteItem method
 */
@RunWith(MockitoJUnitRunner.class)
public class ToBuyLogicDeleteItemUnitTest {

    @Mock
    private ServerRequest sr;

    @Mock
    private ToBuy view;

    @Test
    public void deleteItemTest() throws JSONException {

        JSONArray jsonArray = new JSONArray();
        ArrayList<String> itemList = new ArrayList<String>();
        String url = "";

        int mockPosition = 1;
        ArrayList<String> mockItemList = new ArrayList<String>();
        mockItemList.add("Chocolate");
        mockItemList.add("Biscuit");
        mockItemList.add("Popcorn");

        JSONArray mockJsonArray = new JSONArray();
        JSONObject mockObject1 = new JSONObject();
        mockObject1.put("item", "Chocolate");
        mockObject1.put("id", 1);
        JSONObject mockObject2 = new JSONObject();
        mockObject1.put("item", "Biscuit");
        mockObject1.put("id", 2);
        JSONObject mockObject3 = new JSONObject();
        mockObject1.put("item", "Popcorn");
        mockObject1.put("id", 3);
        mockJsonArray.put(mockObject1);
        mockJsonArray.put(mockObject2);
        mockJsonArray.put(mockObject3);

        ArrayList<String> expectedItemList = new ArrayList<String>();
        expectedItemList.add("Chocolate");
        expectedItemList.add("Popcorn");


        JSONObject expectedItemObject1 = new JSONObject();
        JSONObject expectedItemObject2 = new JSONObject();
        JSONObject expectedItemObject3 = new JSONObject();

        try {
            expectedItemObject1.put("item", "Chocolate");
            expectedItemObject1.put("id", 1);

            expectedItemObject1.put("item", "Popcorn");
            expectedItemObject1.put("id", 3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray expectedJsonArray = new JSONArray();
        expectedJsonArray.put(expectedItemObject1);
        expectedJsonArray.put(expectedItemObject2);

        ToBuyLogic tbl = new ToBuyLogic(view, sr);

        //mokcing getFromServer
        doNothing().when(sr).removeFromServer(isA(String.class));
        tbl.deleteItem(mockPosition, mockItemList, mockJsonArray);

        assertEquals(expectedItemList, mockItemList);
        JSONAssert.assertEquals(expectedJsonArray, mockJsonArray, true);
        ;

    }
}