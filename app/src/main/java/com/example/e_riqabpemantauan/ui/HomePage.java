package com.example.e_riqabpemantauan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_riqabpemantauan.R;

public class HomePage extends AppCompatActivity {

    private ImageView pengesesahan_log;
    private ImageView log_harian;
    private ImageView merit_pelatih;
    private ImageView kes_khas;

    private TextView pengesesahan_log_txt;
    private TextView log_harian_txt;
    private TextView merit_pelatih_txt;
    private TextView kes_khas_txt;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Utama");

        pengesesahan_log_txt = (TextView) findViewById(R.id.pengesahan_log_text);
        log_harian_txt = (TextView) findViewById(R.id.log_harian_text);
        merit_pelatih_txt = (TextView) findViewById(R.id.merit_pelatih_text);
        kes_khas_txt = (TextView) findViewById(R.id.kes_khas_text);

        pengesesahan_log = (ImageView) findViewById(R.id.pengesahan_log);
        log_harian = (ImageView) findViewById(R.id.log_harian);
        merit_pelatih = (ImageView) findViewById(R.id.merit_pelatih);
        kes_khas = (ImageView) findViewById(R.id.kes_khas);

        pengesesahan_log_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity1();
            }
        });

        log_harian_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity2();
            }
        });

        merit_pelatih_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity3();
            }
        });

        kes_khas_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity4();
            }
        });

        pengesesahan_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity1();
            }
        });


        log_harian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity2();
            }
        });

        merit_pelatih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity3();
            }
        });

        kes_khas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity4();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) {
            Intent intent = new Intent(HomePage.this, Login.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void launchActivity1() {
        Intent intent = new Intent(this, DailyVerificationLog.class);

        startActivity(intent);
    }

    private void launchActivity2() {
        Intent intent = new Intent(this, RecordDailyLog.class);

        startActivity(intent);
    }

    private void launchActivity3() {
        Intent intent = new Intent(this, CoachMerit.class);

        startActivity(intent);
    }
    private void launchActivity4() {
        Intent intent = new Intent(this, SpecialCaseCoachList.class);

        startActivity(intent);
    }

}
