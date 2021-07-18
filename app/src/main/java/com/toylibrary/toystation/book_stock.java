package com.toylibrary.toystation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class book_stock extends AppCompatActivity {

    Button submitProd;
    EditText prodId, prodName;
    RadioGroup stock;
    int radioButton;
    RadioButton mythology, phonics, abc, onetwothree, picture, sparkle, series, flipflap, story, game, category;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_stock);

        submitProd = findViewById(R.id.submitprod);
        prodId = findViewById(R.id.BookIdInput);
        prodName = findViewById(R.id.BookNameInput);
        stock = findViewById(R.id.Bookstock);
        mythology = findViewById(R.id.mythology);
        phonics = findViewById(R.id.phonics);
        abc = findViewById(R.id.abc);
        onetwothree = findViewById(R.id.onetwothree);
        picture = findViewById(R.id.picture);
        sparkle = findViewById(R.id.sparkle);
        series = findViewById(R.id.series);
        flipflap = findViewById(R.id.flipflap);
        story = findViewById(R.id.story);
        game = findViewById(R.id.game);

        db = FirebaseFirestore.getInstance();

        submitProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prodId.getText().toString().isEmpty()) {
                    prodId.setError("ID cannot be empty!");

                } else if (prodName.getText().toString().isEmpty()) {
                    prodName.setError("Name cannot be empty!");
                } else {
                    Map<String, Object> prod = new HashMap<>();
                    category = findViewById(stock.getCheckedRadioButtonId());
                    prod.put("Product Name", prodName.getText().toString());
                    prod.put("Category", category.getText().toString());

                    db.collection("Books").document(prodId.getText().toString()).set(prod);

                    Toast.makeText(book_stock.this, "Success!", Toast.LENGTH_SHORT).show();
                    prodId.setText("");
                    prodName.setText("");
                    prodId.requestFocus();

                    //stock.clearCheck();
                }
            }
        });
    }
}
