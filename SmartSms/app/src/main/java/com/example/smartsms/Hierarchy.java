package com.example.smartsms;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Hierarchy extends AppCompatActivity {

    ArrayAdapter<String> listAdapter;
    ArrayAdapter<String> listAdapter2;
    private Spinner spinerList;
    private Spinner spinerList2;
    private ArrayList<String> list2;
    private ArrayList<String> list;
    private SqliteDB sqldb;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hierarchy_layout);

        sqldb = new SqliteDB(this);


        populateList();
        listAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,list);
        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinerList=findViewById(R.id.spinner);
        spinerList.setAdapter(listAdapter);

        listAdapter2=new ArrayAdapter(this,android.R.layout.simple_spinner_item,list2);
        listAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinerList2=findViewById(R.id.spinner2);
        spinerList2.setAdapter(listAdapter2);

        ImageButton backButton=findViewById(R.id.BackButton);
        Button okButton=findViewById(R.id.OKButton);
        okButton.setOnClickListener(new  View.OnClickListener() {

            public void onClick (View v) {
                ok();
                //
                return;
            }


        });
        backButton.setOnClickListener(new  View.OnClickListener() {

            public void onClick (View v) {
                finish();return;
            }


        });
    }

    public void populateList()
    {
        SQLiteDatabase db = sqldb.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT COLOR from COLOR ", null);
        list=new ArrayList<>();
        list2=new ArrayList<>();
        String color="blad";

        for(int i=1;i<15;i++) {

            list2.add(String.valueOf(i));
        }

        if (c != null ) { System.out.print("fff");
            if  (c.moveToFirst()) {
                do {

                    if(c.getString(c.getColumnIndex("COLOR")).equals("#ffcc0000"))color="red";
                    if(c.getString(c.getColumnIndex("COLOR")).equals("#ff0099cc"))color="blue";
                    if(c.getString(c.getColumnIndex("COLOR")).equals("#ffaaaaaa"))color="grey";
                    if(c.getString(c.getColumnIndex("COLOR")).equals("#ff000000"))color="black";
                    if(c.getString(c.getColumnIndex("COLOR")).equals("#ff669900"))color="green";
                    if(c.getString(c.getColumnIndex("COLOR")).equals("#ffff8800"))color="orange";
                  //  color = c.getString(c.getColumnIndex("COLOR"));
                    if(color.equals("blad")){
                        Toast.makeText(Hierarchy.this,"there is not such a color " ,Toast.LENGTH_LONG).show();
                        return;
                    }
                    list.add(color);

                }while (c.moveToNext());
            }
        }
    }

    private void ok() {

        String color = spinerList.getSelectedItem().toString();
        if(color.equals("red"))color="#ffcc0000";
        if(color.equals("blue"))color="#ff0099cc";
        if(color.equals("grey"))color="#ffaaaaaa";
        if(color.equals("black"))color="#ff000000";
        if(color.equals("green"))color="#ff669900";
        if(color.equals("orange"))color="#ffff8800";
        String number = spinerList2.getSelectedItem().toString();

        sqldb.deleteColorPriority(color);
        sqldb.addColorPriority(new ColorPriority(color,Integer.parseInt(number)));
        Toast.makeText(Hierarchy.this," ok " ,Toast.LENGTH_LONG).show();

    }
}
