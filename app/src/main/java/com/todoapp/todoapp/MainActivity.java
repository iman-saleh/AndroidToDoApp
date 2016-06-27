package com.todoapp.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> todoItems;
    ArrayAdapter<String> aToDoAdapter;
    ListView lvItems;
    EditText etEditText;
    final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);
        etEditText = (EditText) findViewById(R.id.etEditText);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoItems.remove(position);
                aToDoAdapter.notifyDataSetChanged();
                writeItems();
                //Display a message that the item was edited successfully
                Toast.makeText(MainActivity.this, R.string.deleteSuccessful, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //When clicking on an item in the list, the user is sent to the edit activity/screen
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra("position", position);
                i.putExtra("text", aToDoAdapter.getItem(position));
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    public void populateArrayItems() {
        //Read items from the persistent storage, and create adapter to link the list view to the Array of items
        readItems();
        aToDoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,todoItems);
    }

    public void onAddItem(View view) {
        //Event handler for adding a new item. The item is added to the adapter and the edit text is cleared
        //The list items are also saved to persistent file
        aToDoAdapter.add(etEditText.getText().toString());
        etEditText.setText("");
        writeItems();
    }

    private void writeItems() {
        //Same list items to a file: todo.txt
        File fileDir = getFilesDir();
        File file = new File(fileDir, "todo.txt");
        try {
            FileUtils.writeLines(file, todoItems);
        } catch (IOException e) {}
    }

    private void readItems() {
        //Read list items from a file; todo.txt
        File fileDir = getFilesDir();
        File file = new File(fileDir, "todo.txt");
        try {
            todoItems = new ArrayList<String>(FileUtils.readLines(file));
        } catch (IOException e) {
            //Needed to add this line to avoid an exception the first time the app is launched and the file doesn't exist
            todoItems = new ArrayList<String>();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK && requestCode ==  REQUEST_CODE) {
            todoItems.set(data.getIntExtra("position", 0), data.getStringExtra("text"));
            aToDoAdapter.notifyDataSetChanged();
            writeItems();
            //Display a message that the item was edited successfully
            Toast.makeText(this, R.string.editSuccessful, Toast.LENGTH_LONG).show();
        }
    }


}
