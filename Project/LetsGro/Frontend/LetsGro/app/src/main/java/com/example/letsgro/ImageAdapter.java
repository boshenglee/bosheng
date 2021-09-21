package com.example.letsgro;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * This is an ImageAdapter class which is used to control grid view in Receipt fragment
 *
 * Example: ImageAdapter imageAdapter = new ImageAdapter(getActivity().getApplicationContext());
 *
 * @author boshenglee
 * @version 1.0
 */
class ImageAdapter extends BaseAdapter {

    private Context context;

    LayoutInflater inflter;
    List<String> imageList = new ArrayList<>();

    /**
     * Constructor of ImageAdapter class to set the value of contect and imageList
     * @param applicationContext
     */
    public ImageAdapter(Context applicationContext) {
        this.context = applicationContext;
        inflter = (LayoutInflater.from(applicationContext));
    }

    /**
     * A method used to set the data of image list
     * @param imageList the array list of image
     */
    void setData(List<String> imageList){
        this.imageList.clear();
        this.imageList.addAll(imageList);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return imageList.size();
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
     * A getView method of image adapter class to generate view of the element in gridView,
     * @param position the position of the image in gridView
     * @param view
     * @param parent
     * @return view
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflter.inflate(R.layout.receipt, null);
        ImageView receiptImageView = (ImageView) view.findViewById(R.id.receipt);
        try {
            if(imageList.size()!=0)
                receiptImageView.setImageURI(Uri.parse(imageList.get(position)));
        }catch(Error e){
            Log.e("receipt",e.toString());
        }
        return view;
    }
}
