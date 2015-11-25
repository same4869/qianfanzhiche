package com.xun.qianfanzhiche.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

@SuppressLint("ViewConstructor")
public class PathComposerLayout extends RelativeLayout {

	public static byte RIGHTBOTTOM = 1, CENTERBOTTOM = 2, LEFTBOTTOM = 3, LEFTCENTER = 4, LEFTTOP = 5, CENTERTOP = 6, RIGHTTOP = 7, RIGHTCENTER = 8;
	private boolean hasInit = false;
	private boolean areButtonsShowing = false;
	private Context mycontext;
	private ImageView cross;
	private RelativeLayout rlButton;
	private PathAnimations myani;
	private LinearLayout[] llayouts;
	private int duretime = 300;

	private PathStatusInterface pathStatusInterface;

	public PathComposerLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mycontext = context;
	}

	public PathComposerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mycontext = context;
	}

	public PathComposerLayout(Context context) {
		super(context);
		this.mycontext = context;
	}

	public void init(int[] imgResId, int showhideButtonId, int crossId, byte pCode, int radius, final int durationMillis) {
		duretime = durationMillis;
		int align1 = 12, align2 = 14;
		if (pCode == RIGHTBOTTOM) {
			align1 = ALIGN_PARENT_RIGHT;
			align2 = ALIGN_PARENT_BOTTOM;
		} else if (pCode == CENTERBOTTOM) {
			align1 = CENTER_HORIZONTAL;
			align2 = ALIGN_PARENT_BOTTOM;
		} else if (pCode == LEFTBOTTOM) {
			align1 = ALIGN_PARENT_LEFT;
			align2 = ALIGN_PARENT_BOTTOM;
		} else if (pCode == LEFTCENTER) {
			align1 = ALIGN_PARENT_LEFT;
			align2 = CENTER_VERTICAL;
		} else if (pCode == LEFTTOP) {
			align1 = ALIGN_PARENT_LEFT;
			align2 = ALIGN_PARENT_TOP;
		} else if (pCode == CENTERTOP) {
			align1 = CENTER_HORIZONTAL;
			align2 = ALIGN_PARENT_TOP;
		} else if (pCode == RIGHTTOP) {
			align1 = ALIGN_PARENT_RIGHT;
			align2 = ALIGN_PARENT_TOP;
		} else if (pCode == RIGHTCENTER) {
			align1 = ALIGN_PARENT_RIGHT;
			align2 = CENTER_VERTICAL;
		}
		RelativeLayout.LayoutParams thislps = (LayoutParams) this.getLayoutParams();
		Bitmap mBottom = BitmapFactory.decodeResource(mycontext.getResources(), imgResId[0]);
		if (pCode == CENTERBOTTOM || pCode == CENTERTOP) {
			if (thislps.width != -1 && thislps.width != -2 && thislps.width < (radius + mBottom.getWidth() + radius * 0.1) * 2) {
				thislps.width = (int) ((radius * 1.1 + mBottom.getWidth()) * 2);
			}
		} else {
			if (thislps.width != -1 && thislps.width != -2 && thislps.width < radius + mBottom.getWidth() + radius * 0.1) {
				thislps.width = (int) (radius * 1.1 + mBottom.getWidth());
			}
		}
		if (pCode == LEFTCENTER || pCode == RIGHTCENTER) {
			if (thislps.height != -1 && thislps.height != -2 && thislps.height < (radius + mBottom.getHeight() + radius * 0.1) * 2) {
				thislps.width = (int) ((radius * 1.1 + mBottom.getHeight()) * 2);
			}
		} else {
			if (thislps.height != -1 && thislps.height != -2 && thislps.height < radius + mBottom.getHeight() + radius * 0.1) {
				thislps.height = (int) (radius * 1.1 + mBottom.getHeight());
			}
		}
		this.setLayoutParams(thislps);
		RelativeLayout rl1 = new RelativeLayout(mycontext);

		rlButton = new RelativeLayout(mycontext);
		llayouts = new LinearLayout[imgResId.length];
		for (int i = 0; i < imgResId.length; i++) {
			ImageView img = new ImageView(mycontext);

			img.setImageResource(imgResId[i]);
			LinearLayout.LayoutParams llps = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

			img.setLayoutParams(llps);
			llayouts[i] = new LinearLayout(mycontext);
			llayouts[i].setId(100 + i);
			llayouts[i].addView(img);

			RelativeLayout.LayoutParams rlps = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			rlps.alignWithParent = true;
			rlps.addRule(align1, RelativeLayout.TRUE);
			rlps.addRule(align2, RelativeLayout.TRUE);
			llayouts[i].setLayoutParams(rlps);
			llayouts[i].setVisibility(View.INVISIBLE);
			rl1.addView(llayouts[i]);
		}
		RelativeLayout.LayoutParams rlps1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		rlps1.alignWithParent = true;
		rlps1.addRule(align1, RelativeLayout.TRUE);
		rlps1.addRule(align2, RelativeLayout.TRUE);
		rl1.setLayoutParams(rlps1);

		RelativeLayout.LayoutParams buttonlps = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		buttonlps.alignWithParent = true;
		buttonlps.addRule(align1, RelativeLayout.TRUE);
		buttonlps.addRule(align2, RelativeLayout.TRUE);
		rlButton.setLayoutParams(buttonlps);
		rlButton.setBackgroundResource(showhideButtonId);
		cross = new ImageView(mycontext);
		cross.setImageResource(crossId);
		RelativeLayout.LayoutParams crosslps = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		crosslps.alignWithParent = true;
		crosslps.addRule(CENTER_IN_PARENT, RelativeLayout.TRUE);
		cross.setLayoutParams(crosslps);
		rlButton.addView(cross);
		myani = new PathAnimations(rl1, pCode, radius);
		rlButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (areButtonsShowing) {
					myani.startAnimationsOut(duretime);
					cross.startAnimation(PathAnimations.getRotateAnimation(-270, 0, duretime));
					pathStatusInterface.onPathIsExpand(false);
				} else {
					myani.startAnimationsIn(duretime);
					cross.startAnimation(PathAnimations.getRotateAnimation(0, -270, duretime));
					pathStatusInterface.onPathIsExpand(true);
				}
				areButtonsShowing = !areButtonsShowing;
			}
		});

		cross.startAnimation(PathAnimations.getRotateAnimation(0, 360, 200));
		this.addView(rl1);
		this.addView(rlButton);
		hasInit = true;

	}

	public void collapse() {
		myani.startAnimationsOut(duretime);
		cross.startAnimation(PathAnimations.getRotateAnimation(-270, 0, duretime));
		areButtonsShowing = false;
		pathStatusInterface.onPathIsExpand(false);
	}

	public void expand() {
		myani.startAnimationsIn(duretime);
		cross.startAnimation(PathAnimations.getRotateAnimation(0, -270, duretime));
		areButtonsShowing = true;
		pathStatusInterface.onPathIsExpand(true);
	}

	public boolean isInit() {
		return hasInit;
	}

	public boolean isShow() {
		return areButtonsShowing;
	}

	public void setButtonsOnClickListener(final OnClickListener l) {

		if (llayouts != null) {
			for (int i = 0; i < llayouts.length; i++) {
				if (llayouts[i] != null)
					llayouts[i].setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(final View view) {
							collapse();
							l.onClick(view);
						}

					});
			}
		}
	}

	public void setOnPathStatusListener(PathStatusInterface pathStatusInterface) {
		this.pathStatusInterface = pathStatusInterface;
	}

	public interface PathStatusInterface {
		void onPathIsExpand(boolean isExpand);
	}
}
