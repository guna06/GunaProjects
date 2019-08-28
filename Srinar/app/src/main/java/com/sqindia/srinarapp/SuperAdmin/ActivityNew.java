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
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.sqindia.srinarapp.Adapter.CustomAdapter;
import com.sqindia.srinarapp.Adapter.OperatorListAdapter;
import com.sqindia.srinarapp.Fonts.FontsOverride;
import com.sqindia.srinarapp.MachiningModule.MachiningEntryActivity;
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


public class ActivityNew extends Activity {


    RecyclerView recyclerView;
    EditText editTextSearch;
    ArrayList<String> names;
    String session_token;
    SharedPreferences.Editor editor;
    OperatorList operatorList;
    private List<OperatorList> operatorListListModel;
    OperatorListAdapter adapter;
    LinearLayout back;
    Dialog loader_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_operator);


        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ActivityNew.this, v1);

        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        session_token = sharedPreferences.getString("str_sessiontoken", "");
        operatorListListModel=new ArrayList<>();
        back=findViewById(R.id.back);


        //set default Loader:
        loader_dialog = new Dialog(ActivityNew.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);

        names = new ArrayList<>();


        if (Util.Operations.isOnline(ActivityNew.this)) {

            new viewOperatorAsync().execute();
        }
        else
        {
            new SweetAlertDialog(ActivityNew.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("No Internet Connectivity")
                    .show();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        editTextSearch = (EditText) findViewById(R.id.editTextSearch);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OperatorListAdapter(ActivityNew.this, operatorListListModel);
        recyclerView.setAdapter(adapter);


        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(getApplicationContext(),AddOperator.class);
                startActivity(back);
                finish();
            }
        });


    }



    private void filter(String text) {
        //new array list that will hold the filtered data
//        ArrayList<String> filterdNames = new ArrayList<>();
        List<OperatorList> filteredList = new ArrayList<>();

        //looping through existing elements
//        for (String s : operatorListListModel.) {
//            //if the existing elements contains the search input
//            if (s.toLowerCase().contains(text.toLowerCase())) {
//                //adding the element to filtered list
//                filterdNames.add(s);
//            }
//        }

        for (int i = 0; i < operatorListListModel.size(); i++){

            String s = operatorListListModel.get(i).operator_name;
            if (s.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filteredList.add(operatorListListModel.get(i));
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filteredList);
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
                }
            } catch (Exception e) {

            }
        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back=new Intent(getApplicationContext(),AddOperator.class);
        startActivity(back);
        finish();
    }

}
