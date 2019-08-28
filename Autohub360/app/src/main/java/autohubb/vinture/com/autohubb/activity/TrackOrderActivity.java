package autohubb.vinture.com.autohubb.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;

public class TrackOrderActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_payment_info);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(TrackOrderActivity.this, v1);

    }



}
