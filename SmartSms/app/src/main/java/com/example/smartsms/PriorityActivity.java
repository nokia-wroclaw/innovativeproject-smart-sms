package com.example.smartsms;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Vector;

public class PriorityActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_PERMISSION_REQUEST = 1;
    static final String EXTRA_GIGAWATTS = "com.example.smartsms.PriorityActivity";

    //Ringtones
    Spinner ringtoneSpinnerList;
    ArrayList<String> ringtoneString;
    ArrayAdapter<String> ringtoneListAdapter;

    //Media Player
    MediaPlayer mediaPlayer;


    //Containers For Buttons
    Vector<ImageButton> imgButtonsVector;
    Vector<ImageButton> colorButtonsVector;
    Vector idSongsVector;

    //Variables to create new Priority
    ImageButton imgSelectedButton;
    ImageButton colorSelectedButton;

    EditText text;

    ImageButton goBackButton;
    ImageButton createButton;

    private SqliteDB dataBase;

    CountDownTimer timer;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.priority_main);

        dataBase = new SqliteDB(this);

        mediaPlayer = null;

        //Initializing Variables
        imgButtonsVector = new Vector<ImageButton>();
        colorButtonsVector = new Vector<ImageButton>();
        idSongsVector = new Vector();
        ringtoneString=new ArrayList<>();

        goBackButton = (ImageButton) findViewById(R.id.returnPriorityButton);
        goBackButton.setOnClickListener(this);

        createButton = (ImageButton) findViewById(R.id.createPriorityButton);
        createButton.setOnClickListener(this);

        text = (EditText)findViewById(R.id.priorityNameText);

        //Set Image Buttons, adding action listeners
        ViewGroup imgLayout = (ViewGroup) findViewById(R.id.imageLayout);
        for (int i = 0; i < imgLayout.getChildCount(); i++) {

            View imageChild = imgLayout.getChildAt(i);
            if (imageChild instanceof ImageButton) {
                ImageButton imgButton = (ImageButton) imageChild;
                imgButton.setOnClickListener(this);
                imgButtonsVector.add(imgButton);
            }
        }


        //Set Color Buttons, adding action listeners
        ViewGroup colorLayout = (ViewGroup) findViewById(R.id.colorLayout);
        for (int i = 0; i < colorLayout.getChildCount(); i++) {

            View colorChild = colorLayout.getChildAt(i);
            if (colorChild instanceof ImageButton) {
                ImageButton imgButton = (ImageButton) colorChild;
                imgButton.setOnClickListener(this);
                //imgButton.setFocusable(true);
                colorButtonsVector.add(imgButton);
            }
        }

        if (ContextCompat.checkSelfPermission(PriorityActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(PriorityActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(PriorityActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(PriorityActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else {
            selectRingtone();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void selectRingtone(){
        ringtoneSpinnerList = (Spinner) findViewById(R.id.spinnerRingtone);

        getMusic();
        ringtoneListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ringtoneString);
        ringtoneListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ringtoneSpinnerList.setAdapter(ringtoneListAdapter);
        ringtoneSpinnerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

                boolean firstOpen =false;

                if(mediaPlayer == null){
                    firstOpen =true;
                }
                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    if(timer!=null) {
                        timer.cancel();
                    }
                }


                mediaPlayer = new MediaPlayer();

                if(firstOpen==false) {
                    Toast.makeText(getBaseContext(), ringtoneString.get(position), Toast.LENGTH_SHORT).show();
                }

                long tmpId = (long)idSongsVector.elementAt(position);
                Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,  tmpId );
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                try {
                    mediaPlayer.setDataSource(getApplicationContext(), contentUri);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(firstOpen==false) {
                    mediaPlayer.start();

                    //Timer stopping mediaPlayer after 8s
                    timer = new CountDownTimer(8000, 8000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            // Nothing to do
                        }

                        @Override
                        public void onFinish() {
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.stop();
                                mediaPlayer.release();
                            }
                        }
                    };
                    timer.start();
                }


            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void getMusic() {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null,null,null,null,null);
        if(songCursor != null && songCursor.moveToFirst()){
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            //int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            do {

                String currentTitle = songCursor.getString(songTitle);
               // String currentArtist = songCursor.getString(songArtist);
                long currentId = songCursor.getLong(songCursor.getColumnIndex(MediaStore.Audio.Media._ID));
                ringtoneString.add(currentTitle);
                idSongsVector.add(currentId);
            }while(songCursor.moveToNext());

            }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
       switch (requestCode){
           case MY_PERMISSION_REQUEST:{
               if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                   if(ContextCompat.checkSelfPermission(PriorityActivity.this,
                          Manifest.permission.READ_EXTERNAL_STORAGE )==PackageManager.PERMISSION_GRANTED){
                       Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();

                       selectRingtone();
                   }
                   else{
                       Toast.makeText(this, "Permission Denaied!",Toast.LENGTH_SHORT).show();
                       finish();
                   }
                   return;
               }

           }
       }
    }

    void addPriority(){

        String colorCode = (String)this.colorSelectedButton.getTag();

        String imgCode= (String)this.imgSelectedButton.getTag();

        String ringtoneName =  ringtoneSpinnerList.getSelectedItem().toString();
        int ringtoneId =  ringtoneString.indexOf(ringtoneName);
        String ringtonePath = idSongsVector.get(ringtoneId).toString();

        String textName = text.getText().toString();

        //Adding priority
       Priority priority = new Priority(textName,colorCode,imgCode,ringtonePath);

       if(dataBase.addPriority(priority)==true){
           Toast.makeText(getBaseContext(), "Priority Was Added Sucessfully!", Toast.LENGTH_SHORT).show();
       }
    }

    public void returnToMainButton(){
        super.onBackPressed();
    }


    @Override
    public void onClick(View v) {

        int tmpId = v.getId();

       for(int i = 0; i <this.colorButtonsVector.size(); i++){
           if(colorButtonsVector.get(i).getId()==tmpId) {

              // colorButtonsVector.get(i).findFocus().clearFocus();
               this.colorSelectedButton = colorButtonsVector.get(i);
             // colorButtonsVector.get(i).requestFocus();

           }
       }

        for(int i = 0; i<imgButtonsVector.size(); i++){
            if(tmpId==imgButtonsVector.get(i).getId()){
                this.imgSelectedButton = imgButtonsVector.get(i);
            }
        }

        if(tmpId == this.goBackButton.getId()){
            this.returnToMainButton();
        }

        if(tmpId == this.createButton.getId()){
            addPriority();
        }
    }

}
