package com.toocms.drink5.boss.config;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;

import java.io.File;

/**
 * @author Zero
 * @date 2016/4/20 16:47
 */
public class MyVoicePlay implements View.OnClickListener {

    MediaPlayer mediaPlayer = null;
    Activity activity;
    public static boolean isPlaying = false;
    public static MyVoicePlay currentPlayListener = null;
    private String filePath;
    private People people;

    public MyVoicePlay(Activity context, String filePath, People people) {
        this.activity = context;
        this.filePath = filePath;
        this.people = people;
    }

    public void stopPlayVoice() {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
        }
        isPlaying = false;
    }

    public void playVoice(String filePath) {
        if ((new File(filePath)).exists()) {
            AudioManager audioManager = (AudioManager) this.activity.getSystemService(Context.AUDIO_SERVICE);
            this.mediaPlayer = new MediaPlayer();
            audioManager.setMode(0);
            audioManager.setSpeakerphoneOn(true);
            this.mediaPlayer.setAudioStreamType(2);

            try {
                this.mediaPlayer.setDataSource(filePath);
                this.mediaPlayer.prepare();
                this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        MyVoicePlay.this.mediaPlayer.release();
                        MyVoicePlay.this.mediaPlayer = null;
                        MyVoicePlay.this.stopPlayVoice();
                        people.endPlay();
                    }
                });
                isPlaying = true;
                currentPlayListener = this;
                this.mediaPlayer.start();
                if (isPlaying = true) {
                    people.startPlay();
                }
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }
    }

    public interface People {
        void startPlay();
        void endPlay();
        void stopPlay();
    }

    public void onClick(View v) {
        if (isPlaying) {
            currentPlayListener.stopPlayVoice();
            people.stopPlay();
        }else{
            this.playVoice(this.filePath);
        }
    }
}
