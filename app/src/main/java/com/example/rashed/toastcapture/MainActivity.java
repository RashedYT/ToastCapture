package com.example.rashed.toastcapture;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("MainActivity","exiting");
    }

    public void generate(View v){
        Toast.makeText(this,"online",Toast.LENGTH_SHORT).show();
    }
}
