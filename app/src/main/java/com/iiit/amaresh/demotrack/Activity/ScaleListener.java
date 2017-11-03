package com.iiit.amaresh.demotrack.Activity;

import android.view.ScaleGestureDetector;

/**
 * Created by mobileapplication on 11/3/17.
 */

public class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }
}
