package com.cerminara.yazzy.ui.views.tutorialview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
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
 * Use {@link TutorialView} to insert a tutorial above your layout.
 * Create the {@link TutorialView} and add a series of {@link Step} to build the tutorial (the steps are displayed to the user in the order they are inserted via TutorialView.addStep()).
 * For each step, this view displays a circle around the target view and the specified text.
 * <p>
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

	/**
	 * Add a new {@link Step} to the tutorial
	 *
	 * @param step the {@link Step} to be added
	 */
	public void addStep(Step step) {
		if (mOverlayView != null)
			mOverlayView.addStep(step);
	}

	/**
	 * Creates and adds a new {@link Step} to the tutorial using the provided values
	 *
	 * @param view     the target view
	 * @param inWindow specifies if the position measurement should be related to the window (if true) or to the parent (if false)
	 * @param scale    the scale factor of the circle in relation to the target view sizes
	 * @param text     the text of the tutorial step
	 */
	public void addStep(View view, boolean inWindow, float scale, String text) {
		if (mOverlayView != null)
			mOverlayView.addStep(view, inWindow, scale, text);
	}

	/**
	 * Set the {@link com.cerminara.yazzy.ui.views.tutorialview.OverlayView.OnFinishedListener} that specifies what to do when the tutorial is completed (eg. write something on SharedPreferences)
	 *
	 * @param listener the {@link com.cerminara.yazzy.ui.views.tutorialview.OverlayView.OnFinishedListener}
	 */
	public void setOnFinishedListener(OverlayView.OnFinishedListener listener) {
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


	/**
	 * Set the duration of the animation (changing position of the circle and text) in ms
	 *
	 * @param animationMS the new duration in milliseconds
	 */
	public void setAnimationMS(int animationMS) {
		mTextInAnimation.setDuration(animationMS / 2);
		mTextOutAnimation.setDuration(animationMS / 2);
		mButtonInAnimation.setDuration(animationMS / 2);
		mButtonOutAnimation.setDuration(animationMS / 2);
		if (mOverlayView != null)
			mOverlayView.setAnimationMS(animationMS);
	}

	/**
	 * Set the interpolator for the animation
	 *
	 * @param interpolator the new interpolator
	 */
	public void setAnimationInterpolator(Interpolator interpolator) {
		mTextInAnimation.setInterpolator(interpolator);
		mTextOutAnimation.setInterpolator(interpolator);
		mButtonInAnimation.setInterpolator(interpolator);
		mButtonOutAnimation.setInterpolator(interpolator);
		if (mOverlayView != null)
			mOverlayView.setAnimationInterpolator(interpolator);
	}

	/**
	 * Changes the background color of the "next step" button in the tutorial.
	 *
	 * @param color the button background color
	 */
	public void setNextButtonBackgroundColor(@ColorInt int color) {
		mNextButton.setBackgroundTintList(ColorStateList.valueOf(color));
	}
}
