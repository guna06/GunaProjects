package com.sqindia.srinarapp.SuperAdmin;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sqindia.srinarapp.Fonts.FontsOverride;
import com.sqindia.srinarapp.R;
import com.sqindia.srinarapp.Utils.Config;
import com.sqindia.srinarapp.Utils.HttpUtils;
import com.sqindia.srinarapp.Utils.Util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;


import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.sqindia.srinarapp.SuperAdmin.AdminDashboard.getDataColumn;
import static com.sqindia.srinarapp.SuperAdmin.AdminDashboard.isDownloadsDocument;
import static com.sqindia.srinarapp.SuperAdmin.AdminDashboard.isExternalStorageDocument;
import static com.sqindia.srinarapp.SuperAdmin.AdminDashboard.isGooglePhotosUri;
import static com.sqindia.srinarapp.SuperAdmin.AdminDashboard.isMediaDocument;

public class AddMachine extends Activity {
    Button addmac_btn,viewmac_btn;
    LinearLayout back,lnr_single,upload_lnr,lnr_select_file;
    String Str_machine,session_token,get_excel_file,unique_id,path,msg;
    EditText mac_name_edt;
    SharedPreferences.Editor editor;
    TextView upload_machine_excel,select_file_tv;
    ImageView upload_btn;
    Dialog loader_dialog;


    public static final int requestcode = 1;
    String filename = "";
    int filesize = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_machine);



        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(AddMachine.this, v1);


        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        session_token = sharedPreferences.getString("str_sessiontoken", "");


        addmac_btn=findViewById(R.id.addmac_btn);
        back=findViewById(R.id.back);
        mac_name_edt=findViewById(R.id.mac_name_edt);
        viewmac_btn=findViewById(R.id.viewmac_btn);
        lnr_single=findViewById(R.id.lnr_single);
        upload_lnr=findViewById(R.id.upload_lnr);
        upload_machine_excel=findViewById(R.id.upload_machine_excel);
        lnr_select_file=findViewById(R.id.lnr_select_file);
        select_file_tv=findViewById(R.id.select_file_tv);
        upload_btn=findViewById(R.id.upload_btn);

        lnr_single.setVisibility(View.VISIBLE);
        upload_lnr.setVisibility(View.GONE);


        //set default Loader:
        loader_dialog = new Dialog(AddMachine.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);

        unique_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(getApplicationContext(),AdminDashboard.class);
                startActivity(back);
                finish();
            }
        });


        upload_machine_excel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag","hshsbdhsbdbsbd");
                lnr_single.setVisibility(View.GONE);
                upload_lnr.setVisibility(View.VISIBLE);
                Animation RightSwipe = AnimationUtils.loadAnimation(AddMachine.this, R.anim.move_right);
                upload_lnr.startAnimation(RightSwipe);
            }
        });



        lnr_select_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {

                    startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"),requestcode);
                } catch (Exception ex) {
                    System.out.println("browseClick :"+ex);
                }
            }
        });





        addmac_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Str_machine=mac_name_edt.getText().toString().trim();

                if (Util.Operations.isOnline(AddMachine.this)) {

                    if(!Str_machine.equals(""))
                    {
                        new addMachineAsync().execute();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Machine name is Empty",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    new SweetAlertDialog(AddMachine.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("No Internet Connectivity")
                            .show();
                }
            }
        });


        viewmac_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent mac_view=new Intent(getApplicationContext(),ViewMAchineEntry.class);
                startActivity(mac_view);
                finish();

            }
        });


        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Util.Operations.isOnline(AddMachine.this)) {

                    if(get_excel_file!=null){
                        //####$$$$$$$$$$ CALL EXCEL FILE UPLOAD ****************************
                        new UploadExcelToServer(get_excel_file).execute();


                    }else
                    {
                        Toast.makeText(getApplicationContext(),"Please Select File",Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"No Internet Connectivity",Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public class addMachineAsync extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";


            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("machinename", Str_machine);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_ADD_MACHINE, json,session_token);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                String msg = jo.getString("message");


                if (status.equals("true")) {


                    Log.e("tag","Success");
                    new SweetAlertDialog(AddMachine.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("MESSAGE!!!")
                            .setContentText("Machine Added successfully..")

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                                    startActivity(i);
                                }
                            })
                            .show();
                } else {
                    new SweetAlertDialog(AddMachine.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING MESSAGE!!!")
                            .setContentText(msg)

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                }
                            })
                            .show();
                }
            } catch (Exception e) {

            }


        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCode) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri uri = data.getData();

                  /*  if (filesize >= FILE_SIZE_LIMIT) {
                        Toast.makeText(this,"The selected file is too large. Selet a new file with size less than 2mb",Toast.LENGTH_LONG).show();
                    } else {*/
                    path = getPath(this, uri);
//                    String mimeType = getContentResolver().getType(uri);
//                    if (mimeType == null) {
//                         path = getPath(this, uri);
//                        Log.e("tag","guna Path-------->"+filename);
//                        if (path == null) {
//                            filename = FilenameUtils.getName(uri.toString());
//                        } else {
//                            File file = new File(path);
                    //filename = file.getName();
//                            Log.e("tag","guna File Name-------->"+filename);
//                        }
//                    } else {
                    Uri returnUri = data.getData();
//                        Cursor returnCursor = getContentResolver().query(returnUri, null, null, null, null);
//                        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
//                        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
//                        returnCursor.moveToFirst();
                    //filename = returnCursor.getString(nameIndex);
//                        String size = Long.toString(returnCursor.getLong(sizeIndex));
//                    }
//                    File fileSave = getExternalFilesDir(null);
//                    String sourcePath = path;
                    Log.e("tag","guna Source Path-------->"+filename);
                    get_excel_file=path;
                    File file = new File(get_excel_file);
                    filename = file.getName();
                    select_file_tv.setText(filename);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }





    public static String getPath(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else
            if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }





    private class UploadExcelToServer extends AsyncTask<Void, Integer, String> {
        String excel_path;


        public UploadExcelToServer(String get_excel_file) {
            this.excel_path=get_excel_file;
        }


        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
            loader_dialog.show();
        }




        @Override
        protected String doInBackground(Void... voids) {
            return uploadFile();
        }


        @SuppressWarnings("deprecation")
        private String uploadFile() {

            String responseString = null;
            HttpClient httpclient = new DefaultHttpClient();
            String virtual_url = Config.WEB_URL_MACHINE_EXCEL;
            HttpPost httppost = new HttpPost(virtual_url);
            httppost.setHeader("sessiontoken", session_token);


            try {

                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                File sourceFile = new File(String.valueOf(excel_path));
                entity.addPart("xlfilename", new FileBody(sourceFile));
                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == 200) {
                    responseString = EntityUtils.toString(r_entity);

                } else {
                    Log.e("tag","Guna2");
                    responseString = "Error occurred! Http Status Code:" + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loader_dialog.dismiss();

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                msg = jo.getString("message");


                if (status.equals("true")) {

                    new SweetAlertDialog(AddMachine.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("MESSAGE!!!")
                            .setContentText(msg)

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                                    startActivity(i);
                                    finish();

                                }
                            })
                            .show();
                } else {
                    new SweetAlertDialog(AddMachine.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING MESSAGE!!!")
                            .setContentText(msg)

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                }
                            })
                            .show();
                }
            } catch (Exception e) {

            }
        }


    }
}
