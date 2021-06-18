package com.toylibrary.toystation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    EditText email, pwd, nameKid, nameMother, nameFather, phoneMother, phoneFather, deposit, regCharges, dateOfMembership, id;
    Button submit;
    FirebaseFirestore db;
    FirebaseUser CurrentUser;
    FirebaseAuth AuthUser;
    String UID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.emailInput);
        deposit = findViewById(R.id.depositInput);
        pwd = findViewById(R.id.pwdInput);
        nameKid = findViewById(R.id.nameKidInput);
        nameMother = findViewById(R.id.nameMotherInput);
        nameFather = findViewById(R.id.nameFatherInput);
        phoneMother = findViewById(R.id.phoneMotherInput);
        phoneFather = findViewById(R.id.phoneFatherInput);
        regCharges = findViewById(R.id.regChargesInput);
        dateOfMembership = findViewById(R.id.dateOfMembershipInput);
        id = findViewById(R.id.idInput);
        submit = findViewById(R.id.submit);

        db = FirebaseFirestore.getInstance();

        AuthUser = FirebaseAuth.getInstance();


        submit.setOnClickListener(v -> {
            if (nameKid.getText().toString().isEmpty()) {
                nameKid.setError("Name cannot be empty!");
            } else if (nameMother.getText().toString().isEmpty()) {
                nameMother.setError("Name cannot be empty!");
            } else if (nameFather.getText().toString().isEmpty()) {
                nameFather.setError("Name cannot be empty!");
            } else if (phoneMother.getText().toString().isEmpty() && (phoneMother.getText().toString().length() != 10) && phoneFather.getText().toString().isEmpty() && (phoneFather.getText().toString().length() != 10)) {
                Toast.makeText(this, "Enter atleast 1 valid phone number", Toast.LENGTH_SHORT).show();
                if (phoneMother.getText().toString().length() != 10 && !phoneMother.getText().toString().equals("")){
                    phoneMother.setError("Enter valid phone number");
                }
                if (phoneFather.getText().toString().length() != 10 && !phoneFather.getText().toString().equals("")){
                    phoneFather.setError("Enter valid phone number");
                }
            } else if (regCharges.getText().toString().isEmpty()) {
                regCharges.setError("Name cannot be empty!");
            } else if (deposit.getText().toString().isEmpty()) {
                deposit.setError("Name cannot be empty!");
            } else if (dateOfMembership.getText().toString().isEmpty()) {
                dateOfMembership.setError("Name cannot be empty!");
            } else if (email.getText().toString().isEmpty()) {
                email.setError("email cannot be empty!");
            } else if (pwd.getText().toString().isEmpty()) {
                pwd.setError("Password cannot be empty!");
            } else {
                String Email = email.getText().toString();
                String Password = pwd.getText().toString();
                AuthUser.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                        //FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>();
                        UID = CurrentUser.getUid();

                        try {
                            File file = new File("password.txt");
                            if (!file.exists()) {
                                file.createNewFile();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        writeToTxt(Password);

                        try {
                            String encryptedOutput = encryptFunct();
                            writeToTxt(encryptedOutput);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Map<String, Object> userId = new HashMap<>();
                        userId.put("id", id.getText().toString());
                        userId.put("pwd", readFromTxt());
                        db.collection("UserTypeCategorize").document(UID).set(userId);
                        Map<String, Object> userDetails = new HashMap<>();
                        userDetails.put("NameOfKid", nameKid.getText().toString());
                        userDetails.put("MotherName", nameMother.getText().toString());
                        userDetails.put("FatherName", nameFather.getText().toString());
                        userDetails.put("MotherPhone", phoneMother.getText().toString());
                        userDetails.put("FatherPhone", phoneFather.getText().toString());
                        userDetails.put("PlanScheme", "");
                        userDetails.put("RegistrationCharges", regCharges.getText().toString());
                        userDetails.put("DateOfMembership", dateOfMembership.getText().toString());
                        userDetails.put("Deposit", deposit.getText().toString());
                        db.collection("users").document(id.getText().toString()).set(userDetails);
                        Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                        email.setText("");
                        pwd.setText("");
                        id.setText("");
                        nameKid.setText("");
                        nameMother.setText("");
                        nameFather.setText("");
                        phoneMother.setText("");
                        phoneFather.setText("");
                        deposit.setText("");
                        regCharges.setText("");
                        dateOfMembership.setText("");
                    } else {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void writeToTxt(String prescription) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("password.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(prescription);
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String encryptFunct() throws Exception {
        String encryptInputText = readFromTxt();
        SecretKeySpec encryptionKey = hashKeyGenerator();
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);
        byte[] encVal = cipher.doFinal(encryptInputText.getBytes());
        return Base64.encodeToString(encVal, Base64.DEFAULT);
    }

    private String readFromTxt() {
        String result = "";
        InputStream inputStream = null;
        try {
            inputStream = openFileInput("password.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (inputStream != null) {

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String tempString = "";
            StringBuilder stringBuilder = new StringBuilder();

            while (true) {
                try {
                    if ((tempString = bufferedReader.readLine()) == null) {
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                stringBuilder.append(tempString).append("\n");

            }

            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            result = stringBuilder.toString();
        }
        return result;
    }

    private SecretKeySpec hashKeyGenerator() throws Exception {
        byte[] inputData = "6969696969".getBytes();
        byte[] outputData;
        MessageDigest shaVal = MessageDigest.getInstance("SHA-256");
        shaVal.update(inputData);
        outputData = shaVal.digest();
        return new SecretKeySpec(outputData, "AES");
    }
}