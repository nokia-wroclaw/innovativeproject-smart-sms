package com.example.smartsms;

import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{
    private RecyclerViewAdapter adapter;
    private List<String> dataToView = new LinkedList<>(Arrays.asList("tekst", "tekst 2", "tekst 3", "tekst 4", "tekst 5", "tekst 6", "tekst 7", "tekst 8", "tekst 9", "tekst", "tekst 2", "tekst 3", "tekst 4", "tekst 5", "tekst 6", "tekst 7", "tekst 8", "tekst 9"));
    private ConstraintLayout constraintLayout;
    ArrayAdapter<String> listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        constraintLayout = findViewById(R.id.coordinator_layout);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(dataToView);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);



        ArrayList<String> list=new ArrayList<>();
        list.add("priority 1");
        list.add("priority 2");
        list.add("priority 3");
        list.add("priority 4");
        list.add("priority 5");
        list.add("priority 6");
        listAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,list);
        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Button btn = findViewById(R.id.addNewButton);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.rules_view);
                Spinner spinerList;
                spinerList=findViewById(R.id.spinner);
                spinerList.setAdapter(listAdapter);
            }
        });

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof RecyclerViewAdapter.MyViewHolder) {

            String name = dataToView.get(viewHolder.getAdapterPosition());
            adapter.removeItem(viewHolder.getAdapterPosition());

            Snackbar snackbar = Snackbar
                    .make(constraintLayout, "Deleted: " + name, Snackbar.LENGTH_LONG);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}