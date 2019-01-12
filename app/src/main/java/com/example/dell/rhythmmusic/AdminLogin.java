package com.example.dell.rhythmmusic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLogin extends AppCompatActivity {
    private EditText alemail, alpass;
    private Button albtn;
    private CheckBox a_s_h_pass;
    private String email, password;
    private FirebaseAuth auth;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String aemail;
    private String apassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        alemail = findViewById(R.id.alemail);
        alpass = findViewById(R.id.alpass);
        a_s_h_pass = findViewById(R.id.a_s_h_pass);
        albtn = findViewById(R.id.albtn);

        a_s_h_pass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton button,
                                         boolean isChecked) {

                if (isChecked) {

                    a_s_h_pass.setText(R.string.hide_pwd);

                    alpass.setInputType(InputType.TYPE_CLASS_TEXT);
                    alpass.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());

                } else {
                    a_s_h_pass.setText(R.string.show_pwd);

                    alpass.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    alpass.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());

                }

            }
        });
        aemail="rajnikant1362@gmail.com";
        apassword= "raj@123";
       /* auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(aemail,apassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getBaseContext(), "success.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });*/
        albtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                email = alemail.getText().toString();
                password = alpass.getText().toString();

               /* auth = FirebaseAuth.getInstance();
                auth.signInWithEmailAndPassword(email,password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {*/
                if (email.isEmpty() || password.isEmpty()) {
                    if(email.isEmpty())
                        alemail.setError("required");
                    if(password.isEmpty())
                        alpass.setError("required");

                } else {
                    if (email.equals(aemail) && password.equals(apassword)) {
                        boolean Isalogin = true;
                        editor = getBaseContext().getSharedPreferences("adb", MODE_PRIVATE).edit();
                        editor.putString("kemail", email);
                        editor.putString("kpass", password);
                        editor.putBoolean("IsLogin",Isalogin);
                        editor.commit();
                        startActivity(new Intent(getBaseContext(), AdminMain.class));
                    } else {
                        Toast.makeText(getBaseContext(), "you are invalid admin", Toast.LENGTH_SHORT).show();
                    }


                           /* }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getBaseContext(), "you are invalid admin", Toast.LENGTH_SHORT).show();

                            }
                        });*/
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
