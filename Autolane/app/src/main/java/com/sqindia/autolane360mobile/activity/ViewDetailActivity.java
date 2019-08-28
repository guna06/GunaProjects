package com.sqindia.autolane360mobile.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sqindia.autolane360mobile.R;
import com.sqindia.autolane360mobile.font.FontsOverride;


public class ViewDetailActivity extends Activity {
    Intent i;

    String filter,str_stockid,str_description,str_mileage,str_transmission,str_keys,str_head,str_vehicle_class,str_engine,str_startcode,str_vin,str_location,str_exterior,str_interior,str_desc,str_door,str_manufacture,str_condition;
    TextView viewimages,mileage_tv,transmission_tv,keys_tv,head_tv,vehicleclass_tv,engine_tv,startcode_tv,vinstatus_tv,location_tv,exterior_tv,interior_tv,description_tv,doors_tv,manufacture_tv,accondition_tv;
    ImageView back_iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_detail_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ViewDetailActivity.this, v1);



        i=getIntent();
        str_stockid=i.getStringExtra("stockid");
        str_head=i.getStringExtra("heading");
        str_startcode=i.getStringExtra("startcode");
        str_vin=i.getStringExtra("vinstatus");
        str_location=i.getStringExtra("location");
        str_exterior=i.getStringExtra("exterior_color");
        str_interior=i.getStringExtra("interior_color");
        str_desc=i.getStringExtra("description");
        str_door=i.getStringExtra("door");
        str_manufacture=i.getStringExtra("manufacture");
        str_condition=i.getStringExtra("ac_condition");
        str_engine=i.getStringExtra("engine");
        str_vehicle_class=i.getStringExtra("vehicle_class");
        str_keys=i.getStringExtra("carkeys");
        str_transmission=i.getStringExtra("transmission");
        str_mileage=i.getStringExtra("mileage");
        str_description=i.getStringExtra("description");
        filter=i.getStringExtra("filter");


        head_tv=findViewById(R.id.head_tv);
        back_iv=findViewById(R.id.back_iv);
        startcode_tv=findViewById(R.id.startcode_tv);
        vinstatus_tv=findViewById(R.id.vinstatus_tv);
        location_tv=findViewById(R.id.location_tv);
        exterior_tv=findViewById(R.id.exterior_tv);
        interior_tv=findViewById(R.id.interior_tv);
        description_tv=findViewById(R.id.description_tv);
        doors_tv=findViewById(R.id.doors_tv);
        manufacture_tv=findViewById(R.id.manufacture_tv);
        accondition_tv=findViewById(R.id.accondition_tv);
        engine_tv=findViewById(R.id.engine_tv);
        vehicleclass_tv=findViewById(R.id.vehicleclass_tv);
        keys_tv=findViewById(R.id.keys_tv);
        transmission_tv=findViewById(R.id.transmission_tv);
        mileage_tv=findViewById(R.id.mileage_tv);
        description_tv=findViewById(R.id.description_tv);
        //viewimages=findViewById(R.id.viewimages);

        if(str_mileage.equals("null"))
        {
            mileage_tv.setText("-");
        }
        else
        {
            mileage_tv.setText(str_mileage+" Miles");
        }


        head_tv.setText(str_head);
        startcode_tv.setText(str_startcode);
        vinstatus_tv.setText(str_vin);
        location_tv.setText(str_location);
        exterior_tv.setText(str_exterior);
        interior_tv.setText(str_interior);
        description_tv.setText(str_desc);
        doors_tv.setText(str_door);
        manufacture_tv.setText(str_manufacture);
        accondition_tv.setText(str_condition);
        engine_tv.setText(str_engine);
        vehicleclass_tv.setText(str_vehicle_class);
        keys_tv.setText(str_keys);
        transmission_tv.setText(str_transmission);




        if(str_description.equals("null"))
        {
            description_tv.setText("-");
        }else
        {
            description_tv.setText(str_description);
        }


       /* viewimages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(getApplicationContext(),ImageActivity.class);
                i.putExtra("getstockid",str_stockid);
                startActivity(back);
                finish();
            }
        });
*/


        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(getApplicationContext(),ViewCarActivity.class);
                startActivity(back);
                finish();

               if(filter.equals("Sold"))
                {
                    Intent back1=new Intent(getApplicationContext(),SoldCarActivity.class);
                    startActivity(back1);
                    finish();
                }
                else
                {
                    Intent back2=new Intent(getApplicationContext(),ViewCarActivity.class);
                    startActivity(back2);
                    finish();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(filter.equals("Sold"))
        {
            Intent back1=new Intent(getApplicationContext(),SoldCarActivity.class);
            startActivity(back1);
            finish();
        }
        else
        {
            Intent back2=new Intent(getApplicationContext(),ViewCarActivity.class);
            startActivity(back2);
            finish();
        }
    }
}
