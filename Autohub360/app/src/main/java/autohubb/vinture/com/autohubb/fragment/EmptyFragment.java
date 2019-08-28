package autohubb.vinture.com.autohubb.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;

public class EmptyFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mycars, container, false);
        View v1 = getActivity().getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(getActivity(), v1);


        return view;
    }




    //MYCARS API CALL: ---------------------------------------------------------------------->

    public class MyCars_Task extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;


        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading..., please wait.");
            dialog.show();


        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            /*try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("adminId",userid_str);
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequestToken(Config.GET_CAR_LIST,token,json);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }*/
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

/*
            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");

                if (status.equals("true")) {
                    JSONArray jsonArray=jo.getJSONArray("cars");
                    noparts_found.setVisibility(View.GONE);
                    for(int l=0;l<jsonArray.length();l++)
                    {
                        carList = new PrivateCarList();
                        JSONObject jsonObject =jsonArray.getJSONObject(l);
                        carList.car_stockid=jsonObject.getString("carstock_id");
                        carList.car_adminid=jsonObject.getString("admin_id");
                        carList.car_usertype=jsonObject.getString("user_type");
                        carList.car_date=jsonObject.getString("date");
                        carList.car_type=jsonObject.getString("car_type");
                        carList.car_year=jsonObject.getString("car_year");
                        carList.car_brand=jsonObject.getString("car_brand");
                        carList.car_model=jsonObject.getString("car_model");

                        carList.car_mileage=jsonObject.getString("car_mileage");
                        carList.car_location=jsonObject.getString("car_location");
                        carList.car_mileage=jsonObject.getString("car_mileage");
                        carList.car_vinstatus=jsonObject.getString("vin_status");
                        carList.car_engine=jsonObject.getString("engine");
                        carList.car_vehicleclass=jsonObject.getString("vehicle_class");
                        carList.car_transmission=jsonObject.getString("transmission");
                        carList.car_doors=jsonObject.getString("pasenger_door");
                        carList.car_manufacture=jsonObject.getString("manufactured");
                        carList.car_ac_condition=jsonObject.getString("ac_condition");
                        carList.car_interior_color=jsonObject.getString("interior_color");
                        carList.car_exterior_color=jsonObject.getString("exterior_color");
                        carList.car_keys=jsonObject.getString("car_keys");
                        carList.car_startcode=jsonObject.getString("start_code");
                        carList.car_description=jsonObject.getString("description");
                        carList.car_price=jsonObject.getString("price");
                        carList.car_image=jsonObject.getString("car_image");
                        carListList.add(carList);
                    }


                    // Setup and Handover data to recyclerview


                    LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setHasFixedSize(true);
                    viewCarAdapter = new ViewCarAdapter(ViewCarActivity.this, carListList);
                    recyclerView.setAdapter(viewCarAdapter);


                } else {
                    //String message = jo.getString("message");
                    noparts_found.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
            }
*/
        }
    }



}
