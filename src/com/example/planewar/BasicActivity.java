package com.example.planewar;

import com.example.planewar.tools.Utils;

import android.app.Activity;

/**
 * ����activity����
 * @author wangk
 */
public abstract class BasicActivity extends Activity {
	

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.windowSizeGet(this);
		initData();
	}

	// ��ʼ������
	public abstract  void initData();
}
