package com.todoapp.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    EditText etText;
    int elementId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        //Read values sent from main activity
        etText = (EditText) findViewById(R.id.etEditText);
        elementId = getIntent().getIntExtra("position", 0);
        //Set the text to the value sent from the main activity
        etText.setText(getIntent().getStringExtra("text"));
        //Set focus and set cursor to be at the end
        etText.requestFocus();
        etText.setSelection(etText.getText().length());
    }

    public void onEditItem(View view) {
        //Save the edited text in Extras and Submit the form, go back to main activity
        Intent data = new Intent(EditItemActivity.this, MainActivity.class);
        data.putExtra("position", elementId);
        data.putExtra("text", etText.getText().toString());
        setResult(RESULT_OK, data);
        finish();
    }
}
