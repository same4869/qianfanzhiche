package com.xun.qianfanzhiche.view;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class RadarView extends FrameLayout {
	private Context mContext;
	private int viewSize = 800;
	private Paint mPaintLine;
	private Paint mPaintSector;
	public boolean isstart = false;
	private ScanThread mThread;
	private Paint mPaintPoint;
	private RectF mRectF;
	private int start = 0;
	private int end = 90;

	public RadarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initPaint();
		mThread = new ScanThread(this);
		setBackgroundColor(Color.TRANSPARENT);
	}

	public RadarView(Context context) {
		super(context);
		mContext = context;
		initPaint();
		mThread = new ScanThread(this);
		setBackgroundColor(Color.TRANSPARENT);
	}
	
	public void resetViewSize(int viewSize){
		this.viewSize = viewSize;
	}

	private void initPaint() {
		mPaintLine = new Paint();
		mPaintLine.setStrokeWidth(10);
		mPaintLine.setAntiAlias(true);
		mPaintLine.setStyle(Style.STROKE);
		mPaintLine.setColor(0xff000000);

		mPaintSector = new Paint();
		mPaintSector.setColor(0x9D00ff00);
		mPaintSector.setAntiAlias(true);
	}

	public void setViewSize(int size) {
		this.viewSize = size;
		setMeasuredDimension(viewSize, viewSize);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(viewSize, viewSize);
	}

	public void start() {
		mThread.start();
		isstart = true;
	}

	public void stop() {
		if (isstart) {
			Thread.interrupted();
			isstart = false;
		}
	}

	public void setList(ArrayList<View> list) {
		for (int i = 0; i < list.size(); i++) {
			int xy[] = getRamdomXY();
			list.get(i).setX(xy[0]);
			list.get(i).setY(xy[1]);
			addView(list.get(i));
		}
	}

	private int[] getRamdomXY() {
		Random rand = new Random();
		int x = rand.nextInt(900);
		int y = rand.nextInt(900);
		int r = (int) ((float) viewSize / 2);
		if ((x >= r - 350 || x <= r + 350) && (y >= r - 350 || y <= r + 350)) {
			int xy[] = new int[2];
			xy[0] = x;
			xy[1] = y;
			return xy;
		} else {
			return getRamdomXY();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawCircle(viewSize / 2, viewSize / 2, 175, mPaintLine);
		canvas.drawCircle(viewSize / 2, viewSize / 2, 350, mPaintLine);
		canvas.drawLine(viewSize / 2, 0, viewSize / 2, viewSize, mPaintLine);
		canvas.drawLine(0, viewSize / 2, viewSize, viewSize / 2, mPaintLine);
		Shader mShader = new SweepGradient(viewSize / 2, viewSize / 2, Color.TRANSPARENT, Color.parseColor("#33bbff"));
		mPaintSector.setShader(mShader);
		canvas.concat(matrix);
		canvas.drawCircle(viewSize / 2, viewSize / 2, 350, mPaintSector);
		super.onDraw(canvas);
	}

	private Matrix matrix;

	protected class ScanThread extends Thread {

		private RadarView view;

		public ScanThread(RadarView view) {
			this.view = view;
		}

		@Override
		public void run() {
			while (true) {
				if (isstart) {
					view.post(new Runnable() {
						public void run() {
							start = start + 1;
							matrix = new Matrix();
							matrix.postRotate(start, viewSize / 2, viewSize / 2);
							view.invalidate();
						}
					});
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
