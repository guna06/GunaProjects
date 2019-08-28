package autohubb.vinture.com.autohubb.MyOrdersModule;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.activity.MainActivity;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;
import autohubb.vinture.com.autohubb.utils.Util;
import es.dmoral.toasty.Toasty;

public class MyRequestFragment extends Fragment implements OnBackPressed{
    RecyclerView recyclerView_requst;
    AVLoadingIndicatorView av_loader;
    String get_mail_from_reg,get_otp_from_reg,get_apikey_from_login,get_email_from_login,get_userid_from_login,get_username_from_login;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    LinearLayout noparts_found,crete_quotation;
    MyRequestPojo myRequestPojo;
    private List<MyRequestPojo> myRequestPojoList=new ArrayList<>();
    MyRequestAdapter myRequestAdapter;
    List<RequestQuotationPojo> requestItems;
    Dialog dialog;
    TextView dialog_head_tv,tv;
    Button ok_btn,cancel_btn;
    ImageView logo_img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myrequest, container, false);
        View v1 = getActivity().getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(getActivity(), v1);
        recyclerView_requst=view.findViewById(R.id.recyclerView_requst);
        av_loader = view. findViewById(R.id.avi);
        noparts_found=view.findViewById(R.id.noparts_found);
        crete_quotation=view.findViewById(R.id.crete_quotation);
        noparts_found.setVisibility(View.GONE);
        crete_quotation.setVisibility(View.GONE);


        sharedlogin = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg=sharedlogin.getString("get_otp_from_reg","");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login=sharedlogin.getString("get_email_from_login","");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login=sharedlogin.getString("get_username_from_login","");


        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView_requst.setLayoutManager(manager);
        recyclerView_requst.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayout.VERTICAL));
        recyclerView_requst.setHasFixedSize(true);
        myRequestAdapter = new MyRequestAdapter(getActivity(), myRequestPojoList);
        recyclerView_requst.setAdapter(myRequestAdapter);

        if (Util.Operations.isOnline(getContext())) {

            new MyRequest_Task().execute();
        }
        else
        {
            Toasty.warning(getContext(), "Check your Internet Connection", Toast.LENGTH_SHORT, true).show();
        }





        crete_quotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                requestItems = new ArrayList<>();
                for(int i=0;i<myRequestPojoList.size();i++)
                {
                    RequestQuotationPojo requestItem = new RequestQuotationPojo();
                    requestItem.id = myRequestPojoList.get(i).quotReqId;
                    requestItem.price="1";
                    requestItems.add(requestItem);
                }


                if (Util.Operations.isOnline(getActivity())) {
                    new CreateQuotation_Task().execute();
                }
                else
                {
                    Toasty.warning(getActivity(), "Check your Internet Connection", Toast.LENGTH_SHORT, true).show();
                }


               /* dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_logout);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);
                dialog_head_tv=dialog.findViewById(R.id.dialog_head_tv);
                tv=dialog.findViewById(R.id.tv);
                ok_btn=dialog.findViewById(R.id.ok_btn);
                cancel_btn=dialog.findViewById(R.id.cancel_btn);
                logo_img=dialog.findViewById(R.id.logo_img);
                Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/helevetical.ttf");

                dialog_head_tv.setTypeface(tf);
                tv.setTypeface(tf);
                ok_btn.setTypeface(tf);
                cancel_btn.setTypeface(tf);
                logo_img.setBackgroundResource(R.drawable.icon_dash_4);
                tv.setText("Are you want to move All Request for Quotation? Otherwise Delete Request.");
                dialog_head_tv.setText("Quotation Request!");

                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                    }
                });


                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();*/



            }
        });


        return view;

    }


    @Override
    public void onPause() {
        super.onPause();

      /*  Intent i=new Intent(getContext(), MainActivity.class);
        startActivity(i);
        getActivity().finish();*/
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getActivity(),MainActivity.class);
        startActivity(i);
        getActivity().finish();
    }


    //MyRequest API CALL: ---------------------------------------------------------------------->
    public class MyRequest_Task extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            av_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_LIST_ITEM+get_userid_from_login,get_apikey_from_login);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            av_loader.setVisibility(View.GONE);

            try{

                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                if (status.equals("true")) {
                    String message=jo.getString("message");
                    JSONObject data_object=jo.getJSONObject("data");

                    myRequestPojoList.clear();
                    JSONArray car_array=data_object.getJSONArray("quotReqs");
                    Log.e("tag","Printing Length---------->"+car_array.length());
                    if(car_array.length()==0)
                    {
                        crete_quotation.setVisibility(View.GONE);
                        noparts_found.setVisibility(View.VISIBLE);
                    }
                    else {
                        crete_quotation.setVisibility(View.VISIBLE);
                        noparts_found.setVisibility(View.GONE);
                        for(int i=0;i<car_array.length();i++)
                        {
                            try {
                                myRequestPojo = new MyRequestPojo();
                                JSONObject jsonObject = car_array.getJSONObject(i);
                                myRequestPojo.quotReqId=jsonObject.getString("quotReqId");
                                myRequestPojo.productName=jsonObject.getString("productName");
                                myRequestPojo.productType=jsonObject.getString("productType");
                                myRequestPojo.vehicleVin=jsonObject.getString("vehicleVin");
                                myRequestPojo.vehicleYear=jsonObject.getString("vehicleYear");
                                myRequestPojo.vehicleMake=jsonObject.getString("vehicleMake");
                                myRequestPojo.vehicleModel=jsonObject.getString("vehicleModel");
                                myRequestPojoList.add(myRequestPojo);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }


                }
                else
                {
                }

            }
            catch (Exception e)
            {

            }

            // Setup and Handover data to recyclerview
            recyclerView_requst.setAdapter(myRequestAdapter);


        }







    }



    //Create Quotation API CALL: ---------------------------------------------------------------------->
    public class CreateQuotation_Task extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;


        protected void onPreExecute() {
            super.onPreExecute();
            av_loader.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId",get_userid_from_login);


                JSONArray array=new JSONArray();
                for(int k=0;k<requestItems.size();k++)
                {
                    JSONObject object=new JSONObject();
                    object.put("id",requestItems.get(k).id);
                    object.put("quantity",requestItems.get(k).price);
                    array.put(object);
                }

                jsonObject.put("quoteItems",array);

                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_QUOTE_QUOTE,get_apikey_from_login,json);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            av_loader.setVisibility(View.GONE);

            try{
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");

                if (status.equals("true")) {
                    String message=jo.getString("message");
                    JSONObject object=jo.getJSONObject("data");

                    String quoteId=object.getString("quoteId");
                    Log.e("tag","WELCOME! UR ORDER ID IS------->"+quoteId);
                    Intent i=new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                    getActivity().finish();
                }
                else
                {

                }


            }
            catch (Exception e)
            {

            }

            // Setup and Handover data to recyclerview
            // shipping_recyclerview.setAdapter(shippingMethodAdapter);


        }
    }



}
