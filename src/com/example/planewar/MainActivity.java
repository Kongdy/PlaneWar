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
	private Dialog waitDialog; // 等待进度条
	private StartViewDoListener sdListener;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initData() {
		/*
		 * 请再此之后研究android系统设置的修改,例如直接修改android系统的超时时间
		 */
		// 设置不允许屏幕休眠
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
		
//		waitDialog = new Dialog(this, R.style.wait_dialog);// 加载资源等待框
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
