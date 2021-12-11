package com.example.mymusicapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    Button local,online,storage;
    String music_url = "https://firebasestorage.googleapis.com/v0/b/login-286eb.appspot.com/o/music%2FBassa%20Island%20Game%20Loop%20-%20Latinesque%20-%20Kevin%20MacLeod.mp3?alt=media&token=d3f2b934-3993-4dc8-bf39-eb199ed10a30";
    ImageView playPause;
    MediaPlayer mediaPlayer;
    TextView musicStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        local = findViewById(R.id.local);
        online = findViewById(R.id.online);
        storage = findViewById(R.id.user_storage);
        playPause = findViewById(R.id.play_pause_button);
        musicStatus = findViewById(R.id.music_status);


        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mediaPlayer!=null){
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        musicStatus.setText("Pause");
                        playPause.setImageResource(R.drawable.play);
                    }else if (!mediaPlayer.isPlaying()){
                        mediaPlayer.start();
                        playPause.setImageResource(R.drawable.pause);
                        musicStatus.setText("Running");
                    }


                }
            }
        });




        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.local_music);
                mediaPlayer.start(); // no need to call prepare(); create() does that for you
               if (mediaPlayer.isPlaying()){
                   musicStatus.setText("Running");
                   playPause.setImageResource(R.drawable.pause);
               }

               mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                   @Override
                   public void onCompletion(MediaPlayer mp) {
                       musicStatus.setText("Stop");
                       playPause.setImageResource(R.drawable.play);
                   }
               });
            }

        });


        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                );

                try {
                    mediaPlayer.setDataSource(music_url);
                    mediaPlayer.prepare(); // might take long! (for buffering, etc)

                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();

                        }
                    });




                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


        storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,11);


            }
        });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==11 && resultCode==RESULT_OK && data!=null){

            // music
            Uri uri = data.getData();

         mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(
                   new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            );
            try {
                mediaPlayer.setDataSource(getApplicationContext(), uri);
                mediaPlayer.prepare();
               mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                   @Override
                   public void onPrepared(MediaPlayer mp) {

                       mp.start();
                   }
               });
            } catch (IOException e) {
                e.printStackTrace();
            }





        }



    }
}