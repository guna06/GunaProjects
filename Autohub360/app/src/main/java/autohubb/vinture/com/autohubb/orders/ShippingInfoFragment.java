package autohubb.vinture.com.autohubb.orders;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;

public class ShippingInfoFragment extends Fragment {
    AVLoadingIndicatorView avi;
    String get_mail_from_reg,get_otp_from_reg,get_apikey_from_login,get_email_from_login,get_userid_from_login,get_username_from_login;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    TextView product_info_tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shipping_info, container, false);
        View v1 = getActivity().getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(getActivity(), v1);


        product_info_tv=view.findViewById(R.id.product_info_tv);

        sharedlogin = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg=sharedlogin.getString("get_otp_from_reg","");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login=sharedlogin.getString("get_email_from_login","");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login=sharedlogin.getString("get_username_from_login","");


        avi=view.findViewById(R.id.avi);

        new CallGetPolicy_Task().execute();



        return view;
    }

    //Get Policy ---------------------------------------------------------------------->
    public class CallGetPolicy_Task extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;


        protected void onPreExecute() {
            super.onPreExecute();
            avi.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_GET_POLICY,get_apikey_from_login);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            avi.setVisibility(View.GONE);

            try{
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                Log.e("tag","1");

                if (status.equals("true")) {
                    Log.e("tag","2");
                    String message=jo.getString("message");
                    Log.e("tag","3");

                    JSONObject data_object=jo.getJSONObject("data");
                    JSONObject first=data_object.getJSONObject("shipping_poliy");

                    String id=first.getString("id");
                    String key=first.getString("key");
                    String value=first.getString("value");

                    Log.e("tag","3"+id+key+value);
                    product_info_tv.setText(value);


                }
                else
                {

                }


            }
            catch (Exception e)
            {

            }




        }
    }
}
