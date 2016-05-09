package com.example.planewar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder.Callback2;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * �Զ���surfaceview����
 * @author wangk
 */
public abstract class BasicSurfaceView extends SurfaceView implements Callback2 {
	
	public boolean refreshFlag; // ����ˢ�¿���
	private RenderUIThread renderUIThread;

	SurfaceHolder sfh;

	public BasicSurfaceView(Context context) {
		super(context);
		sfh = this.getHolder();
		sfh.addCallback(this);
		initPaint(); // ��ʼ������
		initEntity(); // ��ʼ������
		initTouch(); // ��ʼ������¼�
	}
	
	// UI�����߳�
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
				if (endTime - startTime < 50) { // ������Ļ֡����20֡����
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
		renderUIThread = new RenderUIThread(); // ��������߳�
		refreshFlag = true;// ��������ˢ��
		// ����ˢ���߳�
		renderUIThread.start();
	}
	
	public abstract void myDraw(Canvas canvas);
	public abstract void backgroundOp();
	abstract void initPaint();
	abstract void initEntity();
	abstract void initTouch();
}
