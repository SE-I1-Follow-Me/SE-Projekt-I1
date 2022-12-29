package com.google.android.material.bottomnavigation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class BottomNavigationView extends View {
    public BottomNavigationView(Context context) {
        this(context, null);
    }

    public BottomNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
