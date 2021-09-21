package com.example.letsgro;


import com.example.letsgro.Logic.ReceiptLogic;
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


/**
 * A test class to test the receiptLogic addImage method
 */
@RunWith(MockitoJUnitRunner.class)
public class ReciptLogicSendDataAndImageUnitTest {

    @Mock
    private ServerRequest sr;

    @Mock
    private Receipt view;

    @Test
    public void sendDataAndImageTest() throws JSONException {

        JSONArray jsonArray = new JSONArray();
        ArrayList<String> imageList = new ArrayList<String>();
        String url = "";

        String mockData = "apple 2.99 09/05/2020 walmart";
        String mockImageLoc = "firstImage.jpg";


        ArrayList<String> expectedImageList = new ArrayList<String>();
        expectedImageList.add(mockImageLoc);

        JSONObject expectedReceiptObject = new JSONObject();
        try {
            expectedReceiptObject.put("id", 7);
            expectedReceiptObject.put("imgLocation", "firstImage.jpg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray expectedJsonArray = new JSONArray();
        expectedJsonArray.put(expectedReceiptObject);

        JSONObject mockResponse = new JSONObject();
        mockResponse.put("id", 7);
        mockResponse.put("imgLocation", "firstImage.jpg");

        ReceiptLogic rl = new ReceiptLogic(sr, view);

        doAnswer(invocation -> {
            JSONObject arg0 = invocation.getArgumentAt(0, JSONObject.class);
            String arg1 = invocation.getArgumentAt(1, String.class);
            JSONArray arg2 = invocation.getArgumentAt(2, JSONArray.class);

            rl.onSuccesForAdd(mockResponse, arg2);
            JSONAssert.assertEquals(expectedJsonArray, arg2, true);
            return null;
        }).when(sr).sendToServer(any(JSONObject.class), any(String.class), any(JSONArray.class));
        doNothing().when(sr).sendToSeverWithoutResponse(any(JSONObject.class), any(String.class));
        rl.sendDataAndImage(mockData, mockImageLoc, imageList, jsonArray);


        assertEquals(expectedImageList, imageList);

    }
}
