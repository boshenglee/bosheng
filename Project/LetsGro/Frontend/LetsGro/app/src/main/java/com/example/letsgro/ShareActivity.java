package com.example.letsgro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ShareActivity extends AppCompatActivity {

    private WebSocketClient mWebSocketClient;

    ArrayList<String> shareItemList;

    TextView status;
    ListView shareItemListView;
    TextAdapter shareItemAdapter;
    JSONArray jsonArray;

    int accountID;
    int roomID;
    String sharingAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> Sharing </font>"));

        shareItemList = new ArrayList<String>();
        jsonArray = new JSONArray();

        shareItemListView = (ListView)findViewById(R.id.shareItemListView);
        status = (TextView)findViewById(R.id.showStatus);
        shareItemAdapter = new TextAdapter(this);
        final FloatingActionButton shareAddButton= (FloatingActionButton)findViewById(R.id.shareFloatingAddButton);

        Intent i = getIntent();
        accountID = i.getExtras().getInt("accID");
        roomID = i.getExtras().getInt("roomID");
        sharingAcc = i.getExtras().getString("sharingAcc");

        status.setText("\n Sharing with "+sharingAcc);

        connectWebSocket();

        shareItemAdapter.setData(shareItemList);
        shareItemListView.setAdapter(shareItemAdapter);



        //onClick method
        shareAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText itemInput = new EditText(ShareActivity.this);
                itemInput.setSingleLine();
                AlertDialog dialog = new AlertDialog.Builder(ShareActivity.this)
                        .setTitle("New Item")
                        .setView(itemInput)
                        .setNegativeButton("ADD", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String itemName = itemInput.getText().toString();

                                if(itemName != null && itemName.length() > 0){

                                    //need to modify
                                    mWebSocketClient.send(itemName);
                                }


                            }
                        }).setPositiveButton("Cancel",null).create();
                dialog.show();
            }
        });

        shareItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            /**
             * On item click funtion when user click the item on the list view
             * @param parent parent of adapter view
             * @param view view
             * @param position position of item in list view
             * @param id id of item in list view
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final RadioButton itemcheck = (RadioButton)view.findViewById(R.id.shareDone);

                if (itemcheck.isChecked()) {
                    itemcheck.setChecked(true);
                } else {
                    itemcheck.setChecked(false);
                }

                itemcheck.setChecked(true);

                AlertDialog dialog = new AlertDialog.Builder(ShareActivity.this)
                        .setTitle("Remove this item?")
                        .setNegativeButton("Delete",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteShareItem(position);
                            }
                        })
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                itemcheck.setChecked(false);
                            }
                        })
                        .create();
                dialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.share_activity_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.deletSharing){
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Delete this sharing item list?")
                    .setNegativeButton("Yes",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("Delete room", String.valueOf(roomID));
                            deleteRoom(roomID);
                            finish();
                        }
                    })
                    .setPositiveButton("Cancel",null)
                    .create();
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebSocketClient.close();
    }

    public void deleteRoom(int roomID){

        String url = "http://10.24.226.91:8080/deleteRoom/"+roomID;

        StringRequest req = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("delete room", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("delete room",error.toString());
            }
        });

        AppController.getInstance().addToRequestQueue(req, "string_req");
    }


    public void deleteShareItem(int position){

        int tempID = -1;
        try {
            tempID = jsonArray.getJSONObject(position).getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(tempID !=-1) {
            mWebSocketClient.send("D " + tempID);
            Log.d("DWebSocket delete","D "+tempID);
        }
    }

    private void connectWebSocket() {
        URI uri;
        try {
            /*
             * To test the clientside without the backend, simply connect to an echo server such as:
             *  "ws://echo.websocket.org"
             */
            String url = "ws://10.24.226.91:8080/room/"+accountID;
            uri = new URI(url);
//             uri = new URI("ws://echo.websocket.org");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {

            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
            }

            @Override
            public void onMessage(String msg) {
                Log.i("WebsocketReceive", msg);

                String temp1[] = msg.split(" ");
                if(temp1.length==2) {
                    int location = -1;
                    if (temp1[0].equals("D")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                if (jsonArray.getJSONObject(i).getString("id").equals(temp1[1])) {
                                    location = i;
                                    break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (location != -1) {
                            Log.d("Delete from list", shareItemList.get(location));
                            shareItemList.remove(location);
                            jsonArray.remove(location);
                        }
                    }
                }


                else {

                    Scanner s = new Scanner(msg);
                    s.useDelimiter("\n");
                    while (s.hasNext()) {
                        String[] temp = s.next().split(":");
                        int id = Integer.parseInt(temp[1].trim());
                        temp = s.next().split(":");
                        String item = temp[1].trim();

                        shareItemList.add(item);
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("id", id);
                            obj.put("item", item);
                            jsonArray.put(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shareItemAdapter.setData(shareItemList);
                    }
                });


            }

            @Override
            public void onClose(int errorCode, String reason, boolean remote) {
                Log.i("Websocket", "Closed " + reason);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }


        };
        mWebSocketClient.connect();
    }


}

class TextAdapter extends BaseAdapter{

    ArrayList<String> shareItemList;
    Context context;
    LayoutInflater layoutInflater;

    public TextAdapter(Context context) {
        shareItemList = new ArrayList<String>();
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<String> itemList){
        this.shareItemList.clear();
        this.shareItemList.addAll(itemList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return shareItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= layoutInflater.inflate(R.layout.shareitem, null);
        TextView itemTextView = (TextView) convertView.findViewById(R.id.shareItem);
        itemTextView.setText("  "+ shareItemList.get(position));

        return convertView;
    }
}