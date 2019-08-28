package autohubb.vinture.com.autohubb.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;

public class PaymentsInfoActivity extends Activity {
    LinearLayout back_lv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_payment_info);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(PaymentsInfoActivity.this, v1);

        back_lv=findViewById(R.id.back_lv);

        back_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
