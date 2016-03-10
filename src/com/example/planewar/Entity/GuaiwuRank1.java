package com.example.planewar.Entity;

import com.example.planewar.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * 普通的最小怪物
 * @author wangk
 */
public class GuaiwuRank1 extends GuaiWuFather {
	
	public static int currentCount; // 怪物当前数量
	public static int sumCount = 8; // 怪物最多数量
	
	public GuaiwuRank1(Context context) {
		super(context, R.drawable.small,1,true,3);
		crashHarmValue = 100;
		speed = 50;
		bloodValue = 100;
		score = 100;
		isCrashHarmful = true;
	}
	

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}
	
	@Override
	public Bitmap drawSelf(Canvas canvas, float x, float y, Paint paint) {
		return super.drawSelf(canvas, x, y, paint);
	}
	
	
}
