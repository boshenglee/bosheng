package com.example.letsgro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToBuy#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToBuy extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;
    ListView listView;
    final List<String> itemList = new ArrayList<>();

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
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_to_buy, container, false);
        listView = (ListView)view.findViewById(R.id.listView);
        final CustomAdapter customAdapter = new CustomAdapter(getActivity().getApplicationContext());
        customAdapter.setData(itemList);
        listView.setAdapter(customAdapter);

        final FloatingActionButton addButton = view.findViewById(R.id.floatingAddButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText itemInput = new EditText(getActivity());
                itemInput.setSingleLine();
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("New Item")
                        .setMessage("What is the item?")
                        .setView(itemInput)
                        .setNegativeButton("ADD", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                itemList.add(itemInput.getText().toString());
                                customAdapter.setData(itemList);
                            }
                        }).setPositiveButton("Cancel",null).create();
                dialog.show();
            }
        });
        return view;
    }

    class CustomAdapter extends BaseAdapter {

        Context context;
        List<String> itemList = new ArrayList<>();
        LayoutInflater inflter;

        public CustomAdapter(Context applicationContext) {
            this.context = context;
            inflter = (LayoutInflater.from(applicationContext));
        }

        void setData(List<String> itemList){
            this.itemList.clear();
            this.itemList.addAll(itemList);
            notifyDataSetChanged();

        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.item, null);
            TextView itemTextView = (TextView) view.findViewById(R.id.item);
            itemTextView.setText(itemList.get(i));
            RadioButton done = (RadioButton)view.findViewById(R.id.done);
            return view;
        }
    }


}