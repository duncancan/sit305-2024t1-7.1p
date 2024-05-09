package com.example.a71plostfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateNew extends AppCompatActivity {

    // References for screen elements
    Button btn_back, btn_save;
    EditText et_itemName, et_itemDescription, et_itemFoundDate, et_itemLocation, et_userName, et_userPhone;
    RadioButton rbtn_lost, rbtn_found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_new);

        // Find screen elements
        btn_back = findViewById(R.id.btn_back);
        btn_save = findViewById(R.id.btn_save);
        et_itemName = findViewById(R.id.et_itemName);
        et_itemDescription = findViewById(R.id.et_itemDescription);
        et_itemFoundDate = findViewById(R.id.et_itemFoundDate);
        et_itemLocation = findViewById(R.id.et_itemLocation);
        et_userName = findViewById(R.id.et_userName);
        et_userPhone = findViewById(R.id.et_userPhone);
        rbtn_lost = findViewById(R.id.rbtn_lost);
        rbtn_found = findViewById(R.id.rbtn_found);

        // When the user clicks save, perform field validation and save to database
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostModel postModel;

                // First, get all fields
                String itemName = et_itemName.getText().toString();
                String itemDescription = et_itemDescription.getText().toString();
                String itemFoundDate = et_itemFoundDate.getText().toString();
                String itemLocation = et_itemLocation.getText().toString();
                String userName = et_userName.getText().toString();
                String userPhone = et_userPhone.getText().toString();
                String postType;
                if (rbtn_found.isChecked()) {
                    postType = "Found";
                } else { // Lost is checked by default in the XML, so we know it's one of the two
                    postType = "Lost";
                }

                // Strings for matching due date and phone number fields
                String dateMatchRegex = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";
                String phoneMatchRegex = "^(?:\\+?(61))? ?(?:\\((?=.*\\)))?(0?[2-57-8])\\)? ?(\\d\\d(?:[- ](?=\\d{3})|(?!\\d\\d[- ]?\\d[- ]))\\d\\d[- ]?\\d[- ]?\\d{3})$"; // source: https://regex101.com/r/dkFASs/6

                // Validate fields aren't blank and valid
                if (itemName.equals("")) {
                    Toast.makeText(CreateNew.this, "Item name is mandatory", Toast.LENGTH_SHORT).show();
                }
                else if (itemFoundDate.equals("")) {
                    Toast.makeText(CreateNew.this, "Please enter a date the item was lost or found", Toast.LENGTH_SHORT).show();
                }
                // Check that a valid found date has been entered
                else if (!itemFoundDate.matches(dateMatchRegex)) {
                    Toast.makeText(CreateNew.this, "Lost/found date must be in format yyyy-mm-dd", Toast.LENGTH_SHORT).show();
                }
                else if (itemLocation.equals("")) {
                    Toast.makeText(CreateNew.this, "Location field is mandatory", Toast.LENGTH_SHORT).show();
                }
                else if (userName.equals("")) {
                    Toast.makeText(CreateNew.this, "Your name is mandatory", Toast.LENGTH_SHORT).show();
                }
                else if (userPhone.equals("")) {
                    Toast.makeText(CreateNew.this, "Your phone number is mandatory", Toast.LENGTH_SHORT).show();
                }
                else if (!userPhone.matches(phoneMatchRegex)) {
                    Toast.makeText(CreateNew.this, "Please enter a valid Australian phone number with area code", Toast.LENGTH_SHORT).show();
                }
                // If everything was OK, add the post to the database
                else {
                    DatabaseHelper dbHelper = new DatabaseHelper(CreateNew.this);
                    postModel = new PostModel(postType, itemName, itemDescription, itemFoundDate, itemLocation, userName, userPhone);
                    boolean success = dbHelper.addPost(postModel);
                    if (success) {
                        Intent intent = new Intent(CreateNew.this, MainActivity.class);
                        startActivity(intent);
                    }
                }

            }
        });

        // If they click back instead, take them back to the main activity screen without saving
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNew.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}