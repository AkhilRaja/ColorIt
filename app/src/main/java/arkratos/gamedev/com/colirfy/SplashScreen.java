package arkratos.gamedev.com.colirfy;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.VideoView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.artself);

        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame);

        final VideoView mVideoView  = (VideoView)findViewById(R.id.videoView);
        //mVideoView.setMediaController(new MediaController(this));
        mVideoView.setVideoURI(uri);
        mVideoView.setZOrderOnTop(true);
        //mVideoView.start();

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.start();

                frameLayout.setVisibility(View.GONE);

            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
                startActivity(intent);
            }
        });
    }
}
