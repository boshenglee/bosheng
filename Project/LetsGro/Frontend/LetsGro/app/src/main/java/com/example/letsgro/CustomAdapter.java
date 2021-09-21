package com.example.letsgro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A customAdapter class to control the itemListView in ToBuy fragment
 *
 * Example: CustomAdapter listAdapter = new CustomAdapter(getActivity().getApplicationContext(),itemList);
 *
 * @author boshenglee
 * @version 1.0
 */
public class CustomAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflter;
    List<String> itemList = new ArrayList<>();

    /**
     * CustomAdapter constructor to set the value of context and itemList
     * @param applicationContext the context of the application
     */
    public CustomAdapter(Context applicationContext) {
        this.context = applicationContext;
        inflter = (LayoutInflater.from(applicationContext));
    }

    /**
     * A setData funtion to set itemList in customAdapter
     * @param itemList the array list of item
     */
    public void setData(List<String> itemList){
        this.itemList.clear();
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * A getView method to generate the view for the element in listview
     * @param position position of item in list view
     * @param view
     * @param viewGroup
     * @return view
     */
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.item, null);
        TextView itemTextView = (TextView) view.findViewById(R.id.item);
        itemTextView.setText("  "+ itemList.get(position));
        return view;
    }
}
