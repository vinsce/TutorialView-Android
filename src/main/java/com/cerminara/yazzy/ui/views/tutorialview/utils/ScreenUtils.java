package com.cerminara.yazzy.ui.views.tutorialview.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by vinsce on 12/06/16 at 13.19.
 *
 * @author vinsce
 */
public class ScreenUtils {

	public static Point getScreenSize(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size;
	}

	public static int dpToPx(int dp, Context cnt) {
		float density = cnt.getResources().getDisplayMetrics().density;
		return Math.round((float) dp * density);
	}
}
