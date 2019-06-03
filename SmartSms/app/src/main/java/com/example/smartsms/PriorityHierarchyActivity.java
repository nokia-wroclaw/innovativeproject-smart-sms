package com.example.smartsms;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PriorityHierarchyActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Priority> priorities;
    private List<String> priorityName;
    private List<String> ringtonesList;
    private SqliteDB db;
    private ListView listPriority;
    private int position;
    private String nameDelete;
    private CustomAdapter addapter;
    private PriorityHierarchyActivity.CustomAdapter adapter;
    private List<ColorPriority> hierarchy;
    private List<String> colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_prio);
        db = new SqliteDB(this);

        populateList();
        listPriority=(ListView) findViewById(R.id.listPriorities);
        listPriority.setClickable(true);


        addapter=new PriorityHierarchyActivity.CustomAdapter (priorities,getApplicationContext());
        listPriority.setAdapter(addapter);
        listPriority.setClickable(true);

        listPriority.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                nameDelete=priorities.get(position).name;

                position=position;

                return;
            }
        })


        ;

        ImageButton backButton=findViewById(R.id.BackButtonP);
        final ImageButton deleteButton=findViewById(R.id.DeleteButtonP);
        backButton.setOnClickListener(new  View.OnClickListener() {

            public void onClick (View v) {
                finish();return;
            }


        });

        /*
        ImageButton editButton=findViewById(R.id.EditButton);
        editButton.setOnClickListener(new  View.OnClickListener() {

            public void onClick (View v) {

                PriorityListView();
                return;
            }


        });
*/

        deleteButton.setOnClickListener(new  View.OnClickListener(){
            public void onClick (View v) {

                if(db.getPriority(nameDelete)!=null) {
                    db.deletePriority(nameDelete);
                    Toast.makeText(PriorityHierarchyActivity.this,"Priority "+nameDelete+" was deleted",Toast.LENGTH_LONG).show();

                    populateList();
                    addapter=new PriorityHierarchyActivity.CustomAdapter(priorities,getApplicationContext());
                    listPriority.setAdapter(addapter);


                }
                return;
            }
        });

    }

    public void populateList()
    {

        priorities = new ArrayList<Priority>(db.getAllPriority());
        ArrayList sort = new ArrayList<Priority>();

        priorityName = new ArrayList<String>();
        ringtonesList = new ArrayList<String>();

    }

    //public void PriorityListView(){
     //   Intent i = new Intent(PriorityHierarchyActivity.this, Hierarchy.class);
     //   startActivity(i);
    //}

    @Override
    public void onClick(View v) {

    }

    public class CustomAdapter extends ArrayAdapter<Priority> implements View.OnClickListener{

        private ArrayList<Priority> dataSet;
        Context mContext;

        public CustomAdapter(ArrayList<Priority> data, Context context) {
            super(context, R.layout.custom_priority_list_view,data);
            this.dataSet=data;
            this.mContext=context;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView=getLayoutInflater().inflate(R.layout.custom_priority_list_view,parent,false);

            TextView name=(TextView)convertView.findViewById(R.id.textPriorityName);
            TextView color=(TextView)convertView.findViewById(R.id.textViewPriorityColor);
            String colorString = dataSet.get(position).color.toString();
            name.setText("Priority name: ");
            //name.setTextColor(Color.parseColor(colorString));
            name.setBackgroundColor(Color.parseColor(colorString));
            color.setText("        " +dataSet.get(position).name.toString());
            color.setBackgroundColor(Color.parseColor(colorString));

            return convertView;
        }

        @Override
        public void onClick(View v) {

            // System.out.println(v.getTag());
        }
    }

}
