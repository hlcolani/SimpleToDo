package com.example.simpletodo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //obtain reference to ListView
        lvItems = (ListView) findViewById(R.id.lvItems);
        //initialize items list
        readItems();
        //initialize adapter from items list
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        //connect adapter to view
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }

    public void onAddItem(View v) {
        //obtain ref to text box
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        //obtain text box content as String
        String itemText = etNewItem.getText().toString();
        //add item to list via adapter
        itemsAdapter.add(itemText);
        // store the updated list
        writeItems();
        //clear text box
        etNewItem.setText("");
        //notify user
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();

    }

    private void setupListViewListener() {
        // set the ListView's itemLongClickListener
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                //remove item at index given by position from list
                items.remove(position);
                //notify Adapter
                itemsAdapter.notifyDataSetChanged();
                //return true to tell the framework that the long click was consumed
                Log.i("MainActivity", "Removed item " + position);
                // store the updated list
                writeItems();
                return true;
            }
        });
    }

    //returns the file in which the data is stored
    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    //read the items from the file system
    private void readItems() {
        try {
            // create the array using the content in the file
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            //print error to console
            e.printStackTrace();
            //initialize list to an empty list
            items = new ArrayList<>();
        }
    }

    //write items to the file system
    private void writeItems() {
        try {
            // save the item list as a line-delimited text file
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            // print the error to the console
            e.printStackTrace();
        }
    }

}
