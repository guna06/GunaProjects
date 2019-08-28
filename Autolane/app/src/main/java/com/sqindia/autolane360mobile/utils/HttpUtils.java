package com.sqindia.autolane360mobile.utils;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;


@SuppressWarnings("deprecation")

//asdff
public class HttpUtils {

    public static final String TAG = "tagH";
    private final static HttpClient mHhttpclient = new DefaultHttpClient();

    public static String makeRequest(String url, String json) {
        Log.e(TAG, "URL-->" + url);
        Log.e("tag", "input-->" + json);


        try {
            Log.v(TAG, "inside-->");

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);


            // receive response as inputStream
            InputStream inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if (inputStream != null) {
                String result = convertInputStreamToString(inputStream);
                Log.e(TAG, "output-->" + result);
                return result;
            } else {
                Log.e(TAG, "output-->" + inputStream);

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String makeRequest1(String url) {
        Log.e("tag", "URL-->" + url);


        try {

            HttpGet httpPost = new HttpGet(url);
            //httpPost.setEntity(new StringEntity(json));
           // httpPost.setHeader("Accept", "application/json");
          httpPost.setHeader("Content-Type", "application/json");
           // httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");
            //httpPost.setHeader("token",token);

            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
            InputStream inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null) {
                String result = convertInputStreamToString(inputStream);
                Log.e(TAG, "output-->" + result);
                return result;
            } else {
                Log.e(TAG, "output-->" + inputStream);

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String makeRequestToken(String url, String token,String json) {
        Log.e("tag", "URL-->" + url);
        Log.e("tag", "input-->" + json);


        try {
            Log.v(TAG, "inside-->");

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-Type", "application/json");
            // httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");
            httpPost.setHeader("X-API-KEY",token);

            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
            InputStream inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null) {
                String result = convertInputStreamToString(inputStream);
                Log.e(TAG, "output-->" + result);
                return result;
            } else {
                Log.e(TAG, "output-->" + inputStream);

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static JSONObject logout(String url, String session) throws JSONException {
        InputStream is = null;
        String result = "";
        JSONObject jArray = null;
        Log.e("tag_", "started");
        try {

            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("session_id", session);
            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse response = null;
            String text;
            try {
                response = client.execute(httppost);
                Log.e("tag_", "stsL_" + response.getStatusLine());
                Log.e("tag_", "stsL_" + response.getStatusLine().getReasonPhrase());
                Log.e("tag_", "stsL_" + response.getStatusLine().getStatusCode());
            } catch (IOException e) {
                Log.e("INFO", e.getMessage());
            }


            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("tag_", "stsL2_" + is);

        } catch (Exception e) {
            Log.e("tag", "Error in http connection " + e.toString());

        }


        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.e("tag_", "stsL20ifaft_" + result);
        } catch (Exception e) {
            Log.e("tag", "Error converting result " + e.toString());
            // result = "sam";
        }

        try {
            jArray = new JSONObject(result);
            Log.e("tag_", "stsL21if_" + jArray);
        } catch (JSONException e) {
            Log.e("tag0", result);
            Log.e("tag2", "Error parsing data " + e.toString());
        }

        return jArray;
        // return jArray;
        // }

    }



























/*
    public static String makeRequest1(String url, String json, String token) {
        Log.v(TAG, "URL-->" + url);
        Log.v(TAG, "input-->" + json);
        Log.d("tag", token);


        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("sessionToken", token);
            //text/html
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);

            // receive response as inputStream
            InputStream inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if (inputStream != null) {
                String result = convertInputStreamToString(inputStream);
                Log.v(TAG, "output-->" + result);
                return result;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
*/


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Header adding multiple parameter>>>>>>>>>>>>>>>>>>>>>>>>>>>>






    public static String GET(String url){
        InputStream inputStream = null;
        Log.e("tag"," OUTPUT -->" + url);
        String result = "";
        try {
            // create HttpClient
            //HttpClient httpclient = new DefaultHttpClient();
            // make GET request to the given URL
            HttpResponse httpResponse = mHhttpclient.execute(new HttpGet(url));
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if(inputStream != null){
                result = convertInputStreamToString(inputStream);
                //inputStream.close();
            }
            else
                result = "Did not work!";
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();

        Log.e("tag"," OUTPUT -->" + result);


        return result;

    }



}
