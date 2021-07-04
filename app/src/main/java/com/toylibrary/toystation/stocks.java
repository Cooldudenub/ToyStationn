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

public class stocks extends AppCompatActivity {

    Button submitProd;
    EditText prodId, prodName, prodPrice, prodPiece;
    RadioGroup stock;
    int radioButton;
    RadioButton ts, tsw;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks);

        submitProd = findViewById(R.id.submitprod);
        prodId = findViewById(R.id.prodIdInput);
        prodName = findViewById(R.id.prodNameInput);
        prodPrice = findViewById(R.id.prodPriceInput);
        prodPiece = findViewById(R.id.prodPieceInput);
        stock = findViewById(R.id.stock);
        ts = findViewById(R.id.ts);
        tsw = findViewById(R.id.tsw);

        db = FirebaseFirestore.getInstance();

        submitProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prodId.getText().toString().isEmpty()) {
                    prodId.setError("Name cannot be empty!");
                } else if (prodName.getText().toString().isEmpty()) {
                    prodName.setError("Name cannot be empty!");
                } else if (stock.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Select stock type!", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> prod = new HashMap<>();
                    prod.put("Product Name", prodName.getText().toString());
                    prod.put("Product Price", prodPrice.getText().toString());
                    prod.put("Product Piece", prodPiece.getText().toString());
                    radioButton = stock.getCheckedRadioButtonId();

                    if (ts.isChecked()) {
                        db.collection("TS").document(prodId.getText().toString()).set(prod);
                        Toast.makeText(getApplicationContext(), "hua!", Toast.LENGTH_SHORT).show();
                    } else {
                        db.collection("TSW").document(prodId.getText().toString()).set(prod);
                        Toast.makeText(getApplicationContext(), "TSW hua!", Toast.LENGTH_SHORT).show();

                    }
                    Toast.makeText(stocks.this, "Success!", Toast.LENGTH_SHORT).show();
                    prodId.setText("");
                    prodName.setText("");
                    prodPrice.setText("");
                    prodPiece.setText("");
                    //stock.clearCheck();
                }
            }
        });

    }
}