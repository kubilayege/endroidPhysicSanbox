package com.github.webninjasi.sandboxgl;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class SurfaceViewActivity extends Activity {
    private GamePanel gamePanel;
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gamePanel = new GamePanel(this  );
        setContentView(gamePanel);

        // Color info
        mBrushTextView = new TextView(this);
        mBrushTextView.setText("Brush: " + colors[brushColor] + "/" + brushSize);
        mBrushTextView.setTextColor(Color.GREEN);
        mBrushTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);

        FrameLayout.LayoutParams brushTextViewParams = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        brushTextViewParams.gravity = Gravity.BOTTOM;
        brushTextViewParams.bottomMargin = 85;
        brushTextViewParams.leftMargin = 50;


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
                gamePanel.setBrushRadius(i);
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
                gamePanel.setColor(i);
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
        addContentView(mBrushTextView, brushTextViewParams);
        addContentView(mRadiusSlider, seekBarParams);
        addContentView(mColorSlider, seekBarParams2);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
