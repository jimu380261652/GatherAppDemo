package com.jimu.dev;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    TextClock tvClockTm,tvClockTm1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 1);
            }
        }

        tvClockTm = findViewById(R.id.tvClockTm);
        tvClockTm1 = findViewById(R.id.tvClockTm1);
        findViewById(R.id.btnTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                refreshTm(v.isSelected());
                //VoiceUtils.getInstance().startPlay(MainActivity.this,VoiceUtils.VoiceTypeEnum.VOICE_COOK_YR);
                //startActivity(new Intent(MainActivity.this,Camera2Activity.class));
                ToastUtil.show(MainActivity.this,"烹饪已完成", Toast.LENGTH_LONG);
            }
        });
    }

    public void refreshTm(boolean is24Hour) {
        if (is24Hour) {
            tvClockTm.setFormat12Hour("HH:mm");
            tvClockTm.setFormat24Hour("HH:mm");
        } else {
            tvClockTm.setFormat12Hour("hh:mm");
            tvClockTm.setFormat24Hour("hh:mm");
        }
    }
}