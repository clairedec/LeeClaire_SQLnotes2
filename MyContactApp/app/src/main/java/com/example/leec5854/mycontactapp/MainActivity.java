package com.example.leec5854.mycontactapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText editName;
    EditText editAddress;
    EditText editNumber;
    EditText editSearch;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName= findViewById(R.id.editText_name);
        editAddress= findViewById(R.id.editText_address);
        editNumber= findViewById(R.id.editText_number);
        editSearch= findViewById(R.id.editText_search);

        myDb = new DatabaseHelper(this);
        Log.d("MyContactApp", "MainActivity: instantiated myDb");

    }

    public void addData(View view){
        Log.d("MyContactApp", "MainActivity: Add contact button pressed");

        boolean isInserted=myDb.insertData(editName.getText().toString(), editAddress.getText().toString(), editNumber.getText().toString());
        if(isInserted==true){
            Toast.makeText(MainActivity.this, "Success - contact inserted", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(MainActivity.this, "FAILED - contact not inserted", Toast.LENGTH_LONG).show();
        }
    }

    public void viewData(View view){
        Cursor res = myDb.getAllData();
        Log.d("MyContactApp", "MainActivity: viewData: received cursor");

        if(res.getCount()==0){
            showMessage("Error", "No data found in database");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            //Append res column 0, 1, 2, 3 to the buffer - see Stringbuffer and Cursor api's
            //Delimit each of the "appends" with line feed "\n"
            buffer.append("\nName: " + res.getString(1));
            buffer.append("\nAddress: " + res.getString(2));
            buffer.append("\nNumber: " + res.getString(3));

        }

        showMessage("Data", buffer.toString());

    }

    private void showMessage(String title, String message) {
        Log.d("MyContactApp", "MainActivity: showMessage: assembling AlertDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }

    public static final String EXTRA_MESSAGE = "com.example.clairelee.mycontactapp_p2.MESSAGE";
    public void searchRecord(View view){
        Log.d("MyContactApp", "MainActivity: launching SearchActivity");
        Cursor x = myDb.getAllData();
        Intent intent = new Intent ( this, SearchActivity.class);
        StringBuffer buffer = new StringBuffer();

        while(x.moveToNext()){
            if(buffer.length()!=0 && x.getString(1).equals(editSearch.getText().toString())) {
                buffer.append("Name: " + x.getString(1) + "\n");
                buffer.append("Address: " + x.getString(2) + "\n");
                buffer.append("Number: " + x.getString(3) + "\n");
            }
            else
            {
                buffer.append("No name in database");
            }
        }

       /* if(buffer.length()==0){
            buffer.append("No name in database");
        }*/
        intent.putExtra(EXTRA_MESSAGE, buffer.toString());
        startActivity(intent);
        }


    }


