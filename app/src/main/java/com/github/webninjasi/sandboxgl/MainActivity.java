package com.github.webninjasi.sandboxgl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.opengl.GLSurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button openglButton;
    private Button surfaceViewButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openglButton = (Button) findViewById(R.id.openglButton);
        surfaceViewButton = (Button) findViewById(R.id.surfaceButton);
        openglButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGl_Compute_Activity();
            }
        });
        surfaceViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                surfaceView_Activity();
            }
        });
    }

    public void openGl_Compute_Activity(){
        Intent niyet = new Intent(this, OpenGLActivity.class);
        startActivity(niyet);
    }
    public void surfaceView_Activity(){
        Intent niyet = new Intent(this, SurfaceViewActivity.class);
        startActivity(niyet);
    }
    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }
}
