package com.example.smartsms;

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

import java.util.ArrayList;
import java.util.List;

public class ListOfRulesActivity extends AppCompatActivity{


    private List<Rule> rules;
    private List<String> rulesName;
    private List<String> rulesPhone;
    private List<String> rulesKeyWords;
    private SqliteDB db;
    private ListView listRules;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_rules);

        db = new SqliteDB(this);
        populateList();
        listRules=(ListView) findViewById(R.id.listRules);
        listRules.setClickable(true);

        CustomAddapter addapter=new CustomAddapter();
        listRules.setAdapter(addapter);
        listRules.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                return;
            }
        })


        ;

        ImageButton backButton=findViewById(R.id.BackButton);
        ImageButton deleteButton=findViewById(R.id.DeleteButton);
        backButton.setOnClickListener(new  View.OnClickListener() {

            public void onClick (View v) {
                finish();return;
            }


        });
        deleteButton.setOnClickListener(new  View.OnClickListener(){
            public void onClick (View v) {

                return;
            }
        });

    }


    public void populateList()
    {
        System.out.println("vvvvvvv");
        rules=new ArrayList<Rule>(db.getAllRule());
        rulesName=new ArrayList<String>();
        rulesPhone=new ArrayList<String>();
        rulesKeyWords=new ArrayList<String>();
        for(int i=0;i<rules.size();i++)
        {
            System.out.println("aaaaa");
            rulesName.add(rules.get(i).name);
            rulesPhone.add(rules.get(i).phoneNumber);
            rulesKeyWords.add(rules.get(i).phrase);
        }

    }

     class CustomAddapter extends BaseAdapter{


         @Override
         public int getCount() {
             return rules.size();
         }

         @Override
         public Object getItem(int position) {
             return null;
         }

         @Override
         public long getItemId(int position) {
             return 0;
         }

         @Override
         public View getView(int position, View convertView, ViewGroup parent) {

             convertView=getLayoutInflater().inflate(R.layout.custom_rules_list_view,null);
             TextView name=(TextView)convertView.findViewById(R.id.textViewRuleName);
             TextView phone=(TextView)convertView.findViewById(R.id.textViewRulePhone);
             name.setText("name: "+rulesName.get(position).toString());
             phone.setText("phone's number: "+rulesPhone.get(position).toString());

             return convertView;
         }
     }
}