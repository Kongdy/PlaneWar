package com.example.planewar.Entity;

import com.example.planewar.R;
import com.example.planewar.tools.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 大型boss的父类，拥有各种技能
 * @author wangk
 */
public class Boss extends GuaiWuFather{

	private int totalBlood;
	
	public Boss(Context context, int iconResId,int bloodValue) {
		super(context, iconResId,3,true,3);
		this.bloodValue = bloodValue;
		this.totalBlood = bloodValue;
	}

	@Override
	public void move() {
		if(this.y<(Utils.SCREENHEIGHT_/2-getHeight()) && this.y >= 0) {
			this.y = this.y+Utils.SCREENHEIGHT_/100;
		}
	}
	
	@Override
	public Bitmap drawSelf(Canvas canvas, float x, float y, Paint paint) {
		getParams(canvas, x, y, paint);
		if(bitmap == null && this.y >= 0) {
			if(!createBitmap.isAlive()) {
				createBitmap.run();
			}
		}
		if(bitmap != null&&isAlive) {
			drawBitmap(currentFrame,portrait);
		}
		if(isExplosion) {
			if(iconResId != R.drawable.bossplanebomb) {
				changeIconResId(R.drawable.bossplanebomb);
				currentFrame  = 1;
				totalFrame  = 5;
			}
			if(destorySelf == null) {
				destorySelf = new DestorySelf();
				destorySelf.start();
			}
			if(currentFrame>=totalFrame) {
				isAlive = false;
			}
		} else {
			if(bloodValue < (totalBlood*2)/3) {
				setCurrentFrame(2);
			}
			if(bloodValue < totalBlood/3) {
				setCurrentFrame(1);
			}
		}
		if(isCanAutoMove) {
			move();
		}
		return bitmap;
	}

}
