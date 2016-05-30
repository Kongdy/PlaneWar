package com.example.planewar.Entity;

import com.example.planewar.R;
import com.example.planewar.tools.Utils;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * 子弹对象
 * @author wangk
 */
public class Bullet extends EntityFather {
	
	public int shootDamage; // 射击产生的伤害
	public int shootSpeed; // 单位间隔时间内所移动的距离，即速度
	
	public Bullet(Context context,int bulletType) {
		super(context,bulletType,1,1);
		isAlive = true;
		isCanAutoMove = true;
	}
	
	@Override
	public Bitmap drawSelf(Canvas canvas, float x, float y, Paint paint) {
		if(super.drawSelf(canvas, x, y, paint) == null || bitmap==null) {
			CreateBitmap createBitmapBullet = new CreateBitmap();
			createBitmapBullet.start();
		}
		return super.drawSelf(canvas, x, y, paint);
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}
	
	/**
	 * 子弹移动特性
	 */
	public void move() {
		
		if(Bullet.this.y > 0 && Bullet.this.y < Utils.SCREENHEIGHT_) {
			Bullet.this.y = Bullet.this.y - shootSpeed;
		} else {
			isAlive = false;
		}
		/*
		 * 保留,以后备用
		 */
//		ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
//		valueAnimator.setDuration(1000L); // 设置持续时间
//		valueAnimator.setInterpolator(new AccelerateInterpolator()); // 设置内插器
//		// 动画更新事件
//		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
//			@Override
//			public void onAnimationUpdate(ValueAnimator animation) {
//				
//				Log.i("move", "before  moving");
////				if(rd != null) {
////				//	rd.refreshDraw();
////					Log.i("move", "bullet is moving");
////				}
//			}
//		});
//		valueAnimator.start();
	}
	
	@Override
	public boolean isCollide(EntityFather entity) {
		if( (this.x+this.getWidth())>entity.x && this.x < (entity.x+entity.getWidth()) 
				&& (this.y+this.getHeight())> entity.y &&  this.y  < (entity.y+entity.getHeight()) ) {
			this.isAlive = false;
			return true;
		}
		return false;
	}

	public int getShootDamage() {
		return shootDamage;
	}

	public void setShootDamage(int shootDamage) {
		this.shootDamage = shootDamage;
	}

	public int getShootSpeed() {
		return shootSpeed;
	}

	public void setShootSpeed(int shootSpeed) {
		this.shootSpeed = shootSpeed;
	}
	
	

}
