package com.example.dell.rhythmmusic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SongView extends AppCompatActivity{
    private View view;
    private ListView songlv;
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    List<AudioContent> songList;
    private AudioContent audioContent;
    String songCategory;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_view);
        Intent it = getIntent();
        songCategory = it.getStringExtra("key");
        setTitle(songCategory);


        songList = new ArrayList<>();
        songlv = findViewById(R.id.songlv);


        loadData(songCategory);
    }

    public void loadData(String category) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.show();

        databaseReference = database.getReference("audios/"+category);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {



                audioContent = dataSnapshot.getValue(AudioContent.class);
//                String syu =audioContent.getSongName();
                songList.add(audioContent);
                songlv.setAdapter(new AudioContentAdapter(getBaseContext(), songList));
                progressDialog.dismiss();


                songlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        /*final ProgressDialog pd = new ProgressDialog(SongView.this);
                        pd.setTitle("Loading");
                        pd.show();*/

                        String name = ((TextView) view.findViewById(R.id.txtsongname)).getText().toString();
                        String url = ((TextView) view.findViewById(R.id.txtsongurl)).getText().toString();
                        Toast.makeText(SongView.this, "clicked", Toast.LENGTH_SHORT).show();



                        Intent it = new Intent(getBaseContext(),AudioPlayer.class);
                        it.putExtra("songName",name);
                        it.putExtra("songUrl",url);
                        startActivity(it);
                        /*Toast.makeText(SongView.this, "activity", Toast.LENGTH_SHORT).show();
                        pd.dismiss();*/


                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(getBaseContext(),MusicActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);    }
}
