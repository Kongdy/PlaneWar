package com.example.planewar.view;

import com.example.planewar.R;
import com.example.planewar.Entity.EntityFather;
import com.example.planewar.Entity.ShowGoods;
import com.example.planewar.tools.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.ViewGroup;

/**
 * 开始界面
 * @author wangk
 */
public class StartView extends BasicSurfaceView {
	
	private Bitmap background1;
	private Bitmap background2;
	
	private Paint defaultPaint;
	
	private int bg1Position;
	private int bg2Position;
	
	private EntityFather mLogo;
	private EntityFather mStartBtn;
	
	private StartViewDoListener listener;

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
			mLogo.drawSelf(canvas, 0.4f, defaultPaint);
			mStartBtn.drawSelf(canvas, 0.8f, defaultPaint);
		}
	}

	@Override
	public void backgroundOp() {
		if(bg1Position < Utils.SCREENHEIGHT_) {
			bg1Position+=10;
		} else {
			bg1Position = bg2Position - Utils.SCREENHEIGHT_;
		}
		if(bg2Position < Utils.SCREENHEIGHT_) {
			bg2Position+=10;
		} else {
			bg2Position = bg1Position - Utils.SCREENHEIGHT_;
		}
	}

	@Override
	void initPaint() {
		defaultPaint = new Paint();
		defaultPaint.setAntiAlias(true);
		bg1Position = 0;
		bg2Position = bg1Position - Utils.SCREENHEIGHT_;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			if(mStartBtn.isInArea(event)) {
				if(listener != null) {
					listener.startDo();
				}
			}
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	void initEntity() {
		background1 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_01);
		background2 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_02);
		mLogo = new ShowGoods(context, R.drawable.mlogo);
		mStartBtn = new ShowGoods(context, R.drawable.game_start_btn);
		mLogo.changeSize(200, 100);
		mStartBtn.changeSize(150, 30);
		//planeWarMain = new MySurfaceView(context);
	}

	@Override
	void initTouch() {
	}
	
	public void setListener(StartViewDoListener listener) {
		this.listener = listener;
	}
	
	/**
	 * 点击开始按钮要做的事情
	 */
	public interface StartViewDoListener{
		public void startDo();
	}
	
	/**
	 * 停止
	 */
	public void stop() {
		refreshFlag = false;
	}

}
