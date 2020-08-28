package com.mohannad.coupon.view.ui.video;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.URLUtil;
import android.widget.MediaController;
import android.widget.VideoView;

import com.mohannad.coupon.R;
import com.mohannad.coupon.databinding.ActivityVideoBinding;
import com.mohannad.coupon.utils.BaseActivity;

public class VideoActivity extends BaseActivity {
    // Tag for the instance state bundle.
    private static final String PLAYBACK_TIME = "play_time";
    // Current playback position (in milliseconds).
    private int mCurrentPosition = 0;
    private String urlVideo;
    private ActivityVideoBinding videoBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoBinding = DataBindingUtil.setContentView(this, R.layout.activity_video);
        urlVideo = getIntent().getStringExtra("url");
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }
        MediaController mediaController = new MediaController(this);
        mediaController.setMediaPlayer(videoBinding.videoView);
        videoBinding.videoView.setMediaController(mediaController);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Load the media each time onStart() is called.
        initializePlayer(urlVideo);
    }

    private void initializePlayer(String urlVideo) {
        // Show the "progress loading..." while the video loads.
        videoBinding.loadingProgress.setVisibility(VideoView.VISIBLE);

        // Buffer and decode the video sample.
        Uri videoUri = getMedia(urlVideo);
        videoBinding.videoView.setVideoURI(videoUri);

        // Listener for onPrepared() event (runs after the media is prepared).
        videoBinding.videoView.setOnPreparedListener(
                mediaPlayer -> {
                    // Hide progress loading ...
                    videoBinding.loadingProgress.setVisibility(VideoView.GONE);

                    // Restore saved position, if available.
                    if (mCurrentPosition > 0) {
                        videoBinding.videoView.seekTo(mCurrentPosition);
                    } else {
                        // Skipping to 1 shows the first frame of the video.
                        videoBinding.videoView.seekTo(1);
                    }

                    // Start playing!
                    videoBinding.videoView.start();
                });

    }


    // Release all media-related resources. In a more complicated app this
    // might involve unregistering listeners or releasing audio focus.
    private void releasePlayer() {
        videoBinding.videoView.stopPlayback();
    }

    // Get a Uri for the media sample regardless of whether that sample is
    // embedded in the app resources or available on the internet.
    private Uri getMedia(String mediaName) {
        if (URLUtil.isValidUrl(mediaName)) {
            // Media name is an external URL.
            return Uri.parse(mediaName);
        } else {
            // you can also put a video file in raw package and get file from there as shown below
            return Uri.parse("android.resource://" + getPackageName() +
                    "/raw/" + mediaName);
        }
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current playback position (in milliseconds) to the
        // instance state bundle.
        outState.putInt(PLAYBACK_TIME, videoBinding.videoView.getCurrentPosition());
    }
    @Override
    protected void onPause() {
        super.onPause();

        // In Android versions less than N (7.0, API 24), onPause() is the
        // end of the visual lifecycle of the app.  Pausing the video here
        // prevents the sound from continuing to play even after the app
        // disappears.
        //
        // This is not a problem for more recent versions of Android because
        // onStop() is now the end of the visual lifecycle, and that is where
        // most of the app teardown should take place.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            videoBinding.videoView.pause();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();

        // Media playback takes a lot of resources, so everything should be
        // stopped and released at this time.
        releasePlayer();
    }
}