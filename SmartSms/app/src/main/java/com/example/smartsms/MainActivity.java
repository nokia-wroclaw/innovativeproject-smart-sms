package com.example.smartsms;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MessageListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{
    SqliteDB sqldb;
    //SmsReceiver smsReceiver;
    MediaPlayer mediaPlayer;
    RecyclerViewAdapter adapter;
    List<Rule> dataToView = new LinkedList<>();//new LinkedList<>(Arrays.asList("tekst", "tekst 2", "tekst 3", "tekst 4", "tekst 5", "tekst 6", "tekst 7", "tekst 8", "tekst 9", "tekst", "tekst 2", "tekst 3", "tekst 4", "tekst 5", "tekst 6", "tekst 7", "tekst 8", "tekst 9"));
    private LinearLayout linearLayout;
    private int MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //SmsReceiver.bindContext(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        linearLayout = findViewById(R.id.coordinator_layout);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(this, dataToView);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        sqldb = new SqliteDB(this);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECEIVE_SMS},
                MY_PERMISSIONS_REQUEST_SMS_RECEIVE);

        SmsReceiver.bindListener(this);

        //startService(new Intent(this, MyService.class));
        //test DataBase

        /*sqldb.deleteRule("zasada7");
        sqldb.deletePriority("tak");
        sqldb.deletePriority("praca");
        sqldb.deleteRule("kot");
        Priority priority = new Priority("tak","nie","sth","path");
        Rule rule = new Rule("kot","start","892189129",priority);
        sqldb.addPriority(priority);
        priority = new Priority("praca","red","path","path");
        sqldb.addPriority(priority);
        sqldb.addRule(rule);
        priority = null;
        priority = sqldb.getPriority("praca");
        sqldb.addPriority(priority);
        rule= new Rule("zasada7","stop","892189124",priority);
        sqldb.addRule(rule);
        rule= new Rule("tom","stop","793337518",priority);
        sqldb.addRule(rule);
        rule= new Rule("info","stop","119119",priority);
        sqldb.addRule(rule);
        rule = sqldb.getRule("kot");
        sqldb.addRule(rule);
        ArrayList<Priority> pr = sqldb.getAllPriority();
        ArrayList<Rule> rl = sqldb.getAllRule();
        sqldb.deleteRule("zasada7");
        sqldb.deletePriority("tak");
        sqldb.deletePriority("praca");
        sqldb.deleteRule("kot");*/
    }

    @Override
    public void messageReceived(Rule message) {
        Toast.makeText(this, message.name, Toast.LENGTH_SHORT).show();
        adapter.addItem(message);
        mediaPlayer = new MediaPlayer();
        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,  Long.parseLong( message.priority.musicPath ));
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {

            mediaPlayer.setDataSource(getApplicationContext(), contentUri);
            mediaPlayer.prepare();
            mediaPlayer.start();

            CountDownTimer timer = new CountDownTimer(8000, 8000) {

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_SMS_RECEIVE) {
            Log.d("TAG", "My permission request sms received successfully");
        }
    }
    
    public void swapToRulesActivity(View v){
        Intent i = new Intent(this,RulesActivity.class);
        startActivity(i);
    }

    public void priorityMenuView(){
        Intent i = new Intent(MainActivity.this, PriorityActivity.class);
        startActivity(i);
    }
    public void RuleListView(){
        Intent i = new Intent(MainActivity.this, ListOfRulesActivity.class);
        startActivity(i);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof RecyclerViewAdapter.MyViewHolder) {

            String name = dataToView.get(viewHolder.getAdapterPosition()).name;
            adapter.removeItem(viewHolder.getAdapterPosition());
              Snackbar snackbar = Snackbar
                    .make(linearLayout, "Deleted: " + name, Snackbar.LENGTH_LONG);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            snackbar.setActionTextColor(Color.WHITE);
            snackbar.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_priority: {
                priorityMenuView();
                return true;
            }
            case R.id.action_rule: {
                RuleListView();
                return true;

            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}