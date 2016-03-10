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
 * �ɻ�����
 * 
 * @author wangk
 */
public class Plane extends EntityFather {

	public boolean bulletAutoFlag;
	private long bulletTime;
	CopyOnWriteArrayList<Bullet> bullets = new CopyOnWriteArrayList<Bullet>(); // �ӵ�����
	private BulletThread bt;
	private DestorySelf destoryself;
	public int score; // ��õķ���

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
	 * �ɻ������Ƴ���Ļ�����յ����ԣ�����д
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
			bulletAutoFlag = false; // �ر��ӵ�
			isCanAutoMove = false; // �رմ����ƶ�
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
	 * ���������Ƿ񱻻���
	 * 
	 * @param canvas
	 * @param paint
	 * @param guaiwus
	 */
	public void shoot(Canvas canvas, Paint paint,
			CopyOnWriteArrayList<GuaiWuFather> guaiwus) {
		for (Bullet b : bullets) {
			b.drawSelf(canvas, b.x, b.y, paint);
			for (GuaiWuFather guaiwu : guaiwus) { // ���������Ƿ񱻻���
				if (b.isAlive && !guaiwu.isExplosion) {
					if (b.isCollide(guaiwu)) {
						if (guaiwu instanceof GuaiwuRank1) {
						}
						if (guaiwu instanceof GuaiwuRank2) {
						}
						guaiwu.bloodValue = guaiwu.bloodValue - b.shootDamage; // ��Ѫ
						if (guaiwu.bloodValue < 0) {
							b.isCanAutoMove = false;
							guaiwu.isCanAutoMove = false;
							guaiwu.isExplosion = true;
							this.score = score + guaiwu.score;
						}
					}
				}
				if (!this.isExplosion && !guaiwu.isExplosion
						&& guaiwu.isCrashHarmful) { // �ж��Ƿ��Լ���ɱ��
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

	// �ӵ��Զ������߳�
	public class BulletThread extends Thread {
		@Override
		public void run() {
			while (bulletAutoFlag) {
				try {
					Thread.sleep(bulletTime); // ������
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
					bullet.shootSpeed = 50; // �����λʱ�����
					bullet.shootDamage = bulletlevel * 50; // ����˺�
					bullets.add(bullet);
				}
			}
		}
	}

	/**
	 * �����ײ
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
