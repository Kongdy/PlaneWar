package com.example.planewar;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.planewar.view.MySurfaceView;
import com.example.planewar.view.StartView;

public class MainActivity extends BasicActivity {
	
	private MySurfaceView planeWarMain;
	private StartView startView;
	private Dialog waitDialog; // �ȴ�������
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public void initData() {
		/*
		 * ���ٴ�֮���о�androidϵͳ���õ��޸�,����ֱ���޸�androidϵͳ�ĳ�ʱʱ��
		 */
		// ���ò�������Ļ����
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		RelativeLayout view = (RelativeLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_main,null,false);
	//	planeWarMain = new MySurfaceView(getApplicationContext());
		startView = new StartView(getApplicationContext());
		view.addView(startView);
		setContentView(view);
		
//		waitDialog = new Dialog(this, R.style.wait_dialog);// ������Դ�ȴ���
//		waitDialog.setContentView(R.layout.view_wait_dialog);
//		waitDialog.setCancelable(false);
//		waitDialog.show();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	//	planeWarMain.handleTouch(event);
		return super.onTouchEvent(event);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	//	planeWarMain.destroyDrawingCache();
		startView.destroyDrawingCache();
	}
	
	
}
