package com.example.planewar.Entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View.OnTouchListener;

/**
 * ���ж�����ͼ�ĸ���
 * 
 * @author wangk
 *
 */
public abstract class EntityFather implements OnTouchListener {
	public Bitmap bitmap;
	public Context context;

	public int width; // ������
	public int height; // ����߶�

	public int newWidth;// ��ָ�����
	public int newHeight;// ��ָ���߶�

	public int iconResId; // ͼƬ��Դ�ļ�
	
	public boolean isExplosion; // �Ƿ�ը

	public float x; // λͼx����
	public float y; // λͼy����

	private Canvas canvas; // �������Ļ���

	public CreateBitmap createBitmap; // ��ȡͼƬ�߳�

	private Paint paint;
	
	public int totalFrame;
	public int currentFrame;
	
	public boolean isAlive; // �Ƿ���
	
	public boolean isCanAutoMove; // �Ƿ�����Զ��ƶ�

	public BitmapFactory.Options option = new BitmapFactory.Options(); // ����ͼƬ�����ż���

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
	 * �ı�ռ�߶ȺͿ��
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
	 * �ı���Դ�ļ�id
	 */
	public void changeIconResId(int iconResId) {
		this.iconResId = iconResId;
		bitmap = BitmapFactory.decodeResource(context.getResources(),
				iconResId, option);
	}

	/**
	 * �ı�λͼ
	 * 
	 * @param bitmap
	 */
	public void changeBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	/**
	 * ��ȡλͼ
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
	 * �������������̣߳���ֹ����
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
	 * ��ͼƬ�����ڻ�����
	 */
	public void drawBitmap() {
		canvas.save();
		canvas.drawBitmap(bitmap, x, y, paint);
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();
		canvas.restore();
	}
	
	/**
	 * @param frameSize ����֡
	 * @param isPortrait �Ƿ����� 
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
	 * ����λͼx���꣬�������Ļ�������ؾ���
	 * 
	 * @return
	 */
	public float getX() {
		return x;
	}

	/**
	 * ����λͼy���꣬�������Ļ��������ؾ���
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
	 * �ж��Ƿ�����ײ������д
	 * @return
	 */
	public boolean isCollide(EntityFather entity) {
		return false;
	}
	
	/**
	 *�����߳�
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
