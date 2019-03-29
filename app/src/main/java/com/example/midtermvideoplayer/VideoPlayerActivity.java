package com.example.midtermvideoplayer;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.io.IOException;

public class VideoPlayerActivity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {

    SurfaceView videoSurface;
    MediaPlayer player;
    VideoControllerView controller;
    int mPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        SurfaceHolder videoHolder = videoSurface.getHolder();
        videoHolder.addCallback(this);

        player = new MediaPlayer();
        controller = new VideoControllerView(this);

        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(this, Uri.parse("https://s3-ap-northeast-1.amazonaws.com/mid-exam/Video/taeyeon.mp4"));
//            player.setDataSource(this, Uri.parse("https://s3-ap-northeast-1.amazonaws.com/mid-exam/Video/protraitVideo.mp4"));
            player.setOnPreparedListener(this);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                mPercent = percent;
            }
        });
        player.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                android.view.ViewGroup.LayoutParams lp = videoSurface.getLayoutParams();
                lp.width = width*2;
                lp.height = height*2;
                videoSurface.setLayoutParams(lp);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controller.show();
        return false;
    }

    // Implement SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(holder);
        player.prepareAsync();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    // End SurfaceHolder.Callback

    // Implement MediaPlayer.OnPreparedListener
    @Override
    public void onPrepared(MediaPlayer mp) {
        controller.setMediaPlayer(this);
        controller.setAnchorView((LinearLayout) findViewById(R.id.video_container));

//        int videoWidth = player.getVideoWidth();
//        int videoHeight = player.getVideoHeight();
//        float videoProportion = (float) videoWidth / (float) videoHeight;
//        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
//        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
//        float screenProportion = (float) screenWidth / (float) screenHeight;
//        android.view.ViewGroup.LayoutParams lp = videoSurface.getLayoutParams();
//
//        if (videoProportion > screenProportion) {
//            lp.width = screenWidth;
//            lp.height = (int) ((float) screenWidth / videoProportion);
//        } else {
//            lp.width = (int) (videoProportion * (float) screenHeight);
//            lp.height = screenHeight;
//        }
//        videoSurface.setLayoutParams(lp);

        player.start();
    }
    // End MediaPlayer.OnPreparedListener

    // Implement VideoMediaController.MediaPlayerControl
    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getBufferPercentage() {

        return mPercent;
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public void seekTo(int i) {
        player.seekTo(i);
    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void toggleMute(boolean m) {
        if (!m) {
            player.setVolume(0, 0);
        } else {
            player.setVolume(1, 1);
        }

    }

    @Override
    public void toggleFullScreen() {
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (getActionBar().isShowing()) {
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
                getActionBar().hide();
            } else {
                int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
                decorView.setSystemUiVisibility(uiOptions);
                getActionBar().show();
            }
        }
    }
    // End VideoMediaController.MediaPlayerControl

}
