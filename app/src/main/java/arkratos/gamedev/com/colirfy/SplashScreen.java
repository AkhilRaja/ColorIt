package arkratos.gamedev.com.colirfy;


import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.Artself);

        VideoView mVideoView  = (VideoView)findViewById(R.id.videoView);
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.setVideoURI(uri);
        mVideoView.requestFocus();
        mVideoView.start();
    }
}
