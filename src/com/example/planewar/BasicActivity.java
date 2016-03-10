package com.example.planewar;

import com.example.planewar.tools.Utils;

import android.app.Activity;

/**
 * 所有activity基类
 * @author wangk
 */
public abstract class BasicActivity extends Activity {
	

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.windowSizeGet(this);
		initData();
	}

	// 初始化数据
	public abstract  void initData();
}
