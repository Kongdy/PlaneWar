package com.example.planewar.Entity;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

/**
 * 仅用于显示的entity
 * @author wangk
 */
public class ShowGoods extends EntityFather {
	
	public ShowGoods(Context context, int iconResId) {
		super(context, iconResId, 1, 0);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}

	@Override
	public void move() {
	}
	
}
