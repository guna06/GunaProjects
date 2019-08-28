package com.sqindia.srinarapp.SuperAdmin;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.sqindia.srinarapp.MachiningModule.ViewMachineJob;
import com.sqindia.srinarapp.Fonts.FontsOverride;
import com.sqindia.srinarapp.R;
import com.sqindia.srinarapp.Utils.Config;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdminDashboard extends Activity {
    LinearLayout lnr_machining,lnr_operator,lnr_monthtarget,back;
    public static final int requestcode = 1;
    String token;
    Dialog dialog;
    String filename = "";
    int filesize = 2;
    int FILE_SIZE_LIMIT = 50;
    Typeface segoeui;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(AdminDashboard.this, v1);
        segoeui = Typeface.createFromAsset(getAssets(), "fonts/segoeuil.ttf");
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        token = sharedPreferences.getString("str_sessiontoken", "");

        lnr_machining=findViewById(R.id.lnr_machining);
        lnr_operator=findViewById(R.id.lnr_operator);
        lnr_monthtarget=findViewById(R.id.lnr_monthtarget);
        back=findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mac=new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(mac);
                finish();
            }
        });

        lnr_machining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnr_machining.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dash_fill_border) );
                Intent mac=new Intent(getApplicationContext(),AddMachine.class);
                startActivity(mac);
                finish();
            }
        });


        lnr_operator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnr_operator.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dash_fill_border) );
                Intent mac=new Intent(getApplicationContext(),AddOperator.class);
                startActivity(mac);
                finish();
            }
        });

        lnr_monthtarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnr_monthtarget.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dash_fill_border) );
               Intent upload=new Intent(getApplicationContext(),FileUploadActivity.class);
               startActivity(upload);
               finish();
            }
        });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mac=new Intent(getApplicationContext(),DashboardActivity.class);
        startActivity(mac);
        finish();
    }




    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private class UploadExcelToServer extends AsyncTask<Void, Integer, String> {
        String excel_path;


        public UploadExcelToServer(String get_excel_file) {
            this.excel_path=get_excel_file;

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
        protected void onPostExecute(String result) {

            Log.e("tag", "Response from server: " + result);

            try {
                JSONObject jo = new JSONObject(result);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                if (status.equals("true")) {
                    new SweetAlertDialog(getApplicationContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("MESSAGE!!!")
                            .setContentText(msg)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent i = new Intent(getApplicationContext(), ViewMachineJob.class);
                                    getApplicationContext().startActivity(i);

                                }
                            })
                            .show();
                } else {
                    new SweetAlertDialog(getApplicationContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("MESSAGE!!!")
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
