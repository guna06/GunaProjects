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


public class UploadCarPageTwoFragment extends Fragment implements View.OnClickListener{
    public static ImageView front_dash_iv,engine_view_iv,front_birdview_iv,back_seatview_iv;
    public static ImageView upload5,upload6,upload7,upload8;
    Dialog cameraDialog;
    LinearLayout lnr_takepic, lnr_takegallery;
    Bitmap bitmap;
    String selectedImagePath;
    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;
    String path_front_dashboard,path_engine_view,path_front_bird_eye,path_backseat;

    protected static final int TAKE_CAR_PIC5 = 9;
    protected static final int TAKE_CAR_PIC6 = 10;
    protected static final int TAKE_CAR_PIC7 = 11;
    protected static final int TAKE_CAR_PIC8 = 12;

    protected static final int SELECT_CAR_PIC5 = 13;
    protected static final int SELECT_CAR_PIC6 = 14;
    protected static final int SELECT_CAR_PIC7 = 15;
    protected static final int SELECT_CAR_PIC8 = 16;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_five, container, false);

        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getActivity());
        edit = sharedPrefces.edit();

        cameraDialog = new Dialog(getActivity());
        cameraDialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);


        front_dash_iv=view.findViewById(R.id.front_dash_iv);
        engine_view_iv=view.findViewById(R.id.engine_view_iv);
        front_birdview_iv=view.findViewById(R.id.front_birdview_iv);
        back_seatview_iv=view.findViewById(R.id.back_seatview_iv);

        upload5=view.findViewById(R.id.upload5);
        upload6=view.findViewById(R.id.upload6);
        upload7=view.findViewById(R.id.upload7);
        upload8=view.findViewById(R.id.upload8);


        front_dash_iv.setOnClickListener(this);
        engine_view_iv.setOnClickListener(this);
        front_birdview_iv.setOnClickListener(this);
        back_seatview_iv.setOnClickListener(this);

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

            // CAR 5
            case R.id.front_dash_iv:


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
                            startActivityForResult(cameraIntent, TAKE_CAR_PIC5);

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
                            startActivityForResult(pickIntent, SELECT_CAR_PIC5);
                        }


                    }
                });
                cameraDialog.show();

                break;


            // CAR 6
            case R.id.engine_view_iv:

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
                            startActivityForResult(cameraIntent, TAKE_CAR_PIC6);
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
                            startActivityForResult(pickIntent, SELECT_CAR_PIC6);
                        }
                    }
                });
                cameraDialog.show();
                break;


            // CAR 7
            case R.id.front_birdview_iv:

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
                            startActivityForResult(cameraIntent, TAKE_CAR_PIC7);
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
                            startActivityForResult(pickIntent, SELECT_CAR_PIC7);
                        }
                    }
                });
                cameraDialog.show();

                break;






            // car 8
            case R.id.back_seatview_iv:

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
                            startActivityForResult(cameraIntent, TAKE_CAR_PIC8);
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
                            startActivityForResult(pickIntent, SELECT_CAR_PIC8);
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

        //********************CAR PIC 5 -------TAKE
        if (resultCode == RESULT_OK && requestCode == TAKE_CAR_PIC5) {

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

            front_dash_iv.setImageBitmap(bitmap);
            //upload1.setVisibility(View.GONE);
            path_front_dashboard=mediaFile4.getAbsolutePath();
            edit.putString("store_frontdashboard_path",path_front_dashboard);
            edit.commit();


        }








        //********************CAR PIC 6 -------TAKE
        else if (resultCode == RESULT_OK && requestCode == TAKE_CAR_PIC6){

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
            engine_view_iv.setImageBitmap(bitmap);
            // upload2.setVisibility(View.GONE);
            path_engine_view=mediaFile4.getAbsolutePath();
            edit.putString("store_engineview_path",path_engine_view);
            edit.commit();
        }





        //********************CAR PIC 7 -------TAKE

        if (resultCode == RESULT_OK && requestCode == TAKE_CAR_PIC7) {

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


            front_birdview_iv.setImageBitmap(bitmap);
            // upload3.setVisibility(View.GONE);
            path_front_bird_eye=mediaFile4.getAbsolutePath();
            edit.putString("store_frontbird_path",path_front_bird_eye);
            edit.commit();

        }








        //********************CAR PIC 8 -------TAKE
        else if (resultCode == RESULT_OK && requestCode == TAKE_CAR_PIC8){

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


            back_seatview_iv.setImageBitmap(bitmap);
            //upload4.setVisibility(View.GONE);
            path_backseat=mediaFile4.getAbsolutePath();
            edit.putString("store_backseat_path",path_backseat);
            edit.commit();

        }




        //********************CAR PIC 5 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_CAR_PIC5){
            try {
                Uri selectedMediaUri = data.getData();
                path_front_dashboard = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    front_dash_iv.setImageBitmap(bitmap);
                    //upload1.setVisibility(View.GONE);
                    edit.putString("store_frontdashboard_path",path_front_dashboard);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }


        }



        //********************CAR PIC 6 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_CAR_PIC6){
            try {
                Uri selectedMediaUri = data.getData();
                path_engine_view = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    engine_view_iv.setImageBitmap(bitmap);
                    //upload2.setVisibility(View.GONE);

                    edit.putString("store_engineview_path",path_engine_view);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }

        }



        //********************CAR PIC 7 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_CAR_PIC7){
            try {
                Uri selectedMediaUri = data.getData();
                path_front_bird_eye = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    front_birdview_iv.setImageBitmap(bitmap);
                    // upload3.setVisibility(View.GONE);

                    edit.putString("store_frontbird_path",path_front_bird_eye);
                    edit.commit();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }

        }


        //********************CAR PIC 8 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_CAR_PIC8){
            try {
                Uri selectedMediaUri = data.getData();
                path_backseat = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    back_seatview_iv.setImageBitmap(bitmap);
                    // upload4.setVisibility(View.GONE);

                    edit.putString("store_backseat_path",path_backseat);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }
        }
    }
}
