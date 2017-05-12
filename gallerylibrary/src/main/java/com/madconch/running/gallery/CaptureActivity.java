package com.madconch.running.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.madconch.running.base.widget.activity.BaseActivity;

public class CaptureActivity extends BaseActivity {
    SurfaceView surfaceView;

    @Override
    protected View provideContentView(ViewGroup container) {
        return LayoutInflater.from(getContext()).inflate(R.layout.activity_capture, container, false);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
    }
}
