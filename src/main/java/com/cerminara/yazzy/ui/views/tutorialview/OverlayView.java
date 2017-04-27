package com.cerminara.yazzy.ui.views.tutorialview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.Interpolator;

import com.cerminara.yazzy.ui.views.tutorialview.utils.ScreenUtils;

import java.util.ArrayList;

/**
 * Created by vinsce on 06/08/16 at 23.07.
 *
 * @author vinsce
 */
public class OverlayView extends View {
	private Paint mPaint;
	private ValueAnimator animator;
	private PorterDuffXfermode mClearMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
	private ArrayList<Step> mSteps;
	private Step mCurStep, mNextStep;
	private int mCurX, mCurY, mCurR;

	private int mCurIndex;

	private OnFinishedListener mListener;
	private OnStepChangedListener mStepListener;

	private boolean blurred = false;


	public OverlayView(Context context) {
		super(context);
		init(context);
	}

	public OverlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public OverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context cnt) {
		mSteps = new ArrayList<>(5);
		setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(0x77BBBBBB);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setShadowLayer(4, 0, 0, 0xFF000000);

		// Circle animation
		animator = ValueAnimator.ofFloat(0, 1);

		animator.setDuration(600);

		animator.addUpdateListener(
				new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						float value = (Float) (animation.getAnimatedValue());

						mCurX = (int) (mCurStep.getCenterX() + value * (mNextStep.getCenterX() - mCurStep.getCenterX()));
						mCurY = (int) (mCurStep.getCenterY() + value * (mNextStep.getCenterY() - mCurStep.getCenterY()));
						mCurR = (int) (mCurStep.getRadius() + value * (mNextStep.getRadius() - mCurStep.getRadius()));
						invalidate();
					}
				}
		);
		animator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				if (mStepListener != null)
					mStepListener.beforeStepChanges(mCurStep, mNextStep);
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (mStepListener != null)
					mStepListener.afterStepChanges(mCurStep, mNextStep);
				setCurrentStep(mCurIndex + 1);
				mCurIndex++;
				if (mSteps.size() == mCurIndex + 1)
					mNextStep = null;
				else mNextStep = mSteps.get(mCurIndex + 1);
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});
		animator.setInterpolator(new AnticipateOvershootInterpolator());

	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mCurStep == null)
			return;
		canvas.drawPaint(mPaint);
		mPaint.setXfermode(mClearMode);
		// clears the circle
		canvas.drawCircle(mCurX, mCurY, mCurR, mPaint);
		mPaint.setXfermode(null);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}

	public void nextStep() {
		if (mNextStep == null)
			onFinished();
		else
			animator.start();
	}

	public void addStep(Step s) {
		Point screenSize = ScreenUtils.getScreenSize(getContext());
		int minSize = (Math.min(screenSize.x / 2, screenSize.y / 2) * 4) / 5;
		if (s.getRadius() > minSize)
			s.setRadius(minSize);
		mSteps.add(s);
		if (mSteps.size() == 1) {
			// s is the first step
			setCurrentStep(0);
			if (mStepListener != null)
				mStepListener.onFirstStepLoaded(s);
		} else if (mSteps.get(mSteps.size() - 2) == (mCurStep)) {
			// s is the new next step
			mNextStep = s;
		}
	}

	public void addStep(View view, boolean inWindow, float scale, String text) {
		Step step = new Step();
		step.setRadius(((int) (Math.max(view.getMeasuredHeight(), view.getMeasuredWidth()) * scale)) / 2);
		if (!inWindow) {
			step.setCenterX(view.getLeft());
			step.setCenterY(view.getTop());
		} else {
			int[] pos = new int[2];
			int[] thisPos = new int[2];
			view.getLocationOnScreen(pos);
			getLocationOnScreen(thisPos);
			step.setCenterX(pos[0] + (view.getMeasuredWidth() / 2) - thisPos[0]);
			step.setCenterY(pos[1] + (view.getMeasuredHeight() / 2) - thisPos[1]);
		}
		step.setText(text);
		addStep(step);
	}

	public void addStep(View view, boolean inWindow, String text) {
		addStep(view, inWindow, 1, text);
	}


	private void setCurrentStep(int index) {
		mCurStep = mSteps.get(index);
		mCurX = mCurStep.getCenterX();
		mCurY = mCurStep.getCenterY();
		mCurR = mCurStep.getRadius();
		postInvalidate();
	}

	public void setListener(OnFinishedListener listener) {
		mListener = listener;
	}

	private void onFinished() {
		if (mListener == null)
			setVisibility(GONE);
		else mListener.onFinished();
	}

	public void setStepListener(OnStepChangedListener stepListener) {
		mStepListener = stepListener;
		if (mCurStep != null)
			stepListener.onFirstStepLoaded(mCurStep);
	}

	public void setAnimationMS(int ms) {
		animator.setDuration(ms);
	}

	public void setAnimationInterpolator(Interpolator interpolator) {
		animator.setInterpolator(interpolator);
	}

	public interface OnFinishedListener {
		void onFinished();
	}

	@SuppressWarnings("WeakerAccess")
	public interface OnStepChangedListener {
		void beforeStepChanges(Step s1, Step s2);

		void afterStepChanges(Step s1, Step s2);

		void onFirstStepLoaded(Step s1);

	}
}
