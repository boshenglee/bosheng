package com.example.letsgro;

import com.example.letsgro.Logic.ReceiptLogic;
import com.example.letsgro.Logic.ToBuyLogic;
import com.example.letsgro.Network.ServerRequest;

import org.json.JSONArray;
import org.json.JSONException;
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
 * A test class to test the receiptLogic loadImage method
 */
@RunWith(MockitoJUnitRunner.class)
public class ReceiptLogicLoadImageUnitTest {


    @Mock
    private ServerRequest sr;

    @Mock
    private Receipt view;


    @Test
    public void loadImageTest() throws JSONException {

        JSONArray jsonArray = new JSONArray();
        ArrayList<String> imageList = new ArrayList<String>();
        String url = "";
        String mockResponse = "[{\"id\":1,\"imgLocation\":\"firstImage.jpg\"},{\"id\":2,\"imgLocation\":\"secondImage.jpg\"}]";

        final JSONArray expectedjsonArray = new JSONArray(mockResponse);

        ArrayList<String> expectedImageList = new ArrayList<String>();
        expectedImageList.add("firstImage.jpg");
        expectedImageList.add("secondImage.jpg");

        ReceiptLogic rl = new ReceiptLogic(sr, view);

        //mokcing getFromServer
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgumentAt(0, String.class);
            JSONArray arg1 = invocation.getArgumentAt(1, JSONArray.class);
            ArrayList<String> arg2 = invocation.getArgumentAt(2, ArrayList.class);

            rl.onSuccess(mockResponse, arg1, arg2);
            JSONAssert.assertEquals(expectedjsonArray, arg1, true);
            assertEquals(expectedImageList, arg2);
            return null;
        }).when(sr).getFromServer(any(String.class), any(JSONArray.class), any(ArrayList.class));
        rl.loadImage(imageList, jsonArray);


    }
}
