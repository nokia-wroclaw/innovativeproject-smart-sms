package com.example.smartsms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;

public class PriorityActivity extends AppCompatActivity {

    static final String EXTRA_GIGAWATTS = "com.example.smartsms.PriorityActivity";

    ArrayList<String> ringtoneString=new ArrayList<>(Arrays.asList("RingTone 1", "RingTone 2", "RingTone 3", "RingTone 4", "RingTone 5"));
    ArrayAdapter<String> ringtoneListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.priority_main);

        ringtoneListAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,ringtoneString);
        ringtoneListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner ringtoneSpinnerList = findViewById(R.id.spinnerRingtone);
        ringtoneSpinnerList.setAdapter(ringtoneListAdapter);
        //Intent intent = getIntent();
    }
}
