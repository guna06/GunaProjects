package com.sqindia.autolane360mobile.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sqindia.autolane360mobile.R;
import com.sqindia.autolane360mobile.adapter.CustomAdapterArrayList;
import com.sqindia.autolane360mobile.utils.Util;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class AddCarPageTwoFragment extends Fragment {


    Spinner caryear_spn,carmake_spn,carmodel_spn,cartype_spn,startcode_spn,engine_spn,transmission_spn;
    Typeface helvetica;
    ArrayList<String> caryear_arraylist=new ArrayList<>();
    ArrayList<String> carmake_arraylist=new ArrayList<>();
    ArrayList<String> carmodel_arraylist=new ArrayList<>();
    ArrayList<String> cartype_arraylist=new ArrayList<>();
    ArrayList<String> startcode_arraylist=new ArrayList<>();
    ArrayList<String> engine_arraylist=new ArrayList<>();
    ArrayList<String> transmission_arraylist=new ArrayList<>();
    CustomAdapterArrayList carYearAdapter,carMakeAdapter,carModelAdapter,carTypeAdapter,startCodeAdapter,engineAdapter,transmissionAdapter
           ;


    public static String str_year,str_carmake,str_carmodel,str_cartypes,str_startcodes,str_engines,str_transmission;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_two, container, false);
        helvetica = Typeface.createFromAsset(getActivity().getAssets(), "fonts/helevetical.ttf");

        caryear_spn=view.findViewById(R.id.caryear_spn);
        carmake_spn=view.findViewById(R.id.carmake_spn);
        carmodel_spn=view.findViewById(R.id.carmodel_spn);
        cartype_spn=view.findViewById(R.id.cartype_spn);
        startcode_spn=view.findViewById(R.id.startcode_spn);
        engine_spn=view.findViewById(R.id.engine_spn);
        transmission_spn=view.findViewById(R.id.transmission_spn);



        carmake_arraylist.add("Select Car Make *");
        carMakeAdapter = new CustomAdapterArrayList(getActivity(), android.R.layout.simple_dropdown_item_1line, carmake_arraylist) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {

                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTypeface(helvetica);
                tv.setTextSize(16);
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.spin_text));
                }
                return view;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextSize(19);
                tv.setTypeface(helvetica);
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(R.color.hintcolor));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.textcolor));
                }
                return view;
            }
        };
        carmake_spn.setAdapter(carMakeAdapter);


        carmodel_arraylist.add("Select Car Model *");
        carModelAdapter = new CustomAdapterArrayList(getActivity(), android.R.layout.simple_dropdown_item_1line, carmodel_arraylist) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {

                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTypeface(helvetica);
                tv.setTextSize(16);
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.spin_text));
                }
                return view;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextSize(19);
                tv.setTypeface(helvetica);
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(R.color.hintcolor));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.textcolor));
                }
                return view;
            }
        };
        carmodel_spn.setAdapter(carModelAdapter);


        if(caryear_arraylist.isEmpty())
        {
            if (Util.Operations.isOnline(getActivity())) {
                new getCarYear().execute();
            }
            else
            {
                Toast.makeText(getActivity(),"Please Check Internet Connectivity",Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            carYearAdapter = new CustomAdapterArrayList(getActivity(), android.R.layout.simple_dropdown_item_1line, caryear_arraylist) {
                @Override
                public boolean isEnabled(int position) {
                    if (position == 0) {

                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    tv.setTypeface(helvetica);
                    tv.setTextSize(16);
                    if (position == 0) {
                        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        tv.setTextColor(getResources().getColor(R.color.spin_text));
                    }
                    return view;
                }


                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    tv.setTextSize(19);
                    tv.setTypeface(helvetica);
                    if (position == 0) {
                        tv.setTextColor(getResources().getColor(R.color.hintcolor));
                    } else {
                        tv.setTextColor(getResources().getColor(R.color.textcolor));
                    }
                    return view;
                }
            };
            caryear_spn.setAdapter(carYearAdapter);


            carMakeAdapter = new CustomAdapterArrayList(getActivity(), android.R.layout.simple_dropdown_item_1line, carmake_arraylist) {
                @Override
                public boolean isEnabled(int position) {
                    if (position == 0) {

                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    tv.setTypeface(helvetica);
                    tv.setTextSize(16);
                    if (position == 0) {
                        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        tv.setTextColor(getResources().getColor(R.color.spin_text));
                    }
                    return view;
                }


                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    tv.setTextSize(19);
                    tv.setTypeface(helvetica);
                    if (position == 0) {
                        tv.setTextColor(getResources().getColor(R.color.hintcolor));
                    } else {
                        tv.setTextColor(getResources().getColor(R.color.textcolor));
                    }
                    return view;
                }
            };
            carmake_spn.setAdapter(carMakeAdapter);

            carModelAdapter = new CustomAdapterArrayList(getActivity(), android.R.layout.simple_dropdown_item_1line, carmodel_arraylist) {
                @Override
                public boolean isEnabled(int position) {
                    if (position == 0) {

                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    tv.setTypeface(helvetica);
                    tv.setTextSize(16);
                    if (position == 0) {
                        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        tv.setTextColor(getResources().getColor(R.color.spin_text));
                    }
                    return view;
                }


                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    tv.setTextSize(19);
                    tv.setTypeface(helvetica);
                    if (position == 0) {
                        tv.setTextColor(getResources().getColor(R.color.hintcolor));
                    } else {
                        tv.setTextColor(getResources().getColor(R.color.textcolor));
                    }
                    return view;
                }
            };
            carmodel_spn.setAdapter(carModelAdapter);
        }




        //Add Car Type Data:
        cartype_arraylist.add("Select Car Types");
        cartype_arraylist.add("Automobiles");
        cartype_arraylist.add("Pick-ups");
        cartype_arraylist.add("SUVs");
        cartype_arraylist.add("Motorcycles");
        cartype_arraylist.add("Heavy Duty Trucks");
        cartype_arraylist.add("Trailers");
        cartype_arraylist.add("Buses");
        cartype_arraylist.add("Equipments");
        cartype_arraylist.add("Misellellous");
        cartype_arraylist.add("Recreational"); cartype_arraylist.add("Vans");

        carTypeAdapter = new CustomAdapterArrayList(getActivity(), R.layout.spinner_item, cartype_arraylist) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {

                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTypeface(helvetica);
                tv.setTextSize(16);
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.spin_text));
                }
                return view;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextSize(19);
                tv.setTypeface(helvetica);
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(R.color.hintcolor));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.textcolor));
                }
                return view;
            }
        };
        cartype_spn.setAdapter(carTypeAdapter);



        //Add Start Code Data:
        startcode_arraylist.add("Select Start Codes");
        startcode_arraylist.add("Run & Drive");
        startcode_arraylist.add("Run");
        startcode_arraylist.add("Not Working");

        startCodeAdapter = new CustomAdapterArrayList(getActivity(), android.R.layout.simple_dropdown_item_1line, startcode_arraylist) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {

                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTypeface(helvetica);
                tv.setTextSize(16);
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.spin_text));
                }
                return view;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextSize(19);
                tv.setTypeface(helvetica);
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(R.color.hintcolor));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.textcolor));
                }
                return view;
            }
        };
        startcode_spn.setAdapter(startCodeAdapter);



        //Add Engine Data:
        engine_arraylist.add("Select Engines");
        engine_arraylist.add("4 Cylinder");
        engine_arraylist.add("6 Cylinder");
        engine_arraylist.add("8 Cylinder");

        engineAdapter = new CustomAdapterArrayList(getActivity(), android.R.layout.simple_dropdown_item_1line, engine_arraylist) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {

                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTypeface(helvetica);
                tv.setTextSize(16);
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.spin_text));
                }
                return view;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextSize(19);
                tv.setTypeface(helvetica);
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(R.color.hintcolor));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.textcolor));
                }
                return view;
            }
        };
        engine_spn.setAdapter(engineAdapter);




        //Add Transmission Data:
        transmission_arraylist.add("Select Car Transmission");
        transmission_arraylist.add("Automatic");
        transmission_arraylist.add("Manual");


        transmissionAdapter = new CustomAdapterArrayList(getActivity(), android.R.layout.simple_dropdown_item_1line, transmission_arraylist) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {

                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTypeface(helvetica);
                tv.setTextSize(16);
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.spin_text));
                }
                return view;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextSize(19);
                tv.setTypeface(helvetica);
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(R.color.hintcolor));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.textcolor));
                }
                return view;
            }
        };
        transmission_spn.setAdapter(transmissionAdapter);







        /*// Initializing a String Array
        String[] acConditions = new String[]{
                "Select AC Condition",
                "Working Great",
                "Working",
                "Not Working"


        };




        // Initializing a String Array
        String[] carKeys = new String[]{
                "Select Car Keys",
                "Available",
                "Not Available"



        };
*/


        caryear_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_year= (String) adapterView.getItemAtPosition(i);
                Log.e("tag","print year------------>"+str_year);
                if (Util.Operations.isOnline(getActivity())) {
                    new getCarMake(str_year).execute();
                }
                else
                {
                    Toast.makeText(getActivity(),"Please check Internet Connectivity",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        carmake_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_carmake= (String) adapterView.getItemAtPosition(i);
                Log.e("tag","print make------------>"+str_carmake);
                if (Util.Operations.isOnline(getActivity())) {
                    new getCarModel(str_year,str_carmake).execute();
                }
                else
                {
                    Toast.makeText(getActivity(),"Please check Internet Connectivity",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        carmodel_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_carmodel= (String) adapterView.getItemAtPosition(i);
                Log.e("tag","print Model------------>"+str_carmodel);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        cartype_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_cartypes= (String) adapterView.getItemAtPosition(i);
                Log.e("tag","print Type------------>"+str_cartypes);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        startcode_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_startcodes= (String) adapterView.getItemAtPosition(i);
                Log.e("tag","print startcodes------------>"+str_startcodes);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        engine_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_engines= (String) adapterView.getItemAtPosition(i);
                Log.e("tag","print Engines------------>"+str_engines);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        transmission_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_transmission= (String) adapterView.getItemAtPosition(i);
                Log.e("tag","print Transmission------------>"+str_transmission);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });








        return view;
    }



    //GET YEAR API CALL: ---------------------------------------------------------------------->
    public class getCarYear extends AsyncTask<String, String, String> {
        private ProgressDialog dialog;
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading Year..., please wait.");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://carqueryapi.com/api/0.3/?cmd=getYears")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Postman-Token", "9a479868-31a0-4760-a9d8-7d9007955ad5")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            try {
                JSONObject jo = new JSONObject(s);
                caryear_arraylist.clear();
                JSONObject object=jo.getJSONObject("Years");
                String min_year = object.getString("min_year");
                String max_year = object.getString("max_year");


                int minyear=Integer.parseInt(min_year);
                int maxyaer=Integer.parseInt(max_year);

                caryear_arraylist.add(0,"Select Year *");
                for(int i=maxyaer;i>=minyear;i--)

                {

                    caryear_arraylist.add(String.valueOf(i));
                }

                caryear_spn.setVisibility(View.VISIBLE);

// Spinner Adapter:
                carYearAdapter = new CustomAdapterArrayList(getActivity(), android.R.layout.simple_dropdown_item_1line, caryear_arraylist) {
                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {

                            return false;
                        } else {
                            return true;
                        }
                    }

                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setTypeface(helvetica);
                        tv.setTextSize(16);
                        if (position == 0) {
                            tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                        } else {
                            tv.setTextColor(getResources().getColor(R.color.spin_text));
                        }
                        return view;
                    }


                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setTextSize(19);
                        tv.setTypeface(helvetica);
                        if (position == 0) {
                            tv.setTextColor(getResources().getColor(R.color.hintcolor));
                        } else {
                            tv.setTextColor(getResources().getColor(R.color.textcolor));
                        }
                        return view;
                    }
                };
                caryear_spn.setAdapter(carYearAdapter);


                if (dialog.isShowing()) {
                    dialog.dismiss();
                }


            } catch (Exception e) {

            }
        }
    }



    //GET MAKE API CALL: ---------------------------------------------------------------------->
    public class getCarMake extends AsyncTask<String, String, String> {
        private ProgressDialog dialog1;
        String year_str;
        public getCarMake(String str_caryear) {
            this.year_str=str_caryear;

        }

        protected void onPreExecute() {
            super.onPreExecute();
            dialog1 = new ProgressDialog(getActivity());
            dialog1.setMessage("Loading Car Make..., please wait.");
            dialog1.show();
        }

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.carqueryapi.com/api/0.3/?cmd=getMakes&year="+year_str)
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Postman-Token", "9a479868-31a0-4760-a9d8-7d9007955ad5")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (dialog1.isShowing()) {
                dialog1.dismiss();
            }


            try {
                JSONObject jo = new JSONObject(s);
                carmake_arraylist.clear();
                JSONArray jsonArray=jo.getJSONArray("Makes");
                carmake_arraylist.add(0,"Select Car Make *");
                for(int l=0;l<jsonArray.length();l++)
                {
                    JSONObject object=jsonArray.getJSONObject(l);
                    String make_id = object.getString("make_id");
                    String make_display = object.getString("make_display");
                    carmake_arraylist.add(make_display);
                }

                carMakeAdapter = new CustomAdapterArrayList(getActivity(), android.R.layout.simple_dropdown_item_1line, carmake_arraylist) {
                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {

                            return false;
                        } else {
                            return true;
                        }
                    }

                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setTypeface(helvetica);
                        tv.setTextSize(16);
                        if (position == 0) {
                            tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                        } else {
                            tv.setTextColor(getResources().getColor(R.color.spin_text));
                        }
                        return view;
                    }


                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setTextSize(19);
                        tv.setTypeface(helvetica);
                        if (position == 0) {
                            tv.setTextColor(getResources().getColor(R.color.hintcolor));
                        } else {
                            tv.setTextColor(getResources().getColor(R.color.textcolor));
                        }
                        return view;
                    }
                };
                carmake_spn.setAdapter(carMakeAdapter);


            } catch (Exception e) {

            }
        }
    }



    //GET MODEL API CALL: ---------------------------------------------------------------------->
    public class getCarModel extends AsyncTask<String, String, String> {
        private ProgressDialog dialog2;
        String getyear_str;
        String make_str;

        public getCarModel(String str_caryear, String str_carmake) {
            this.getyear_str=str_caryear;
            this.make_str=str_carmake;

        }


        protected void onPreExecute() {
            super.onPreExecute();
            dialog2 = new ProgressDialog(getActivity());
            dialog2.setMessage("Loading Car Model..., please wait.");
            dialog2.show();
        }

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.carqueryapi.com/api/0.3/?cmd=getModels&year="+getyear_str+"&make="+make_str)
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Postman-Token", "9a479868-31a0-4760-a9d8-7d9007955ad5")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (dialog2.isShowing()) {
                dialog2.dismiss();
            }

            try {
                JSONObject jo = new JSONObject(s);

                JSONArray jsonArray=jo.getJSONArray("Models");
                carmodel_arraylist.clear();

                carmodel_arraylist.add(0,"Select Car Model *");

                for(int l=0;l<jsonArray.length();l++)
                {
                    JSONObject object=jsonArray.getJSONObject(l);
                    String model_name = object.getString("model_name");
                    carmodel_arraylist.add(model_name);
                }


                carModelAdapter = new CustomAdapterArrayList(getActivity(), android.R.layout.simple_dropdown_item_1line, carmodel_arraylist) {
                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {

                            return false;
                        } else {
                            return true;
                        }
                    }

                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setTypeface(helvetica);
                        tv.setTextSize(16);
                        if (position == 0) {
                            tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                        } else {
                            tv.setTextColor(getResources().getColor(R.color.spin_text));
                        }
                        return view;
                    }


                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setTextSize(19);
                        tv.setTypeface(helvetica);
                        if (position == 0) {
                            tv.setTextColor(getResources().getColor(R.color.hintcolor));
                        } else {
                            tv.setTextColor(getResources().getColor(R.color.textcolor));
                        }
                        return view;
                    }
                };
                carmodel_spn.setAdapter(carModelAdapter);


            } catch (Exception e) {

            }
        }
    }
}
