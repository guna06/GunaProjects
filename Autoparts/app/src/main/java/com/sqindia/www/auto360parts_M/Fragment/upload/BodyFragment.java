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

public class BodyFragment extends Fragment implements View.OnClickListener{
    ImageView doors_pic1_iv,doors_pic2_iv,bumpers_pic1_iv,bumpers_pic2_iv,hoods_pic1_iv,hoods_pic2_iv,others_pic1_iv,others_pic2_iv;
    ImageView doors_plus1,doors_plus2,bumpers_plus1,bumpers_plus2,hoods_plus1,hoods_plus2,others_plus1,others_plus2;
    CheckBox chk_doors1,chk_doors2,chk_bumpers1,chk_bumpers2,chk_hoods1,chk_hoods2,chk_others1,chk_others2;
    Dialog cameraDialog;
    LinearLayout lnr_takepic, lnr_takegallery;
    Bitmap bitmap;
    String selectedImagePath;
    public static TextView body_box1,body_box2,body_box3;
    public static EditText body_others;
    String path_doors1,path_doors2,path_bumpers1,path_bumpers2,path_hoods1,path_hoods2,path_body_other1,path_body_other2;
    String path_redoors1,path_redoors2,path_rebumpers1,path_rebumpers2,path_rehoods1,path_rehoods2,path_rebody_other1,path_rebody_other2;
    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;

    protected static final int TAKE_DOORS_PIC1 = 1;
    protected static final int TAKE_DOORS_PIC2 = 2;
    protected static final int SELECT_DOORS_PIC1 = 3;
    protected static final int SELECT_DOORS_PIC2 = 4;

    protected static final int TAKE_BUMPERS_PIC1 = 5;
    protected static final int TAKE_BUMPERS_PIC2 = 6;
    protected static final int SELECT_BUMPERS_PIC1 = 7;
    protected static final int SELECT_BUMPERS_PIC2 = 8;

    protected static final int TAKE_HOODS_PIC1 = 9;
    protected static final int TAKE_HOODS_PIC2 = 10;
    protected static final int SELECT_HOODS_PIC1 = 11;
    protected static final int SELECT_HOODS_PIC2 = 12;

    protected static final int TAKE_OTHERS_PIC1 = 17;
    protected static final int TAKE_OTHERS_PIC2 = 18;
    protected static final int SELECT_OTHERS_PIC1 = 19;
    protected static final int SELECT_OTHERS_PIC2 = 20;



    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_body, container, false);
        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getActivity());
        edit = sharedPrefces.edit();


        cameraDialog = new Dialog(getActivity());
        cameraDialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

        body_box1=view.findViewById(R.id.body_box1);
        body_box2=view.findViewById(R.id.body_box2);
        body_box3=view.findViewById(R.id.body_box3);
        body_others=view.findViewById(R.id.body_others);



        //@@@@@@@@@@@@@@@@ Engine FindviewbyId
        doors_pic1_iv=view.findViewById(R.id.doors_pic1_iv);
        doors_pic2_iv=view.findViewById(R.id.doors_pic2_iv);
        doors_plus1=view.findViewById(R.id.doors_plus1);
        doors_plus2=view.findViewById(R.id.doors_plus2);
        chk_doors1=view.findViewById(R.id.chk_doors1);
        chk_doors2=view.findViewById(R.id.chk_doors2);

        chk_doors1.setVisibility(View.GONE);
        chk_doors2.setVisibility(View.GONE);


        //@@@@@@@@@@@@@@@@ A/C FindviewbyId
        bumpers_pic1_iv=view.findViewById(R.id.bumpers_pic1_iv);
        bumpers_pic2_iv=view.findViewById(R.id.bumpers_pic2_iv);
        bumpers_plus1=view.findViewById(R.id.bumpers_plus1);
        bumpers_plus2=view.findViewById(R.id.bumpers_plus2);
        chk_bumpers1=view.findViewById(R.id.chk_bumpers1);
        chk_bumpers2=view.findViewById(R.id.chk_bumpers2);

        chk_bumpers1.setVisibility(View.GONE);
        chk_bumpers2.setVisibility(View.GONE);


        //@@@@@@@@@@@@@@@@ Emmission FindviewbyId
        hoods_pic1_iv=view.findViewById(R.id.hoods_pic1_iv);
        hoods_pic2_iv=view.findViewById(R.id.hoods_pic2_iv);
        hoods_plus1=view.findViewById(R.id.hoods_plus1);
        hoods_plus2=view.findViewById(R.id.hoods_plus2);
        chk_hoods1=view.findViewById(R.id.chk_hoods1);
        chk_hoods2=view.findViewById(R.id.chk_hoods2);

        chk_hoods1.setVisibility(View.GONE);
        chk_hoods2.setVisibility(View.GONE);


        //@@@@@@@@@@@@@@@@ OTHERS FindviewbyId
        others_pic1_iv=view.findViewById(R.id.others_pic1_iv);
        others_pic2_iv=view.findViewById(R.id.others_pic2_iv);
        others_plus1=view.findViewById(R.id.others_plus1);
        others_plus2=view.findViewById(R.id.others_plus2);
        chk_others1=view.findViewById(R.id.chk_others1);
        chk_others2=view.findViewById(R.id.chk_others2);


        chk_others1.setVisibility(View.GONE);
        chk_others2.setVisibility(View.GONE);



        doors_pic1_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.camera_box));
        doors_pic2_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.camera_box));
        bumpers_pic1_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.camera_box));
        bumpers_pic2_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.camera_box));
        hoods_pic1_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.camera_box));
        hoods_pic2_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.camera_box));
        others_pic1_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.camera_box));
        others_pic2_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.camera_box));



        chk_doors1.setChecked(false);
        chk_doors2.setChecked(false);
        chk_bumpers1.setChecked(false);
        chk_bumpers2.setChecked(false);
        chk_hoods1.setChecked(false);
        chk_hoods2.setChecked(false);
        chk_others1.setChecked(false);
        chk_others2.setChecked(false);



       final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        edit = sharedPreferences.edit();
        //get image Body path value:
        path_redoors1=sharedPreferences.getString("store_doors1","");
        path_redoors2=sharedPreferences.getString("store_doors2","");
        path_rebumpers1=sharedPreferences.getString("store_bumpers1","");
        path_rebumpers2=sharedPreferences.getString("store_bumpers2","");
        path_rehoods1=sharedPreferences.getString("store_hoods1","");
        path_rehoods2=sharedPreferences.getString("store_hoods2","");
        path_rebody_other1=sharedPreferences.getString("store_bodyother1","");
        path_rebody_other2=sharedPreferences.getString("store_bodyother2","");




        if(path_redoors1!=null) {
            chk_doors1.setChecked(true);
            File imgFile1 = new File(path_redoors1);

            if (imgFile1.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile1.getAbsolutePath());
                chk_doors1.setVisibility(View.VISIBLE);
                doors_pic1_iv.setImageBitmap(myBitmap);
                doors_plus1.setVisibility(View.GONE);
            }
        }

        if(path_redoors2!=null) {
            chk_doors2.setChecked(true);
            File imgFile2 = new File(path_redoors2);

            if (imgFile2.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile2.getAbsolutePath());
                chk_doors2.setVisibility(View.VISIBLE);
                doors_pic2_iv.setImageBitmap(myBitmap);
                doors_plus2.setVisibility(View.GONE);
            }
        }


        if(path_rebumpers1!=null) {
            chk_bumpers1.setChecked(true);
            File imgFile3 = new File(path_rebumpers1);

            if (imgFile3.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile3.getAbsolutePath());
                chk_bumpers1.setVisibility(View.VISIBLE);
                bumpers_pic1_iv.setImageBitmap(myBitmap);
                bumpers_plus1.setVisibility(View.GONE);
            }
        }



        if(path_rebumpers2!=null) {
            chk_bumpers2.setChecked(true);
            File imgFile4 = new File(path_rebumpers2);

            if (imgFile4.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile4.getAbsolutePath());
                chk_bumpers2.setVisibility(View.VISIBLE);
                bumpers_pic2_iv.setImageBitmap(myBitmap);
                bumpers_plus2.setVisibility(View.GONE);
            }
        }


        if(path_rehoods1!=null) {
            chk_hoods1.setChecked(true);
            File imgFile5 = new File(path_rehoods1);

            if (imgFile5.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile5.getAbsolutePath());
                chk_hoods1.setVisibility(View.VISIBLE);
                hoods_pic1_iv.setImageBitmap(myBitmap);
                hoods_plus1.setVisibility(View.GONE);
            }
        }



        if(path_rehoods2!=null) {
            chk_hoods2.setChecked(true);
            File imgFile6 = new File(path_rehoods2);

            if (imgFile6.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile6.getAbsolutePath());
                chk_hoods2.setVisibility(View.VISIBLE);
                hoods_pic2_iv.setImageBitmap(myBitmap);
                hoods_plus2.setVisibility(View.GONE);
            }
        }


        if(path_rebody_other1!=null) {
            chk_others1.setChecked(true);
            File imgFile7 = new File(path_rebody_other1);

            if (imgFile7.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile7.getAbsolutePath());
                chk_others1.setVisibility(View.VISIBLE);
                others_pic1_iv.setImageBitmap(myBitmap);
                others_plus1.setVisibility(View.GONE);
            }
        }


        if(path_rebody_other2!=null) {
            chk_others2.setChecked(true);
            File imgFile8 = new File(path_rebody_other2);

            if (imgFile8.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile8.getAbsolutePath());
                chk_others2.setVisibility(View.VISIBLE);
                others_pic2_iv.setImageBitmap(myBitmap);
                others_plus2.setVisibility(View.GONE);
            }
        }



        //Doors Checkbox1 Condition--------------------------------------------------->
        chk_doors1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!chk_doors1.isChecked()){
                    edit.remove("store_doors1");
                    edit.commit();
                    chk_doors1.setChecked(false);
                    chk_doors1.setVisibility(View.GONE);
                    doors_pic1_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    doors_pic1_iv.setEnabled(true);
                    doors_plus1.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_doors1.setChecked(true);
                    chk_doors1.setVisibility(View.VISIBLE);
                    doors_pic1_iv.setEnabled(false);
                    doors_plus1.setVisibility(View.GONE);
                }

            }
        });



        //Doors Checkbox2 Condition--------------------------------------------------->
        chk_doors2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if(!chk_doors2.isChecked()){
                    edit.remove("store_doors2");
                    edit.commit();
                    chk_doors2.setChecked(false);
                    chk_doors2.setVisibility(View.GONE);
                    doors_pic2_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    doors_pic2_iv.setEnabled(true);
                    doors_plus2.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_doors2.setChecked(true);
                    chk_doors2.setVisibility(View.VISIBLE);
                    doors_pic2_iv.setEnabled(false);
                    doors_plus2.setVisibility(View.GONE);
                }
            }
        });





        //BUMPERS Checkbox1 Condition--------------------------------------------------->
        chk_bumpers1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if(!chk_bumpers1.isChecked()){
                    edit.remove("store_bumpers1");
                    edit.commit();
                    chk_bumpers1.setChecked(false);
                    chk_bumpers1.setVisibility(View.GONE);
                    bumpers_pic1_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    bumpers_pic1_iv.setEnabled(true);
                    bumpers_plus1.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_bumpers1.setChecked(true);
                    chk_bumpers1.setVisibility(View.VISIBLE);
                    bumpers_pic1_iv.setEnabled(false);
                    bumpers_plus1.setVisibility(View.GONE);
                }

            }
        });




        //BUMPERS Checkbox2 Condition--------------------------------------------------->
        chk_bumpers2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if(!chk_bumpers2.isChecked()){
                    edit.remove("store_bumpers2");
                    edit.commit();
                    chk_bumpers2.setChecked(false);
                    chk_bumpers2.setVisibility(View.GONE);
                    bumpers_pic2_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    bumpers_pic2_iv.setEnabled(true);
                    bumpers_plus2.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_bumpers2.setChecked(true);
                    chk_bumpers2.setVisibility(View.VISIBLE);
                    bumpers_pic2_iv.setEnabled(false);
                    bumpers_plus2.setVisibility(View.GONE);
                }

            }
        });
        //HOODS Checkbox1 Condition--------------------------------------------------->
        chk_hoods1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if(!chk_hoods1.isChecked()){
                    edit.remove("store_hoods1");
                    edit.commit();
                    chk_hoods1.setChecked(false);
                    chk_hoods1.setVisibility(View.GONE);
                    hoods_pic1_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    hoods_pic1_iv.setEnabled(true);
                    hoods_plus1.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_hoods1.setChecked(true);
                    chk_hoods1.setVisibility(View.VISIBLE);
                    hoods_pic1_iv.setEnabled(false);
                    hoods_plus1.setVisibility(View.GONE);
                }
            }
        });


        //HOODS Checkbox2 Condition--------------------------------------------------->
        chk_hoods2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!chk_hoods2.isChecked()){
                    edit.remove("store_hoods2");
                    edit.commit();
                    chk_hoods2.setChecked(false);
                    chk_hoods2.setVisibility(View.GONE);
                    hoods_pic2_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    hoods_pic2_iv.setEnabled(true);
                    hoods_plus2.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_hoods2.setChecked(true);
                    chk_hoods2.setVisibility(View.VISIBLE);
                    hoods_pic2_iv.setEnabled(false);
                    hoods_plus2.setVisibility(View.GONE);
                }
            }
        });


        //Others Checkbox1 Condition--------------------------------------------------->
        chk_others1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!chk_others1.isChecked()){
                    edit.remove("store_bodyother1");
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
                    edit.remove("store_bodyother2");
                    edit.commit();
                    chk_others2.setChecked(false);
                    chk_others2.setVisibility(View.GONE);
                    others_pic2_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    others_pic2_iv.setEnabled(true);
                    others_plus2.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_others2.setChecked(true);
                    chk_others2.setVisibility(View.VISIBLE);
                    others_pic2_iv.setEnabled(false);
                    others_plus2.setVisibility(View.GONE);
                }

            }
        });



        doors_pic1_iv.setOnClickListener(this);
        doors_pic2_iv.setOnClickListener(this);
        bumpers_pic1_iv.setOnClickListener(this);
        bumpers_pic2_iv.setOnClickListener(this);
        hoods_pic1_iv.setOnClickListener(this);
        hoods_pic2_iv.setOnClickListener(this);
        others_pic1_iv.setOnClickListener(this);
        others_pic2_iv.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            // DOORS PIC ONE
            case R.id.doors_pic1_iv:


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
                            startActivityForResult(cameraIntent, TAKE_DOORS_PIC1);
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
                            startActivityForResult(pickIntent, SELECT_DOORS_PIC1);
                        }


                    }
                });
                cameraDialog.show();

                break;


            // DOORS PIC TWO
            case R.id.doors_pic2_iv:

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
                            startActivityForResult(cameraIntent, TAKE_DOORS_PIC2);
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
                            startActivityForResult(pickIntent, SELECT_DOORS_PIC2);
                        }

                    }
                });
                cameraDialog.show();
                break;







            // BUMPERS PIC ONE
            case R.id.bumpers_pic1_iv:

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
                            startActivityForResult(cameraIntent, TAKE_BUMPERS_PIC1);
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
                            startActivityForResult(pickIntent, SELECT_BUMPERS_PIC1);
                        }
                    }
                });
                cameraDialog.show();

                break;






            // BUMPERS PIC TWO
            case R.id.bumpers_pic2_iv:

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
                            startActivityForResult(cameraIntent, TAKE_BUMPERS_PIC2);
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
                            startActivityForResult(pickIntent, SELECT_BUMPERS_PIC2);
                        }
                    }
                });
                cameraDialog.show();

                break;






            // HOODS PIC ONE
            case R.id.hoods_pic1_iv:

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
                            startActivityForResult(cameraIntent, TAKE_HOODS_PIC1);
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
                            startActivityForResult(pickIntent, SELECT_HOODS_PIC1);
                        }


                    }
                });
                cameraDialog.show();

                break;





            // EMISSION PIC TWO
            case R.id.hoods_pic2_iv:

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
                            startActivityForResult(cameraIntent, TAKE_HOODS_PIC2);
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
                            startActivityForResult(pickIntent, SELECT_HOODS_PIC2);
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

        //********************DOORS PIC 1 -------TAKE
        if (resultCode == RESULT_OK && requestCode == TAKE_DOORS_PIC1) {

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

                doors_pic1_iv.setImageBitmap(bitmap);
                chk_doors1.setChecked(true);
                chk_doors1.setVisibility(View.VISIBLE);
                doors_plus1.setVisibility(View.GONE);
                path_doors1=mediaFile4.getAbsolutePath();
                edit.putString("store_doors1",path_doors1);
                edit.commit();








        }








        //********************DOORS PIC 2 -------TAKE
        else if (resultCode == RESULT_OK && requestCode == TAKE_DOORS_PIC2){

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
                doors_pic2_iv.setImageBitmap(bitmap);
                chk_doors2.setChecked(true);
                chk_doors2.setVisibility(View.VISIBLE);
                doors_plus2.setVisibility(View.GONE);
                path_doors1=mediaFile4.getAbsolutePath();
                edit.putString("store_doors2",path_doors1);
                edit.commit();



        }








        //********************DOORS PIC 1 -------SELECT

        else if (resultCode == RESULT_OK && requestCode == SELECT_DOORS_PIC1){


            try {
                Uri selectedMediaUri = data.getData();
                path_doors1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    doors_pic1_iv.setImageBitmap(bitmap);
                    chk_doors1.setChecked(true);
                    chk_doors1.setVisibility(View.VISIBLE);
                    doors_plus1.setVisibility(View.GONE);
                    edit.putString("store_doors1", path_doors1);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }

        }








        //********************DOORS PIC 2 -------TAKE
        else if (resultCode == RESULT_OK && requestCode == SELECT_DOORS_PIC2){


            try {
                Uri selectedMediaUri = data.getData();
                path_doors2 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    doors_pic2_iv.setImageBitmap(bitmap);
                    chk_doors2.setChecked(true);
                    chk_doors2.setVisibility(View.VISIBLE);
                    doors_plus2.setVisibility(View.GONE);
                    edit.putString("store_doors2", path_doors2);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }
        }




















        //********************BUMPERS PIC 1 -------TAKE

        if (resultCode == RESULT_OK && requestCode == TAKE_BUMPERS_PIC1) {

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

                bumpers_pic1_iv.setImageBitmap(bitmap);
                chk_bumpers1.setChecked(true);
                chk_bumpers1.setVisibility(View.VISIBLE);
                bumpers_plus1.setVisibility(View.GONE);
                path_bumpers1=mediaFile4.getAbsolutePath();
                edit.putString("store_bumpers1", path_bumpers1);
                edit.commit();






        }








        //********************BUMPERS PIC 2 -------TAKE
        else if (resultCode == RESULT_OK && requestCode == TAKE_BUMPERS_PIC2){

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
                bumpers_pic2_iv.setImageBitmap(bitmap);
                chk_bumpers2.setChecked(true);
                chk_bumpers2.setVisibility(View.VISIBLE);
                bumpers_plus2.setVisibility(View.GONE);

                path_bumpers2=mediaFile4.getAbsolutePath();
                edit.putString("store_bumpers2", path_bumpers2);
                edit.commit();






        }









        //********************BUMPERS PIC 1 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_BUMPERS_PIC1){


            try {
                Uri selectedMediaUri = data.getData();
                path_bumpers1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    bumpers_pic1_iv.setImageBitmap(bitmap);
                    chk_bumpers1.setChecked(true);
                    chk_bumpers1.setVisibility(View.VISIBLE);
                    bumpers_plus1.setVisibility(View.GONE);
                    edit.putString("store_bumpers1", path_bumpers1);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }


        }








        //********************BUMPERS PIC 2 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_BUMPERS_PIC2){

            try {
                Uri selectedMediaUri = data.getData();
                path_bumpers2 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    bumpers_pic2_iv.setImageBitmap(bitmap);
                    chk_bumpers2.setChecked(true);
                    chk_bumpers2.setVisibility(View.VISIBLE);
                    bumpers_plus2.setVisibility(View.GONE);
                    edit.putString("store_bumpers2", path_bumpers2);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }

        }







        //********************HOODS PIC 1 -------TAKE
        if (resultCode == RESULT_OK && requestCode == TAKE_HOODS_PIC1) {


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

                hoods_pic1_iv.setImageBitmap(bitmap);
                chk_hoods1.setChecked(true);
                chk_hoods1.setVisibility(View.VISIBLE);
                hoods_plus1.setVisibility(View.GONE);
                path_hoods1=mediaFile4.getAbsolutePath();

                edit.putString("store_hoods1", path_hoods1);
                edit.commit();




        }






        //********************HOODS PIC 2 -------TAKE
        if (resultCode == RESULT_OK && requestCode == TAKE_HOODS_PIC2) {

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


                hoods_pic2_iv.setImageBitmap(bitmap);
                chk_hoods2.setChecked(true);
                chk_hoods2.setVisibility(View.VISIBLE);
                hoods_plus2.setVisibility(View.GONE);
                path_hoods2=mediaFile4.getAbsolutePath();
                edit.putString("store_hoods2", path_hoods2);
                edit.commit();







        }






        //********************HOODS PIC 1 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_HOODS_PIC1){



            try {
                Uri selectedMediaUri = data.getData();
                path_hoods1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    hoods_pic1_iv.setImageBitmap(bitmap);
                    chk_hoods1.setChecked(true);
                    chk_hoods1.setVisibility(View.VISIBLE);
                    hoods_plus1.setVisibility(View.GONE);
                    edit.putString("store_hoods1", path_hoods1);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }

        }








        //********************HOODS PIC 2 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_HOODS_PIC2){


            try {
                Uri selectedMediaUri = data.getData();
                path_hoods2 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    hoods_pic2_iv.setImageBitmap(bitmap);
                    chk_hoods2.setChecked(true);
                    chk_hoods2.setVisibility(View.VISIBLE);
                    hoods_plus2.setVisibility(View.GONE);
                    edit.putString("store_hoods2", path_hoods2);
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


        }






        //********************OTHERS PIC 1 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_OTHERS_PIC1){

            try {
                Uri selectedMediaUri = data.getData();
                path_body_other1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    others_pic1_iv.setImageBitmap(bitmap);
                    chk_others1.setChecked(true);
                    chk_others1.setVisibility(View.VISIBLE);
                    others_plus1.setVisibility(View.GONE);
                    edit.putString("store_bodyother1", path_body_other1);
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
                path_body_other2 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    others_pic2_iv.setImageBitmap(bitmap);
                    chk_others2.setChecked(true);
                    chk_others2.setVisibility(View.VISIBLE);
                    others_plus2.setVisibility(View.GONE);
                    edit.putString("store_bodyother2", path_body_other2);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }
        }
    }
}