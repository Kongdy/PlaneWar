package com.example.planewar;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.example.planewar.view.MySurfaceView;
import com.example.planewar.view.StartView;
import com.example.planewar.view.StartView.StartViewDoListener;

public class MainActivity extends BasicActivity {
	
	private MySurfaceView planeWarMain;
	private StartView startView;
	private Dialog waitDialog; // �ȴ�������
	private StartViewDoListener sdListener;
	

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
		startView = new StartView(getApplicationContext());
		sdListener = new StartViewDoListener() {
			@Override
			public void startDo() {
				setContentView(null);
				startView.stop();
				startView.destroyDrawingCache();
				planeWarMain = new MySurfaceView(getApplicationContext());
				setContentView(planeWarMain);
			}
		};
		startView.setListener(sdListener);
		setContentView(startView);
		
//		waitDialog = new Dialog(this, R.style.wait_dialog);// ������Դ�ȴ���
//		waitDialog.setContentView(R.layout.view_wait_dialog);
//		waitDialog.setCancelable(false);
//		waitDialog.show();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(planeWarMain != null) {
			planeWarMain.handleTouch(event);
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(planeWarMain != null) {
			planeWarMain.destroyDrawingCache();
		}
		if(startView != null) {
			startView.destroyDrawingCache();
		}
	}
	
	
}
