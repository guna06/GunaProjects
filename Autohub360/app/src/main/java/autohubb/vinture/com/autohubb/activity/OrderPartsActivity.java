package autohubb.vinture.com.autohubb.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;

public class OrderPartsActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_parts_activity);


        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(OrderPartsActivity.this, v1);
    }
}
