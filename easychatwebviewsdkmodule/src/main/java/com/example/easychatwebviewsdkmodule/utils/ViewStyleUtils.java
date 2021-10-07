package com.example.easychatwebviewsdkmodule.utils;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

public class ViewStyleUtils {

    public static void setBackgroundColorDraw(View tv, float radius, int stroke, String color) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.WHITE);
        gd.setCornerRadius(radius);
        gd.setStroke(stroke, Color.parseColor(color));
        gd.setShape(GradientDrawable.RECTANGLE);
        tv.setBackground(gd);
    }

    public static void setBackgroundColor(View tv, float radius, int stroke, String color, String backColor,boolean dashed) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor(backColor));
        gd.setCornerRadius(radius);
        if(!dashed)
            gd.setStroke(stroke, Color.parseColor(color));
        else
            gd.setStroke(stroke, Color.parseColor(color),10,10);
        gd.setShape(GradientDrawable.RECTANGLE);
        tv.setBackground(gd);
    }
}
