package com.example.letsgro;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Analysis#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Analysis extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private TextView spent;
    private TextView store;
    private TextView item;
    private RequestQueue mqueue;
    private BarChart barChart;
    private Integer accountID;
    private String startDate;
    private String endDate;
    ArrayList<String> dates = new ArrayList<>();
    ArrayList<BarEntry> barEntries = new ArrayList<>();
    ArrayList<String> spends = new ArrayList<>();



    public Analysis() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Analysis.
     */
    // TODO: Rename and change types and number of parameters
    public static Analysis newInstance(String param1, String param2) {
        Analysis fragment = new Analysis();
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
        mqueue = Volley.newRequestQueue(getContext());
        getanalysis();

//        createBarGraph("2020/11/01", "2020/11/05");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_analysis, container, false);
        spent = (TextView) view.findViewById(R.id.total_spent);
        store = (TextView) view.findViewById(R.id.most_visit_store);
        item = (TextView) view.findViewById(R.id.highest_item);
        barChart = (BarChart) view.findViewById(R.id.graph);
        createBarGraph("2020/11/01", "2020/11/05");



        return view;
    }

    public void getanalysis(){
        String url = "http://10.24.226.91:8080/analysis/account/" + accountID;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.replace(",", " $$$$%%% ");
                Scanner sc = new Scanner(response);
                String first = sc.next();
                String second = "";
                String third = "";
                String temp = sc.next();
                temp = sc.next();
                while (!temp.equals("$$$$%%%")){
                    second += temp + " ";
                    temp = sc.next();
                }
                while (sc.hasNext()){
                    third = sc.next();
                }
                spent.setText(first);
                store.setText(second);
                item.setText(third);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
            }
        });
        mqueue.add(request);
    }

    public void createBarGraph(String Date1, String Date2){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try{
            Date date1 = simpleDateFormat.parse(Date1);
            Date date2 = simpleDateFormat.parse(Date2);

            Calendar mDate1 = Calendar.getInstance();
            Calendar mDate2 = Calendar.getInstance();
            mDate1.clear();
            mDate2.clear();

            mDate1.setTime(date1);
            mDate2.setTime(date2);

            dates = new ArrayList<>();
            dates = getList(mDate1, mDate2);

            barEntries = new ArrayList<>();

            barEntries.add(new BarEntry(150f, 0));
            barEntries.add(new BarEntry(122f, 1));
            barEntries.add(new BarEntry(25f, 2));
            barEntries.add(new BarEntry(300f, 3));
            barEntries.add(new BarEntry(49f, 4));





        } catch (ParseException e) {
            e.printStackTrace();
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");
        barDataSet.setColor(Color.rgb(196, 132, 132));
        BarData barData = new BarData(dates, barDataSet);
        barChart.setData(barData);
        barChart.setDescription("total spent of the user by date");
        barChart.invalidate();
    }

    public ArrayList<String> getList(Calendar startDate, Calendar endDate){
        ArrayList<String> list = new ArrayList<>();
        while (startDate.compareTo(endDate) <= 0){
            list.add(getDate(startDate));
            startDate.add(Calendar.DAY_OF_MONTH, 1);
        }
        return list;
    }

    public String getDate(Calendar cld){
        String curDate = cld.get(Calendar.YEAR) + "/" + (cld.get(Calendar.MONTH) + 1) + "/" + cld.get(Calendar.DAY_OF_MONTH);
        try{
            Date dates = new SimpleDateFormat("yyyy/MM/dd").parse(curDate);
            curDate = new SimpleDateFormat("yyyy/MM/dd").format(dates);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curDate;
    }



    //get data for the graph
//    public void graphData(){
//        String url = "http://10.24.226.91:8080/analysisByDate/account/" + accountID;
//        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Scanner sc = new Scanner(response);
//                String next = sc.next();
//                while (sc.hasNext()){
//                    if (next.contains("/")){
//                        dates.add(next);
//                    }
//                    else{
//                        spends.add(next);
//                    }
//                    next += sc.next() + " ";
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("error", error.toString());
//            }
//        });
//        mqueue.add(request);
//    }
}
