package com.example.letsgro;

import android.content.Context;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link History#newInstance} factory method to
 * create an instance of this fragment.
 * @author Yen Wang
 * @version 1.0
 */
public class History extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     *the request queue use to store the requested item
     */
    private RequestQueue mQueue;
    /**
     * a arraylist of JSONonjects that stores the objects
     */
    ArrayList<JSONObject> items = new ArrayList<>();
    /**
     * Arraylist of String that stores the names of the item
     */
    List<String> names = new ArrayList<>();
    /**
     * Arraylist of String that stores the prices of the item
     */
    List<String> prices = new ArrayList<>();
    /**
     * Arraylist of String that stores there the item is bought
     */
    List<String> stores = new ArrayList<>();
    /**
     * Arraylist of String that stores the date when the item is being bought
     */
    List<String> dates = new ArrayList<>();
    /**
     * A view object to handle view
     */
    private View view;
    /**
     * ListView to display item in list view
     */
    private ListView itemListView;
    /**
     * a custom adapter to show the names, prices, stores, and dates of the items
     */
    private CustomAdapter listAdapter;
    /**
     * The id of the user
     */
    private int accountID;

    /**
     * constructor of the history class
     */
    public History() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment History.
     */
    // TODO: Rename and change types and number of parameters
    public static History newInstance(String param1, String param2) {
        History fragment = new History();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        accountID = Integer.parseInt(mParam1);
        mQueue = Volley.newRequestQueue(getActivity());
        getArray();
        listAdapter = new CustomAdapter(getActivity().getApplicationContext(),items);
        listAdapter.setdata(names, prices, stores, dates);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_history, container, false);
        itemListView = (ListView) view.findViewById(R.id.itemListView);
        itemListView.setAdapter(listAdapter);
        return  view;


    }


    /**
     * request and get names, prices, stores, and dates of the items from backend
     */
    public void getArray(){
        String url = "http://10.24.226.91:8080/history/account/"+accountID;

        items.clear();
        names.clear();
        prices.clear();
        stores.clear();
        dates.clear();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Response", response.toString());
                try{
                    for (int i = 0; i < response.length(); i++){
                        JSONObject item_list = response.getJSONObject(i);
                        String name = item_list.getString("item");
                        String price = item_list.getString("price");
                        String store = item_list.getString("store");
                        String date = item_list.getString("groDate");
                        items.add(item_list);
                        names.add(name);
                        prices.add(price);
                        stores.add(store);
                        dates.add(date);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                listAdapter.setdata(names, prices, stores, dates);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error", error.toString());
                    }
                }
        );
        mQueue.add(request);
    }


    /**
     * @author Yen Wang
     * @version 1.0
     * A customAdapter class to control the itemListView in history
     */
    public class CustomAdapter extends BaseAdapter {


        Context context;
        LayoutInflater inflater;
        List<String> names = new ArrayList<>();
        List<String> prices = new ArrayList<>();
        List<String> stores = new ArrayList<>();
        List<String> dates = new ArrayList<>();


        /**
         * CustomAdapter constructor
         * @param applicationContext
         * @param items
         */
        public CustomAdapter(Context applicationContext, ArrayList<JSONObject> items) {
            this.context = applicationContext;
            inflater = (LayoutInflater.from(applicationContext));
        }

        /**
         * A setData function to set itemList in customAdapter
         * @param names
         * @param prices
         * @param stores
         * @param dates
         */
        void setdata(List<String> names, List<String> prices, List<String> stores, List<String> dates){
            this.names.clear();
            this.names.addAll(names);
            this.prices.clear();
            this.prices.addAll(prices);
            this.stores.clear();
            this.stores.addAll(stores);
            this.dates.clear();
            this.dates.addAll(dates);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return names.size();
        }

        @Override
        public Object getItem(int position) {
            return names.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        /**
         * A getView method to custom adapter class
         * @param position
         * @param view
         * @param viewGroup
         * @return view
         */
        @Override
        public View getView(int position, View view, ViewGroup viewGroup){
            
            view = inflater.inflate(R.layout.adapter_view, null);
            TextView item_name = (TextView) view.findViewById(R.id.name);
            TextView item_date = (TextView) view.findViewById(R.id.date);
            TextView item_price = (TextView) view.findViewById(R.id.price);
            TextView item_store = (TextView) view.findViewById(R.id.store);


            item_name.setText(names.get(position));
            item_date.setText(dates.get(position));
            item_price.setText(prices.get(position));
            item_store.setText(stores.get(position));

            return view;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }





}