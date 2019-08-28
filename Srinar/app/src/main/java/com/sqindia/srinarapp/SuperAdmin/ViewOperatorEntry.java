package com.sqindia.srinarapp.SuperAdmin;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.sqindia.srinarapp.Adapter.OperatorListAdapter;
import com.sqindia.srinarapp.ElectroplatingModule.ElectroplateEntryActivity;
import com.sqindia.srinarapp.Fonts.FontsOverride;
import com.sqindia.srinarapp.Model.OperatorList;
import com.sqindia.srinarapp.R;
import com.sqindia.srinarapp.Utils.Config;
import com.sqindia.srinarapp.Utils.HttpUtils;
import com.sqindia.srinarapp.Utils.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ViewOperatorEntry extends Activity {

    LinearLayout back;
    String session_token;
    SharedPreferences.Editor editor;
    OperatorList operatorList;
    private List<OperatorList> operatorListListModel;
    OperatorListAdapter operatorListAdapter;
    RecyclerView view_mac_recycler;
    EditText myFilter;
    ArrayAdapter<String> dataAdapter = null;
    ArrayList<String> names=new ArrayList<>();
    //ListView listview;
    Dialog loader_dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_operator);


        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ViewOperatorEntry.this, v1);


        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        session_token = sharedPreferences.getString("str_sessiontoken", "");
        operatorListListModel=new ArrayList<>();

        //************* Add Cast:
        back=findViewById(R.id.back);
        view_mac_recycler=findViewById(R.id.view_mac_recycler);


        //set default Loader:
        loader_dialog = new Dialog(ViewOperatorEntry.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);



        if (Util.Operations.isOnline(ViewOperatorEntry.this)) {

            new viewOperatorAsync().execute();
        }
        else
        {
            new SweetAlertDialog(ViewOperatorEntry.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("No Internet Connectivity")
                    .show();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(getApplicationContext(),AddOperator.class);
                startActivity(back);
                finish();
            }
        });


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back=new Intent(getApplicationContext(),AddOperator.class);
        startActivity(back);
        finish();
    }

    //@@@@@@@@@@@@@@@@@@@@@@@@ CALL VIEW OPERATOR API
    public class viewOperatorAsync extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";


            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_VIEW_OPERATOR, session_token);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loader_dialog.dismiss();

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");

                if (status.equals("true")) {

                    JSONArray jsonArray=jo.getJSONArray("operator");

                    for(int l=0;l<jsonArray.length();l++)
                    {

                        operatorList = new OperatorList();
                        JSONObject jsonObject =jsonArray.getJSONObject(l);
                        operatorList._id=jsonObject.getString("operatorid");
                        operatorList.operator_id=jsonObject.getString("employeeid");
                        operatorList.operator_name=jsonObject.getString("operatorname");
                        operatorList.operator_dept=jsonObject.getString("dept");
                        names.add(jsonObject.getString("operatorname"));
                        operatorListListModel.add(operatorList);

                    }


                } else {
                    String msg = jo.getString("message");
                    new SweetAlertDialog(ViewOperatorEntry.this, SweetAlertDialog.WARNING_TYPE)
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
