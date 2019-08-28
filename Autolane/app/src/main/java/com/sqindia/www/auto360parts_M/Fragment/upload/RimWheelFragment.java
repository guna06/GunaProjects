package com.sqindia.www.auto360parts_M.Fragment.upload;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sqindia.www.auto360parts_M.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class RimWheelFragment extends Fragment implements View.OnClickListener{

    ImageView break_pic1_iv,break_pic2_iv,suspense_pic1_iv,suspense_pic2_iv,rims_pic1_iv,rims_pic2_iv,wheels_pic1_iv,wheels_pic2_iv,others_pic1_iv,others_pic2_iv;
    ImageView break_plus1,break_plus2,suspense_plus1,suspense_plus2,rims_plus1,rims_plus2,wheels_plus1,wheels_plus2,others_plus1,others_plus2;
    CheckBox chk_break1,chk_break2,chk_suspense1,chk_suspense2,chk_rims1,chk_rims2,chk_wheels1,chk_wheels2,chk_others1,chk_others2;
    Dialog cameraDialog;
    LinearLayout lnr_takepic, lnr_takegallery;
    Bitmap bitmap;
    String selectedImagePath;
    public static TextView rim_box1,rim_box2,rim_box3,rim_box4;
    public static EditText rim_others;

    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;
    String path_brake1,path_brake2,path_suspension1,path_suspension2,path_rims1,path_rims2,path_wheels1,path_wheels2,path_wheel_other1,path_wheel_other2;


    protected static final int TAKE_BREAK_PIC1 = 1;
    protected static final int TAKE_BREAK_PIC2 = 2;
    protected static final int SELECT_BREAK_PIC1 = 3;
    protected static final int SELECT_BREAK_PIC2 = 4;


    protected static final int TAKE_SUSPENSE_PIC1 = 5;
    protected static final int TAKE_SUSPENSE_PIC2 = 6;
    protected static final int SELECT_SUSPENSE_PIC1 = 7;
    protected static final int SELECT_SUSPENSE_PIC2 = 8;

    protected static final int TAKE_RIMS_PIC1 = 9;
    protected static final int TAKE_RIMS_PIC2 = 10;
    protected static final int SELECT_RIMS_PIC1 = 11;
    protected static final int SELECT_RIMS_PIC2 = 12;


    protected static final int TAKE_WHEELS_PIC1 = 13;
    protected static final int TAKE_WHEELS_PIC2 = 14;
    protected static final int SELECT_WHEELS_PIC1 = 15;
    protected static final int SELECT_WHEELS_PIC2 = 16;


    protected static final int TAKE_OTHERS_PIC1 = 17;
    protected static final int TAKE_OTHERS_PIC2 = 18;
    protected static final int SELECT_OTHERS_PIC1 = 19;
    protected static final int SELECT_OTHERS_PIC2 = 20;



    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rimwheel, container, false);
        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getActivity());
        edit = sharedPrefces.edit();

        cameraDialog = new Dialog(getActivity());
        cameraDialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);


        rim_box1=view.findViewById(R.id.rim_box1);
        rim_box2=view.findViewById(R.id.rim_box2);
        rim_box3=view.findViewById(R.id.rim_box3);
        rim_box4=view.findViewById(R.id.rim_box4);
        rim_others=view.findViewById(R.id.rim_others);

        //@@@@@@@@@@@@@@@@ BRAKE FindviewbyId
        break_pic1_iv=view.findViewById(R.id.break_pic1_iv);
        break_plus1=view.findViewById(R.id.break_plus1);
        chk_break1=view.findViewById(R.id.chk_break1);
        break_pic2_iv=view.findViewById(R.id.break_pic2_iv);
        break_plus2=view.findViewById(R.id.break_plus2);
        chk_break2=view.findViewById(R.id.chk_break2);


        chk_break1.setVisibility(View.GONE);
        chk_break2.setVisibility(View.GONE);


        //@@@@@@@@@@@@@@@@ SUSPENSIONS FindviewbyId
        suspense_pic1_iv=view.findViewById(R.id.suspense_pic1_iv);
        suspense_plus1=view.findViewById(R.id.suspense_plus1);
        chk_suspense1=view.findViewById(R.id.chk_suspense1);
        suspense_pic2_iv=view.findViewById(R.id.suspense_pic2_iv);
        suspense_plus2=view.findViewById(R.id.suspense_plus2);
        chk_suspense2=view.findViewById(R.id.chk_suspense2);


        chk_suspense1.setVisibility(View.GONE);
        chk_suspense2.setVisibility(View.GONE);

        //@@@@@@@@@@@@@@@@ RIMS FindviewbyId:
        rims_pic1_iv=view.findViewById(R.id.rims_pic1_iv);
        rims_plus1=view.findViewById(R.id.rims_plus1);
        chk_rims1=view.findViewById(R.id.chk_rims1);
        rims_pic2_iv=view.findViewById(R.id.rims_pic2_iv);
        rims_plus2=view.findViewById(R.id.rims_plus2);
        chk_rims2=view.findViewById(R.id.chk_rims2);

        chk_rims1.setVisibility(View.GONE);
        chk_rims2.setVisibility(View.GONE);

        //@@@@@@@@@@@@@@@@ WHEELS FindviewbyId:
        wheels_pic1_iv=view.findViewById(R.id.wheels_pic1_iv);
        wheels_plus1=view.findViewById(R.id.wheels_plus1);
        chk_wheels1=view.findViewById(R.id.chk_wheels1);
        wheels_pic2_iv=view.findViewById(R.id.wheels_pic2_iv);
        wheels_plus2=view.findViewById(R.id.wheels_plus2);
        chk_wheels2=view.findViewById(R.id.chk_wheels2);

        chk_wheels1.setVisibility(View.GONE);
        chk_wheels2.setVisibility(View.GONE);

        //@@@@@@@@@@@@@@@@ OTHERS FindviewbyId:
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
        //get image Interior path value:
        path_brake1=sharedPreferences.getString("store_brake1","");
        path_brake2=sharedPreferences.getString("store_brake2","");
        path_suspension1=sharedPreferences.getString("store_suspension1","");
        path_suspension2=sharedPreferences.getString("store_suspension2","");
        path_rims1=sharedPreferences.getString("store_rims1","");
        path_rims2=sharedPreferences.getString("store_rims2","");
        path_wheels1=sharedPreferences.getString("store_wheel1","");
        path_wheels2=sharedPreferences.getString("store_wheel2","");
        path_wheel_other1=sharedPreferences.getString("store_wheelother1","");
        path_wheel_other2=sharedPreferences.getString("store_wheelother2","");


        if(path_brake1!=null) {
            chk_break1.setChecked(true);
            File imgFile = new File(path_brake1);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                chk_break1.setVisibility(View.VISIBLE);
                break_pic1_iv.setImageBitmap(myBitmap);
                break_plus1.setVisibility(View.GONE);
            }
        }

        if(path_brake2!=null) {
            chk_break2.setChecked(true);
            File imgFile = new File(path_brake2);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                chk_break2.setVisibility(View.VISIBLE);
                break_pic2_iv.setImageBitmap(myBitmap);
                break_plus2.setVisibility(View.GONE);
            }
        }


        if(path_suspension1!=null) {
            chk_suspense1.setChecked(true);
            File imgFile = new File(path_suspension1);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                chk_suspense1.setVisibility(View.VISIBLE);
                suspense_pic1_iv.setImageBitmap(myBitmap);
                suspense_plus1.setVisibility(View.GONE);
            }
        }


        if(path_suspension2!=null) {
            chk_suspense2.setChecked(true);
            File imgFile = new File(path_suspension2);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                chk_suspense2.setVisibility(View.VISIBLE);
                suspense_pic2_iv.setImageBitmap(myBitmap);
                suspense_plus2.setVisibility(View.GONE);
            }
        }


        if(path_rims1!=null) {
            chk_rims1.setChecked(true);
            File imgFile = new File(path_rims1);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                chk_rims1.setVisibility(View.VISIBLE);
                rims_pic1_iv.setImageBitmap(myBitmap);
                rims_plus1.setVisibility(View.GONE);
            }
        }


        if(path_rims2!=null) {
            chk_rims2.setChecked(true);
            File imgFile = new File(path_rims2);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                chk_rims2.setVisibility(View.VISIBLE);
                rims_pic2_iv.setImageBitmap(myBitmap);
                rims_plus2.setVisibility(View.GONE);
            }
        }


        if(path_wheels1!=null) {
            chk_wheels1.setChecked(true);
            File imgFile = new File(path_wheels1);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                chk_wheels1.setVisibility(View.VISIBLE);
                wheels_pic1_iv.setImageBitmap(myBitmap);
                wheels_plus1.setVisibility(View.GONE);
            }
        }


        if(path_wheels2!=null) {
            chk_wheels2.setChecked(true);
            File imgFile = new File(path_wheels2);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                chk_wheels2.setVisibility(View.VISIBLE);
                wheels_pic2_iv.setImageBitmap(myBitmap);
                wheels_plus2.setVisibility(View.GONE);
            }
        }

        if(path_wheel_other1!=null) {
            chk_others1.setChecked(true);
            File imgFile = new File(path_wheel_other1);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                chk_others1.setVisibility(View.VISIBLE);
                others_pic1_iv.setImageBitmap(myBitmap);
                others_plus1.setVisibility(View.GONE);
            }
        }


        if(path_wheel_other2!=null) {
            chk_others2.setChecked(true);
            File imgFile = new File(path_wheel_other2);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                chk_others2.setVisibility(View.VISIBLE);
                others_pic2_iv.setImageBitmap(myBitmap);
                others_plus2.setVisibility(View.GONE);
            }
        }


        break_pic1_iv.setOnClickListener(this);
        break_pic2_iv.setOnClickListener(this);
        suspense_pic1_iv.setOnClickListener(this);
        suspense_pic2_iv.setOnClickListener(this);
        rims_pic1_iv.setOnClickListener(this);
        rims_pic2_iv.setOnClickListener(this);
        wheels_pic1_iv.setOnClickListener(this);
        wheels_pic2_iv.setOnClickListener(this);
        others_pic1_iv.setOnClickListener(this);
        others_pic2_iv.setOnClickListener(this);


        //BRAKE Checkbox1 Condition--------------------------------------------------->
        chk_break1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!chk_break1.isChecked()){
                    edit.remove("store_brake1");
                    edit.commit();
                    chk_break1.setChecked(false);
                    chk_break1.setVisibility(View.GONE);
                    break_pic1_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    break_pic1_iv.setEnabled(true);
                    break_plus1.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_break1.setChecked(true);
                    chk_break1.setVisibility(View.VISIBLE);
                    break_pic1_iv.setEnabled(false);
                    break_plus1.setVisibility(View.GONE);
                }
            }
        });



        //BRAKE Checkbox2 Condition--------------------------------------------------->
        chk_break2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!chk_break2.isChecked()){
                    edit.remove("store_brake2");
                    edit.commit();
                    chk_break2.setChecked(false);
                    chk_break2.setVisibility(View.GONE);
                    break_pic2_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    break_pic2_iv.setEnabled(true);
                    break_plus2.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_break2.setChecked(true);
                    chk_break2.setVisibility(View.VISIBLE);
                    break_pic2_iv.setEnabled(false);
                    break_plus2.setVisibility(View.GONE);
                }
            }
        });




        //SUSPENSIONS Checkbox1 Condition--------------------------------------------------->
        chk_suspense1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!chk_suspense1.isChecked()){
                    edit.remove("store_suspension1");
                    edit.commit();
                    chk_suspense1.setChecked(false);
                    chk_suspense1.setVisibility(View.GONE);
                    suspense_pic1_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    suspense_pic1_iv.setEnabled(true);
                    suspense_plus1.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_suspense1.setChecked(true);
                    chk_suspense1.setVisibility(View.VISIBLE);
                    suspense_pic1_iv.setEnabled(false);
                    suspense_plus1.setVisibility(View.GONE);
                }
            }
        });




        //SUSPENSIONS Checkbox2 Condition--------------------------------------------------->
        chk_suspense2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!chk_suspense2.isChecked()){
                    edit.remove("store_suspension2");
                    edit.commit();
                    chk_suspense2.setChecked(false);
                    chk_suspense2.setVisibility(View.GONE);
                    suspense_pic2_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    suspense_pic2_iv.setEnabled(true);
                    suspense_plus2.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_suspense2.setChecked(true);
                    chk_suspense2.setVisibility(View.VISIBLE);
                    suspense_pic2_iv.setEnabled(false);
                    suspense_plus2.setVisibility(View.GONE);
                }
            }
        });



        //RIMS Checkbox1 Condition--------------------------------------------------->
        chk_rims1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if(!chk_rims1.isChecked()){
                    edit.remove("store_rims1");
                    edit.commit();
                    chk_rims1.setChecked(false);
                    chk_rims1.setVisibility(View.GONE);
                    rims_pic1_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    rims_pic1_iv.setEnabled(true);
                    rims_plus1.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_rims1.setChecked(true);
                    chk_rims1.setVisibility(View.VISIBLE);
                    rims_pic1_iv.setEnabled(false);
                    rims_plus1.setVisibility(View.GONE);
                }
            }
        });


        //RIMS Checkbox2 Condition--------------------------------------------------->
        chk_rims2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!chk_rims2.isChecked()){
                    edit.remove("store_rims2");
                    edit.commit();
                    chk_rims2.setChecked(false);
                    chk_rims2.setVisibility(View.GONE);
                    rims_pic2_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    rims_pic2_iv.setEnabled(true);
                    rims_plus2.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_rims2.setChecked(true);
                    chk_rims2.setVisibility(View.VISIBLE);
                    rims_pic2_iv.setEnabled(false);
                    rims_plus2.setVisibility(View.GONE);
                }
            }
        });


        //WHEELS Checkbox1 Condition--------------------------------------------------->
        chk_wheels1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!chk_wheels1.isChecked()){
                    edit.remove("store_wheel1");
                    edit.commit();
                    chk_wheels1.setChecked(false);
                    chk_wheels1.setVisibility(View.GONE);
                    wheels_pic1_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    wheels_pic1_iv.setEnabled(true);
                    wheels_plus1.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_wheels1.setChecked(true);
                    chk_wheels1.setVisibility(View.VISIBLE);
                    wheels_pic1_iv.setEnabled(false);
                    wheels_plus1.setVisibility(View.GONE);
                }
            }
        });


        //WHEELS Checkbox2 Condition--------------------------------------------------->
        chk_wheels2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!chk_wheels2.isChecked()){
                    edit.remove("store_wheel2");
                    edit.commit();
                    chk_wheels2.setChecked(false);
                    chk_wheels2.setVisibility(View.GONE);
                    wheels_pic2_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    wheels_pic2_iv.setEnabled(true);
                    wheels_plus2.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_wheels2.setChecked(true);
                    chk_wheels2.setVisibility(View.VISIBLE);
                    wheels_pic2_iv.setEnabled(false);
                    wheels_plus2.setVisibility(View.GONE);
                }
            }
        });


        //Others Checkbox1 Condition--------------------------------------------------->
        chk_others1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!chk_others1.isChecked()){
                    edit.remove("store_wheelother1");
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
                    edit.remove("store_wheelother1");
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

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            // BRAKE PIC ONE
            case R.id.break_pic1_iv:


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
                            startActivityForResult(cameraIntent, TAKE_BREAK_PIC1);
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
                            startActivityForResult(pickIntent, SELECT_BREAK_PIC1);
                        }


                    }
                });
                cameraDialog.show();

                break;


            // BRAKE PIC TWO
            case R.id.break_pic2_iv:

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
                            startActivityForResult(cameraIntent, TAKE_BREAK_PIC2);
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
                            startActivityForResult(pickIntent, SELECT_BREAK_PIC2);
                        }
                    }
                });
                cameraDialog.show();

                break;







            // SUSPENSION PIC ONE
            case R.id.suspense_pic1_iv:

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
                            startActivityForResult(cameraIntent, TAKE_SUSPENSE_PIC1);
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
                            startActivityForResult(pickIntent, SELECT_SUSPENSE_PIC1);
                        }


                    }
                });
                cameraDialog.show();

                break;






            // SUSPENSION PIC TWO
            case R.id.suspense_pic2_iv:

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
                            startActivityForResult(cameraIntent, TAKE_SUSPENSE_PIC2);
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
                            startActivityForResult(pickIntent, SELECT_SUSPENSE_PIC2);
                        }

                    }
                });
                cameraDialog.show();
                break;






            // RIMS PIC ONE
            case R.id.rims_pic1_iv:

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
                            startActivityForResult(cameraIntent, TAKE_RIMS_PIC1);
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
                            startActivityForResult(pickIntent, SELECT_RIMS_PIC1);
                        }


                    }
                });
                cameraDialog.show();

                break;





            // RIMS PIC TWO
            case R.id.rims_pic2_iv:

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
                            startActivityForResult(cameraIntent, TAKE_RIMS_PIC2);
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
                            startActivityForResult(pickIntent, SELECT_RIMS_PIC2);
                        }


                    }
                });
                cameraDialog.show();

                break;





            // WHEELS PIC TWO
            case R.id.wheels_pic1_iv:

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
                            startActivityForResult(cameraIntent, TAKE_WHEELS_PIC1);
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
                            startActivityForResult(pickIntent, SELECT_WHEELS_PIC1);
                        }
                    }
                });
                cameraDialog.show();
                break;




            // WHEELS PIC TWO
            case R.id.wheels_pic2_iv:

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
                            startActivityForResult(cameraIntent, TAKE_WHEELS_PIC2);
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
                            startActivityForResult(pickIntent, SELECT_WHEELS_PIC2);
                        }
                    }
                });
                cameraDialog.show();
                break;



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



            // OTHERS PIC TWO
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

        //********************BRAKE PIC 1 -------TAKE
        if (resultCode == RESULT_OK && requestCode == TAKE_BREAK_PIC1) {

            bitmap = (Bitmap) data.getExtras().get("data");
            File sd = Environment.getExternalStorageDirectory();
            File imagepath = new File(sd.getAbsolutePath() + File.separator +
                    "FolderName" + File.separator + "InsideFolderName");

            if (!imagepath.isDirectory())
            {
                imagepath.mkdirs();
            }

            File mediaFile2 = new File(imagepath + File.separator + "img_" +
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(mediaFile2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
                break_pic1_iv.setImageBitmap(bitmap);
                chk_break1.setChecked(true);
                chk_break1.setVisibility(View.VISIBLE);
                break_plus1.setVisibility(View.GONE);

                path_brake1=mediaFile2.getAbsolutePath();
                edit.putString("store_brake1",path_brake1);
                edit.commit();





        }








        //********************BRAKE PIC 2 -------TAKE
        else if (resultCode == RESULT_OK && requestCode == TAKE_BREAK_PIC2){

            bitmap = (Bitmap) data.getExtras().get("data");
            File sd = Environment.getExternalStorageDirectory();
            File imagepath = new File(sd.getAbsolutePath() + File.separator +
                    "FolderName" + File.separator + "InsideFolderName");

            if (!imagepath.isDirectory())
            {
                imagepath.mkdirs();
            }

            File mediaFile2 = new File(imagepath + File.separator + "img_" +
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(mediaFile2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
                break_pic2_iv.setImageBitmap(bitmap);
                chk_break2.setChecked(true);
                chk_break2.setVisibility(View.VISIBLE);
                break_plus2.setVisibility(View.GONE);

                path_brake2=mediaFile2.getAbsolutePath();
                edit.putString("store_brake2",path_brake2);
                edit.commit();





        }








        //********************BRAKE PIC 1 -------SELECT

        else if (resultCode == RESULT_OK && requestCode == SELECT_BREAK_PIC1){
            try {
                Uri selectedMediaUri = data.getData();
                path_brake1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    break_pic1_iv.setImageBitmap(bitmap);
                    chk_break1.setChecked(true);
                    chk_break1.setVisibility(View.VISIBLE);
                    break_plus1.setVisibility(View.GONE);
                    edit.putString("store_brake1", path_brake1);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }

        }








        //********************BRAKE PIC 2 -------TAKE
        else if (resultCode == RESULT_OK && requestCode == SELECT_BREAK_PIC2){
            try {
                Uri selectedMediaUri = data.getData();
                path_brake2 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    break_pic2_iv.setImageBitmap(bitmap);
                    chk_break2.setChecked(true);
                    chk_break2.setVisibility(View.VISIBLE);
                    break_plus2.setVisibility(View.GONE);
                    edit.putString("store_brake2", path_brake2);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }
        }






        //********************SUSPENSION PIC 1 -------TAKE

        if (resultCode == RESULT_OK && requestCode == TAKE_SUSPENSE_PIC1) {

            bitmap = (Bitmap) data.getExtras().get("data");
            File sd = Environment.getExternalStorageDirectory();
            File imagepath = new File(sd.getAbsolutePath() + File.separator +
                    "FolderName" + File.separator + "InsideFolderName");

            if (!imagepath.isDirectory())
            {
                imagepath.mkdirs();
            }

            File mediaFile2 = new File(imagepath + File.separator + "img_" +
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(mediaFile2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            suspense_pic1_iv.setImageBitmap(bitmap);
                chk_suspense1.setChecked(true);
                chk_suspense1.setVisibility(View.VISIBLE);
                suspense_plus1.setVisibility(View.GONE);


                path_suspension1=mediaFile2.getAbsolutePath();
                edit.putString("store_suspension1",path_suspension1);
                edit.commit();





        }








        //********************SUSPENSION PIC 2 -------TAKE
        else if (resultCode == RESULT_OK && requestCode == TAKE_SUSPENSE_PIC2){

            bitmap = (Bitmap) data.getExtras().get("data");
            File sd = Environment.getExternalStorageDirectory();
            File imagepath = new File(sd.getAbsolutePath() + File.separator +
                    "FolderName" + File.separator + "InsideFolderName");

            if (!imagepath.isDirectory())
            {
                imagepath.mkdirs();
            }

            File mediaFile2 = new File(imagepath + File.separator + "img_" +
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(mediaFile2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
                suspense_pic2_iv.setImageBitmap(bitmap);
                chk_suspense2.setChecked(true);
                chk_suspense2.setVisibility(View.VISIBLE);
                suspense_plus2.setVisibility(View.GONE);

                path_suspension2=mediaFile2.getAbsolutePath();
                edit.putString("store_suspension2",path_suspension2);
                edit.commit();

        }









        //********************SUSPENSION PIC 1 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_SUSPENSE_PIC1){
            try {
                Uri selectedMediaUri = data.getData();
                path_suspension1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    suspense_pic1_iv.setImageBitmap(bitmap);
                    chk_suspense1.setChecked(true);
                    chk_suspense1.setVisibility(View.VISIBLE);
                    suspense_plus1.setVisibility(View.GONE);
                    edit.putString("store_suspension1", path_suspension1);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }

        }








        //********************SUSPENSION PIC 2 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_SUSPENSE_PIC2){
            try {
                Uri selectedMediaUri = data.getData();
                path_suspension2 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    suspense_pic2_iv.setImageBitmap(bitmap);
                    chk_suspense2.setChecked(true);
                    chk_suspense2.setVisibility(View.VISIBLE);
                    suspense_plus2.setVisibility(View.GONE);
                    edit.putString("store_suspension2", path_suspension2);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }
        }







        //********************RIMS PIC 1 -------TAKE
        if (resultCode == RESULT_OK && requestCode == TAKE_RIMS_PIC1) {

            bitmap = (Bitmap) data.getExtras().get("data");
            File sd = Environment.getExternalStorageDirectory();
            File imagepath = new File(sd.getAbsolutePath() + File.separator +
                    "FolderName" + File.separator + "InsideFolderName");

            if (!imagepath.isDirectory())
            {
                imagepath.mkdirs();
            }

            File mediaFile2 = new File(imagepath + File.separator + "img_" +
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(mediaFile2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
                rims_pic1_iv.setImageBitmap(bitmap);
                chk_rims1.setChecked(true);
                chk_rims1.setVisibility(View.VISIBLE);
                rims_plus1.setVisibility(View.GONE);


                path_rims1=mediaFile2.getAbsolutePath();
                edit.putString("store_rims1",path_rims1);
                edit.commit();

        }






        //********************RIMS PIC 2 -------TAKE
        if (resultCode == RESULT_OK && requestCode == TAKE_RIMS_PIC2) {

            bitmap = (Bitmap) data.getExtras().get("data");
            File sd = Environment.getExternalStorageDirectory();
            File imagepath = new File(sd.getAbsolutePath() + File.separator +
                    "FolderName" + File.separator + "InsideFolderName");

            if (!imagepath.isDirectory())
            {
                imagepath.mkdirs();
            }

            File mediaFile2 = new File(imagepath + File.separator + "img_" +
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(mediaFile2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


                rims_pic2_iv.setImageBitmap(bitmap);
                chk_rims2.setChecked(true);
                chk_rims2.setVisibility(View.VISIBLE);
                rims_plus2.setVisibility(View.GONE);


                path_rims2=mediaFile2.getAbsolutePath();
                edit.putString("store_rims2",path_rims2);
                edit.commit();





        }






        //********************RIMS PIC 1 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_RIMS_PIC1){
            try {
                Uri selectedMediaUri = data.getData();
                path_rims1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    rims_pic1_iv.setImageBitmap(bitmap);
                    chk_rims1.setChecked(true);
                    chk_rims1.setVisibility(View.VISIBLE);
                    rims_plus1.setVisibility(View.GONE);
                    edit.putString("store_rims1", path_rims1);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }

        }








        //********************RIMS PIC 2 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_RIMS_PIC2){
            try {
                Uri selectedMediaUri = data.getData();
                path_rims2 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    rims_pic2_iv.setImageBitmap(bitmap);
                    chk_rims2.setChecked(true);
                    chk_rims2.setVisibility(View.VISIBLE);
                    rims_plus2.setVisibility(View.GONE);
                    edit.putString("store_rims2", path_rims2);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }
        }







        //********************WHEELS PIC 1 -------TAKE
        if (resultCode == RESULT_OK && requestCode == TAKE_WHEELS_PIC1) {

            bitmap = (Bitmap) data.getExtras().get("data");
            File sd = Environment.getExternalStorageDirectory();
            File imagepath = new File(sd.getAbsolutePath() + File.separator +
                    "FolderName" + File.separator + "InsideFolderName");

            if (!imagepath.isDirectory())
            {
                imagepath.mkdirs();
            }

            File mediaFile2 = new File(imagepath + File.separator + "img_" +
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(mediaFile2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
                wheels_pic1_iv.setImageBitmap(bitmap);
                chk_wheels1.setChecked(true);
                chk_wheels1.setVisibility(View.VISIBLE);
                wheels_plus1.setVisibility(View.GONE);

                path_wheels1=mediaFile2.getAbsolutePath();
                edit.putString("store_wheel1",path_wheels1);
                edit.commit();


        }






        //********************WHEELS PIC 2 -------TAKE
        if (resultCode == RESULT_OK && requestCode == TAKE_WHEELS_PIC2) {

            bitmap = (Bitmap) data.getExtras().get("data");
            File sd = Environment.getExternalStorageDirectory();
            File imagepath = new File(sd.getAbsolutePath() + File.separator +
                    "FolderName" + File.separator + "InsideFolderName");

            if (!imagepath.isDirectory())
            {
                imagepath.mkdirs();
            }

            File mediaFile2 = new File(imagepath + File.separator + "img_" +
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(mediaFile2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


                wheels_pic2_iv.setImageBitmap(bitmap);
                chk_wheels2.setChecked(true);
                chk_wheels2.setVisibility(View.VISIBLE);
                wheels_plus2.setVisibility(View.GONE);

                path_wheels2=mediaFile2.getAbsolutePath();
                edit.putString("store_wheel2",path_wheels2);
                edit.commit();





        }






        //********************WHEELS PIC 1 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_WHEELS_PIC1){
            try {
                Uri selectedMediaUri = data.getData();
                path_wheels1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    wheels_pic1_iv.setImageBitmap(bitmap);
                    chk_wheels1.setChecked(true);
                    chk_wheels1.setVisibility(View.VISIBLE);
                    wheels_plus1.setVisibility(View.GONE);
                    edit.putString("store_wheel1", path_wheels1);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }


        }



        //********************WHEELS PIC 2 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_WHEELS_PIC2){
            try {
                Uri selectedMediaUri = data.getData();
                path_wheels2 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    wheels_pic2_iv.setImageBitmap(bitmap);
                    chk_wheels2.setChecked(true);
                    chk_wheels2.setVisibility(View.VISIBLE);
                    wheels_plus2.setVisibility(View.GONE);
                    edit.putString("store_wheel2", path_wheels2);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }
        }






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

            File mediaFile2 = new File(imagepath + File.separator + "img_" +
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(mediaFile2);
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

                path_wheel_other1=mediaFile2.getAbsolutePath();
                edit.putString("store_wheelother1",path_wheel_other1);
                edit.commit();


        }






        //********************OTHERS PIC 2 -------TAKE
        if (resultCode == RESULT_OK && requestCode == TAKE_OTHERS_PIC2) {

            bitmap = (Bitmap) data.getExtras().get("data");
            File sd = Environment.getExternalStorageDirectory();
            File imagepath = new File(sd.getAbsolutePath() + File.separator +
                    "FolderName" + File.separator + "InsideFolderName");

            if (!imagepath.isDirectory())
            {
                imagepath.mkdirs();
            }

            File mediaFile2 = new File(imagepath + File.separator + "img_" +
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(mediaFile2);
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
                path_wheel_other2=mediaFile2.getAbsolutePath();
                edit.putString("store_wheelother2",path_wheel_other2);
                edit.commit();





        }






        //********************OTHERS PIC 1 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_OTHERS_PIC1){

            try {
                Uri selectedMediaUri = data.getData();
                path_wheel_other1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    others_pic1_iv.setImageBitmap(bitmap);
                    chk_others1.setChecked(true);
                    chk_others1.setVisibility(View.VISIBLE);
                    others_plus1.setVisibility(View.GONE);
                    edit.putString("store_wheelother1", path_wheel_other1);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }
        }



        //********************OTHERS PIC 2 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_OTHERS_PIC2){
            try {
                Uri selectedMediaUri = data.getData();
                path_wheel_other2 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    others_pic2_iv.setImageBitmap(bitmap);
                    chk_others2.setChecked(true);
                    chk_others2.setVisibility(View.VISIBLE);
                    others_plus2.setVisibility(View.GONE);
                    edit.putString("store_wheelother2", path_wheel_other2);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }
        }
    }
}