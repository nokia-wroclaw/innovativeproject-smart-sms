package com.example.smartsms;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.app.PendingIntent.getActivity;

public class RulesActivity extends AppCompatActivity implements View.OnTouchListener  {
    public final int REQUEST_CODE_ASK_PERMISSIONS = 1001;
    ArrayAdapter<String> listAdapter;
    private  TextView name;
    private  TextView phone;
    private  TextView keyWords;
    private TextInputLayout InputName;
    private TextInputLayout InputPhone;
    private TextInputLayout InputKeyWords;
    private Spinner spinerList;
    private ArrayList<String> list;
    private SqliteDB sqldb;

    float dX;
    float dY;
    private int lastAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules_view);

        sqldb = new SqliteDB(this);
        populateList();

        listAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,list);
        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinerList=findViewById(R.id.spinner);
        spinerList.setAdapter(listAdapter);


        View floatingButton=findViewById(R.id.floatingActionButton);
        floatingButton.setOnTouchListener(this);

        ImageButton backButton=findViewById(R.id.BackButton);
        ImageButton okButton=findViewById(R.id.OKButton);
        name = (TextView) findViewById(R.id.editTextName);
        phone = (TextView) findViewById(R.id.editTextPhone);
        keyWords = (TextView) findViewById(R.id.editTextKeyWords);

        InputName =  findViewById(R.id.textInputLayoutName);
        InputPhone =  findViewById(R.id.textInputLayoutPhone);
        InputKeyWords = findViewById(R.id.textInputLayoutKeyWord);

        okButton.setOnClickListener(new  View.OnClickListener() {

            public void onClick (View v) {
               addRule();
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


    //asking for the permission to get to the phone's contacts
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean contactPermission()
    {
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.READ_CONTACTS);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_CONTACTS},
                    REQUEST_CODE_ASK_PERMISSIONS);}
        if(hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED)return false;

        return true;

    }

    //getting to phone's contacts and setting text in phone's and name's textView
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode,resultCode,data);


        if(requestCode==1){
            if (resultCode == RESULT_OK)
            {
                Uri contactData = data.getData();
                Cursor c = getContentResolver().query(contactData, null, null, null, null);
                if (c.moveToFirst()) {

                   // String contactName = c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    if (Integer.valueOf(hasNumber) == 1) {

                        String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phone.setText(number);

                    }


                    //name.setText(contactName);

                }
            }
        }


    }

    //adding the rule to the database
    public void addRule()
    {
        String text;
        boolean check=true;


        if(name.getText().toString().length()<1)
        {
            InputName.setError("Field can't be empty");
            check=false;
        }

       else if(sqldb.getRule(name.getText().toString()).name!=null)
        {
            InputName.setError("this Rule's name already exists");
            check=false;
        }
        else {InputName.setError(null);}

        /*if(phone.getText().toString().length()<1)
        {
            InputPhone.setError("Field can't be empty");
            check=false;
        }
        else {InputPhone.setError(null);}*/

        if((spinerList.getSelectedItem() ==null))
        {
            check=false;
            Toast.makeText(RulesActivity.this,"There is no priority",Toast.LENGTH_LONG).show();
        }

        if(check==false){  return;}

        text = spinerList.getSelectedItem().toString();
        Priority priority=sqldb.getPriority(text);
        Rule r=new Rule(name.getText().toString(),keyWords.getText().toString(),phone.getText().toString(),priority);
        sqldb.addRule(r);

        Toast.makeText(RulesActivity.this,"Rule was added",Toast.LENGTH_LONG).show();
    }


    //creating list from priorities' names from the database
    public void populateList()
    {
        SQLiteDatabase db = sqldb.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT NAME from PRIORITY ", null);
        list=new ArrayList<>();
        String name;

        if (c != null ) { System.out.print("fff");
            if  (c.moveToFirst()) {
                do {

                    name = c.getString(c.getColumnIndex("NAME"));
                    list.add(name);
                }while (c.moveToNext());
            }
        }
    }

    //draggable and clickable floating button
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onTouch(View view, MotionEvent event) {


        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:

                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
              // Toast.makeText(RulesActivity.this,"pressed",Toast.LENGTH_LONG).show();
                break;

          /*  case MotionEvent.ACTION_MOVE:
                view.setY(event.getRawY() + dY);
                view.setX(event.getRawX() + dX);
                lastAction = MotionEvent.ACTION_MOVE;


                break;
*/
            case MotionEvent.ACTION_UP:
                if (lastAction ==MotionEvent.ACTION_DOWN )
                {
                    if(contactPermission()==true ){
                        Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                        pickContact.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                        startActivityForResult(pickContact, 1);
                    }}


                break;

            default:
                return false;
        }
        return true;
    }
}
