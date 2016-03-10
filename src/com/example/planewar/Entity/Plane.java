package com.example.planewar.Entity;

import java.util.concurrent.CopyOnWriteArrayList;

import com.example.planewar.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * 飞机对象
 * 
 * @author wangk
 */
public class Plane extends EntityFather {

	public boolean bulletAutoFlag;
	private long bulletTime;
	CopyOnWriteArrayList<Bullet> bullets = new CopyOnWriteArrayList<Bullet>(); // 子弹集合
	private BulletThread bt;
	private DestorySelf destoryself;
	public int score; // 获得的分数

	public int bulletlevel;

	public Plane(Context context) {
		super(context, R.drawable.myplane, 2, 1);
		bulletTime = 200l;
		bulletAutoFlag = true;
		bt = new BulletThread();
		isExplosion = false;
		isAlive = true;
		isCanAutoMove = true;
		bulletlevel = 2;
	}

	/*
	 * 飞机具有移出屏幕不回收的特性，故重写
	 */
	@Override
	public Bitmap drawSelf(Canvas canvas, float x, float y, Paint paint) {
		getParams(canvas, x, y, paint);
		if (bitmap != null && isAlive) {
			drawBitmap(currentFrame, false);
			if (!bt.isAlive() && bulletAutoFlag) {
				bt.start();
			}
		} else {
			if (!createBitmap.isAlive() && isAlive) {
				createBitmap.start();
			}
		}
		if (isExplosion) {
			bulletAutoFlag = false; // 关闭子弹
			isCanAutoMove = false; // 关闭触摸移动
			if (iconResId != R.drawable.myplaneexplosion) {
				changeIconResId(R.drawable.myplaneexplosion);
				currentFrame = 1;
			}
			if (destoryself == null) {
				destoryself = new DestorySelf();
				destoryself.start();
			}
			if (currentFrame > totalFrame) {
				isAlive = false;
			}
		}
		return bitmap;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}

	@Override
	public void move() {

	}

	/**
	 * 遍历怪物是否被击中
	 * 
	 * @param canvas
	 * @param paint
	 * @param guaiwus
	 */
	public void shoot(Canvas canvas, Paint paint,
			CopyOnWriteArrayList<GuaiWuFather> guaiwus) {
		for (Bullet b : bullets) {
			b.drawSelf(canvas, b.x, b.y, paint);
			for (GuaiWuFather guaiwu : guaiwus) { // 遍历怪物是否被击中
				if (b.isAlive && !guaiwu.isExplosion) {
					if (b.isCollide(guaiwu)) {
						if (guaiwu instanceof GuaiwuRank1) {
						}
						if (guaiwu instanceof GuaiwuRank2) {
						}
						guaiwu.bloodValue = guaiwu.bloodValue - b.shootDamage; // 减血
						if (guaiwu.bloodValue < 0) {
							b.isCanAutoMove = false;
							guaiwu.isCanAutoMove = false;
							guaiwu.isExplosion = true;
							this.score = score + guaiwu.score;
						}
					}
				}
				if (!this.isExplosion && !guaiwu.isExplosion
						&& guaiwu.isCrashHarmful) { // 判断是否自己被杀死
					if (this.isCollide(guaiwu)) {
						if (guaiwu instanceof GuaiwuRank1) {
						}
						guaiwu.isCanAutoMove = false;
						guaiwu.isExplosion = true;
					}
				}
			}
			if (!b.isAlive) {
				bullets.remove(b);
			}
		}
	}

	// 子弹自动发射线程
	public class BulletThread extends Thread {
		@Override
		public void run() {
			while (bulletAutoFlag) {
				try {
					Thread.sleep(bulletTime); // 发射间隔
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				for (int i = 0; i < bulletlevel; i++) {
					Bullet bullet = new Bullet(context);
					if (i%2 == 0 || i ==0) {
						bullet.x = Plane.this.x + (2 * Plane.this.width) / 3;
						bullet.y = Plane.this.y + Plane.this.height / 4;
					} else {
						bullet.x = Plane.this.x + Plane.this.width / 50;
						bullet.y = Plane.this.y + Plane.this.height / 4;
					}
					bullet.shootSpeed = 50; // 射击单位时间距离
					bullet.shootDamage = bulletlevel * 50; // 射击伤害
					bullets.add(bullet);
				}
			}
		}
	}

	/**
	 * 检测碰撞
	 */
	@Override
	public boolean isCollide(EntityFather entity) {
		if ((this.x + this.getWidth()) > entity.x
				&& this.x < (entity.x + entity.getWidth())
				&& (this.y + this.getHeight()) > entity.y
				&& this.y < (entity.y + entity.getHeight())) {
			this.isExplosion = true;
			return true;
		}
		return false;
	}
}
