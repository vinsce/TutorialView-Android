package com.cerminara.yazzy.ui.views.tutorialview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cerminara.yazzy.ui.views.tutorialview.utils.ScreenUtils;

/**
 * Created by vinsce on 10/08/16 at 0.59.
 *
 * @author vinsce
 */
public class TutorialView extends FrameLayout implements OverlayView.OnStepChangedListener {
	private OverlayView mOverlayView;
	private TextView mTextView;
	private FloatingActionButton mNextButton;
	private LinearLayout mTextLayout;

	private LayoutParams mTopLayoutParams, mBottomLayoutParams;

	private Animation mTextInAnimation, mTextOutAnimation, mButtonInAnimation, mButtonOutAnimation;
	private Step mCurStep;


	public TutorialView(Context context) {
		super(context);
		init(context);
	}

	public TutorialView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public TutorialView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context cnt) {
		mTopLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		mTopLayoutParams.gravity = Gravity.TOP;
		mTopLayoutParams.leftMargin = ScreenUtils.dpToPx(16, cnt);
		mTopLayoutParams.rightMargin = ScreenUtils.dpToPx(16, cnt);
		mTopLayoutParams.topMargin = ScreenUtils.dpToPx(16, cnt);
		mBottomLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		mBottomLayoutParams.gravity = Gravity.BOTTOM;
		mBottomLayoutParams.leftMargin = ScreenUtils.dpToPx(16, cnt);
		mBottomLayoutParams.rightMargin = ScreenUtils.dpToPx(16, cnt);
		mBottomLayoutParams.bottomMargin = ScreenUtils.dpToPx(16, cnt);

		mOverlayView = new OverlayView(cnt);
		mOverlayView.setStepListener(this);
		addView(mOverlayView);

		mTextView = new TextView(cnt);
		mTextView.setTextSize(32);
		mTextView.setTextColor(Color.WHITE);
		mTextView.setTypeface(Typeface.SANS_SERIF);
		mTextView.setShadowLayer(4f, 0f, 0f, Color.parseColor("#3ba3d6"));

		mNextButton = new FloatingActionButton(cnt);
		mNextButton.setImageResource(R.drawable.action_proceed);
		mNextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mOverlayView != null)
					mOverlayView.nextStep();
			}
		});

		mTextLayout = new LinearLayout(cnt);
		mTextLayout.setOrientation(LinearLayout.VERTICAL);
		mTextLayout.addView(mTextView);
		mTextLayout.addView(mNextButton);
		mTextLayout.setLayoutParams(mTopLayoutParams);
		mTextLayout.setGravity(Gravity.END);
		addView(mTextLayout);
		mTextLayout.bringToFront();
		mTextLayout.setPadding(ScreenUtils.dpToPx(16, cnt), ScreenUtils.dpToPx(16, cnt), ScreenUtils.dpToPx(16, cnt), ScreenUtils.dpToPx(16, cnt));
		mTextLayout.setClipChildren(false);
		mTextLayout.setClipToPadding(false);

		mButtonInAnimation = AnimationUtils.loadAnimation(cnt, R.anim.slide_in_right);
		mButtonOutAnimation = AnimationUtils.loadAnimation(cnt, R.anim.slide_out_left);
		mButtonOutAnimation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mNextButton.startAnimation(mButtonInAnimation);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});

		mTextInAnimation = AnimationUtils.loadAnimation(cnt, R.anim.slide_in_left);

		mTextOutAnimation = AnimationUtils.loadAnimation(cnt, R.anim.slide_out_right);
		mTextOutAnimation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				mNextButton.startAnimation(mButtonOutAnimation);
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				setRightLayoutParamsAndText(mCurStep);
				mTextView.startAnimation(mTextInAnimation);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		setAnimationMS(600);
		setAnimationInterpolator(new AnticipateOvershootInterpolator());

	}

	public void addStep(Step step) {
		if (mOverlayView != null)
			mOverlayView.addStep(step);
	}

	public void addStep(View view, boolean inWindow, float scale, String text) {
		if (mOverlayView != null)
			mOverlayView.addStep(view, inWindow, scale, text);
	}

	public void setListener(OverlayView.OnFinishedListener listener) {
		if (mOverlayView != null)
			mOverlayView.setListener(listener);
	}

	@Override
	public void beforeStepChanges(Step s1, final Step s2) {
		mCurStep = s2;
		mTextView.startAnimation(mTextOutAnimation);
	}

	@Override
	public void afterStepChanges(Step s1, Step s2) {
		mTextLayout.setVisibility(View.VISIBLE);
	}

	@Override
	public void onFirstStepLoaded(Step s1) {
		mCurStep = s1;
		setRightLayoutParamsAndText(s1);
	}

	private void setRightLayoutParamsAndText(Step step) {
		if (step.getCenterY() > getHeight() / 2) {
			mTextLayout.setLayoutParams(mTopLayoutParams);
		} else {
			mTextLayout.setLayoutParams(mBottomLayoutParams);
		}
		mTextView.setText(step.getText());
	}


	public void setAnimationMS(int animationMS) {
		mTextInAnimation.setDuration(animationMS / 2);
		mTextOutAnimation.setDuration(animationMS / 2);
		mButtonInAnimation.setDuration(animationMS / 2);
		mButtonOutAnimation.setDuration(animationMS / 2);
		if (mOverlayView != null)
			mOverlayView.setAnimationMS(animationMS);
	}

	public void setAnimationInterpolator(Interpolator interpolator) {
		mTextInAnimation.setInterpolator(interpolator);
		mTextOutAnimation.setInterpolator(interpolator);
		mButtonInAnimation.setInterpolator(interpolator);
		mButtonOutAnimation.setInterpolator(interpolator);
		if (mOverlayView != null)
			mOverlayView.setAnimationInterpolator(interpolator);
	}
}
