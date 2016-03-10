package com.example.planewar.Entity;

import com.example.planewar.tools.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * 怪物的父类
 * @author wangk
 */
public abstract class GuaiWuFather extends EntityFather {
	
	public boolean isCrashHarmful; // 是否能对主角产生碰撞伤害
	public boolean isShoot; // 是否能射击子弹
	
	public int crashHarmValue; // 所能产生的碰撞伤害
	
	public long speed; // 移动速度
	
	public int bloodValue; // 血量
	
	private boolean portrait; // 帧方向
	
	public int score; // 死亡可获分数
	
	private DestorySelf destorySelf; // 毁灭程序

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
