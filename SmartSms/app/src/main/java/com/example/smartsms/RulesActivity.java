package com.example.smartsms;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class RulesActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    ArrayAdapter<String> listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules_view);

        ArrayList<String> list=new ArrayList<>();
        list.add("priority 1");
        list.add("priority 2");
        list.add("priority 3");
        list.add("priority 4");
        list.add("priority 5");
        list.add("priority 6");
        listAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,list);
        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinerList;
        spinerList=findViewById(R.id.spinner);
        spinerList.setAdapter(listAdapter);

        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(this);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.navigation_Back:
                finish();
                break;
        }

    return  false;

    }
}
