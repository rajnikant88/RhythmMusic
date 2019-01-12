package com.example.dell.rhythmmusic;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    private EditText sname, scontact, semail, spass, scpass;
    private Button sbtn;
    private String name, contact, email, password, cpassword;
    private TextView lgntxt;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        lgntxt = findViewById(R.id.lgntxt);

        lgntxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                startActivity(new Intent(getBaseContext(),Login.class));
            }
        });

        sname = findViewById(R.id.sname);
        scontact = findViewById(R.id.scontact);
        semail = findViewById(R.id.semail);
        spass = findViewById(R.id.spass);
        scpass = findViewById(R.id.scpass);
        sbtn = findViewById(R.id.sbtn);

        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = sname.getText().toString();
                contact = scontact.getText().toString();
                email = semail.getText().toString();
                password = spass.getText().toString();
                cpassword = scpass.getText().toString();

                if (name.isEmpty() || contact.isEmpty() || email.isEmpty() || password.isEmpty() || cpassword.isEmpty())
                {
                   if(name.isEmpty())
                       sname.setError("required");
                   if(contact.isEmpty())
                        scontact.setError("required");
                   if(email.isEmpty())
                        semail.setError("required");
                   if(password.isEmpty())
                        spass.setError("required");
                    if(cpassword.isEmpty())
                        scpass.setError("required");
                }
                else if(!(password.equals(cpassword))){

                    Toast.makeText(getBaseContext(), "password didn't match", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(email,password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(getBaseContext(), "success.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getBaseContext(), "try again.", Toast.LENGTH_SHORT).show();
                                    sname.setText("");
                                    scontact.setText("");
                                    semail.setText("");
                                    spass.setText("");
                                    scpass.setText("");
                                }
                            });
                }
            }
        });
    }
    public void onBackPressed(){
        Intent it = new Intent(getBaseContext(),MusicActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);
    }


}
