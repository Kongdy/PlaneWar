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
 * 自定义surfaceView
 * @author wangk
 */
public class MySurfaceView extends BasicSurfaceView {

	private Context context;

	List<EntityFather> entitys;

	private Plane plane; // 飞机

	private Paint paint;
	private Paint scorePaint;

	private float bg1;// 背景1y坐标
	private float bg2; // 背景2y坐标

	private Bitmap background1; // 背景1
	private Bitmap background2;// 背景2

	private CopyOnWriteArrayList<GuaiWuFather> guaiwus = new CopyOnWriteArrayList<GuaiWuFather>(); // 怪物集合

	
	private RenderUIThread renderUIThread;
	private MonsterBornThread monsterBornThread; // 怪物生成类

	private boolean monsterOpen; // 是否生成怪物

	public MySurfaceView(Context context) {
		super(context);
		this.context = context;
		entitys = new ArrayList<EntityFather>();
	}

	/**
	 * 初始化点击绑定
	 */
	@Override
	void initTouch() {
		plane.isCanAutoMove = false; // 飞机默认开始未被点击
	}

	/**
	 * 初始化对象
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
	 * 初始化画笔
	 */
	@Override
	void initPaint() {
		// 飞机画笔
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setAntiAlias(true); // 防锯齿
		// paint.setDither(true);
		// paint.setFilterBitmap(true);
		// 分数画笔
		scorePaint = new Paint();
		scorePaint.setTextSize(100); // 设置文字大小
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
		if (canvas != null) { // 防止在退出的过程中产生空指针异常
			// 绘制背景
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
				monsterOpen = false; // 死亡后，不再生成敌机
			}
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		super.surfaceCreated(holder);
		monsterBornThread = new MonsterBornThread(); // 怪物生成线程
		monsterOpen = true; // 开启怪物
		monsterBornThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		refreshFlag = false; // 停止刷新线程
		monsterOpen = false; // 怪物生成线程
		plane.bulletAutoFlag = false;// 停止子弹发射线程
	}

	@Override
	public void surfaceRedrawNeeded(SurfaceHolder holder) {
	}

	/**
	 * 接收来自界面的触摸分发事件
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
			// 判断，如果在飞机的控件坐标内点中，则绑定拖动事件
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
	 * 关闭所有触摸绑定
	 */
	private void closeAllTouchBind() {
		plane.isCanAutoMove = false;
	}

	/**
	 * 随机生成怪物
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
	 * 背景操作
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
	 * 返回一个x轴的随机坐标
	 * 
	 * @return
	 */
	public int randomX() {
		return (int) (Math.random() * (Utils.SCREENWIDTH_));
	}

	/**
	 * 返回一个y轴的随机坐标
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
