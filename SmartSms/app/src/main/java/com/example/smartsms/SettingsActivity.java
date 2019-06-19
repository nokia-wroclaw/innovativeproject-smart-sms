package com.example.smartsms;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener  {
    Button buttonOne, buttonTwo;
    Switch Switch1;
    ImageButton returnButton;
    Button changeButton;
    private SqliteDB dataBase;
    private Uri uri;
    private static final int REQUEST_CODE = 43;
    int status;
    boolean statusBoolean;
    private ArrayList<Priority> priorities;
    String statusMode = "status";
    CompoundButton.OnCheckedChangeListener switchListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_view);

        dataBase = new SqliteDB(this);
        Switch1 = (Switch) findViewById(R.id.volumeSwitch);

        if(dataBase.getMode(statusMode).name==null)
        {
           // Toast.makeText(SettingsActivity.this,"INN = ",Toast.LENGTH_LONG).show();
            Mode tmpMode = new Mode(statusMode,false);
            dataBase.addMode(tmpMode);
        }


        if(dataBase.getMode(statusMode).IsOn == true){
            //Toast.makeText(SettingsActivity.this,"True  = ",Toast.LENGTH_LONG).show();
            Switch1.setChecked(true);
            status =1;
        }else {
            //Toast.makeText(SettingsActivity.this,"False  = ",Toast.LENGTH_LONG).show();
            Switch1.setChecked(false);
            status =0;
        }


        switchListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (Switch1.isChecked() == true) {
                    status = 1;
                    dataBase.setMode(statusMode, true);
                    Toast.makeText(SettingsActivity.this,"Database  = "+dataBase.getMode(statusMode).IsOn+" was! Status " + status,Toast.LENGTH_LONG).show();
                    statusBoolean = dataBase.getMode(statusMode).IsOn;
                }
                if(Switch1.isChecked() == false){
                    status = 0;
                    dataBase.setMode(statusMode, false);
                    Toast.makeText(SettingsActivity.this,"Database  = "+dataBase.getMode(statusMode).IsOn+" was! Status " + status,Toast.LENGTH_LONG).show();
                    statusBoolean = dataBase.getMode(statusMode).IsOn;
                }


            }
        };


        Switch1.setOnCheckedChangeListener(switchListener);

        returnButton = (ImageButton) findViewById(R.id.returnSettingsButton);
        changeButton = (Button) findViewById(R.id.changeSettingsButton);

        buttonOne = (Button) findViewById(R.id.buttonImport);
        buttonTwo = (Button) findViewById(R.id.buttonExport);
        buttonOne.setOnClickListener(this);
        buttonTwo.setOnClickListener(this);

    }

    public void returnToMainButton(){
        super.onBackPressed();
    }

    private void startSearch()
    {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");//db files other options application/x-sqlite3 and application/x-paradox
        intent.addCategory((Intent.CATEGORY_OPENABLE));
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

        if(data != null)
        {
            uri = data.getData();
            try {
                Log.d("URI",uri.getPath().toString());
                dataBase.importDatabase(uri.toString());

            } catch (

                    IOException ie) {
                ie.printStackTrace();
            }
        }
    }


    @Override
    public void onClick(View v) {
        int tmpId = v.getId();

        if(tmpId == this.changeButton.getId()){
            String tmpName;

            priorities = dataBase.getAllPriority();
            for(int i = 0 ; i < priorities.size(); i++){
                //Color.RED
                if (!priorities.get(i).color.equals("#ffcc0000")){
                    tmpName = priorities.get(i).name;
                    if(dataBase.getMode(statusMode).IsOn == true) dataBase.setMode(tmpName,false);
                    else dataBase.setMode(tmpName,true);
                }
            }
            Toast.makeText(SettingsActivity.this,"Changes was made!! to " + dataBase.getMode(statusMode).IsOn,Toast.LENGTH_LONG).show();
        }

        if(tmpId == this.returnButton.getId()){

            this.returnToMainButton();
        }

        if(tmpId==R.id.buttonImport)
        {
            startSearch();

        }
        if (tmpId == R.id.buttonExport) {
            try {
                dataBase.exportDatabase("/data/media/0/Download/Sample.db");

            } catch (
                    IOException ie) {
                ie.printStackTrace();
            }
        }
    }
}
