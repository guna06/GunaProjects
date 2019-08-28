package autohubb.vinture.com.autohubb.VehicleModule;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
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

public class MyCarsFragment extends Fragment{
    RecyclerView private_recyclerView;
    CommercialCarListPojo commercialCarList;
    MyCarAdapter privateCarAdapter;
    private List<CommercialCarListPojo> carListList=new ArrayList<>();
    LinearLayout noparts_found;
    TextView nodata_tv1,nodata_tv2,register_link;
    Typeface helvetical;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    String get_mail_from_reg,get_otp_from_reg,get_apikey_from_login,get_email_from_login,get_userid_from_login,get_username_from_login;
    AVLoadingIndicatorView av_loader;
    FloatingActionButton fab;
    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mycars, container, false);

        View v1 = getActivity().getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(getActivity(), v1);
        helvetical = Typeface.createFromAsset(getActivity().getAssets(), "fonts/helevetical.ttf");

        sharedlogin = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg=sharedlogin.getString("get_otp_from_reg","");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login=sharedlogin.getString("get_email_from_login","");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login=sharedlogin.getString("get_username_from_login","");
        av_loader = view. findViewById(R.id.avi);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        fab=view.findViewById(R.id.fab);

        nodata_tv1=view.findViewById(R.id.nodata_tv1);
        nodata_tv2=view.findViewById(R.id.nodata_tv2);
        noparts_found=view.findViewById(R.id.noparts_found);
        private_recyclerView=view.findViewById(R.id.recyclerView);
        nodata_tv1.setTypeface(helvetical);
        nodata_tv2.setTypeface(helvetical);
        noparts_found.setVisibility(View.GONE);
       // register_link=view.findViewById(R.id.register_link);


        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        private_recyclerView.setLayoutManager(manager);
        private_recyclerView.setHasFixedSize(true);
        privateCarAdapter = new MyCarAdapter(getActivity(), carListList);
        private_recyclerView.setAdapter(privateCarAdapter);




        if (Util.Operations.isOnline(getContext())) {
            new MyCars_Task().execute();
        }
        else
        {
            Toasty.warning(getContext(), "Check your Internet Connection", Toast.LENGTH_SHORT, true).show();
        }




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_select_cartype);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/helevetical.ttf");
                ImageView close_icon=dialog.findViewById(R.id.close_icon);
               TextView private_car=dialog.findViewById(R.id.private_car) ;
               TextView commercial_car=dialog.findViewById(R.id.commercial_car);
                private_car.setTypeface(tf);
                commercial_car.setTypeface(tf);

                commercial_car.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent c=new Intent(getActivity(), CommercialCarListActivity.class);
                        startActivity(c);
                        getActivity().finish();
                    }
                });



                private_car.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i=new Intent(getActivity(), PrivateCarRegisterActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    }
                });


                close_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });



        return view;

    }



    //MYCARS API CALL: ---------------------------------------------------------------------->
    public class MyCars_Task extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            //av_loader.setVisibility(View.VISIBLE);
            mShimmerViewContainer.startShimmerAnimation();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_GET_CAR_DETAIL+get_userid_from_login,get_apikey_from_login);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //av_loader.setVisibility(View.GONE);
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);

        try{

            JSONObject jo = new JSONObject(s);
            String status = jo.getString("status");
            if (status.equals("true")) {
                String message=jo.getString("message");
                JSONObject data_object=jo.getJSONObject("data");
                JSONArray car_array=data_object.getJSONArray("vehicles");

                if(car_array.length()==0)
                {
                    noparts_found.setVisibility(View.VISIBLE);
                }
                else {
                    noparts_found.setVisibility(View.GONE);
                    for(int i=0;i<car_array.length();i++)
                    {
                        try {
                            commercialCarList = new CommercialCarListPojo();
                            JSONObject jsonObject = car_array.getJSONObject(i);
                            commercialCarList.vehicle_id=jsonObject.getString("id");
                            commercialCarList.vin=jsonObject.getString("vin");
                            commercialCarList.user_id=jsonObject.getString("userId");
                            commercialCarList.vehicle_type=jsonObject.getString("vehicleTypeId");
                            commercialCarList.year=jsonObject.getString("year");
                            commercialCarList.make=jsonObject.getString("make");
                            commercialCarList.model=jsonObject.getString("model");
                            commercialCarList.trim=jsonObject.getString("trim");
                            commercialCarList.mileage_range=jsonObject.getString("mileageRange");
                            commercialCarList.actual_mileage=jsonObject.getString("actualMileage");
                            commercialCarList.carImage=jsonObject.getString("image");
                            commercialCarList.mileage=jsonObject.getString("vehicleType");
                            commercialCarList.business_type=jsonObject.getString("businessType");
                            commercialCarList.companyname=jsonObject.getString("companyName");
                            commercialCarList.country=jsonObject.getString("country");
                            carListList.add(commercialCarList);
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
            private_recyclerView.setAdapter(privateCarAdapter);


        }
    }

}
