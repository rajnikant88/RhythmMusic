package com.example.dell.rhythmmusic;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dell.rhythmmusic.RequestContent;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Requests extends AppCompatActivity {
    private ListView requestslv;
    public static FirebaseDatabase db = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference;
    List<RequestContent> requestList;
    private RequestContent requestContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        setTitle("Song Requests");

        requestList = new ArrayList<>();
        requestslv = findViewById(R.id.requestslv);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.show();

        databaseReference = db.getReference("Requests/");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Toast.makeText(Requests.this, "childadded", Toast.LENGTH_SHORT).show();

                requestContent = dataSnapshot.getValue(RequestContent.class);
                requestList.add(requestContent);
                requestslv.setAdapter(new RequestContentAdapter(getBaseContext(),requestList));
                progressDialog.dismiss();
                Toast.makeText(getBaseContext(), "Request Recieved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
