package com.sqindia.autolane360mobile.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.sqindia.autolane360mobile.R;

import java.util.Calendar;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class AddCarPageOneFragment extends Fragment {
    public static TextView date_edt;
    public static EditText input_mileage,input_location,input_vinstatus,input_interior,input_exterior,input_carprice;
    TextInputLayout l1,l2,l3,l4,l5,l6,l7,l8;
    private Calendar calendar;
    private int year, month, day;
    String str_doors,str_manufacture;



    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/helevetical.ttf");

        hideSoftKeyboard();

        //date_edt=view.findViewById(R.id.date_edt);
        input_location=view.findViewById(R.id.input_location);
        input_mileage=view.findViewById(R.id.input_mileage);
        input_vinstatus=view.findViewById(R.id.input_vinstatus);
        //input_vehicleclass=view.findViewById(R.id.input_vehicleclass);
        input_interior=view.findViewById(R.id.input_interior);
        input_exterior=view.findViewById(R.id.input_exterior);
        input_carprice=view.findViewById(R.id.input_carprice);


        l1=view.findViewById(R.id.input_layout_mileage);
        l3=view.findViewById(R.id.input_layout_location);
        l4=view.findViewById(R.id.input_layout_vinstatus);
        //l5=view.findViewById(R.id.input_layout_vehicleclass);
        l6=view.findViewById(R.id.input_layout_interior);
        l7=view.findViewById(R.id.input_layout_exterior);
        l8=view.findViewById(R.id.input_layout_carprice);

        l1.setTypeface(tf);
        l3.setTypeface(tf);
        l4.setTypeface(tf);
        //l5.setTypeface(tf);
        l6.setTypeface(tf);
        l7.setTypeface(tf);
        l8.setTypeface(tf);

        input_mileage.setTypeface(tf);
        input_location.setTypeface(tf);
        input_vinstatus.setTypeface(tf);
        //input_vehicleclass.setTypeface(tf);
        input_interior.setTypeface(tf);
        input_exterior.setTypeface(tf);
        input_carprice.setTypeface(tf);
        return view;
    }




    public void hideSoftKeyboard() {
        if(getActivity().getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

}
