package com.example.planewar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder.Callback2;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 自定义surfaceview基类
 * @author wangk
 */
public abstract class BasicSurfaceView extends SurfaceView implements Callback2 {
	
	public boolean refreshFlag; // 界面刷新开关

	SurfaceHolder sfh;

	public BasicSurfaceView(Context context) {
		super(context);
		sfh = this.getHolder();
		sfh.addCallback(this);
	}
	
	// UI绘制线程
	public class RenderUIThread extends Thread {
		@Override
		public void run() {
			while (refreshFlag) {
				long startTime = System.currentTimeMillis();
				Canvas canvas = sfh.lockCanvas();
				backgroundOp();
				myDraw(canvas);
				sfh.unlockCanvasAndPost(canvas);
				long endTime = System.currentTimeMillis();
				if (endTime - startTime < 50) { // 控制屏幕帧数在20帧以下
					try {
						Thread.sleep(50 - (endTime - startTime));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public abstract void myDraw(Canvas canvas);
	public abstract void backgroundOp();
}
