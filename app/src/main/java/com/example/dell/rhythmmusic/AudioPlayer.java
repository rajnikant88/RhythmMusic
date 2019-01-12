package com.example.dell.rhythmmusic;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AudioPlayer extends AppCompatActivity implements MediaPlayer.OnPreparedListener  {

    private ImageView iplay,ipause,istop,iforward, ibackward;
    private static MediaPlayer mediaPlayer;

    private static double startTime = 0;
    private static double finalTime = 0;
    private static int count =0;

    private Handler myHandler = new Handler();;
    private final int forwardTime = 5000;
    private final int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView tsong, tv1, tv2;
    private static int playposition = 0;

    public static int puaseCount = 0;

//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        setTitle("");

        iplay = findViewById(R.id.iplay);
        ipause = findViewById(R.id.ipause);
        istop = findViewById(R.id.istop);
        iforward = findViewById(R.id.iforward);
        ibackward = findViewById(R.id.ibackward);
        seekbar = findViewById(R.id.seekBar);
        tsong = findViewById(R.id.tsong);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);



        Intent intent = getIntent();
        String name = intent.getStringExtra("songName");
        final String Url = intent.getStringExtra("songUrl");

        tsong.setText(name);


        mediaPlayer = new MediaPlayer();



        try {
            mediaPlayer.setDataSource(Url);


        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();

        seekbar = (SeekBar)findViewById(R.id.seekBar);
        seekbar.setClickable(false);

       /* finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/
        iplay.setEnabled(true);




       iplay.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                if(count >= 1 || puaseCount >= 1){
                    pl(mediaPlayer);
                }
                if(count==0){
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            pl(mp);




                        }
                    });
                }
                    Toast.makeText(getApplicationContext(), "Playing music", Toast.LENGTH_SHORT).show();


                    seekbar.setProgress((int) startTime);
                    myHandler.postDelayed(UpdateSongTime, 100);
                    ipause.setEnabled(true);
                    ibackward.setEnabled(true);
                    istop.setEnabled(true);
                    iplay.setEnabled(false);
                }




        });

        ipause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(AudioPlayer.this, "Pausing music", Toast.LENGTH_SHORT).show();
                mediaPlayer.pause();
                playposition = mediaPlayer.getCurrentPosition();
                puaseCount++;


                ipause.setEnabled(false);
                ibackward.setEnabled(true);
                if(!iplay.isEnabled()){
                    iplay.setEnabled(true);
                }
                if(!istop.isEnabled()){
                    istop.setEnabled(true);
                }
            }
        });

        istop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer!=null && mediaPlayer.isPlaying()){
//                    mediaPlayer.stop();
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                    count++;
                    istop.setEnabled(false);
                    iplay.setEnabled(true);
                }
            }
        });

        iforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int)startTime;

                if((temp+forwardTime)<=finalTime){
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    playposition = mediaPlayer.getCurrentPosition();
                    Toast.makeText(getApplicationContext(),"You have Jumped forward 5 seconds",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Cannot jump forward 5 seconds",Toast.LENGTH_SHORT).show();
                    if(!iplay.isEnabled()){
                        iplay.setEnabled(true);
                    }
                }
            }
        });


        ibackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int)startTime;

                if((temp-backwardTime)>0){
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    playposition = mediaPlayer.getCurrentPosition();
                    Toast.makeText(getApplicationContext(),"You have Jumped backward 5 seconds",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Cannot jump backward 5 seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private Runnable UpdateSongTime = new Runnable() {
        @SuppressLint("DefaultLocale")
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            tv1.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };






        @Override
        public void onPrepared (MediaPlayer mp){
           /* final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Loading");
            progressDialog.show();*/
        }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
            mediaPlayer.stop();
            }
    public void pl(MediaPlayer mdp){
        mdp.seekTo(playposition);
        mdp.start();
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();
        playposition=0;


        seekbar.setMax((int) finalTime);

        tv1.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                startTime)))
        );

        tv2.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                finalTime)))
        );
    }
}
