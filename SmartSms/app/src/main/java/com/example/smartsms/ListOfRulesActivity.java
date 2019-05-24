package com.example.smartsms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListOfRulesActivity extends AppCompatActivity{


    private ArrayList<Rule> rules;
    private List<String> rulesName;
    private List<String> rulesPhone;
    private List<String> rulesKeyWords;
    private List<ColorPriority> hierarchy;
    private SqliteDB db;
    private ListView listRules;
    private String nameDelete;
    private int position;
    private  CustomAddapter addapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_rules);
///
        db = new SqliteDB(this);
       // db.addColorPriority(new ColorPriority("#ffcc0000",10));
       // db.deleteColorPriority("#ffcc0000");db.deleteColorPriority("#ffcc0000");db.deleteColorPriority("#ffcc0000");
       /* db.addColorPriority(new ColorPriority("#ff0099cc",9));
        db.addColorPriority(new ColorPriority("#ffaaaaaa",8));
        db.addColorPriority(new ColorPriority("#ff000000",7));
        db.addColorPriority(new ColorPriority("#ff669900",6));
        db.addColorPriority(new ColorPriority("#ffff8800",5));

*/
        populateList();
        listRules=(ListView) findViewById(R.id.listRules);
        listRules.setClickable(true);

         addapter=new CustomAddapter(rules,getApplicationContext());
        listRules.setAdapter(addapter);
        listRules.setClickable(true);

        listRules.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                nameDelete=rules.get(position).name;

                position=position;

                return;
            }
        })


        ;

        ImageButton backButton=findViewById(R.id.BackButton);
        final ImageButton deleteButton=findViewById(R.id.DeleteButton);
        backButton.setOnClickListener(new  View.OnClickListener() {

            public void onClick (View v) {
                finish();return;
            }


        });

        ImageButton editButton=findViewById(R.id.EditButton);
        editButton.setOnClickListener(new  View.OnClickListener() {

            public void onClick (View v) {

                RuleListView();return;
            }


        });

        deleteButton.setOnClickListener(new  View.OnClickListener(){
            public void onClick (View v) {

                if(db.getRule(nameDelete)!=null) {
                    db.deleteRule(nameDelete);
                    Toast.makeText(ListOfRulesActivity.this,"Rule "+nameDelete+" was deleted",Toast.LENGTH_LONG).show();

                  //  rules.remove(position);
                    populateList();
                    addapter=new CustomAddapter(rules,getApplicationContext());
                    listRules.setAdapter(addapter);


                }
                return;
            }
        });

    }
    public void RuleListView(){
        Intent i = new Intent(ListOfRulesActivity.this, Hierarchy.class);
        startActivity(i);
    }




    public void populateList()
    {
        // System.out.println("vvvvvvv");
        rules=new ArrayList<Rule>(db.getAllRule());
        ArrayList sort=new ArrayList<Rule>();
         hierarchy=new ArrayList<ColorPriority>();
         hierarchy=db.getAllColor();
        rulesName=new ArrayList<String>();
        rulesPhone=new ArrayList<String>();
        rulesKeyWords=new ArrayList<String>();

        Collections.sort(hierarchy);

        for(int j=0;j<hierarchy.size();j++) {

           // System.out.println(hierarchy.get(j).color_priority+" "+hierarchy.get(j).color);
            for (int i = 0; i < rules.size(); i++) {
                //red
                if (rules.get(i).priority.color.equals(hierarchy.get(j).color)) {
                    sort.add(rules.get(i));
                }
            }
        }
       /* for(int i=0;i<rules.size();i++)
        {
            //red
            if(rules.get(i).priority.color.equals("#ffcc0000"))
            {sort.add(rules.get(i));}
        }
        for(int i=0;i<rules.size();i++)
        {
            //blue
            if(rules.get(i).priority.color.equals("#ff0099cc"))
            {sort.add(rules.get(i));}
        }
        for(int i=0;i<rules.size();i++)
        {
            //grey
            if(rules.get(i).priority.color.equals("#ffaaaaaa"))
            {sort.add(rules.get(i));}
        }
        for(int i=0;i<rules.size();i++)
        {
            //black
            if(rules.get(i).priority.color.equals("#ff000000"))
            {sort.add(rules.get(i));}
        }
        for(int i=0;i<rules.size();i++)
        {
            //green
            if(rules.get(i).priority.color.equals("#ff669900"))
            {sort.add(rules.get(i));}
        }

        for(int i=0;i<rules.size();i++)
        {
            //orange
            if(rules.get(i).priority.color.equals("#ffff8800"))
            {sort.add(rules.get(i));}
        }
*/
        rules=sort;
        //System.out.println("size"+rules.size());
        for(int i=0;i<rules.size();i++)
        {
            // System.out.println(rules.get(i).name);
            rulesName.add(rules.get(i).name);
            rulesPhone.add(rules.get(i).phoneNumber);
            rulesKeyWords.add(rules.get(i).phrase);
        }

    }

     class CustomAddapter extends ArrayAdapter<Rule> implements View.OnClickListener{

         private ArrayList<Rule> dataSet;
         Context mContext;

         public CustomAddapter(ArrayList<Rule> data, Context context) {
             super(context, R.layout.custom_rules_list_view,data);
             this.dataSet=data;
             this.mContext=context;
         }


         @Override
         public View getView(int position, View convertView, ViewGroup parent) {

             convertView=getLayoutInflater().inflate(R.layout.custom_rules_list_view,parent,false);
             TextView name=(TextView)convertView.findViewById(R.id.textViewRuleName);
             TextView phone=(TextView)convertView.findViewById(R.id.textViewRulePhone);
             name.setText("name: "+rulesName.get(position).toString());
             phone.setText("phone's number: "+rulesPhone.get(position).toString());

             return convertView;
         }

         @Override
         public void onClick(View v) {

            // System.out.println(v.getTag());
         }
     }
}