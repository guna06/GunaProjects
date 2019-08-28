package com.sqindia.srinarapp.SuperAdmin;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sqindia.srinarapp.Adapter.CustomAdapter;
import com.sqindia.srinarapp.Fonts.FontsOverride;
import com.sqindia.srinarapp.R;
import com.sqindia.srinarapp.Utils.Config;
import com.sqindia.srinarapp.Utils.Util;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

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

public class FileUploadActivity extends Activity{
    MaterialBetterSpinner month_spn,year_spin;
    TextView select_file_tv,txtPercentage,view_part_tv,singlepart_tv;
    String str_month,str_year,get_excel_file,token,path,msg;
    Typeface segoeui;
    ImageView upload_btn;
    LinearLayout back;
    private ProgressBar progressBar;
    Dialog loader_dialog;
    String[] month_data = {"January", "February", "March","April","May","June","July","August","September","October","November","December"};
    String[] year_data = {"2017", "2018", "2019"};
    public static final int requestcode = 1;
    String filename = "";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_upload);

        month_spn=findViewById(R.id.month_spn);
        year_spin=findViewById(R.id.year_spn);
        select_file_tv=findViewById(R.id.select_file_tv);
        upload_btn=findViewById(R.id.upload_btn);
        back=findViewById(R.id.back);
        progressBar=findViewById(R.id.progressBar);
        txtPercentage=findViewById(R.id.txtPercentage);
        view_part_tv=findViewById(R.id.view_part_tv);
        singlepart_tv=findViewById(R.id.singlepart_tv);

        //set default Loader:
        loader_dialog = new Dialog(FileUploadActivity.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);


        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(FileUploadActivity.this, v1);


        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        token = sharedPreferences.getString("str_sessiontoken", "");

        segoeui = Typeface.createFromAsset(getAssets(), "fonts/segoeuil.ttf");

        // Month Spinner Adapter:
        final CustomAdapter monthAdapter = new CustomAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, month_data) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return true;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTypeface(segoeui);
                tv.setTextSize(9);
                tv.setPadding(30, 55, 10, 25);
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextSize(15);
                tv.setPadding(10, 20, 0, 20);
                tv.setTypeface(segoeui);
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        month_spn.setAdapter(monthAdapter);



        month_spn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FontsOverride.overrideFonts(FileUploadActivity.this, view);
                str_month = adapterView.getItemAtPosition(i).toString();
            }
        });

        // year Spinner Adapter:
        final CustomAdapter yearAdapter = new CustomAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, year_data) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return true;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTypeface(segoeui);
                tv.setTextSize(9);
                tv.setPadding(30, 55, 10, 25);
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextSize(15);
                tv.setPadding(10, 20, 0, 20);
                tv.setTypeface(segoeui);
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        year_spin.setAdapter(yearAdapter);




        year_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FontsOverride.overrideFonts(FileUploadActivity.this, view);
                str_year = adapterView.getItemAtPosition(i).toString();
            }
        });


        select_file_tv.setOnClickListener(new View.OnClickListener() {
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



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(getApplicationContext(),AdminDashboard.class);
                startActivity(back);
                finish();
            }
        });



        view_part_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent view_part=new Intent(getApplicationContext(),ViewParts.class);
                startActivity(view_part);
                finish();
            }
        });


        singlepart_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add_part=new Intent(getApplicationContext(),AddSingleParts.class);
                startActivity(add_part);
                finish();
            }
        });


        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Util.Operations.isOnline(FileUploadActivity.this)) {

                    if(get_excel_file!=null)
                    {
                        if(str_month!=null)
                        {
                            if(str_year!=null)
                            {
                                //####$$$$$$$$$$ CALL EXCEL FILE UPLOAD ****************************
                                new UploadExcelToServer(get_excel_file).execute();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Please Select Year",Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Please Select Month",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please Select File",Toast.LENGTH_LONG).show();
                    }
                }else
                {
                    Toast.makeText(getApplicationContext(),"No Internet Connectivity",Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCode) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri uri = data.getData();
                    path = getPath(this, uri);
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
            String virtual_url = Config.WEB_URL_EXCEL_UPLOAD;
            HttpPost httppost = new HttpPost(virtual_url);
            httppost.setHeader("sessiontoken", token);
            httppost.setHeader("month", str_month);
            httppost.setHeader("year", str_year);

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

                    new SweetAlertDialog(FileUploadActivity.this, SweetAlertDialog.SUCCESS_TYPE)
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
                    new SweetAlertDialog(FileUploadActivity.this, SweetAlertDialog.WARNING_TYPE)
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
