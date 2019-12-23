package com.github.webninjasi.sandboxgl;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OpenGLActivity extends AppCompatActivity {

    private GLSurfaceView gLView;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fullscreen();

        mTextView = new TextView(this);
        mTextView.setText("YourText");
        mTextView.setTextColor(Color.GREEN);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        params.leftMargin = 50;
        params.topMargin = 50;

        gLView = new GameSurfaceView(this , mTextView);

        setContentView(gLView);


        addContentView(mTextView, params);
    }

    @Override
    protected void onResume()
    {
        // Don't forget to RESUME the view when the app resumes
        super.onResume();
        gLView.onResume();
    }

    @Override
    protected void onPause()
    {
        // Don't forget to PAUSE the view when the app stops!
        // If you don't do this, the process will still be running,
        // totally screwing the performance and the battery life of the
        // device.
        super.onPause();
        gLView.onPause();
    }

    protected void fullscreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        /*
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
         */
    }
}
