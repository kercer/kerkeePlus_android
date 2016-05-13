package com.kercer.kerkee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kercer.kerkee_example.R;

public class KCMainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View urdBtn = findViewById(R.id.urdBtn);
        urdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KCMainActivity.this,KCUrdActivity.class));
            }
        });
    }

}
