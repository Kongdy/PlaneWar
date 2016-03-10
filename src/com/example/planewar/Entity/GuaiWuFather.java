package com.example.planewar.Entity;

import com.example.planewar.tools.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * ����ĸ���
 * @author wangk
 */
public abstract class GuaiWuFather extends EntityFather {
	
	public boolean isCrashHarmful; // �Ƿ��ܶ����ǲ�����ײ�˺�
	public boolean isShoot; // �Ƿ�������ӵ�
	
	public int crashHarmValue; // ���ܲ�������ײ�˺�
	
	public long speed; // �ƶ��ٶ�
	
	public int bloodValue; // Ѫ��
	
	private boolean portrait; // ֡����
	
	public int score; // �����ɻ����
	
	private DestorySelf destorySelf; // �������

	public GuaiWuFather(Context context, int iconResId,int currentFrame,boolean portrait,int totalFrame) {
		super(context, iconResId,totalFrame,currentFrame);
		this.currentFrame = currentFrame;
		this.portrait = portrait;
		this.isAlive = true;
		isExplosion = false;
		this.totalFrame = totalFrame;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}
	
	@Override
	public Bitmap drawSelf(Canvas canvas, float x, float y, Paint paint) {
		getParams(canvas, x, y, paint);
		if(bitmap == null && this.y >= 0) {
			if(!createBitmap.isAlive()) {
				createBitmap.start();
			}
		}
		if(bitmap != null&&isAlive) {
			drawBitmap(currentFrame,portrait);
		}
		if(isExplosion) {
			if(destorySelf == null) {
				destorySelf = new DestorySelf();
				destorySelf.start();
			}
			if(currentFrame>=totalFrame) {
				isAlive = false;
			}
		}
		if(isCanAutoMove) {
			move();
		}
		return bitmap;
	}
	
	
	@Override
	public void move() {
		if(this.y<Utils.SCREENHEIGHT_ && this.y >= 0) {
			this.y = this.y+speed;
		} else {
			this.isAlive = false;
		}
	}
	
}
