package com.example.planewar.Entity;

import com.example.planewar.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * π÷ŒÔ£¨µ»º∂2
 * @author wangk
 *
 */
public class GuaiwuRank2 extends GuaiWuFather {
	
	public static int currentCount;
	public static int sumCount = 4;

	public GuaiwuRank2(Context context) {
		super(context, R.drawable.middle, 1, true, 4);
		crashHarmValue = 200;
		speed = 65;
		bloodValue =300;
		score = 300;
		isCrashHarmful = true;
	}

}
