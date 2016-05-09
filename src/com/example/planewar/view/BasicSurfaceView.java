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
	private RenderUIThread renderUIThread;

	SurfaceHolder sfh;

	public BasicSurfaceView(Context context) {
		super(context);
		sfh = this.getHolder();
		sfh.addCallback(this);
		initPaint(); // 初始化画笔
		initEntity(); // 初始化对象
		initTouch(); // 初始化点击事件
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
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		refreshFlag = false;
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		renderUIThread = new RenderUIThread(); // 界面绘制线程
		refreshFlag = true;// 开启界面刷新
		// 界面刷新线程
		renderUIThread.start();
	}
	
	public abstract void myDraw(Canvas canvas);
	public abstract void backgroundOp();
	abstract void initPaint();
	abstract void initEntity();
	abstract void initTouch();
}
