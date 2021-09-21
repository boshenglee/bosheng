package com.letgro.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.letgro.backend.BackendApplication;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BackendApplication.class)
@RunWith(SpringRunner.class)
public class TestingSystemTest {

    String ocrText1 = "{\"data\":\"Give us feedback@survery.walmart.com\\nThank you! ID #: 7P9TCP1H2SXR\\nWalmart\\n515-956-3536 Mgr: JONATHAN\\n534 S DUFF AVE\\nAMES IA 50010\\nST# 04256 OP# 004927 TE# 04 TR# O5503\\nCANTALOUPE 000000004050KF\\nAT\\n1 FOR\\n1.93\\n3.86\\nN\\nGV WHT VNGR\\n007874235255 F\\n2.64 0\\n80 CHK GB\\n007874226969 F\\n14.86 0\\nWHOLE MILK\\n007874235186 F\\n2.34 0\\nEGGS 18CT\\n007874212708 F\\n1.57 0\\nDRUMSTICK\\n007562055872 F\\n4.86 0\\nSUBTOTAL\\n30.13\\nTOTAL\\n30.13\\nDISCV TEND\\n30.13\\nDiscover Credit\\n*** ** ********| 1\\nAPPROVAL # 00521R\\nREF # 025000346657\\nAID A000001523010\\nAAC EA2942C1C07FFF16\\nTERMINAL # SCO10388\\n09\\/05\\/20\\n19:19:46\\nCHANGE DUE\\n0.00\\n# ITEMS SOLD\\nTCM 6568 8942 8290 9627 4498\\nLow Prices You Can Trust. Every Day.\\n09\\/05\\/20\\n19:19:46\\n***CUSTOMER COPY***\\n\"}";


    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void WalmartTest() {
        Response response = RestAssured.given().
                header("Content-Type", "application/json").
                header("charset", "utf-8").
                body(ocrText1).
                when().
                post("/receipt/ocrText/account/1");

        assertEquals(200, response.getStatusCode());

        String returnString = response.getBody().asString();
        String expected = "[CANTALOUPE, GV WHT VNGR, 80 CHK GB, WHOLE MILK, EGGS 18CT, DRUMSTICK], [3.86, 2.64, 14.86, 2.34, 1.57, 4.86], 09/05/20, 534 S DUFF AVE AMES IA 50010";
        assertEquals(expected, returnString);
    }
}

