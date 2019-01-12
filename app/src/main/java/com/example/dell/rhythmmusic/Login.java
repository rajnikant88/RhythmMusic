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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private EditText lemail, lpass;
    private Button lbtn;
    private TextView createAcc;
    private CheckBox s_h_pass;
    private String email, password;
    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        lemail = findViewById(R.id.lemail);
        lpass = findViewById(R.id.lpass);
        createAcc = findViewById(R.id.createAcc);
        s_h_pass = findViewById(R.id.s_h_pass);
        lbtn = findViewById(R.id.lbtn);


        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(),SignUp.class));
            }
        });


        s_h_pass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        if (isChecked) {

                            s_h_pass.setText(R.string.hide_pwd);

                            lpass.setInputType(InputType.TYPE_CLASS_TEXT);
                            lpass.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());

                        } else {
                           s_h_pass.setText(R.string.show_pwd);

                            lpass.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            lpass.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());

                        }

                    }
                });
        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = lemail.getText().toString();
                password = lpass.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                        if(email.isEmpty())
                            lemail.setError("required.");
                        if(password.isEmpty())
                            lpass.setError("required.");
                } else {

                    auth = FirebaseAuth.getInstance();
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    boolean Isboolean = true;
                                   SharedPreferences pref = getSharedPreferences("mydb", MODE_PRIVATE);
                                   SharedPreferences.Editor editor = pref.edit();

                                    editor.putString("keyemail", email);
                                    editor.putString("keypass", password);
                                    editor.putBoolean("Islogin", Isboolean);
                                    editor.commit();
                                    startActivity(new Intent(Login.this, MusicActivity.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getBaseContext(), "you are invalid user, please sign up first.", Toast.LENGTH_SHORT).show();
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
