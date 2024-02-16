package com.example.budgetmoniter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

public class AddTransactionActivity extends AppCompatActivity {

    private EditText editTextDescription, editTextAmount;
    private RadioGroup radioGroupType;
    private Button buttonSave;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        dbHelper = new DBHelper(this);

        editTextDescription = findViewById(R.id.editTextDescription);
        editTextAmount = findViewById(R.id.editTextAmount);
        radioGroupType = findViewById(R.id.radioGroupType);
        buttonSave = findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTransaction();
            }
        });
    }

    private void saveTransaction() {
        String description = editTextDescription.getText().toString();
        String amountText = editTextAmount.getText().toString();

        if (description.isEmpty() || amountText.isEmpty()) {
            // Handle empty fields, show a message or toast
            return;
        }

        double amount = Double.parseDouble(amountText);
        RadioButton selectedRadioButton = findViewById(radioGroupType.getCheckedRadioButtonId());

        if (selectedRadioButton == null) {
            // Handle no radio button selected, show a message or toast
            return;
        }

        String type = selectedRadioButton.getText().toString();

        dbHelper.saveTransaction(description, amount, type);

        // Optionally, you can add code to update the UI or perform any other actions after saving the transaction

        // Redirect back to the home activity
        Intent intent = new Intent(AddTransactionActivity.this, Home.class);
        startActivity(intent);
        finish(); // Finish this activity to prevent going back to it using the back button
    }
}
