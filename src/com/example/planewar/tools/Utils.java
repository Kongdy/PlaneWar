package com.example.planewar.tools;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class Utils {
	
	public static int SCREENWIDTH_;
	public static int SCREENHEIGHT_;
	public static int SCREENDPI_;
	
	public static void windowSizeGet(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		SCREENWIDTH_ = dm.widthPixels;
		SCREENHEIGHT_ = dm.heightPixels;
		SCREENDPI_ = dm.densityDpi;
	}
}
