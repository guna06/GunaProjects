package autohubb.vinture.com.autohubb.navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.activity.MainActivity;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;

public class ReceiptsActivity extends Activity {
    LinearLayout back_lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipts);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ReceiptsActivity.this, v1);
        back_lv=findViewById(R.id.back_lv);



        back_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }
}
