package com.example.planewar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;

import com.example.planewar.common.ConstantUtils;
import com.example.planewar.view.MySurfaceView;
import com.example.planewar.view.StartView;

public class MainActivity extends BasicActivity {
	
	private MySurfaceView planeWarMain;
	private StartView startView;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ConstantUtils.TO_MAIN_VIEW:
				toMainView();
				break;
			case ConstantUtils.TO_PLAY_VIEW:
				toPlayView();
				break;
			case ConstantUtils.TO_END_VIEW:
				break;
			default:
				break;
			}
		};
	};

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
		toMainView();
	}
	
	public void toMainView() {
		if(startView == null) {
			startView = new StartView(MainActivity.this);
		}
		setContentView(startView);
		resetView(startView);
	}
	
	/**
	 * @param view current view
	 */
	private void resetView(View view) {
		if(view == null) {
			if(planeWarMain != null) {
				planeWarMain.clear();
				planeWarMain.destroyDrawingCache();
			}
			if(startView != null) {
				startView.clear();
				startView.destroyDrawingCache();
			}
			startView = null;
			planeWarMain = null;
		} else if(view instanceof StartView) {
			if(planeWarMain != null) {
				planeWarMain.clear();
				planeWarMain.destroyDrawingCache();
			}
			planeWarMain = null;
		} else if(view instanceof MySurfaceView) {
			if(startView != null) {
				startView.clear();
				startView.destroyDrawingCache();
			}
			startView = null;
		}
	}
	
	public void toPlayView() {
		if(planeWarMain == null) {
			planeWarMain = new MySurfaceView(MainActivity.this);
		}
		setContentView(planeWarMain);
		resetView(planeWarMain);
	}
	
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	
	public Handler getHandler() {
		return handler;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		resetView(null);
	}
}
