package com.example.smartsms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener  {

    Switch Switch1;
    ImageButton returnButton;
    Button changeButton;
    private SqliteDB dataBase;
    int status = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_view);

        dataBase = new SqliteDB(this);

        Switch1 = (Switch) findViewById(R.id.volumeSwitch);
        returnButton = (ImageButton) findViewById(R.id.returnSettingsButton);
        changeButton = (Button) findViewById(R.id.changeSettingsButton);

    }

    public void returnToMainButton(){
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        int tmpId = v.getId();

        if(tmpId == this.changeButton.getId()){
            if(status == 1) status =0;
            else status =1;
        }

        if(tmpId == this.returnButton.getId()){
            this.returnToMainButton();
        }
    }
}