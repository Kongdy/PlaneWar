package com.example.planewar.view;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback2;
import android.view.SurfaceView;
import android.widget.TextView;

import com.example.planewar.R;
import com.example.planewar.Entity.Bullet;
import com.example.planewar.Entity.EntityFather;
import com.example.planewar.Entity.GuaiWuFather;
import com.example.planewar.Entity.GuaiwuRank1;
import com.example.planewar.Entity.GuaiwuRank2;
import com.example.planewar.Entity.Plane;
import com.example.planewar.tools.Utils;

/**
 * �Զ���surfaceView
 * @author wangk
 */
public class MySurfaceView extends BasicSurfaceView {

	private Context context;

	List<EntityFather> entitys;

	private Plane plane; // �ɻ�

	private Paint paint;
	private Paint scorePaint;

	private float bg1;// ����1y����
	private float bg2; // ����2y����

	private Bitmap background1; // ����1
	private Bitmap background2;// ����2

	private CopyOnWriteArrayList<GuaiWuFather> guaiwus = new CopyOnWriteArrayList<GuaiWuFather>(); // ���Ｏ��

	
	private RenderUIThread renderUIThread;
	private MonsterBornThread monsterBornThread; // ����������

	private boolean monsterOpen; // �Ƿ����ɹ���

	public MySurfaceView(Context context) {
		super(context);
		this.context = context;
		entitys = new ArrayList<EntityFather>();
	}

	/**
	 * ��ʼ�������
	 */
	@Override
	void initTouch() {
		plane.isCanAutoMove = false; // �ɻ�Ĭ�Ͽ�ʼδ�����
	}

	/**
	 * ��ʼ������
	 */
	@Override
	 void initEntity() {
		plane = new Plane(context);
		plane.x = Utils.SCREENWIDTH_ / 2;
		plane.y = Utils.SCREENHEIGHT_ - 300 - plane.getHeight();
		plane.changeSize(50, 50);
		entitys.add(plane);
		background1 = BitmapFactory.decodeResource(getResources(),
				R.drawable.bg_01);
		background2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.bg_02);
	}

	/**
	 * ��ʼ������
	 */
	@Override
	void initPaint() {
		// �ɻ�����
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setAntiAlias(true); // �����
		// paint.setDither(true);
		// paint.setFilterBitmap(true);
		// ��������
		scorePaint = new Paint();
		scorePaint.setTextSize(100); // �������ִ�С
		scorePaint.setColor(Color.RED);
		scorePaint.setAntiAlias(true);
		bg1 = 0;
		bg2 = bg1 - Utils.SCREENHEIGHT_;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	@Override
	public void myDraw(Canvas canvas) {
		if (canvas != null) { // ��ֹ���˳��Ĺ����в�����ָ���쳣
			// ���Ʊ���
			if (background1 != null && background2 != null) {
				canvas.drawBitmap(background1, 0, bg1, paint);
				canvas.drawBitmap(background2, 0, bg2, paint);
			} else {
				canvas.drawColor(Color.RED);
			}
			canvas.drawText(plane.score + "", 50, 100, scorePaint);
			if (plane != null && plane.isAlive) {
				plane.drawSelf(canvas, plane.x, plane.y, paint);
				plane.shoot(canvas, paint, guaiwus);
			}
			for (GuaiWuFather g : guaiwus) {
				if (g.isAlive) {
					g.drawSelf(canvas, g.x, g.y, paint);
				} else {
					if (g instanceof GuaiwuRank1) {
						GuaiwuRank1.currentCount--;
					}
					if(g instanceof GuaiwuRank2) {
						GuaiwuRank1.currentCount--;
					}
					guaiwus.remove(g);
				}
			}
			if(!plane.isAlive && monsterOpen) {
				monsterOpen = false; // �����󣬲������ɵл�
			}
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		super.surfaceCreated(holder);
		monsterBornThread = new MonsterBornThread(); // ���������߳�
		monsterOpen = true; // ��������
		monsterBornThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		refreshFlag = false; // ֹͣˢ���߳�
		monsterOpen = false; // ���������߳�
		plane.bulletAutoFlag = false;// ֹͣ�ӵ������߳�
	}

	@Override
	public void surfaceRedrawNeeded(SurfaceHolder holder) {
	}

	/**
	 * �������Խ���Ĵ����ַ��¼�
	 */
	public void handleTouch(MotionEvent event) {
		float touchX = event.getRawX();
		float touchY = event.getRawY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			if (plane.isCanAutoMove) {
				plane.x = touchX - plane.getWidth() / 2;
				plane.y = touchY - plane.getHeight() / 2;
			}
			if (touchX > Utils.SCREENWIDTH_ || touchX < 0
					|| touchY > Utils.SCREENHEIGHT_ || touchY < 0) {
				closeAllTouchBind();
			}
		case MotionEvent.ACTION_DOWN:
			// �жϣ�����ڷɻ��Ŀؼ������ڵ��У�����϶��¼�
			if (plane.x < touchX && touchX < (plane.x + plane.getWidth())
					&& plane.y < touchY
					&& touchY < (plane.y + plane.getHeight())) {
				plane.isCanAutoMove = true;
			}
			break;
		case MotionEvent.ACTION_UP:
			closeAllTouchBind();
			break;
		default:
			break;
		}
	}

	/**
	 * �ر����д�����
	 */
	private void closeAllTouchBind() {
		plane.isCanAutoMove = false;
	}

	/**
	 * ������ɹ���
	 * @author wangk
	 */
	public class MonsterBornThread extends Thread {
		@Override
		public void run() {
			while (monsterOpen) {
				try {
					sleep(1000l);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (GuaiwuRank1.currentCount < GuaiwuRank1.sumCount) {
					GuaiwuRank1 monster1 = new GuaiwuRank1(getContext());
					monster1.x = randomX();
					monster1.y = 0;
					guaiwus.add(monster1);
					GuaiwuRank1.currentCount++;
				}
				if(GuaiwuRank1.currentCount%2 == 0 && GuaiwuRank2.currentCount < GuaiwuRank2.sumCount) {
					GuaiwuRank2 monster2 = new GuaiwuRank2(getContext());
					monster2.x = randomX();
					monster2.y = 0;
					guaiwus.add(monster2);
					GuaiwuRank2.currentCount++;
				}
			}
		}
	}

	/**
	 * ��������
	 */
	public void backgroundOp() {
		if (bg1 < Utils.SCREENHEIGHT_) {
			bg1 += 10;
		} else {
			bg1 = bg2 - Utils.SCREENHEIGHT_;
		}
		if (bg2 < Utils.SCREENHEIGHT_) {
			bg2 += 10;
		} else {
			bg2 = bg1 - Utils.SCREENHEIGHT_;
		}
	}

	/**
	 * ����һ��x����������
	 * 
	 * @return
	 */
	public int randomX() {
		return (int) (Math.random() * (Utils.SCREENWIDTH_));
	}

	/**
	 * ����һ��y����������
	 * 
	 * @return
	 */
	public int randomY() {
		return (int) (Math.random() * Utils.SCREENHEIGHT_);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

}
