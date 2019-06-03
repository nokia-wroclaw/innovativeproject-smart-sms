package com.example.smartsms;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
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


public class MainActivity extends AppCompatActivity implements MessageListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    SqliteDB sqldb;
    //SmsReceiver smsReceiver;
    MediaPlayer mediaPlayer;
    RecyclerViewAdapter adapter;
    List<CapturedRule> dataToView = new LinkedList<>();//new LinkedList<>(Arrays.asList("tekst", "tekst 2", "tekst 3", "tekst 4", "tekst 5", "tekst 6", "tekst 7", "tekst 8", "tekst 9", "tekst", "tekst 2", "tekst 3", "tekst 4", "tekst 5", "tekst 6", "tekst 7", "tekst 8", "tekst 9"));
    private LinearLayout linearLayout;
    private int MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sqldb = new SqliteDB(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_main);
        linearLayout = findViewById(R.id.coordinator_layout);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<CapturedRule> capturedRules = sqldb.getAllCapturedRule();
        ArrayList<Rule> rules = sqldb.getAllRule();
        for(CapturedRule cr : capturedRules){
            for(Rule r : rules){
                if(cr.nameRule.equals(r.name)){
                    dataToView.add(cr);
                }
            }
        }

        adapter = new RecyclerViewAdapter(this, dataToView);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECEIVE_SMS},
                MY_PERMISSIONS_REQUEST_SMS_RECEIVE);

        SmsReceiver.bindListener(this);
    }

    @Override
    public void messageReceived(CapturedRule message) {
        Toast.makeText(this, message.nameRule, Toast.LENGTH_SHORT).show();
        adapter.addItem(message);

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

    public void HierarchyView(){
        Intent i = new Intent(MainActivity.this, Hierarchy.class);
        startActivity(i);
    }

    public void priorityMenuManagementView(){
        Intent i = new Intent(MainActivity.this, PriorityHierarchyActivity.class);
        startActivity(i);
    }
    public void settingsView(){
        Intent i = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(i);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof RecyclerViewAdapter.MyViewHolder) {

            String name = dataToView.get(viewHolder.getAdapterPosition()).nameRule;
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
            case R.id.action_settings: {
                settingsView();
                return true;
            }
            case R.id.action_priorityManagement: {
                priorityMenuManagementView();
                return true;
            }
            case R.id.action_hierarchy: {
                
                HierarchyView();
                return true;

            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}