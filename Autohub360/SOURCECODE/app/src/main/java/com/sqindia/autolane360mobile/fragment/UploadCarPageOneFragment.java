package com.sqindia.autolane360mobile.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.sqindia.autolane360mobile.R;
import com.sqindia.autolane360mobile.activity.GetFilePathFromDevice;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import static android.app.Activity.RESULT_OK;


public class UploadCarPageOneFragment extends Fragment implements View.OnClickListener{
    public static ImageView front_view_iv,sideview_right_iv,sideview_left_iv,backview_iv;
    public static ImageView upload1,upload2,upload3,upload4;
    Bitmap bitmap;
    String selectedImagePath;
    Dialog cameraDialog;
    LinearLayout lnr_takepic, lnr_takegallery;
    public static String path_fronview,path_sideview_right,path_sideview_left,path_backview;

    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;

    protected static final int TAKE_CAR_PIC1 = 1;
    protected static final int TAKE_CAR_PIC2 = 2;
    protected static final int TAKE_CAR_PIC3 = 3;
    protected static final int TAKE_CAR_PIC4 = 4;


    protected static final int SELECT_CAR_PIC1 = 5;
    protected static final int SELECT_CAR_PIC2 = 6;
    protected static final int SELECT_CAR_PIC3 = 7;
    protected static final int SELECT_CAR_PIC4 = 8;



    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_four, container, false);

        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getActivity());
        edit = sharedPrefces.edit();

        cameraDialog = new Dialog(getActivity());
        cameraDialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);


        front_view_iv=view.findViewById(R.id.front_view_iv);
        sideview_right_iv=view.findViewById(R.id.sideview_right_iv);
        sideview_left_iv=view.findViewById(R.id.sideview_left_iv);
        backview_iv=view.findViewById(R.id.backview_iv);


        upload1=view.findViewById(R.id.upload1);
        upload2=view.findViewById(R.id.upload2);
        upload3=view.findViewById(R.id.upload3);
        upload4=view.findViewById(R.id.upload4);

        front_view_iv.setOnClickListener(this);
        sideview_right_iv.setOnClickListener(this);
        sideview_left_iv.setOnClickListener(this);
        backview_iv.setOnClickListener(this);
        return view;
    }



    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if ((result == PackageManager.PERMISSION_GRANTED) && (result1 == PackageManager.PERMISSION_GRANTED) && (result2 == PackageManager.PERMISSION_GRANTED)) {
            return true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            return false;
        }
    }








    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            // CAR 1
            case R.id.front_view_iv:


                cameraDialog.setContentView(R.layout.camera_dialog);
                lnr_takepic = cameraDialog.findViewById(R.id.lnr_takepic);
                lnr_takegallery = cameraDialog.findViewById(R.id.lnr_takegallery);


                // if button is clicked, close the custom dialog
                lnr_takepic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraDialog.dismiss();
                        if(checkPermission()) {

                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, TAKE_CAR_PIC1);

                        }
                    }
                });


                lnr_takegallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraDialog.dismiss();

                        if (checkPermission()) {
                            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            pickIntent.setType("image/*");
                            startActivityForResult(pickIntent, SELECT_CAR_PIC1);
                        }


                    }
                });
                cameraDialog.show();

                break;


            // CAR 2
            case R.id.sideview_right_iv:

                cameraDialog.setContentView(R.layout.camera_dialog);
                lnr_takepic = cameraDialog.findViewById(R.id.lnr_takepic);
                lnr_takegallery = cameraDialog.findViewById(R.id.lnr_takegallery);

                // if button is clicked, close the custom dialog
                lnr_takepic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraDialog.dismiss();
                        if(checkPermission()) {

                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, TAKE_CAR_PIC2);
                        }
                    }
                });


                lnr_takegallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraDialog.dismiss();

                        if (checkPermission()) {
                            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            pickIntent.setType("image/*");
                            startActivityForResult(pickIntent, SELECT_CAR_PIC2);
                        }

                    }
                });
                cameraDialog.show();
                break;





            // CAR 3
            case R.id.sideview_left_iv:

                cameraDialog.setContentView(R.layout.camera_dialog);
                lnr_takepic = cameraDialog.findViewById(R.id.lnr_takepic);
                lnr_takegallery = cameraDialog.findViewById(R.id.lnr_takegallery);

                // if button is clicked, close the custom dialog
                lnr_takepic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraDialog.dismiss();

                        if(checkPermission()) {

                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, TAKE_CAR_PIC3);
                        }
                    }
                });


                lnr_takegallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraDialog.dismiss();


                        if (checkPermission()) {
                            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            pickIntent.setType("image/*");
                            startActivityForResult(pickIntent, SELECT_CAR_PIC3);
                        }
                    }
                });
                cameraDialog.show();

                break;






            // car 4
            case R.id.backview_iv:
                cameraDialog.setContentView(R.layout.camera_dialog);
                lnr_takepic = cameraDialog.findViewById(R.id.lnr_takepic);
                lnr_takegallery = cameraDialog.findViewById(R.id.lnr_takegallery);

                // if button is clicked, close the custom dialog
                lnr_takepic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraDialog.dismiss();

                        if(checkPermission()) {

                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, TAKE_CAR_PIC4);
                        }
                    }
                });


                lnr_takegallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraDialog.dismiss();


                        if (checkPermission()) {
                            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            pickIntent.setType("image/*");
                            startActivityForResult(pickIntent, SELECT_CAR_PIC4);
                        }
                    }
                });
                cameraDialog.show();

                break;
            default:
                break;
        }

    }








    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        bitmap = null;
        selectedImagePath = null;

        //********************CAR PIC 1 -------TAKE
        if (resultCode == RESULT_OK && requestCode == TAKE_CAR_PIC1) {

            bitmap = (Bitmap) data.getExtras().get("data");
            File sd = Environment.getExternalStorageDirectory();
            File imagepath = new File(sd.getAbsolutePath() + File.separator +
                    "FolderName" + File.separator + "InsideFolderName");

            if (!imagepath.isDirectory())
            {
                imagepath.mkdirs();
            }

            File mediaFile4 = new File(imagepath + File.separator + "img_" +
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(mediaFile4);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            front_view_iv.setImageBitmap(bitmap);
            //upload1.setVisibility(View.GONE);
            path_fronview=mediaFile4.getAbsolutePath();
            edit.putString("store_fronview_path",path_fronview);
            edit.commit();
        }





        //********************CAR PIC 2 -------TAKE
        else if (resultCode == RESULT_OK && requestCode == TAKE_CAR_PIC2){

            bitmap = (Bitmap) data.getExtras().get("data");
            File sd = Environment.getExternalStorageDirectory();
            File imagepath = new File(sd.getAbsolutePath() + File.separator +
                    "FolderName" + File.separator + "InsideFolderName");

            if (!imagepath.isDirectory())
            {
                imagepath.mkdirs();
            }

            File mediaFile4 = new File(imagepath + File.separator + "img_" +
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(mediaFile4);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            sideview_right_iv.setImageBitmap(bitmap);
           // upload2.setVisibility(View.GONE);
            path_sideview_right=mediaFile4.getAbsolutePath();
            edit.putString("store_sideviewright_path",path_sideview_right);
            edit.commit();
        }





        //********************CAR PIC 3 -------TAKE

        if (resultCode == RESULT_OK && requestCode == TAKE_CAR_PIC3) {

            bitmap = (Bitmap) data.getExtras().get("data");
            File sd = Environment.getExternalStorageDirectory();
            File imagepath = new File(sd.getAbsolutePath() + File.separator +
                    "FolderName" + File.separator + "InsideFolderName");

            if (!imagepath.isDirectory())
            {
                imagepath.mkdirs();
            }

            File mediaFile4 = new File(imagepath + File.separator + "img_" +
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(mediaFile4);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            sideview_left_iv.setImageBitmap(bitmap);
           // upload3.setVisibility(View.GONE);
            path_sideview_left=mediaFile4.getAbsolutePath();
            edit.putString("store_sideviewleft_path",path_sideview_left);
            edit.commit();
        }








        //********************CAR PIC 4 -------TAKE
        else if (resultCode == RESULT_OK && requestCode == TAKE_CAR_PIC4){

            bitmap = (Bitmap) data.getExtras().get("data");
            File sd = Environment.getExternalStorageDirectory();
            File imagepath = new File(sd.getAbsolutePath() + File.separator +
                    "FolderName" + File.separator + "InsideFolderName");

            if (!imagepath.isDirectory())
            {
                imagepath.mkdirs();
            }

            File mediaFile4 = new File(imagepath + File.separator + "img_" +
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(mediaFile4);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            backview_iv.setImageBitmap(bitmap);
            //upload4.setVisibility(View.GONE);
            path_backview=mediaFile4.getAbsolutePath();
            edit.putString("store_backview_path",path_backview);
            edit.commit();



        }




        //********************CAR PIC 1 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_CAR_PIC1){
            try {
                Uri selectedMediaUri = data.getData();
                path_fronview = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    front_view_iv.setImageBitmap(bitmap);
                    //upload1.setVisibility(View.GONE);
                    edit.putString("store_fronview_path",path_fronview);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }


        }



        //********************CAR PIC 2 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_CAR_PIC2){
            try {
                Uri selectedMediaUri = data.getData();
                path_sideview_right = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    sideview_right_iv.setImageBitmap(bitmap);
                    //upload2.setVisibility(View.GONE);

                    edit.putString("store_sideviewright_path",path_sideview_right);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }

        }



        //********************CAR PIC 3 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_CAR_PIC3){
            try {
                Uri selectedMediaUri = data.getData();
                path_sideview_left = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    sideview_left_iv.setImageBitmap(bitmap);
                   // upload3.setVisibility(View.GONE);

                    edit.putString("store_sideviewleft_path",path_sideview_left);
                    edit.commit();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }

        }


        //********************CAR PIC 4 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_CAR_PIC4){
            try {
                Uri selectedMediaUri = data.getData();
                path_backview = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    backview_iv.setImageBitmap(bitmap);
                   // upload4.setVisibility(View.GONE);

                    edit.putString("store_backview_path",path_backview);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }
        }


    }





}
