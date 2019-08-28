package autohubb.vinture.com.autohubb.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import autohubb.vinture.com.autohubb.Adapter.CustomAdapterArrayList;
import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.utils.Util;

import static android.app.Activity.RESULT_OK;

public class CommercialCarFragment extends Fragment implements View.OnClickListener{
    Spinner caryear_spn, carmake_spn, carmodel_spn;
    ArrayList<String> caryear_arraylist = new ArrayList<>();
    ArrayList<String> carmake_arraylist = new ArrayList<>();
    ArrayList<String> carmodel_arraylist = new ArrayList<>();
    CustomAdapterArrayList carYearAdapter, carMakeAdapter, carModelAdapter;
    String str_year, str_carmake, str_carmodel;
    Typeface helvetica;
    ArrayList<String> city_arraylist=new ArrayList<>();
    ArrayList<String> trucktype_arraylist=new ArrayList<>();
    CustomAdapterArrayList cityAdapter,trucktype_adapter;
    Spinner city_spn,trucktype_spn;
    String str_city;
    EditText edt_company_name,edt_com_mail,edt_com_ph,edt_com_add1,edt_com_add2,edt_contact_name,edt_contact_ph;
    TextInputLayout til_company_name,til_company_mail,til_company_phone,til_company_address1,til_company_address2,til_contact_name,til_contact_phone;
    TextView head,submit_tv1;
    LinearLayout layout_submit1,layout_submit2,linear_content1,linear_content2;
    public static ImageView image1_iv, image2_iv,upload1,upload2;

    Dialog cameraDialog;
    LinearLayout lnr_takepic, lnr_takegallery;
    Bitmap bitmap;
    String selectedImagePath,path_carimg1,path_carimg2;


    protected static final int TAKE_CAR_PIC1 = 1;
    protected static final int TAKE_CAR_PIC2 = 2;
    protected static final int SELECT_CAR_PIC1 = 3;
    protected static final int SELECT_CAR_PIC2 = 4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_commercialcar, container, false);

        helvetica = Typeface.createFromAsset(getActivity().getAssets(), "fonts/helevetical.ttf");



        city_spn=view.findViewById(R.id.select_city_spn);
        trucktype_spn=view.findViewById(R.id.trucktype_spn);
        head=view.findViewById(R.id.head);

        head.setTypeface(helvetica);
        layout_submit1=view.findViewById(R.id.layout_submit1);
        layout_submit2=view.findViewById(R.id.layout_submit2);
        linear_content1=view.findViewById(R.id.linear_content1);
        linear_content2=view.findViewById(R.id.linear_content2);
        submit_tv1=view.findViewById(R.id.submit_tv1);


        image1_iv = view.findViewById(R.id.image1_iv);
        image2_iv = view.findViewById(R.id.image2_iv);


        layout_submit1.setVisibility(View.VISIBLE);
        layout_submit2.setVisibility(View.GONE);
        linear_content1.setVisibility(View.VISIBLE);
        linear_content2.setVisibility(View.GONE);


        til_company_name=view.findViewById(R.id.til_company_name);
        til_company_mail=view.findViewById(R.id.til_company_mail);
        til_company_phone=view.findViewById(R.id.til_company_phone);
        til_company_address1=view.findViewById(R.id.til_company_address1);
        til_company_address2=view.findViewById(R.id.til_company_address2);
        til_contact_name=view.findViewById(R.id.til_contact_name);
        til_contact_phone=view.findViewById(R.id.til_contact_phone);

        til_company_name.setTypeface(helvetica);
        til_company_mail.setTypeface(helvetica);
        til_company_phone.setTypeface(helvetica);
        til_company_address1.setTypeface(helvetica);
        til_company_address2.setTypeface(helvetica);
        til_contact_name.setTypeface(helvetica);
        til_contact_phone.setTypeface(helvetica);
        submit_tv1.setTypeface(helvetica);

        edt_company_name=view.findViewById(R.id.company_name_et);
        edt_com_mail=view.findViewById(R.id.company_mail_et);
        edt_com_ph=view.findViewById(R.id.company_phone_et);
        edt_com_add1=view.findViewById(R.id.company_address1_et);
        edt_com_add2=view.findViewById(R.id.company_address2_et);
        edt_contact_name=view.findViewById(R.id.contact_name_et);
        edt_contact_ph=view.findViewById(R.id.contact_phone_et);

        edt_company_name.setTypeface(helvetica);
        edt_com_mail.setTypeface(helvetica);
        edt_com_ph.setTypeface(helvetica);
        edt_com_add1.setTypeface(helvetica);
        edt_com_add2.setTypeface(helvetica);
        edt_contact_name.setTypeface(helvetica);
        edt_contact_ph.setTypeface(helvetica);


        caryear_spn = view.findViewById(R.id.year_spn);
        carmake_spn = view.findViewById(R.id.make_spn);
        carmodel_spn = view.findViewById(R.id.model_spn);


        cameraDialog = new Dialog(getActivity());
        cameraDialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);



        city_arraylist.add("Select City");
        city_arraylist.add("India");
        city_arraylist.add("Us");
        city_arraylist.add("Nygeria");
        city_arraylist.add("Ghana");



        trucktype_arraylist.add("Demo 1");
        trucktype_arraylist.add("Demo 2");
        trucktype_arraylist.add("Demo 3");
        trucktype_arraylist.add("Demo 4");


        cityAdapter = new CustomAdapterArrayList(getActivity(), android.R.layout.simple_dropdown_item_1line, city_arraylist) {
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
                tv.setTextSize(18);
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
                tv.setTextSize(18);
                tv.setTypeface(helvetica);
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(R.color.hintcolor));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.textcolor));
                }
                return view;
            }
        };
        city_spn.setAdapter(cityAdapter);


        trucktype_adapter = new CustomAdapterArrayList(getActivity(), android.R.layout.simple_dropdown_item_1line, trucktype_arraylist) {
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
                tv.setTextSize(18);
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
                tv.setTextSize(18);
                tv.setTypeface(helvetica);
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(R.color.hintcolor));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.textcolor));
                }
                return view;
            }
        };
        trucktype_spn.setAdapter(trucktype_adapter);

        city_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_city= (String) adapterView.getItemAtPosition(i);
                Log.e("tag","print City------------>"+str_city);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        layout_submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                linear_content1.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), R.anim.slide_from_left
                ));
                linear_content1.setVisibility(View.GONE);
                linear_content2.setVisibility(View.VISIBLE);
                layout_submit1.setVisibility(View.GONE);
                layout_submit2.setVisibility(View.VISIBLE);


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


                if (caryear_arraylist.isEmpty()) {
                    if (Util.Operations.isOnline(getActivity())) {
                        new getCarYear().execute();
                    } else {
                        Toast.makeText(getActivity(), "Please Check Internet Connectivity", Toast.LENGTH_LONG).show();
                    }
                }

                /*Intent myIntent = new Intent(getActivity(), RegisterActivity.class);
                ActivityOptions options =
                        ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_up, R.anim.slide_out_up);
                getActivity().startActivity(myIntent, options.toBundle());*/
            }
        });



        caryear_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_year = (String) adapterView.getItemAtPosition(i);

                if (Util.Operations.isOnline(getActivity())) {
                    new getCarMake(str_year).execute();
                } else {
                    Toast.makeText(getActivity(), "Please check Internet Connectivity", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        carmake_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_carmake = (String) adapterView.getItemAtPosition(i);
                Log.e("tag", "print make------------>" + str_carmake);
                if (Util.Operations.isOnline(getActivity())) {
                    new getCarModel(str_year, str_carmake).execute();
                } else {
                    Toast.makeText(getActivity(), "Please check Internet Connectivity", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        carmodel_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_carmodel = (String) adapterView.getItemAtPosition(i);
                Log.e("tag", "print Model------------>" + str_carmodel);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        image1_iv.setOnClickListener(this);
        image2_iv.setOnClickListener(this);




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







    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            // CAR 9
            case R.id.image1_iv:
                cameraDialog.setContentView(R.layout.camera_dialog);
                lnr_takepic = cameraDialog.findViewById(R.id.lnr_takepic);
                lnr_takegallery = cameraDialog.findViewById(R.id.lnr_takegallery);


                // if button is clicked, close the custom dialog
                lnr_takepic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraDialog.dismiss();
                        if (checkPermission()) {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, TAKE_CAR_PIC1);
                        }
                    }
                });


                lnr_takegallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraDialog.dismiss();
                        if (checkPermission()) {
                            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            pickIntent.setType("image/*");
                            startActivityForResult(pickIntent, SELECT_CAR_PIC1);
                        }
                    }
                });
                cameraDialog.show();

                break;


            // CAR 10
            case R.id.image2_iv:
                cameraDialog.setContentView(R.layout.camera_dialog);
                lnr_takepic = cameraDialog.findViewById(R.id.lnr_takepic);
                lnr_takegallery = cameraDialog.findViewById(R.id.lnr_takegallery);

                // if button is clicked, close the custom dialog
                lnr_takepic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraDialog.dismiss();
                        if (checkPermission()) {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, TAKE_CAR_PIC2);
                        }
                    }
                });


                lnr_takegallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraDialog.dismiss();
                        if (checkPermission()) {
                            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            pickIntent.setType("image/*");
                            startActivityForResult(pickIntent, SELECT_CAR_PIC2);
                        }
                    }
                });
                cameraDialog.show();
                break;


        }


    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if ((result == PackageManager.PERMISSION_GRANTED) && (result1 == PackageManager.PERMISSION_GRANTED) && (result2 == PackageManager.PERMISSION_GRANTED)) {
            return true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            return false;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        bitmap = null;
        selectedImagePath = null;

        //********************CAR PIC 5 -------TAKE
        if (resultCode == RESULT_OK && requestCode == TAKE_CAR_PIC1) {

            bitmap = (Bitmap) data.getExtras().get("data");
            File sd = Environment.getExternalStorageDirectory();
            File imagepath = new File(sd.getAbsolutePath() + File.separator +
                    "AutoHubb360");

            if (!imagepath.isDirectory())
            {
                imagepath.mkdirs();
            }

            File mediaFile4 = new File(imagepath + File.separator + "img_" +
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(mediaFile4);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            image1_iv.setImageBitmap(bitmap);
            //upload1.setVisibility(View.GONE);
            path_carimg1=mediaFile4.getAbsolutePath();
            Log.e("tag","Take Car Path1"+path_carimg1);
            /*edit.putString("store_birdeyeview_path",path_birdeye_view);
            edit.commit();*/
        }



        //********************CAR PIC 6 -------TAKE
        else if (resultCode == RESULT_OK && requestCode == TAKE_CAR_PIC2){
            bitmap = (Bitmap) data.getExtras().get("data");
            File sd = Environment.getExternalStorageDirectory();
            File imagepath = new File(sd.getAbsolutePath() + File.separator +
                    "AutoHubb360");

            if (!imagepath.isDirectory())
            {
                imagepath.mkdirs();
            }

            File mediaFile4 = new File(imagepath + File.separator + "img_" +
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(mediaFile4);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            image2_iv.setImageBitmap(bitmap);
            //upload2.setVisibility(View.GONE);
            path_carimg2=mediaFile4.getAbsolutePath();
            Log.e("tag","Take Car Path2"+path_carimg2);
            /*edit.putString("store_birdeyeview_path",path_birdeye_view);
            edit.commit();*/
        }












        //********************CAR PIC 9 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_CAR_PIC1){
            try {
                Uri selectedMediaUri = data.getData();
                path_carimg1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    image1_iv.setImageBitmap(bitmap);

                    Log.e("tag","Select Car Path1"+path_carimg1);
                    //upload1.setVisibility(View.GONE);
                    /*edit.putString("store_birdeyeview_path",path_birdeye_view);
                    edit.commit();*/
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }


        }



        //********************CAR PIC 10-------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_CAR_PIC2){
            try {
                Uri selectedMediaUri = data.getData();
                path_carimg2 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                    image2_iv.setImageBitmap(bitmap);
                    Log.e("tag","Select Car Path2"+path_carimg2);
                    //upload2.setVisibility(View.GONE);

                  /*  edit.putString("store_frontseatview_path",path_frontseat_view);
                    edit.commit();*/
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }

        }



    }




}
