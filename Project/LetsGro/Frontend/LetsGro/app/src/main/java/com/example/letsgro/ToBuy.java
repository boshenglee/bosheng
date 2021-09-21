package com.example.letsgro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.letsgro.Logic.ToBuyLogic;
import com.example.letsgro.Network.ServerRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToBuy#newInstance} factory method to
 * create an instance of this fragment.
 *
 * Example:   ToBuy toBuyFragment = new ToBuy();
 * @author boshenglee
 * @version 1.0
 */
public class ToBuy extends Fragment implements IView {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //my own
    /**
     * A view object to handle view
     */
    private View view;
    /**
     * ListView to display item in list view
     */
    private ListView itemListView;
    /**
     *  Custom adpater to control the item list view
     */
    public CustomAdapter listAdapter;
    /**
     * An array list to store the item
     */
    final List<String> itemList = new ArrayList<>();
    /**
     * JSONArray to store the item object
     */
    JSONArray jsonArray;

    int accountID;




    /**
     * Default constructor of ToBuy class
     */
    public ToBuy() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ToBuy.
     */
    // TODO: Rename and change types and number of parameters
    public static ToBuy newInstance(String param1, String param2) {
        ToBuy fragment = new ToBuy();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        accountID = Integer.parseInt(mParam1);
        new AppController();
        jsonArray = new JSONArray();
        listAdapter = new CustomAdapter(getActivity().getApplicationContext());
        ServerRequest sr = new ServerRequest();
        final ToBuyLogic tbl = new ToBuyLogic(this,sr);
        tbl.setAccountID(accountID);
        tbl.loadItemList(jsonArray, (ArrayList<String>) itemList);

    }

    /**
     * Generate the options menu on the menu bar
     * @param menu the menu
     * @param inflater the menu inflater
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.to_buy_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * When user select the options on menu bar
     * @param item the item user select
     * @return item the item user select
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        ServerRequest sr = new ServerRequest();
        final ToBuyLogic tbl = new ToBuyLogic(this,sr);
        tbl.setAccountID(accountID);
        int id = item.getItemId();

        if( id == R.id.deleteAll){
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle("Remove all item?")
                    .setNegativeButton("Remove All",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            itemList.clear();
                            tbl.deleteAllItem();
                            jsonArray = new JSONArray();
                            listAdapter.setData(itemList);
                        }
                    })
                    .setPositiveButton("Cancel",null)
                    .create();
            dialog.show();
        }

        if(id== R.id.share){

            final EditText userToShare = new EditText(getActivity());
            userToShare.setSingleLine();
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setMessage("Who you want to share you list with?")
                    .setView(userToShare)
                    .setNegativeButton("Share", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String name = userToShare.getText().toString();

                            String send = accountID + " " +name;

                            JSONObject shareObj = new JSONObject();
                            try {
                                shareObj.put("data",send);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            postShareUser(shareObj);

                        }
                    }).setPositiveButton("Cancel",null).create();
            dialog.show();
        }

        if(id == R.id.sharing){
            checkResult();
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkResult(){

        String url = "http://10.24.226.91:8080/roomCheck/";
        url = url + accountID;

        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SharingCheck",response);

                int roomID = -1;
                String sharingAcc = "";

                if(response.equals("False")){
                    AlertDialog dialog = new AlertDialog.Builder(getActivity()).setMessage("You are not sharing with anyone.").setPositiveButton("OK",null).create();
                    dialog.show();
                }
                else{
                    Scanner sc = new Scanner(response);
                    sc.useDelimiter("\n");
                    while(sc.hasNext()) {
                        roomID = Integer.parseInt(sc.next());
                        sharingAcc = sc.next();
                    }

                    Intent sharing = new Intent(getActivity().getApplicationContext(), ShareActivity.class);
                    sharing.putExtra("accID",accountID);
                    sharing.putExtra("roomID",roomID);
                    sharing.putExtra("sharingAcc",sharingAcc);
                    startActivity(sharing);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ShareCheck",error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(req,"Stringreq");

    }

    public void postShareUser(JSONObject obj){

        final String url = "http://10.24.226.91:8080/room";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("POSTcheck",response.toString());

                int roomID = -1;
                String sharingAcc = "";

                try {
                    String[] check = response.getString("Data").split("\\r?\\n");
                    String responseString = response.getString("Data");
                    Scanner sc = new Scanner(responseString);
                    sc.useDelimiter("\n");
                    while(sc.hasNext()) {
                        roomID = Integer.parseInt(sc.next());
                        sharingAcc = sc.next();
                    }
                    if(check.length!=1) {
                        Intent share = new Intent(getActivity().getApplicationContext(), ShareActivity.class);
                        share.putExtra("accID",accountID);
                        share.putExtra("roomID",roomID);
                        share.putExtra("sharingAcc",sharingAcc);
                        startActivity(share);
                    }
                    else{
                        Log.d("Cannot","User not allowed");
                        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setMessage("You cant share with this user").setPositiveButton("OK",null).create();
                        dialog.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("POSTcheck",error.toString());
            }
        });

        AppController.getInstance().addToRequestQueue(req, "JSONobj");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_to_buy, container, false);
        itemListView = (ListView)view.findViewById(R.id.itemListView);
        itemListView.setAdapter(listAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final FloatingActionButton addButton = view.findViewById(R.id.floatingAddButton);
        final RadioButton removeButton = view.findViewById(R.id.done);
        ServerRequest sr = new ServerRequest();
        final ToBuyLogic tbl = new ToBuyLogic(this,sr);
        tbl.setAccountID(accountID);


        //add
        addButton.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick funtion when user click the add button
             * @param v view
             */
            @Override
            public void onClick(View v) {
                final EditText itemInput = new EditText(getActivity());
                itemInput.setSingleLine();
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("New Item")
                        .setView(itemInput)
                        .setNegativeButton("ADD", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = itemInput.getText().toString();
                                tbl.addItem(name,jsonArray,(ArrayList)itemList);
                                listAdapter.setData(itemList);
                            }
                        }).setPositiveButton("Cancel",null).create();
                dialog.show();
            }
        });

        //remove
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            /**
             * On item click funtion when user click the item on the list view
             * @param parent parent of adapter view
             * @param view view
             * @param position position of item in list view
             * @param id id of item in list view
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final RadioButton itemcheck = (RadioButton)view.findViewById(R.id.done);

                if (itemcheck.isChecked()) {
                    itemcheck.setChecked(true);
                } else {
                    itemcheck.setChecked(false);
                }

                itemcheck.setChecked(true);

                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Remove this item?")
                        .setNegativeButton("Delete",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tbl.deleteItem(position,(ArrayList)itemList,jsonArray);
                                listAdapter.setData(itemList);
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

    /**
     * To set the item list view with list adapter
     * @param itemList the array list of itme
     */
    @Override
    public void setData(ArrayList<String> itemList) {
        listAdapter.setData(itemList);

    }
}