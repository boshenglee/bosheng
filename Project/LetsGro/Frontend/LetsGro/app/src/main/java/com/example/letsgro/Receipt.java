package com.example.letsgro;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.letsgro.Logic.ReceiptLogic;
import com.example.letsgro.Network.ServerRequest;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Receipt#newInstance} factory method to
 * create an instance of this fragment.
 *
 * Example:  Receipt receiptFragment = new Receipt();
 *
 * @author boshenglee
 * @version 1.0
 */
public class Receipt extends Fragment implements IView {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    /**
     * To show the reult of OCR
     */
    private EditText mResult;
    /**
     * To show the image added
     */
    private ImageView mPreview;
    /**
     * A view object
     */
    private View view;
    /**
     * Button when user decide to add data
     */
    private Button done;
    /**
     * Grid view to displayy receipt
     */
    private GridView imageGrid;
    /**
     * Image adapter to control the grid view
     */
    private ImageAdapter imageAdapter;
    /**
     * An array list to store the image location
     */
    final List<String> imageList = new ArrayList<>();
    /**
     * Uri object
     */
    private Uri image_uri;
    /**
     * To store the path of a picture in the phone
     */
    private String picturePath;
    /**
     * Jsonarray to store the imagejson object
     */
    private JSONArray jsonArray;

    //my own
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;

    /**
     * String array for camera permission
     */
    String cameraPermission[];
    /**
     * String array for storage permission
     */
    String storagePermission[];





    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int accountID;

    //Default constrcutor of receipt fragment.
    public Receipt() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Receipt.
     */
    // TODO: Rename and change types and number of parameters
    public static Receipt newInstance(String param1, String param2) {
        Receipt fragment = new Receipt();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        accountID = Integer.parseInt(mParam1);
        ServerRequest sr = new ServerRequest();
        ReceiptLogic rl = new ReceiptLogic(sr,this);
        rl.setAccountID(accountID);
        imageAdapter = new ImageAdapter(getActivity().getApplicationContext());

        if(!checkStoragePermission()){
            //camera permission not allowed, request it
            requestStoragePermission();
        }

        jsonArray = new JSONArray();
        rl.loadImage((ArrayList<String>) imageList,jsonArray);
    }

    /**
     * Generate the options menu of the menu bar
     * @param menu the menu object
     * @param inflater menu inflater
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_image,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * When user select the add image icon on menu bar
     * @param item the item on the menu bar
     * @return onOptionsItemSelected(item)
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if( id ==R.id.addImage){
            showImageImportDialog();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_receipt, container, false);

        mResult = (EditText)view.findViewById(R.id.result);
        mPreview = (ImageView)view.findViewById(R.id.imageView);
        done = (Button)view.findViewById(R.id.done);

        cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        imageGrid = (GridView)view.findViewById(R.id.imageGrid);
        imageGrid.setAdapter(imageAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ServerRequest sr = new ServerRequest();
        ReceiptLogic rl = new ReceiptLogic(sr,this);
        rl.setAccountID(accountID);

        done.setOnClickListener(new View.OnClickListener() {
            /**
             * Onclick funtion when user click on the done button to send data and save receipt
             * @param v view
             */
            @Override
            public void onClick(View v) {
                String receiptInfo = String.valueOf(mResult.getText());
                if(receiptInfo.equals("")) {
                    Toast.makeText(getContext(), "Please insert Info", Toast.LENGTH_SHORT).show();
                }
                else {

//                    ReceiptLogic rl = new ReceiptLogic();
                    if(mPreview.getTag()==null){
                        mPreview.setTag("");
                    }
                    rl.sendDataAndImage(receiptInfo,mPreview.getTag().toString(), (ArrayList<String>) imageList,jsonArray);
                    imageAdapter.setData(imageList);
                    mResult.setText("");
                    mPreview.setImageURI(null);
                }

            }
        });

        imageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * OnItemClick funtion when user slick on the image of grid to view full image
             * @param parent parent of the adapter view
             * @param view view
             * @param position postion of the image on the gridview
             * @param id the id of the image
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity().getApplicationContext(),FullScreenActivity.class);
                i.putExtra("id",position);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable)imageList);
                i.putExtra("imageList",args);
                startActivity(i);
            }
        });

        imageGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            /**
             * onItemLongClickListener when user long click on the button to delete the receipt in grid view
             * @param parent parent of the adpater view
             * @param view view
             * @param position the position of image on the grid view
             * @param id the id of the image
             * @return true if funtion done
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Remove this image?")
                        .setNegativeButton("Delete",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                rl.deleteImage((ArrayList<String>)imageList,jsonArray,position);
                                for(int index = position; index<jsonArray.length();index++){
                                    JSONObject object = new JSONObject();
                                    try {
                                        object.put("id",Integer.parseInt(jsonArray.getJSONObject(position).getString("id"))-1);
                                        object.put("imgLocation",jsonArray.getJSONObject(position).get("imgLocation"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    jsonArray.put(object);
                                    jsonArray.remove(position);
                                }
                                imageAdapter.setData(imageList);

                            }
                        })
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create();
                dialog.show();
                return true;
            }
        });
    }

    /**
     * Show the dialog to ask for permission
     */
    private void showImageImportDialog(){
        String[] items = {"Camera","Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Select Image");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){ //camera option selected
                    if(!checkCameraPermission()){
                        //camera permission not allowed, request it
                        requestCameraPermission();
                    }
                    else{
                        //permission allowed
                        pickCamera();
                    }
                }
                if(which ==1){ //gallery option selected

                    if(!checkStoragePermission()){
                        //camera permission not allowed, request it
                        requestStoragePermission();
                    }else{
                        pickGallery();
                    }
                }
            }

        });
        dialog.create().show();
    }

    /**
     * Method used when user choose to input photo with gallery
     */
    private void pickGallery() {
        //intent to pick image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        //set intent type to image
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }

    /**
     * Method called when user choose to iunput photo with camera
     */
    private void pickCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Pic"); //title of pic
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image To Text"); //description
        image_uri = getActivity().getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    /**
     * Request permission from user if storage permission is no permitted
     */
    public void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(),storagePermission,STORAGE_REQUEST_CODE);

    }

    /**
     * Check the storage permission
     * @return boolean ; true if permitted, false if not permitted
     */
    public boolean checkStoragePermission() {

        boolean result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)== (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    /**
     * Check the camera permission
     * @return boolean ; true if permitted, false if not permitted
     */
    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)== (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)== (PackageManager.PERMISSION_GRANTED);

        return result &&result1;
    }

    /**
     * Requesr camera permission if the camera permission is not permitted
     */
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(getActivity(),cameraPermission,CAMERA_REQUEST_CODE);
    }

    /**
     * handle permission result
     * @param requestCode reuqestCode to request permission
     * @param permissions array of permissions
     * @param grantResults result of the permission
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && writeStorageAccepted){
                        pickCamera();
                    }
                    else{
                        Toast.makeText(getContext(),"permission denied",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_REQUEST_CODE:
                if(grantResults.length>0){
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(writeStorageAccepted){
                        pickGallery();
                    }
                }
                else{
                    Toast.makeText(getContext(),"permission denied",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * Handle image result
     * @param requestCode the request code for the permission
     * @param resultCode the result code of the permission
     * @param data the data of intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //got image from camera

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                //got image from gallery now crop it
                Uri selectedImage = data.getData();
                picturePath = getRealPathFromURI(selectedImage,
                        getActivity());
                CropImage.activity(selectedImage)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(getContext(),this);
            }
            if(requestCode == IMAGE_PICK_CAMERA_CODE){
                picturePath = getRealPathFromURI(image_uri,
                        getActivity());
                //got image from camera now crop it
                CropImage.activity(image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(getContext(),this);
            }
        }
        //get cropped image
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == Activity.RESULT_OK){
                Uri resultUri = result.getUri();
                //setImage to image view
                mPreview.setImageURI(resultUri);
                mPreview.setTag(picturePath);

                //get drawable bitmap for text  recognition
                mPreview.invalidate();
                BitmapDrawable bitmapDrawable =(BitmapDrawable)mPreview.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                Context context = getActivity().getApplicationContext();
                TextRecognizer recognizer = new TextRecognizer.Builder(context).build();

                if(!recognizer.isOperational()){
                    Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                }
                else{
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();

                    //get text from sb until no text
                    for(int i=0;i<items.size();i++){
                        TextBlock myItem = items.valueAt(i);
                        sb.append(myItem.getValue());
                        sb.append("\n");
                    }

                    mResult.setText(sb.toString()); //set to edit text
                }
            }

            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                //if error
                Exception error = result.getError();
                Toast.makeText(getContext(),""+error,Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * To get the real path in phone gallery
     * @param contentURI uri of the image
     * @param context activity context
     * @return null
     */
    public String getRealPathFromURI(Uri contentURI, Activity context) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = context.managedQuery(contentURI, projection, null,
                null, null);
        if (cursor == null)
            return null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(column_index);
            // cursor.close();
            return s;
        }
        // cursor.close();
        return null;
    }

    /**
     * To set the grid view using image Adapter
     * @param itemList the array list of item
     */
    @Override
    public void setData(ArrayList<String> itemList) {
        imageAdapter.setData(itemList);
    }

    public void onClick(View view){

    }


}