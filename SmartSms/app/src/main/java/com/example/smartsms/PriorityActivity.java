package com.example.smartsms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class PriorityActivity extends AppCompatActivity implements View.OnClickListener  {

    static final String EXTRA_GIGAWATTS = "com.example.smartsms.PriorityActivity";

    ArrayList<String> ringtoneString=new ArrayList<>(Arrays.asList("RingTone 1", "RingTone 2", "RingTone 3", "RingTone 4", "RingTone 5"));
    ArrayAdapter<String> ringtoneListAdapter;

    //Contaoiners For Buttons
    Vector<ImageButton> imagButtonsVector;
    Vector<ImageButton> colorButtonsVector;

    //Variables to create new Priority
    ImageButton imgSelectedButton;
    ImageButton colorSelectedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.priority_main);

        imagButtonsVector = new Vector<ImageButton>();
        colorButtonsVector = new Vector<ImageButton>();


        ringtoneListAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,ringtoneString);
        ringtoneListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner ringtoneSpinnerList = findViewById(R.id.spinnerRingtone);
        ringtoneSpinnerList.setAdapter(ringtoneListAdapter);


        ViewGroup imgLayout = (ViewGroup)findViewById(R.id.imageLayout);
        for (int i = 0; i < imgLayout.getChildCount(); i++) {

            View imageChild = imgLayout.getChildAt(i);
            if(imageChild instanceof ImageButton)
            {
                ImageButton imgButton = (ImageButton) imageChild ;
                imgButton.setOnClickListener(this);
                imagButtonsVector.add(imgButton);
            }
        }


        //Set Color Buttons, adding action listeners
        ViewGroup colorLayout = (ViewGroup)findViewById(R.id.colorLayout);
        for (int i = 0; i < colorLayout.getChildCount(); i++) {

            View colorChild = colorLayout.getChildAt(i);
            if(colorChild instanceof ImageButton)
            {
                ImageButton imgButton = (ImageButton) colorChild  ;
                imgButton.setOnClickListener(this);
                colorButtonsVector.add(imgButton);
            }
        }


        //Intent intent = getIntent();
    }

    public void imgClicked(View v){
        int tmpId = v.getId();
        for(int i = 0; i<imagButtonsVector.size(); i++){
            if(tmpId==imagButtonsVector.get(i).getId()){
                this.imgSelectedButton = imagButtonsVector.get(i);
                //imagButtonsVector.get(i);
            }
        }
    }
    public void colorClicked(View v){
        int tmpId = v.getId();
        for(int i = 0; i<colorButtonsVector.size(); i++){
            if(tmpId==colorButtonsVector.get(i).getId()){
                this.colorSelectedButton = colorButtonsVector.get(i);
                //imagButtonsVector.get(i);
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
}
