package autohubb.vinture.com.autohubb.MyOrdersModule;

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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;
import autohubb.vinture.com.autohubb.utils.Util;
import es.dmoral.toasty.Toasty;

public class MyOrdersFragment extends Fragment {
    RecyclerView recyclerView_requst;
    AVLoadingIndicatorView av_loader;
    String get_mail_from_reg,get_otp_from_reg,get_apikey_from_login,get_email_from_login,get_userid_from_login,get_username_from_login;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    LinearLayout noparts_found;
    MyOrdersPojo myOrdersPojo;
    private List<MyOrdersPojo> myOrdersPojoList=new ArrayList<>();
    MyOrderAdapter myOrderAdapter;
    private ShimmerFrameLayout mShimmerViewContainer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        View v1 = getActivity().getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(getActivity(), v1);


        recyclerView_requst=view.findViewById(R.id.recyclerView_requst);
        av_loader = view. findViewById(R.id.avi);
        noparts_found=view.findViewById(R.id.noparts_found);
        noparts_found.setVisibility(View.GONE);


        sharedlogin = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg=sharedlogin.getString("get_otp_from_reg","");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login=sharedlogin.getString("get_email_from_login","");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login=sharedlogin.getString("get_username_from_login","");
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView_requst.setLayoutManager(manager);
        recyclerView_requst.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayout.VERTICAL));
        recyclerView_requst.setHasFixedSize(true);
        myOrderAdapter = new MyOrderAdapter(getActivity(), myOrdersPojoList);
        recyclerView_requst.setAdapter(myOrderAdapter);

        if (Util.Operations.isOnline(getContext())) {

            new MyOrders_Task().execute();
        }
        else
        {
            Toasty.warning(getContext(), "Check your Internet Connection", Toast.LENGTH_SHORT, true).show();
        }


        return view;
    }




    @Override
    public void onPause() {
        super.onPause();

       /* Intent i=new Intent(getContext(), MainActivity.class);
        startActivity(i);
        getActivity().finish();*/
    }


    @Override
    public void onStop() {
        super.onStop();
    }


    //MyQuotes API CALL: ---------------------------------------------------------------------->
    public class MyOrders_Task extends AsyncTask<String, Void, String> {
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
                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_LIST_ORDERS+get_userid_from_login,get_apikey_from_login);
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
                    JSONArray car_array=data_object.getJSONArray("orders");

                    if(car_array.length()==0)
                    {
                        noparts_found.setVisibility(View.VISIBLE);
                    }
                    else {
                        noparts_found.setVisibility(View.GONE);
                        for(int i=0;i<car_array.length();i++)
                        {
                            try {
                                myOrdersPojo = new MyOrdersPojo();
                                JSONObject jsonObject = car_array.getJSONObject(i);
                                myOrdersPojo.orderId=jsonObject.getString("orderId");
                                myOrdersPojo.orderNumber=jsonObject.getString("orderNumber");
                                myOrdersPojo.orderStatus=jsonObject.getString("status");
                                myOrdersPojoList.add(myOrdersPojo);

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
            recyclerView_requst.setAdapter(myOrderAdapter);


        }







    }


}