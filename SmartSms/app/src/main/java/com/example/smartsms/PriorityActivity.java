package com.example.smartsms;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class PriorityActivity extends AppCompatActivity implements View.OnClickListener  {

    private static final int MY_PERMISSION_REQUEST = 1;
    static final String EXTRA_GIGAWATTS = "com.example.smartsms.PriorityActivity";

    ArrayList<String> ringtoneString;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> ringtoneListAdapter;

    Spinner ringtoneSpinnerList;

    //Contaoiners For Buttons
    Vector<ImageButton> imagButtonsVector;
    Vector<ImageButton> colorButtonsVector;
    Vector idSongsVector;
    Vector<Uri> uriSongsVector;

    //Variables to create new Priority
    ImageButton imgSelectedButton;
    ImageButton colorSelectedButton;

    //Media Player from raw
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.priority_main);

        //Initializing Variables
        imagButtonsVector = new Vector<ImageButton>();
        colorButtonsVector = new Vector<ImageButton>();
        idSongsVector = new Vector();
        uriSongsVector = new Vector<Uri>();
        ringtoneString=new ArrayList<>();


        //Set Image Buttons, adding action listeners
        ViewGroup imgLayout = (ViewGroup) findViewById(R.id.imageLayout);
        for (int i = 0; i < imgLayout.getChildCount(); i++) {

            View imageChild = imgLayout.getChildAt(i);
            if (imageChild instanceof ImageButton) {
                ImageButton imgButton = (ImageButton) imageChild;
                imgButton.setOnClickListener(this);
                imagButtonsVector.add(imgButton);
            }
        }


        //Set Color Buttons, adding action listeners
        ViewGroup colorLayout = (ViewGroup) findViewById(R.id.colorLayout);
        for (int i = 0; i < colorLayout.getChildCount(); i++) {

            View colorChild = colorLayout.getChildAt(i);
            if (colorChild instanceof ImageButton) {
                ImageButton imgButton = (ImageButton) colorChild;
                imgButton.setOnClickListener(this);
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
            doStuff();
        }

    }

    public void doStuff(){
        ringtoneSpinnerList = (Spinner) findViewById(R.id.spinnerRingtone);

        getMusic();
        ringtoneListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ringtoneString);
        ringtoneListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ringtoneSpinnerList.setAdapter(ringtoneListAdapter);
        ringtoneSpinnerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

                if(mediaPlayer != null){
                    mediaPlayer.pause();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }

                mediaPlayer = new MediaPlayer();

                Toast.makeText(getBaseContext(), ringtoneString.get(position), Toast.LENGTH_SHORT).show();

                long tmpId = (long)idSongsVector.elementAt(position);

                Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,  tmpId );
                //mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.setDataSource(getApplicationContext(), contentUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

    }


    public void getMusic() {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null,null,null,null,null);
        if(songCursor != null && songCursor.moveToFirst()){
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
           // songCursor.


            do {

                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                long currentId = songCursor.getLong(songCursor.getColumnIndex(MediaStore.Audio.Media._ID));
                uriSongsVector.add(android.provider.MediaStore.Audio.Media.getContentUri(currentTitle));
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

                       doStuff();
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

    // public void onRequest


    /*
    public void imgClicked(View v){
        int tmpId = v.getId();
        for(int i = 0; i<imagButtonsVector.size(); i++){
            if(tmpId==imagButtonsVector.get(i).getId()){
                this.imgSelectedButton = imagButtonsVector.get(i);
                imagButtonsVector.get(i).setBackgroundColor(Color.RED);
                //imagButtonsVector.get(i);
            }
        }
    }

*/
    void sddPriority(){

        ColorDrawable viewColor = (ColorDrawable)  colorSelectedButton.getBackground();
       // int colorId = viewColor.getColor();
        ///int color = colorSelectedButton.getBackground();
    }
    @Override
    public void onClick(View v) {

        int tmpId = v.getId();

       for(int i = 0; i <this.colorButtonsVector.size(); i++){
           if(colorButtonsVector.get(i).getId()==tmpId) {
               this.colorSelectedButton = colorButtonsVector.get(i);
              // mp.start();
           }
       }

        for(int i = 0; i<imagButtonsVector.size(); i++){
            if(tmpId==imagButtonsVector.get(i).getId()){
                this.imgSelectedButton = imagButtonsVector.get(i);
                imagButtonsVector.get(i).setBackgroundColor(Color.RED);
                //imagButtonsVector.get(i);
            }
        }
    }
}
