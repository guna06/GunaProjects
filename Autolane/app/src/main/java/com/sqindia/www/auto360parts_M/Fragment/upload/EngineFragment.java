package com.sqindia.www.auto360parts_M.Fragment.upload;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sqindia.www.auto360parts_M.Activity.UploadCarParts;
import com.sqindia.www.auto360parts_M.Model.PhotoItem;
import com.sqindia.www.auto360parts_M.R;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class EngineFragment extends Fragment implements View.OnClickListener{
    ImageView engine_pic1_iv,engine_pic2_iv,ac_pic1_iv,ac_pic2_iv,emmission_pic1_iv,emmission_pic2_iv,fuel_pic1_iv,fuel_pic2_iv,others_pic1_iv,others_pic2_iv;
    ImageView engine_plus1,engine_plus2,ac_plus1,ac_plus2,emmission_plus1,emmission_plus2,fuel_plus1,fuel_plus2,others_plus1,others_plus2;
    CheckBox chk_engine1,chk_engine2,chk_ac1,chk_ac2,chk_emmission1,chk_emmission2,chk_fuel1,chk_fuel2,chk_others1,chk_others2;
    Dialog cameraDialog;
    LinearLayout lnr_takepic, lnr_takegallery;
    Bitmap bitmap;
    String selectedImagePath;
    String str_path_engine1, str_path_engine2;
    public PhotoItem photoItem;
    public static TextView engine_box1,engine_box2,engine_box3,engine_box4;
    public static EditText engine_others;
    String path_engine1,path_engine2,path_ac1,path_ac2,path_emission1,path_emission2,path_fuel1,path_fuel2,path_engine_other1,path_engine_other2;
    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;
    protected static final int TAKE_ENGINE_PIC1 = 1;
    protected static final int TAKE_ENGINE_PIC2 = 2;
    protected static final int SELECT_ENGINE_PIC1 = 3;
    protected static final int SELECT_ENGINE_PIC2 = 4;


    protected static final int TAKE_AC_PIC1 = 5;
    protected static final int TAKE_AC_PIC2 = 6;
    protected static final int SELECT_AC_PIC1 = 7;
    protected static final int SELECT_AC_PIC2 = 8;

    protected static final int TAKE_EMISSION_PIC1 = 9;
    protected static final int TAKE_EMISSION_PIC2 = 10;
    protected static final int SELECT_EMISSION_PIC1 = 11;
    protected static final int SELECT_EMISSION_PIC2 = 12;


    protected static final int TAKE_FUEL_PIC1 = 13;
    protected static final int TAKE_FUEL_PIC2 = 14;
    protected static final int SELECT_FUEL_PIC1 = 15;
    protected static final int SELECT_FUEL_PIC2 = 16;


    protected static final int TAKE_OTHERS_PIC1 = 17;
    protected static final int TAKE_OTHERS_PIC2 = 18;
    protected static final int SELECT_OTHERS_PIC1 = 19;
    protected static final int SELECT_OTHERS_PIC2 = 20;

    PhotoItem photoItemEngine=new PhotoItem();

    SharedPreferences myPrefrence;
    SharedPreferences.Editor editor;


    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        photoItem = ((UploadCarParts) getActivity()).photoItem;
        View view = inflater.inflate(R.layout.fragment_engine, container, false);
        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getActivity());
        edit = sharedPrefces.edit();
        cameraDialog = new Dialog(getActivity());
        cameraDialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

        engine_box1=view.findViewById(R.id.engine_box1);
        engine_box2=view.findViewById(R.id.engine_box2);
        engine_box3=view.findViewById(R.id.engine_box3);
        engine_box4=view.findViewById(R.id.engine_box4);
        engine_others=view.findViewById(R.id.engine_others);


        //@@@@@@@@@@@@@@@@ Engine FindviewbyId
        engine_pic1_iv=view.findViewById(R.id.engine_pic1_iv);
        engine_pic2_iv=view.findViewById(R.id.engine_pic2_iv);
        engine_plus1=view.findViewById(R.id.engine_plus1);
        engine_plus2=view.findViewById(R.id.engine_plus2);
        chk_engine1=view.findViewById(R.id.chk_engine1);
        chk_engine2=view.findViewById(R.id.chk_engine2);

        chk_engine1.setVisibility(View.GONE);
        chk_engine2.setVisibility(View.GONE);


        //@@@@@@@@@@@@@@@@ A/C FindviewbyId
        ac_pic1_iv=view.findViewById(R.id.ac_pic1_iv);
        ac_pic2_iv=view.findViewById(R.id.ac_pic2_iv);
        ac_plus1=view.findViewById(R.id.ac_plus1);
        ac_plus2=view.findViewById(R.id.ac_plus2);
        chk_ac1=view.findViewById(R.id.chk_ac1);
        chk_ac2=view.findViewById(R.id.chk_ac2);

        chk_ac1.setVisibility(View.GONE);
        chk_ac2.setVisibility(View.GONE);


        //@@@@@@@@@@@@@@@@ Emmission FindviewbyId
        emmission_pic1_iv=view.findViewById(R.id.emmission_pic1_iv);
        emmission_pic2_iv=view.findViewById(R.id.emmission_pic2_iv);
        emmission_plus1=view.findViewById(R.id.emmission_plus1);
        emmission_plus2=view.findViewById(R.id.emmission_plus2);
        chk_emmission1=view.findViewById(R.id.chk_emmission1);
        chk_emmission2=view.findViewById(R.id.chk_emmission2);

        chk_emmission1.setVisibility(View.GONE);
        chk_emmission2.setVisibility(View.GONE);

        //@@@@@@@@@@@@@@@@ FUEL FindviewbyId
        fuel_pic1_iv=view.findViewById(R.id.fuel_pic1_iv);
        fuel_pic2_iv=view.findViewById(R.id.fuel_pic2_iv);
        fuel_plus1=view.findViewById(R.id.fuel_plus1);
        fuel_plus2=view.findViewById(R.id.fuel_plus2);
        chk_fuel1=view.findViewById(R.id.chk_fuel1);
        chk_fuel2=view.findViewById(R.id.chk_fuel2);

        chk_fuel1.setVisibility(View.GONE);
        chk_fuel2.setVisibility(View.GONE);


        //@@@@@@@@@@@@@@@@ OTHERS FindviewbyId
        others_pic1_iv=view.findViewById(R.id.others_pic1_iv);
        others_pic2_iv=view.findViewById(R.id.others_pic2_iv);
        others_plus1=view.findViewById(R.id.others_plus1);
        others_plus2=view.findViewById(R.id.others_plus2);
        chk_others1=view.findViewById(R.id.chk_others1);
        chk_others2=view.findViewById(R.id.chk_others2);


        chk_others1.setVisibility(View.GONE);
        chk_others2.setVisibility(View.GONE);


        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        edit = sharedPreferences.edit();
        //get image engine path value:
        path_engine1=sharedPreferences.getString("store_engine1","");
        path_engine2=sharedPreferences.getString("store_engine2","");
        path_ac1=sharedPreferences.getString("store_ac1","");
        path_ac2=sharedPreferences.getString("store_ac2","");
        path_emission1=sharedPreferences.getString("store_emmission1","");
        path_emission2=sharedPreferences.getString("store_emmission2","");
        path_fuel1=sharedPreferences.getString("store_fuel1","");
        path_fuel2=sharedPreferences.getString("store_fuel2","");
        path_engine_other1=sharedPreferences.getString("store_engineother1","");
        path_engine_other2=sharedPreferences.getString("store_engineother2","");




        if(path_engine1!=null) {
            chk_engine1.setChecked(true);
            File imgFile = new File(path_engine1);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                chk_engine1.setVisibility(View.VISIBLE);
                engine_pic1_iv.setImageBitmap(myBitmap);
                engine_plus1.setVisibility(View.GONE);
            }
        }

        if(path_engine2!=null) {
            chk_engine2.setChecked(true);
            File imgFile = new File(path_engine2);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                chk_engine2.setVisibility(View.VISIBLE);
                engine_pic2_iv.setImageBitmap(myBitmap);
                engine_plus2.setVisibility(View.GONE);
            }
        }


        if(path_ac1!=null) {
            chk_ac1.setChecked(true);
            File imgFile = new File(path_ac1);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                chk_ac1.setVisibility(View.VISIBLE);
                ac_pic1_iv.setImageBitmap(myBitmap);
                ac_plus1.setVisibility(View.GONE);
            }
        }



        if(path_ac2!=null) {
            chk_ac2.setChecked(true);
            File imgFile = new File(path_ac2);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                chk_ac2.setVisibility(View.VISIBLE);
                ac_pic2_iv.setImageBitmap(myBitmap);
                ac_plus2.setVisibility(View.GONE);
            }
        }


        if(path_emission1!=null) {
            chk_emmission1.setChecked(true);
            File imgFile = new File(path_emission1);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                chk_emmission1.setVisibility(View.VISIBLE);
                emmission_pic1_iv.setImageBitmap(myBitmap);
                emmission_plus1.setVisibility(View.GONE);
            }
        }



        if(path_emission2!=null) {
            chk_emmission2.setChecked(true);
            File imgFile = new File(path_emission2);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                chk_emmission2.setVisibility(View.VISIBLE);
                emmission_pic2_iv.setImageBitmap(myBitmap);
                emmission_plus2.setVisibility(View.GONE);
            }
        }


        if(path_fuel1!=null) {
            chk_fuel1.setChecked(true);
            File imgFile = new File(path_fuel1);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                chk_fuel1.setVisibility(View.VISIBLE);
                fuel_pic1_iv.setImageBitmap(myBitmap);
                fuel_plus1.setVisibility(View.GONE);
            }
        }


        if(path_fuel2!=null) {
            chk_fuel2.setChecked(true);
            File imgFile = new File(path_fuel2);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                chk_fuel2.setVisibility(View.VISIBLE);
                fuel_pic2_iv.setImageBitmap(myBitmap);
                fuel_plus2.setVisibility(View.GONE);
            }
        }


        if(path_engine_other1!=null) {
            chk_others1.setChecked(true);
            File imgFile = new File(path_engine_other1);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                chk_others1.setVisibility(View.VISIBLE);
                others_pic1_iv.setImageBitmap(myBitmap);
                others_plus1.setVisibility(View.GONE);
            }
        }


        if(path_engine_other2!=null) {
            chk_others2.setChecked(true);
            File imgFile = new File(path_engine_other2);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                chk_others2.setVisibility(View.VISIBLE);
                others_pic2_iv.setImageBitmap(myBitmap);
                others_plus2.setVisibility(View.GONE);
            }
        }



        engine_pic1_iv.setOnClickListener(this);
        engine_pic2_iv.setOnClickListener(this);
        ac_pic1_iv.setOnClickListener(this);
        ac_pic2_iv.setOnClickListener(this);
        emmission_pic1_iv.setOnClickListener(this);
        emmission_pic2_iv.setOnClickListener(this);
        fuel_pic1_iv.setOnClickListener(this);
        fuel_pic2_iv.setOnClickListener(this);
        others_pic1_iv.setOnClickListener(this);
        others_pic2_iv.setOnClickListener(this);



        try {
            if (photoItem.engine_acpic1 != "") {
                Log.e("tag", "11111-dddd------" + photoItem.engine_enginepic1);
                engine_pic1_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(photoItem.engine_enginepic1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));

            } else if (photoItem.engine_enginepic1 == null) {
                Log.e("tag", "22222222222-dddd------" + photoItem.engine_enginepic1);
                engine_pic1_iv.setImageResource(R.drawable.camera_box);

            }
        } catch (NullPointerException e) {

        }


        //Engine Checkbox1 Condition--------------------------------------------------->
        chk_engine1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if(!chk_engine1.isChecked()){
                    edit.remove("store_engine1");
                    edit.commit();
                    chk_engine1.setChecked(false);
                    chk_engine1.setVisibility(View.GONE);
                    engine_pic1_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    engine_pic1_iv.setEnabled(true);
                    engine_plus1.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_engine1.setChecked(true);
                    chk_engine1.setVisibility(View.VISIBLE);
                    engine_pic1_iv.setEnabled(false);
                    engine_plus1.setVisibility(View.GONE);
                }

            }
        });



        //Engine Checkbox2 Condition--------------------------------------------------->
        chk_engine2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if(!chk_engine2.isChecked()){
                    edit.remove("store_engine2");
                    edit.commit();
                    chk_engine2.setChecked(false);
                    chk_engine2.setVisibility(View.GONE);
                    engine_pic2_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    engine_pic2_iv.setEnabled(true);
                    engine_plus2.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_engine2.setChecked(true);
                    chk_engine2.setVisibility(View.VISIBLE);
                    engine_pic2_iv.setEnabled(false);
                    engine_plus2.setVisibility(View.GONE);
                }

            }
        });




        //A/C Checkbox1 Condition--------------------------------------------------->
        chk_ac1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if(!chk_ac1.isChecked()){
                    edit.remove("store_ac1");
                    edit.commit();
                    chk_ac1.setChecked(false);
                    chk_ac1.setVisibility(View.GONE);
                    ac_pic1_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    ac_pic1_iv.setEnabled(true);
                    ac_plus1.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_ac1.setChecked(true);
                    chk_ac1.setVisibility(View.VISIBLE);
                    ac_pic1_iv.setEnabled(false);
                    ac_plus1.setVisibility(View.GONE);
                }

            }
        });




        //A/C Checkbox2 Condition--------------------------------------------------->
        chk_ac2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if(!chk_ac2.isChecked()){
                    edit.remove("store_ac2");
                    edit.commit();
                    chk_ac2.setChecked(false);
                    chk_ac2.setVisibility(View.GONE);
                    ac_pic2_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    ac_pic2_iv.setEnabled(true);
                    ac_plus2.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_ac2.setChecked(true);
                    chk_ac2.setVisibility(View.VISIBLE);
                    ac_pic2_iv.setEnabled(false);
                    ac_plus2.setVisibility(View.GONE);
                }

            }
        });


        //Emmission Checkbox1 Condition--------------------------------------------------->
        chk_emmission1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if(!chk_emmission1.isChecked()){
                    edit.remove("store_emmission1");
                    edit.commit();
                    chk_emmission1.setChecked(false);
                    chk_emmission1.setVisibility(View.GONE);
                    emmission_pic1_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    emmission_pic1_iv.setEnabled(true);
                    emmission_plus1.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_emmission1.setChecked(true);
                    chk_emmission1.setVisibility(View.VISIBLE);
                    emmission_pic1_iv.setEnabled(false);
                    emmission_plus1.setVisibility(View.GONE);
                }

            }
        });


        //Emmission Checkbox2 Condition--------------------------------------------------->
        chk_emmission2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if(!chk_emmission2.isChecked()){
                    edit.remove("store_emmission2");
                    edit.commit();
                    chk_emmission2.setChecked(false);
                    chk_emmission2.setVisibility(View.GONE);
                    emmission_pic2_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    emmission_pic2_iv.setEnabled(true);
                    emmission_plus2.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_emmission2.setChecked(true);
                    chk_emmission2.setVisibility(View.VISIBLE);
                    emmission_pic2_iv.setEnabled(false);
                    emmission_plus2.setVisibility(View.GONE);
                }

            }
        });


        //Fuel Checkbox1 Condition--------------------------------------------------->
        chk_fuel1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!chk_fuel1.isChecked()){
                    edit.remove("store_fuel1");
                    edit.commit();
                    chk_fuel1.setChecked(false);
                    chk_fuel1.setVisibility(View.GONE);
                    fuel_pic1_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    fuel_pic1_iv.setEnabled(true);
                    fuel_plus1.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_fuel1.setChecked(true);
                    chk_fuel1.setVisibility(View.VISIBLE);
                    fuel_pic1_iv.setEnabled(false);
                    fuel_plus1.setVisibility(View.GONE);
                }
            }
        });


        //Fuel Checkbox2 Condition--------------------------------------------------->
        chk_fuel2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!chk_fuel2.isChecked()){
                    edit.remove("store_fuel2");
                    edit.commit();
                    chk_fuel2.setChecked(false);
                    chk_fuel2.setVisibility(View.GONE);
                    fuel_pic2_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    fuel_pic2_iv.setEnabled(true);
                    fuel_plus2.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_fuel2.setChecked(true);
                    chk_fuel2.setVisibility(View.VISIBLE);
                    fuel_pic2_iv.setEnabled(false);
                    fuel_plus2.setVisibility(View.GONE);
                }
            }
        });



        //Others Checkbox1 Condition--------------------------------------------------->
        chk_others1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!chk_others1.isChecked()){
                    edit.remove("store_engineother1");
                    edit.commit();
                    chk_others1.setChecked(false);
                    chk_others1.setVisibility(View.GONE);
                    others_pic1_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    others_pic1_iv.setEnabled(true);
                    others_plus1.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_others1.setChecked(true);
                    chk_others1.setVisibility(View.VISIBLE);
                    others_pic1_iv.setEnabled(false);
                    others_plus1.setVisibility(View.GONE);
                }
            }
        });




        //Others Checkbox2 Condition--------------------------------------------------->
        chk_others2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!chk_others2.isChecked()){
                    edit.remove("store_engineother2");
                    edit.commit();
                    chk_others2.setChecked(false);
                    chk_others2.setVisibility(View.GONE);
                    others_pic2_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    others_pic2_iv.setEnabled(true);
                    others_plus2.setVisibility(View.VISIBLE);
                }
                else {
                    chk_others2.setChecked(true);
                    chk_others2.setVisibility(View.VISIBLE);
                    others_pic2_iv.setEnabled(false);
                    others_plus2.setVisibility(View.GONE);
                }
            }
        });





        return view;
    }



    //call Camera Intent Dialog for all Imageview:
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            // ENGINE PIC ONE
            case R.id.engine_pic1_iv:


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
                            startActivityForResult(cameraIntent, TAKE_ENGINE_PIC1);
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
                            startActivityForResult(pickIntent, SELECT_ENGINE_PIC1);
                        }


                    }
                });
                cameraDialog.show();

                break;


            // ENGINE PIC TWO
            case R.id.engine_pic2_iv:

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
                            startActivityForResult(cameraIntent, TAKE_ENGINE_PIC2);
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
                            startActivityForResult(pickIntent, SELECT_ENGINE_PIC2);
                        }


                    }
                });
                cameraDialog.show();

                break;




// Ac PIC ONE
            case R.id.ac_pic1_iv:

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
                            startActivityForResult(cameraIntent, TAKE_AC_PIC1);
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
                            startActivityForResult(pickIntent, SELECT_AC_PIC1);
                        }


                    }
                });
                cameraDialog.show();

                break;




            // Ac PIC ONE
            case R.id.ac_pic2_iv:

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
                            startActivityForResult(cameraIntent, TAKE_AC_PIC2);
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
                            startActivityForResult(pickIntent, SELECT_AC_PIC2);
                        }


                    }
                });
                cameraDialog.show();

                break;




                //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@   3 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            // Emmision PIC ONE
            case R.id.emmission_pic1_iv:

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
                            startActivityForResult(cameraIntent, TAKE_EMISSION_PIC1);
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
                            startActivityForResult(pickIntent, SELECT_EMISSION_PIC1);
                        }


                    }
                });
                cameraDialog.show();

                break;




            // Emmision PIC ONE
            case R.id.emmission_pic2_iv:

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
                            startActivityForResult(cameraIntent, TAKE_EMISSION_PIC2);
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
                            startActivityForResult(pickIntent, SELECT_EMISSION_PIC2);
                        }


                    }
                });
                cameraDialog.show();

                break;







            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@   4 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            // FUEL PIC ONE
            case R.id.fuel_pic1_iv:

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
                            startActivityForResult(cameraIntent, TAKE_FUEL_PIC1);
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
                            startActivityForResult(pickIntent, SELECT_FUEL_PIC1);
                        }


                    }
                });
                cameraDialog.show();

                break;




            // FUEL PIC ONE
            case R.id.fuel_pic2_iv:

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
                            startActivityForResult(cameraIntent, TAKE_FUEL_PIC2);
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
                            startActivityForResult(pickIntent, SELECT_FUEL_PIC2);
                        }
                    }
                });
                cameraDialog.show();
                break;




            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@   5 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            // OTHERS PIC ONE
            case R.id.others_pic1_iv:

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
                            startActivityForResult(cameraIntent, TAKE_OTHERS_PIC1);
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
                            startActivityForResult(pickIntent, SELECT_OTHERS_PIC1);
                        }


                    }
                });
                cameraDialog.show();

                break;




            // Others PIC TWO
            case R.id.others_pic2_iv:

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
                            startActivityForResult(cameraIntent, TAKE_OTHERS_PIC2);
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
                            startActivityForResult(pickIntent, SELECT_OTHERS_PIC2);
                        }


                    }
                });
                cameraDialog.show();

                break;




            default:
                break;
        }

    }
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@



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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmap = null;
        selectedImagePath = null;


        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 1 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

        //********************ENGINE PIC 1 -------TAKE
        if (resultCode == RESULT_OK && requestCode == TAKE_ENGINE_PIC1) {

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

                engine_pic1_iv.setImageBitmap(bitmap);
                chk_engine1.setChecked(true);
                chk_engine1.setVisibility(View.VISIBLE);
                engine_plus1.setVisibility(View.GONE);
                path_engine1=mediaFile4.getAbsolutePath();
                edit.putString("store_engine1", path_engine1);
                edit.commit();


        }





        //********************ENGINE PIC 2 -------TAKE
        else if (resultCode == RESULT_OK && requestCode == TAKE_ENGINE_PIC2){


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

                engine_pic2_iv.setImageBitmap(bitmap);
                chk_engine2.setChecked(true);
                chk_engine2.setVisibility(View.VISIBLE);
                engine_plus2.setVisibility(View.GONE);
                path_engine2=mediaFile4.getAbsolutePath();
                edit.putString("store_engine2", path_engine2);
                edit.commit();

                //storeImageTosdCard(bitmap);

        }














        //********************ENGINE PIC 1 -------SELECT

        else if (resultCode == RESULT_OK && requestCode == SELECT_ENGINE_PIC1) {


            try {
                Uri selectedMediaUri = data.getData();
                path_engine1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    engine_pic1_iv.setImageBitmap(bitmap);
                    chk_engine1.setChecked(true);
                    chk_engine1.setVisibility(View.VISIBLE);
                    engine_plus1.setVisibility(View.GONE);
                    edit.putString("store_engine1", path_engine1);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }

        }


        //********************ENGINE PIC 2 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_ENGINE_PIC2) {
            try {
                Uri selectedMediaUri = data.getData();
                path_engine2 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    engine_pic2_iv.setImageBitmap(bitmap);
                    chk_engine2.setChecked(true);
                    chk_engine2.setVisibility(View.VISIBLE);
                    engine_plus2.setVisibility(View.GONE);
                    edit.putString("store_engine2", path_engine2);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }


        }






        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 2 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

        //********************AC PIC 1 -------TAKE
        if (resultCode == RESULT_OK && requestCode == TAKE_AC_PIC1) {

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

                ac_pic1_iv.setImageBitmap(bitmap);
                chk_ac1.setChecked(true);
                chk_ac1.setVisibility(View.VISIBLE);
                ac_plus1.setVisibility(View.GONE);
                path_ac1=mediaFile4.getAbsolutePath();
                edit.putString("store_ac1", path_ac1);
                edit.commit();




        }





        //********************AC PIC 2 -------TAKE
        else if (resultCode == RESULT_OK && requestCode == TAKE_AC_PIC2){

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


                ac_pic2_iv.setImageBitmap(bitmap);
                chk_ac2.setChecked(true);
                chk_ac2.setVisibility(View.VISIBLE);
                ac_plus2.setVisibility(View.GONE);
                path_ac2=mediaFile4.getAbsolutePath();
                edit.putString("store_ac2", path_ac2);
                edit.commit();

        }








        //********************AC PIC 1 -------SELECT

        else if (resultCode == RESULT_OK && requestCode == SELECT_AC_PIC1) {


            try {
                Uri selectedMediaUri = data.getData();
                path_ac1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    ac_pic1_iv.setImageBitmap(bitmap);
                    chk_ac1.setChecked(true);
                    chk_ac1.setVisibility(View.VISIBLE);
                    ac_plus1.setVisibility(View.GONE);
                    edit.putString("store_ac1", path_ac1);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }

        }


        //********************AC PIC 2 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_AC_PIC2) {
            try {
                Uri selectedMediaUri = data.getData();
                path_ac2 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    ac_pic2_iv.setImageBitmap(bitmap);
                    chk_ac2.setChecked(true);
                    chk_ac2.setVisibility(View.VISIBLE);
                    ac_plus2.setVisibility(View.GONE);
                    edit.putString("store_ac2", path_ac2);
                    edit.commit();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }


        }









        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 3 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

        //********************EMMISSION PIC 1 -------TAKE
        if (resultCode == RESULT_OK && requestCode == TAKE_EMISSION_PIC1) {


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

                emmission_pic1_iv.setImageBitmap(bitmap);
                chk_emmission1.setChecked(true);
                chk_emmission1.setVisibility(View.VISIBLE);
                emmission_plus1.setVisibility(View.GONE);
                path_emission1=mediaFile4.getAbsolutePath();
                edit.putString("store_emmission1", path_emission1);
                edit.commit();



        }





        //********************EMMISSION PIC 2 -------TAKE
        else if (resultCode == RESULT_OK && requestCode == TAKE_EMISSION_PIC2){

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

                emmission_pic2_iv.setImageBitmap(bitmap);
                chk_emmission2.setChecked(true);
                chk_emmission2.setVisibility(View.VISIBLE);
                emmission_plus2.setVisibility(View.GONE);
                path_emission2=mediaFile4.getAbsolutePath();
                edit.putString("store_emmission2", path_emission2);
                edit.commit();


        }








        //********************EMMISSION PIC 1 -------SELECT

        else if (resultCode == RESULT_OK && requestCode == SELECT_EMISSION_PIC1) {


            try {
                Uri selectedMediaUri = data.getData();
                path_emission1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    emmission_pic1_iv.setImageBitmap(bitmap);
                    chk_emmission1.setChecked(true);
                    chk_emmission1.setVisibility(View.VISIBLE);
                    emmission_plus1.setVisibility(View.GONE);
                    edit.putString("store_emmission1", path_emission1);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }

        }


        //********************EMMISSION PIC 2 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_EMISSION_PIC2) {
            try {
                Uri selectedMediaUri = data.getData();
                path_emission2 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    emmission_pic2_iv.setImageBitmap(bitmap);
                    chk_emmission2.setChecked(true);
                    chk_emmission2.setVisibility(View.VISIBLE);
                    emmission_plus2.setVisibility(View.GONE);
                    edit.putString("store_emmission2", path_emission2);
                    edit.commit();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }


        }





        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 4 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

        //********************FUEL PIC 1 -------TAKE
        if (resultCode == RESULT_OK && requestCode == TAKE_FUEL_PIC1) {


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

                fuel_pic1_iv.setImageBitmap(bitmap);
                chk_fuel1.setChecked(true);
                chk_fuel1.setVisibility(View.VISIBLE);
                fuel_plus1.setVisibility(View.GONE);
                path_fuel1=mediaFile4.getAbsolutePath();
                edit.putString("store_fuel1", path_fuel1);
                edit.commit();



        }





        //********************FUEL PIC 2 -------TAKE
        else if (resultCode == RESULT_OK && requestCode == TAKE_FUEL_PIC2){

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

                fuel_pic2_iv.setImageBitmap(bitmap);
                chk_fuel2.setChecked(true);
                chk_fuel2.setVisibility(View.VISIBLE);
                fuel_plus2.setVisibility(View.GONE);
                path_fuel2=mediaFile4.getAbsolutePath();
                edit.putString("store_fuel2", path_fuel2);
                edit.commit();


        }








        //********************FUEL PIC 1 -------SELECT

        else if (resultCode == RESULT_OK && requestCode == SELECT_FUEL_PIC1) {


            try {
                Uri selectedMediaUri = data.getData();
                path_fuel1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    fuel_pic1_iv.setImageBitmap(bitmap);
                    chk_fuel1.setChecked(true);
                    chk_fuel1.setVisibility(View.VISIBLE);
                    fuel_plus1.setVisibility(View.GONE);
                    edit.putString("store_fuel1", path_fuel1);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }

        }


        //********************FUEL PIC 2 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_FUEL_PIC2) {
            try {
                Uri selectedMediaUri = data.getData();
                path_fuel2 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    fuel_pic2_iv.setImageBitmap(bitmap);
                    chk_fuel2.setChecked(true);
                    chk_fuel2.setVisibility(View.VISIBLE);
                    fuel_plus2.setVisibility(View.GONE);
                    edit.putString("store_fuel2", path_fuel2);
                    edit.commit();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {
            }
        }




//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 5 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

        //********************OTHERS PIC 1 -------TAKE
        if (resultCode == RESULT_OK && requestCode == TAKE_OTHERS_PIC1) {

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


                others_pic1_iv.setImageBitmap(bitmap);
                chk_others1.setChecked(true);
                chk_others1.setVisibility(View.VISIBLE);
                others_plus1.setVisibility(View.GONE);
                path_engine_other1=mediaFile4.getAbsolutePath();
                edit.putString("store_engineother1", path_engine_other1);
                edit.commit();




        }





        //********************OTHERS PIC 2 -------TAKE
        else if (resultCode == RESULT_OK && requestCode == TAKE_OTHERS_PIC2){


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

                others_pic2_iv.setImageBitmap(bitmap);
                chk_others2.setChecked(true);
                chk_others2.setVisibility(View.VISIBLE);
                others_plus2.setVisibility(View.GONE);
                path_engine_other2=mediaFile4.getAbsolutePath();
                edit.putString("store_engineother2", path_engine_other2);
                edit.commit();


        }








        //********************OTHERS PIC 1 -------SELECT

        else if (resultCode == RESULT_OK && requestCode == SELECT_OTHERS_PIC1) {


            try {
                Uri selectedMediaUri = data.getData();
                path_engine1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    others_pic1_iv.setImageBitmap(bitmap);
                    chk_others1.setChecked(true);
                    chk_others1.setVisibility(View.VISIBLE);
                    others_plus1.setVisibility(View.GONE);


                    edit.putString("store_engineother1", path_engine1);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }

        }


        //********************OTHERS PIC 2 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_OTHERS_PIC2) {
            try {
                Uri selectedMediaUri = data.getData();
                path_engine2 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    others_pic1_iv.setImageBitmap(bitmap);
                    chk_others1.setChecked(true);
                    chk_others1.setVisibility(View.VISIBLE);
                    others_plus1.setVisibility(View.GONE);


                    edit.putString("store_engineother2", path_engine2);
                    edit.commit();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }


        }





    }

}