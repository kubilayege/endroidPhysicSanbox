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
import android.widget.SeekBar;
import android.widget.TextView;

public class OpenGLActivity extends AppCompatActivity {

    private GameSurfaceView gLView;
    private TextView mFPSTextView;
    private TextView mBrushTextView;
    private SeekBar mRadiusSlider;
    private SeekBar mColorSlider;
    private String[] colors = {
            "Red", "Green", "Blue",
            "Cyan", "Purple",
            "Yellow", "White",
    };
    private int brushSize = 4;
    private int brushColor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fullscreen();

        // Fps info
        mFPSTextView = new TextView(this);
        mFPSTextView.setText("");
        mFPSTextView.setTextColor(Color.GREEN);
        mFPSTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
        FrameLayout.LayoutParams fpsTextViewParams = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        fpsTextViewParams.leftMargin = 50;
        fpsTextViewParams.topMargin = 50;

        // Color info
        mBrushTextView = new TextView(this);
        mBrushTextView.setText("Brush: " + colors[brushColor] + "/" + brushSize);
        mBrushTextView.setTextColor(Color.GREEN);
        mBrushTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);

        FrameLayout.LayoutParams brushTextViewParams = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        brushTextViewParams.gravity = Gravity.BOTTOM;
        brushTextViewParams.bottomMargin = 85;
        brushTextViewParams.leftMargin = 50;

        // GL Render View
        gLView = new GameSurfaceView(this, mFPSTextView);
        setContentView(gLView);

        // Brush Size Slider
        mRadiusSlider = new SeekBar( this );
        mRadiusSlider.setMin(1);
        mRadiusSlider.setMax(10);
        mRadiusSlider.setProgress(4);

        FrameLayout.LayoutParams seekBarParams = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        seekBarParams.gravity = Gravity.BOTTOM;
        seekBarParams.rightMargin = 300;

        // Color Slider
        mColorSlider = new SeekBar( this );
        mColorSlider.setMin(0);
        mColorSlider.setMax(6);
        mColorSlider.setProgress(0);

        FrameLayout.LayoutParams seekBarParams2 = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        seekBarParams2.gravity = Gravity.BOTTOM;
        seekBarParams2.bottomMargin = 150;
        seekBarParams2.rightMargin = 350;

        // Brush Size Callback
        mRadiusSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar,int i, boolean b) {
                gLView.getRenderer().setBrushRadius(i);
                brushSize = i;
                mBrushTextView.setText("Brush: " + colors[brushColor] + "/" + brushSize);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Color Callback
        mColorSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar,int i, boolean b) {
                gLView.getRenderer().setBrushType(i);
                brushColor = i;
                mBrushTextView.setText("Brush: " + colors[brushColor] + "/" + brushSize);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Add elements
        addContentView(mFPSTextView, fpsTextViewParams);
        addContentView(mBrushTextView, brushTextViewParams);
        addContentView(mRadiusSlider, seekBarParams);
        addContentView(mColorSlider, seekBarParams2);
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
