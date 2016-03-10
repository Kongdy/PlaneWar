package com.example.planewar.Entity;

import android.content.Context;

/**
 * 大型boss的父类，拥有各种技能
 * @author wangk
 */
public class Boss extends GuaiWuFather{

	public Boss(Context context, int iconResId) {
		super(context, iconResId,1,true, 1);
	}

	@Override
	public void move() {
	}

}
