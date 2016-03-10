package com.example.planewar.Entity;

import org.cocos2d.nodes.CCSpriteSheet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * 怪物，等级三
 * @author wangk
 *
 */
public class GuaiwuRank3 extends GuaiWuFather {
	
	public GuaiwuRank3(Context context, int iconResId) {
		super(context, iconResId, 1, true, iconResId);
	}

	@Override
	public void move() {
	}


}
