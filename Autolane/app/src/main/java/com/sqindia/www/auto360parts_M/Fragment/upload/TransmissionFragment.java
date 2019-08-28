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

public class TransmissionFragment extends Fragment implements View.OnClickListener{

    ImageView automatic_pic1_iv,automatic_pic2_iv,manual_pic1_iv,manual_pic2_iv,others_pic1_iv,others_pic2_iv;
    ImageView automatic_plus1,automatic_plus2,manual_plus1,manual_plus2,others_plus1,others_plus2;
    CheckBox chk_automatic1,chk_automatic2,chk_manual1,chk_manual2,chk_others1,chk_others2;
    Dialog cameraDialog;
    LinearLayout lnr_takepic, lnr_takegallery;
    Bitmap bitmap;
    String selectedImagePath;
    public static TextView transmission_box1,transmission_box2;
    public static EditText transmission_others;
    String path_automatic1,path_automatic2,path_manual1,path_manual2,path_trans_other1,path_trans_other2;
    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;


    protected static final int TAKE_AUTOMATIC_PIC1 = 1;
    protected static final int TAKE_AUTOMATIC_PIC2 = 2;
    protected static final int SELECT_AUTOMATIC_PIC1 = 3;
    protected static final int SELECT_AUTOMATIC_PIC2 = 4;


    protected static final int TAKE_MANUAL_PIC1 = 5;
    protected static final int TAKE_MANUAL_PIC2 = 6;
    protected static final int SELECT_MANUAL_PIC1 = 7;
    protected static final int SELECT_MANUAL_PIC2 = 8;


    protected static final int TAKE_OTHERS_PIC1 = 17;
    protected static final int TAKE_OTHERS_PIC2 = 18;
    protected static final int SELECT_OTHERS_PIC1 = 19;
    protected static final int SELECT_OTHERS_PIC2 = 20;



    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transmission, container, false);

        cameraDialog = new Dialog(getActivity());
        cameraDialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);
        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getActivity());
        edit = sharedPrefces.edit();


        transmission_box1=view.findViewById(R.id.transmission_box1);
        transmission_box2=view.findViewById(R.id.transmission_box2);
        transmission_others=view.findViewById(R.id.transmission_others);


        //@@@@@@@@@@@@@@@@ Engine FindviewbyId
        automatic_pic1_iv=view.findViewById(R.id.automatic_pic1_iv);
        automatic_pic2_iv=view.findViewById(R.id.automatic_pic2_iv);
        automatic_plus1=view.findViewById(R.id.automatic_plus1);
        automatic_plus2=view.findViewById(R.id.automatic_plus2);
        chk_automatic1=view.findViewById(R.id.chk_automatic1);
        chk_automatic2=view.findViewById(R.id.chk_automatic2);


        //@@@@@@@@@@@@@@@@ A/C FindviewbyId
        manual_pic1_iv=view.findViewById(R.id.manual_pic1_iv);
        manual_pic2_iv=view.findViewById(R.id.manual_pic2_iv);
        manual_plus1=view.findViewById(R.id.manual_plus1);
        manual_plus2=view.findViewById(R.id.manual_plus2);
        chk_manual1=view.findViewById(R.id.chk_manual1);
        chk_manual2=view.findViewById(R.id.chk_manual2);


        //@@@@@@@@@@@@@@@@ OTHERS FindviewbyId
        others_pic1_iv=view.findViewById(R.id.others_pic1_iv);
        others_pic2_iv=view.findViewById(R.id.others_pic2_iv);
        others_plus1=view.findViewById(R.id.others_plus1);
        others_plus2=view.findViewById(R.id.others_plus2);
        chk_others1=view.findViewById(R.id.chk_others1);
        chk_others2=view.findViewById(R.id.chk_others2);


        automatic_pic1_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.camera_box));
        automatic_pic2_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.camera_box));
        manual_pic1_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.camera_box));
        manual_pic2_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.camera_box));
        others_pic1_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.camera_box));
        others_pic2_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.camera_box));


        chk_automatic1.setVisibility(View.GONE);
        chk_automatic2.setVisibility(View.GONE);
        chk_manual1.setVisibility(View.GONE);
        chk_manual2.setVisibility(View.GONE);
        chk_others1.setVisibility(View.GONE);
        chk_others2.setVisibility(View.GONE);


        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        edit = sharedPreferences.edit();
        path_automatic1=sharedPreferences.getString("store_automatic1","");
        path_automatic2=sharedPreferences.getString("store_automatic2","");
        path_manual1=sharedPreferences.getString("store_manual1","");
        path_manual2=sharedPreferences.getString("store_manual2","");
        path_trans_other1=sharedPreferences.getString("store_transother1","");
        path_trans_other2=sharedPreferences.getString("store_transother1","");




        if(path_automatic1!=null) {
            chk_automatic1.setChecked(true);
            File imgFile1 = new File(path_automatic1);

            if (imgFile1.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile1.getAbsolutePath());
                chk_automatic1.setVisibility(View.VISIBLE);
                automatic_pic1_iv.setImageBitmap(myBitmap);
                automatic_plus1.setVisibility(View.GONE);
            }
        }

        if(path_automatic2!=null) {
            chk_automatic2.setChecked(true);
            File imgFile2 = new File(path_automatic2);

            if (imgFile2.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile2.getAbsolutePath());
                chk_automatic2.setVisibility(View.VISIBLE);
                automatic_pic2_iv.setImageBitmap(myBitmap);
                automatic_plus2.setVisibility(View.GONE);
            }
        }


        if(path_manual1!=null) {
            File imgFile3 = new File(path_manual1);
            chk_manual1.setChecked(true);
            if (imgFile3.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile3.getAbsolutePath());
                chk_manual1.setVisibility(View.VISIBLE);
                manual_pic1_iv.setImageBitmap(myBitmap);
                manual_plus1.setVisibility(View.GONE);
            }
        }



        if(path_manual2!=null) {
            chk_manual2.setChecked(true);
            File imgFile4 = new File(path_manual2);

            if (imgFile4.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile4.getAbsolutePath());
                chk_manual2.setVisibility(View.VISIBLE);
                manual_pic2_iv.setImageBitmap(myBitmap);
                manual_plus2.setVisibility(View.GONE);
            }
        }


        if(path_trans_other1!=null) {
            chk_others1.setChecked(true);
            File imgFile5 = new File(path_trans_other1);

            if (imgFile5.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile5.getAbsolutePath());
                chk_others1.setVisibility(View.VISIBLE);
                others_pic1_iv.setImageBitmap(myBitmap);
                others_plus1.setVisibility(View.GONE);
            }
        }



        if(path_trans_other2!=null) {
            chk_others2.setChecked(true);
            File imgFile6 = new File(path_trans_other2);

            if (imgFile6.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile6.getAbsolutePath());
                chk_others2.setVisibility(View.VISIBLE);
                others_pic2_iv.setImageBitmap(myBitmap);
                others_plus2.setVisibility(View.GONE);
            }
        }





        //AUTOMATIC Checkbox1 Condition--------------------------------------------------->
        chk_automatic1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!chk_automatic1.isChecked()){
                    edit.remove("store_automatic1");
                    edit.commit();
                    chk_automatic1.setChecked(false);
                    chk_automatic1.setVisibility(View.GONE);
                    automatic_pic1_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    automatic_pic1_iv.setEnabled(true);
                    automatic_plus1.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_automatic1.setChecked(true);
                    chk_automatic1.setVisibility(View.VISIBLE);
                    automatic_pic1_iv.setEnabled(false);
                    automatic_plus1.setVisibility(View.GONE);
                }
            }
        });



        //AUTOMATIC Checkbox2 Condition--------------------------------------------------->
        chk_automatic2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!chk_automatic2.isChecked()){
                    edit.remove("store_automatic2");
                    edit.commit();
                    chk_automatic2.setChecked(false);
                    chk_automatic2.setVisibility(View.GONE);
                    automatic_pic2_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    automatic_pic2_iv.setEnabled(true);
                    automatic_plus2.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_automatic2.setChecked(true);
                    chk_automatic2.setVisibility(View.VISIBLE);
                    automatic_pic2_iv.setEnabled(false);
                    automatic_plus2.setVisibility(View.GONE);
                }
            }
        });





        //MANUAL Checkbox1 Condition--------------------------------------------------->
        chk_manual1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if(!chk_manual1.isChecked()){
                    edit.remove("store_manual1");
                    edit.commit();
                    chk_manual1.setChecked(false);
                    chk_manual1.setVisibility(View.GONE);
                    manual_pic1_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    manual_pic1_iv.setEnabled(true);
                    manual_plus1.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_manual1.setChecked(true);
                    chk_manual1.setVisibility(View.VISIBLE);
                    manual_pic1_iv.setEnabled(false);
                    manual_plus1.setVisibility(View.GONE);
                }

            }
        });




        //BUMPERS Checkbox2 Condition--------------------------------------------------->
        chk_manual2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if(!chk_manual2.isChecked()){
                    edit.remove("store_manual2");
                    edit.commit();
                    chk_manual2.setChecked(false);
                    chk_manual2.setVisibility(View.GONE);
                    manual_pic2_iv.setImageDrawable(getResources().getDrawable(R.drawable.camera_box));
                    manual_pic2_iv.setEnabled(true);
                    manual_plus2.setVisibility(View.VISIBLE);
                }
                else
                {
                    chk_manual2.setChecked(true);
                    chk_manual2.setVisibility(View.VISIBLE);
                    manual_pic2_iv.setEnabled(false);
                    manual_plus2.setVisibility(View.GONE);
                }

            }
        });


        //Others Checkbox1 Condition--------------------------------------------------->
        chk_others1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if(!chk_others1.isChecked()){
                    edit.remove("store_transother1");
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
                    edit.remove("store_transother1");
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



        automatic_pic1_iv.setOnClickListener(this);
        automatic_pic2_iv.setOnClickListener(this);
        manual_pic1_iv.setOnClickListener(this);
        manual_pic2_iv.setOnClickListener(this);
        others_pic1_iv.setOnClickListener(this);
        others_pic2_iv.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            // AUTOMATIC PIC ONE
            case R.id.automatic_pic1_iv:


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
                            startActivityForResult(cameraIntent, TAKE_AUTOMATIC_PIC1);
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
                            startActivityForResult(pickIntent, SELECT_AUTOMATIC_PIC1);
                        }

                    }
                });
                cameraDialog.show();

                break;


            // AUTOMATIC PIC TWO
            case R.id.automatic_pic2_iv:

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
                            startActivityForResult(cameraIntent, TAKE_AUTOMATIC_PIC2);
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
                            startActivityForResult(pickIntent, SELECT_AUTOMATIC_PIC2);
                        }
                    }
                });
                cameraDialog.show();
                break;







            // MANUAL PIC ONE
            case R.id.manual_pic1_iv:

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
                            startActivityForResult(cameraIntent, TAKE_MANUAL_PIC1);
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
                            startActivityForResult(pickIntent, SELECT_MANUAL_PIC1);
                        }
                    }
                });
                cameraDialog.show();

                break;






            // MANUAL PIC TWO
            case R.id.manual_pic2_iv:

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
                            startActivityForResult(cameraIntent, TAKE_MANUAL_PIC2);
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
                            startActivityForResult(pickIntent, SELECT_MANUAL_PIC2);
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

        //********************AUTOMATIC PIC 1 -------TAKE
        if (resultCode == RESULT_OK && requestCode == TAKE_AUTOMATIC_PIC1) {

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
            automatic_pic1_iv.setImageBitmap(bitmap);
                chk_automatic1.setChecked(true);
                chk_automatic1.setVisibility(View.VISIBLE);
                automatic_plus1.setVisibility(View.GONE);

                path_automatic1=mediaFile4.getAbsolutePath();
                edit.putString("store_automatic1",path_automatic1);
                edit.commit();









        }








        //********************AUTOMATIC PIC 2 -------TAKE
        else if (resultCode == RESULT_OK && requestCode == TAKE_AUTOMATIC_PIC2){

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
            automatic_pic2_iv.setImageBitmap(bitmap);
                automatic_pic2_iv.setImageBitmap(bitmap);
                chk_automatic2.setChecked(true);
                chk_automatic2.setVisibility(View.VISIBLE);
                automatic_plus2.setVisibility(View.GONE);

                path_automatic2=mediaFile4.getAbsolutePath();
                edit.putString("store_automatic2",path_automatic2);
                edit.commit();




        }








        //********************AUTOMATIC PIC 1 -------SELECT

        else if (resultCode == RESULT_OK && requestCode == SELECT_AUTOMATIC_PIC1){

            try {
                Uri selectedMediaUri = data.getData();
                path_automatic1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    automatic_pic1_iv.setImageBitmap(bitmap);
                    chk_automatic1.setChecked(true);
                    chk_automatic1.setVisibility(View.VISIBLE);
                    automatic_plus1.setVisibility(View.GONE);
                    edit.putString("store_automatic1", path_automatic1);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }



        }








        //********************AUTOMATIC PIC 2 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_AUTOMATIC_PIC2){


            try {
                Uri selectedMediaUri = data.getData();
                path_automatic2 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    automatic_pic2_iv.setImageBitmap(bitmap);
                    chk_automatic2.setChecked(true);
                    chk_automatic2.setVisibility(View.VISIBLE);
                    automatic_plus2.setVisibility(View.GONE);
                    edit.putString("store_automatic2", path_automatic2);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }

        }




















        //********************MANUAL PIC 1 -------TAKE

        if (resultCode == RESULT_OK && requestCode == TAKE_MANUAL_PIC1) {

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
                manual_pic1_iv.setImageBitmap(bitmap);
                chk_manual1.setChecked(true);
                chk_manual1.setVisibility(View.VISIBLE);
                manual_plus1.setVisibility(View.GONE);


                path_manual1=mediaFile2.getAbsolutePath();
                edit.putString("store_manual1",path_manual1);
                edit.commit();






        }








        //********************MANUAL PIC 2 -------TAKE
        else if (resultCode == RESULT_OK && requestCode == TAKE_MANUAL_PIC2){

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
                manual_pic2_iv.setImageBitmap(bitmap);
                chk_manual2.setChecked(true);
                chk_manual2.setVisibility(View.VISIBLE);
                manual_plus2.setVisibility(View.GONE);

                path_manual2=mediaFile2.getAbsolutePath();
                edit.putString("store_manual2",path_manual2);
                edit.commit();




        }









        //********************MANUAL PIC 1 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_MANUAL_PIC1){


            try {
                Uri selectedMediaUri = data.getData();
                path_manual1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    manual_pic1_iv.setImageBitmap(bitmap);
                    chk_manual1.setChecked(true);
                    chk_manual1.setVisibility(View.VISIBLE);
                    manual_plus1.setVisibility(View.GONE);
                    edit.putString("store_manual1", path_manual1);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }
        }








        //********************MANUAL PIC 2 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_MANUAL_PIC2){
            try {
                Uri selectedMediaUri = data.getData();
                path_manual2 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    manual_pic2_iv.setImageBitmap(bitmap);
                    chk_manual2.setChecked(true);
                    chk_manual2.setVisibility(View.VISIBLE);
                    manual_plus2.setVisibility(View.GONE);
                    edit.putString("store_manual2", path_manual2);
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

                path_trans_other1=mediaFile2.getAbsolutePath();
                edit.putString("store_transother1",path_trans_other1);
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

                path_trans_other2=mediaFile2.getAbsolutePath();
                edit.putString("store_transother2",path_trans_other2);
                edit.commit();


        }


        //********************OTHERS PIC 1 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_OTHERS_PIC1){



            try {
                Uri selectedMediaUri = data.getData();
                path_trans_other1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    others_pic1_iv.setImageBitmap(bitmap);
                    chk_others1.setChecked(true);
                    chk_others1.setVisibility(View.VISIBLE);
                    others_plus1.setVisibility(View.GONE);
                    edit.putString("store_transother1", path_trans_other1);
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
                path_trans_other2 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    others_pic2_iv.setImageBitmap(bitmap);
                    chk_others2.setChecked(true);
                    chk_others2.setVisibility(View.VISIBLE);
                    others_plus2.setVisibility(View.GONE);
                    edit.putString("store_transother2", path_trans_other2);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }

        }




    }
}