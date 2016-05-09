package com.example.planewar.view;

import com.example.planewar.R;
import com.example.planewar.tools.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

/**
 * 开始界面
 * @author wangk
 */
public class StartView extends BasicSurfaceView {
	
	private Bitmap background1;
	private Bitmap background2;
	
	private Bitmap mLogo;
	
	private Paint defaultPaint;
	
	private int bg1Position;
	private int bg2Position;

	public StartView(Context context) {
		super(context);
	}

	@Override
	public void surfaceRedrawNeeded(SurfaceHolder holder) {
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		super.surfaceCreated(holder);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		super.surfaceDestroyed(holder);
	}

	@Override
	public void myDraw(Canvas canvas) {
		if(canvas != null) {
			if(background1 != null && background2 != null) {
				canvas.drawBitmap(background1, 0, bg1Position, defaultPaint);
				canvas.drawBitmap(background2, 0, bg2Position, defaultPaint);
			} else {
				canvas.drawColor(Color.RED);
			}
			canvas.drawBitmap(mLogo, 0,  0, defaultPaint);
		}
	}

	@Override
	public void backgroundOp() {
		if(bg1Position <= Utils.SCREENHEIGHT_) {
			bg1Position+=10;
		} else {
			bg1Position = bg2Position - Utils.SCREENHEIGHT_;
		}
		if(bg2Position <= Utils.SCREENHEIGHT_) {
			bg2Position+=10;
		} else {
			bg2Position = bg1Position - Utils.SCREENHEIGHT_;
		}
	}

	@Override
	void initPaint() {
		bg1Position = 0;
		bg2Position = bg1Position - Utils.SCREENHEIGHT_;
	}

	@Override
	void initEntity() {
		background1 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_01);
		background2 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_02);
		mLogo = BitmapFactory.decodeResource(getResources(), R.drawable.mlogo);
	}

	@Override
	void initTouch() {
		defaultPaint = new Paint();
		defaultPaint.setAntiAlias(true);
	}

}
