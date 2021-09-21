package com.example.firstapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    String[] items;
    String[] prices;
    String[] descriptions;

    public ItemAdapter(Context c, String[] i,String[] p,String[] d){
        items = i;
        prices = p;
        descriptions = d;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int i) {
        return items[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = mInflater.inflate(R.layout.my_list_view_details,null);
        TextView nametextView = (TextView) v.findViewById(R.id.nameTextView);
        TextView priceTextView= (TextView) v.findViewById(R.id.priceTextView);
        TextView desTextView= (TextView) v.findViewById(R.id.desTextView);

        String name = items[i];
        String des = descriptions[i];
        String price = prices[i];

        nametextView.setText(name);
        desTextView.setText(des);
        priceTextView.setText(price);

        return v;
    }
}
