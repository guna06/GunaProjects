package com.sqindia.autolane360mobile.fragment;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.sqindia.autolane360mobile.R;
import com.sqindia.autolane360mobile.adapter.CustomAdapterArrayList;

import java.util.ArrayList;


public class AddCarPageThreeFragment extends Fragment {

    Spinner doors_spn,manufacture_spn,accondition_spn,carkeys_spn;
    public static EditText input_dealer_info,input_description;
    ArrayList<String> doors_arraylist=new ArrayList<>();
    CustomAdapterArrayList doosrAdapter,manufactureAdapter,acconditionAdapter,carkeysAdapter;
    ArrayList<String> manufacture_arraylist=new ArrayList<>();
    ArrayList<String> accondition_arraylist=new ArrayList<>();
    ArrayList<String> carkey_arraylist=new ArrayList<>();
    Typeface helvetica;
    public static String str_doors,str_manufacture,str_sccondition,str_keys;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);

        doors_spn=view.findViewById(R.id.doors_spn);
        manufacture_spn=view.findViewById(R.id.manufacture_spn);
        accondition_spn=view.findViewById(R.id.accondition_spn);
        carkeys_spn=view.findViewById(R.id.carkeys_spn);
        input_dealer_info=view.findViewById(R.id.input_dealer_info);
        input_description=view.findViewById(R.id.input_description);

        helvetica = Typeface.createFromAsset(getActivity().getAssets(), "fonts/helevetical.ttf");

        //Add Doors Data:
        doors_arraylist.add("Select Doors");
        doors_arraylist.add("2 Doors");
        doors_arraylist.add("4 Doors");


       doosrAdapter = new CustomAdapterArrayList(getActivity(), android.R.layout.simple_dropdown_item_1line, doors_arraylist) {
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
        doors_spn.setAdapter(doosrAdapter);


        //Add Manufacture Data:
        manufacture_arraylist.add("Select Manufacture");
        manufacture_arraylist.add("US");
        manufacture_arraylist.add("Japan");
        manufacture_arraylist.add("Mexico");
        manufacture_arraylist.add("Germany");



        manufactureAdapter = new CustomAdapterArrayList(getActivity(), android.R.layout.simple_dropdown_item_1line, manufacture_arraylist) {
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
        manufacture_spn.setAdapter(manufactureAdapter);


        //Add Manufacture Data:
        accondition_arraylist.add("Select Ac Condition");
        accondition_arraylist.add("Working Great");
        accondition_arraylist.add("Working");
        accondition_arraylist.add("Not Working");




        acconditionAdapter = new CustomAdapterArrayList(getActivity(), android.R.layout.simple_dropdown_item_1line, accondition_arraylist) {
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
        accondition_spn.setAdapter(acconditionAdapter);




        //Add Manufacture Data:
        carkey_arraylist.add("Select Car Keys");
        carkey_arraylist.add("Available");
        carkey_arraylist.add("Not Available");


        carkeysAdapter = new CustomAdapterArrayList(getActivity(), android.R.layout.simple_dropdown_item_1line, carkey_arraylist) {
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
        carkeys_spn.setAdapter(carkeysAdapter);


        doors_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_doors= (String) adapterView.getItemAtPosition(i);
                Log.e("tag","print Doors------------>"+str_doors);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        manufacture_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_manufacture= (String) adapterView.getItemAtPosition(i);
                Log.e("tag","print Manufacture------------>"+str_manufacture);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        accondition_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_sccondition= (String) adapterView.getItemAtPosition(i);
                Log.e("tag","print Condition------------>"+str_sccondition);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        carkeys_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_keys= (String) adapterView.getItemAtPosition(i);
                Log.e("tag","Print Keys----------->"+str_keys);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }
}
