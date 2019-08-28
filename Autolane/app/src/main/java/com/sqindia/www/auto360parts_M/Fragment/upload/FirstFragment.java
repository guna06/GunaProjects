package com.sqindia.www.auto360parts_M.Fragment.upload;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sqindia.www.auto360parts_M.Activity.TransformationActivity;
import com.sqindia.www.auto360parts_M.R;
import com.squareup.picasso.Picasso;

public class FirstFragment extends Fragment {


    public FirstFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView one = (ImageView) view.findViewById(R.id.fragmentOneBackground);

        Log.e("tag","guna----------1>"+"http:104.197.80.225/autoparts360/assets/img/car_images/"+TransformationActivity.get_car1);

        Picasso.get().load("http:104.197.80.225/autoparts360/assets/img/car_images/"+TransformationActivity.get_car1).fit().centerCrop().into(one);


    }
}