package com.example.backgroundmusicapp;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MusicService extends Service {
    ArrayList<String> mp3List;
    String mp3Path = Environment.getExternalStorageDirectory().getPath() + "/";
    MediaPlayer mp;
    int mp3Index=0;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(getApplicationContext(), "onCreate()", Toast.LENGTH_SHORT).show();
        super.onCreate();

        mp3List = new ArrayList<String>();

        File[] listFiles = new File(mp3Path).listFiles();
        String fileName, extName;
        for (File file : listFiles) {
            fileName = file.getName();
            extName = fileName.substring(fileName.length() - 3);
            if (extName.equals("mp3"))
                mp3List.add(fileName);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "onStartCommand()", Toast.LENGTH_SHORT).show();
        try {
            mp = new MediaPlayer();
            if (mp3List.size() > mp3Index) {
                mp.setDataSource(mp3Path + mp3List.get(mp3Index));
                mp.prepare();
                mp.start();
            }
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    mp.reset();

                    mp3Index += 1;
                    if (mp3List.size() <= mp3Index) {
                        mp3Index = 0;
                    }
                    try {
                        mp.setDataSource(mp3Path + mp3List.get(mp3Index));
                        mp.prepare();
                        mp.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(), "onDestroy()", Toast.LENGTH_SHORT).show();
        mp.stop();
        super.onDestroy();
    }
}
