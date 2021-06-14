package com.toylibrary.toystation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText email, pwd, nameKid, nameMother, nameFather, phoneMother, phoneFather, deposit, planScheme, regCharges, dateOfMembership;
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
        planScheme = findViewById(R.id.planSchemeInput);
        regCharges = findViewById(R.id.regChargesInput);
        dateOfMembership = findViewById(R.id.dateOfMembershipInput);
        submit = findViewById(R.id.submit);

        db = FirebaseFirestore.getInstance();

        AuthUser = FirebaseAuth.getInstance();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameKid.getText().toString().isEmpty()) {
                    nameKid.setError("Name cannot be empty!");
                } else if (nameMother.getText().toString().isEmpty()) {
                    nameMother.setError("Name cannot be empty!");
                } else if (nameFather.getText().toString().isEmpty()) {
                    nameFather.setError("Name cannot be empty!");
                } else if (phoneMother.getText().toString().isEmpty() && (phoneMother.getText().toString().length() != 10)) {
                    phoneMother.setError("Phone length should be 10!");
                } else if (phoneFather.getText().toString().isEmpty() && (phoneFather.getText().toString().length() != 10)) {
                    phoneFather.setError("Phone length should be 10!");
                } else if (planScheme.getText().toString().isEmpty()) {
                    planScheme.setError("Name cannot be empty!");
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
                    AuthUser.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                                //FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>();
                                UID = AuthUser.getCurrentUser().getUid();
                                Map<String, Object> userDetails = new HashMap<>();
                                userDetails.put("NameOfKid", nameKid);
                                userDetails.put("MotherName", nameMother);
                                userDetails.put("FatherName", nameFather);
                                userDetails.put("MotherPhone", phoneMother);
                                userDetails.put("FatherPhone", phoneFather);
                                userDetails.put("PlanScheme", planScheme);
                                userDetails.put("RegistrationCharges", regCharges);
                                userDetails.put("DateOfMembership", dateOfMembership);
                                userDetails.put("Deposit", deposit);
                                db.collection("users").document(UID).set(userDetails);
                            } else {
                                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}