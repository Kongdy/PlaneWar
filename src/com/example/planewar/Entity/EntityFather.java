package com.example.planewar.Entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View.OnTouchListener;

/**
 * 所有对象视图的父类
 * 
 * @author wangk
 *
 */
public abstract class EntityFather implements OnTouchListener {
	public Bitmap bitmap;
	public Context context;

	public int width; // 对象宽度
	public int height; // 对象高度

	public int newWidth;// 新指定宽度
	public int newHeight;// 新指定高度

	public int iconResId; // 图片资源文件
	
	public boolean isExplosion; // 是否爆炸

	public float x; // 位图x坐标
	public float y; // 位图y坐标

	private Canvas canvas; // 传进来的画板

	public CreateBitmap createBitmap; // 获取图片线程

	private Paint paint;
	
	public int totalFrame;
	public int currentFrame;
	
	public boolean isAlive; // 是否存活
	
	public boolean isCanAutoMove; // 是否可以自动移动

	public BitmapFactory.Options option = new BitmapFactory.Options(); // 设置图片的缩放级别

	public EntityFather(Context context, int iconResId,int totalFrame,int currentFrame) {
		createBitmap = new CreateBitmap();
		this.context = context;
		this.iconResId = iconResId;
		option.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeResource(context.getResources(),
				iconResId, option);
		width = option.outWidth;
		height = option.outHeight;
		this.totalFrame = totalFrame;
		this.currentFrame = currentFrame;
		isCanAutoMove = true;
	}

	/**
	 * 改变空间高度和宽度
	 * 
	 * @param width
	 * @param height
	 */
	public void changeSize(int width, int height) {
		this.newWidth = width;
		this.newHeight = height;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	/**
	 * 改变资源文件id
	 */
	public void changeIconResId(int iconResId) {
		this.iconResId = iconResId;
		bitmap = BitmapFactory.decodeResource(context.getResources(),
				iconResId, option);
	}

	/**
	 * 改变位图
	 * 
	 * @param bitmap
	 */
	public void changeBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	/**
	 * 获取位图
	 * 
	 * @return
	 */
	public Bitmap getBitmap() {
		return bitmap;
	}

	public int getIconResId() {
		return iconResId;
	}

	/**
	 * 创建对象，启用线程，防止卡顿
	 * @return
	 */
	public void getParams(Canvas canvas, float x, float y, Paint paint) {
		this.x = x;
		this.y = y;
		this.canvas = canvas;
		this.paint = paint;
	}

	public class CreateBitmap extends Thread {
		@Override
		public void run() {

			if (newWidth != 0 && newHeight != 0 && width != newWidth
					&& height != newHeight) {
				option.inSampleSize = width / newWidth < height / newHeight ? width
						/ newWidth
						: height / newHeight;
			}
			option.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					iconResId, option);
		}
	}

	/**
	 * 把图片绘制在画布上
	 */
	public void drawBitmap() {
		canvas.save();
		canvas.drawBitmap(bitmap, x, y, paint);
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();
		canvas.restore();
	}
	
	/**
	 * @param frameSize 播放帧
	 * @param isPortrait 是否竖向 
	 */
	public void drawBitmap(int frameSize,boolean isPortrait) {
		this.currentFrame = frameSize;
		canvas.save();
		if(isPortrait) {
			this.width = bitmap.getWidth();
			this.height = bitmap.getHeight()/totalFrame;
			canvas.clipRect(x, y,x+width,y+height);
			canvas.drawBitmap(bitmap, x, y-(frameSize-1)*height, paint);
		} else {
			this.width = bitmap.getWidth()/totalFrame;
			this.height = bitmap.getHeight();
			canvas.clipRect(x, y,x+width,y+height);
			canvas.drawBitmap(bitmap, x-(frameSize-1)*width, y, paint);
		}
		canvas.restore();
	}

	/**
	 * 返回位图x坐标，即相对屏幕左框的像素距离
	 * 
	 * @return
	 */
	public float getX() {
		return x;
	}

	/**
	 * 返回位图y坐标，即相对屏幕顶框的像素距离
	 * 
	 * @return
	 */
	public float getY() {
		return y;
	}

	public Bitmap drawSelf(Canvas canvas, float x, float y, Paint paint) {
		getParams(canvas, x, y, paint);
		if(bitmap == null && this.y > 0) {
			if(!createBitmap.isAlive()) {
				createBitmap.start();
			}
		}
		if(bitmap != null) {
			drawBitmap();
		}
		if(isCanAutoMove) {
			move();
		}
		return bitmap;
	};

	public abstract void move();
	/**
	 * 判断是否有碰撞，请重写
	 * @return
	 */
	public boolean isCollide(EntityFather entity) {
		return false;
	}
	
	/**
	 *毁灭线程
	 * @author wangk
	 */
	public class DestorySelf extends Thread {
		@Override
		public void run() {
			while(true) {
				try {
					Thread.sleep(150l);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				currentFrame++;
				if(currentFrame>totalFrame) {
					break;
				}
			}
		}
	}

}
