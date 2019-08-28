package autohubb.vinture.com.autohubb.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.VehicleModule.MyCarsFragment;
import autohubb.vinture.com.autohubb.activity.PaymentsInfoActivity;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;

public class HomeFragment extends Fragment {

    TextView t1,t2,private_tv,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14,t15,t16,t17,t18,t19,t20,t21,t22,t23,t24,t25,t26,t27,t28,t29;
    Typeface tf,tf_bold;
    LinearLayout mycar_lv,payments_lnr,add_view_cars;

    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    String get_mail_from_reg,get_otp_from_reg,get_apikey_from_login,get_email_from_login,get_userid_from_login,get_username_from_login;
    AVLoadingIndicatorView av_loader,avi_commercial;
    final long DELAY_MS = 300;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;
    Timer timer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/helevetical.ttf");
        //tf_bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/heleveticalBold.TTF");

        View v1 = getActivity().getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(getActivity(), v1);
        sharedlogin = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg=sharedlogin.getString("get_otp_from_reg","");

        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login=sharedlogin.getString("get_email_from_login","");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login=sharedlogin.getString("get_username_from_login","");
        av_loader=view.findViewById(R.id.avi);
        avi_commercial=view.findViewById(R.id.avi_commercial);
        mycar_lv=view.findViewById(R.id.mycar_lv);
        payments_lnr=view.findViewById(R.id.payments_lnr);
        add_view_cars=view.findViewById(R.id.add_view_cars);

        //row 1
        t1=view.findViewById(R.id.t1);
        t2=view.findViewById(R.id.t2);
        private_tv=view.findViewById(R.id.private_tv);
        t4=view.findViewById(R.id.t4);
        t5=view.findViewById(R.id.t5);
        t6=view.findViewById(R.id.t6);

        //row 2
        t7=view.findViewById(R.id.t7);
        t8=view.findViewById(R.id.t8);
        t9=view.findViewById(R.id.t9);
        t10=view.findViewById(R.id.t10);
        t11=view.findViewById(R.id.t11);
        t12=view.findViewById(R.id.t12);

        //row 3
        t13=view.findViewById(R.id.t13);
        t14=view.findViewById(R.id.t14);
        t15=view.findViewById(R.id.t15);
        t16=view.findViewById(R.id.t16);
        t17=view.findViewById(R.id.t17);
        t18=view.findViewById(R.id.t18);

        //row 4
        t19=view.findViewById(R.id.t19);
        t20=view.findViewById(R.id.t20);
        t21=view.findViewById(R.id.t21);
        t22=view.findViewById(R.id.t22);
        t23=view.findViewById(R.id.t23);
        t24=view.findViewById(R.id.t24);
        t25=view.findViewById(R.id.t25);
        t26=view.findViewById(R.id.t26);
        t27=view.findViewById(R.id.t27);
        t28=view.findViewById(R.id.t28);
        t29=view.findViewById(R.id.t29);

        //toolbar_title.setTypeface(tf);
        t1.setTypeface(tf);
        t2.setTypeface(tf);
        private_tv.setTypeface(tf);
        t4.setTypeface(tf);
        t5.setTypeface(tf);
        t6.setTypeface(tf);

        t7.setTypeface(tf);
        t8.setTypeface(tf);
        t9.setTypeface(tf);
        t10.setTypeface(tf);
        t11.setTypeface(tf);
        t12.setTypeface(tf);

        t13.setTypeface(tf);
        t14.setTypeface(tf);
        t15.setTypeface(tf);
        t16.setTypeface(tf);
        t17.setTypeface(tf);
        t18.setTypeface(tf);

        t19.setTypeface(tf);
        t20.setTypeface(tf);
        t21.setTypeface(tf);
        t22.setTypeface(tf);
        t23.setTypeface(tf);
        t24.setTypeface(tf);
        t25.setTypeface(tf);
        t26.setTypeface(tf);
        t27.setTypeface(tf);
        t28.setTypeface(tf);
        t29.setTypeface(tf);
        private_tv.setVisibility(View.GONE);
        t5.setVisibility(View.GONE);


        add_view_cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCarsFragment myCarsFragment = new MyCarsFragment();
            FragmentTransaction transaction1 = getActivity().getSupportFragmentManager().beginTransaction();
            transaction1.replace(R.id.frame, myCarsFragment);
            transaction1.commit();
            }
        });

        payments_lnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent payment=new Intent(getActivity(),PaymentsInfoActivity.class);
                startActivity(payment);
                getActivity().finish();
            }
        });

        new MyDashboard_Task().execute();


        return view;
    }




    //MYCARS API CALL: ---------------------------------------------------------------------->
    public class MyDashboard_Task extends AsyncTask<String, Void, String> {
       // private ProgressDialog dialog;


        protected void onPreExecute() {
            super.onPreExecute();
            av_loader.setVisibility(View.VISIBLE);
            avi_commercial.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

           try {
                JSONObject jsonObject = new JSONObject();

                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_DASHBOARD+get_userid_from_login,get_apikey_from_login);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

           // av_loader.setVisibility(View.GONE);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    av_loader.setVisibility(View.GONE);
                    avi_commercial.setVisibility(View.GONE);
                    private_tv.setVisibility(View.VISIBLE);
                    t5.setVisibility(View.VISIBLE);

                }
            }, 2000);


            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");

                if (status.equals("true")) {
                    JSONObject object=jo.getJSONObject("data");
                    String private_count=object.getString("private");
                    String commercial_count=object.getString("commercial");

                    private_tv.setText(private_count);
                    t5.setText(commercial_count);

                } else {

                }
            } catch (Exception e) {
            }

        }
    }

}
