package com.cerminara.yazzy.ui.views.tutorialview;

/**
 * Created by vinsce on 27/04/17 at 22.29.
 *
 * @author vinsce
 */

@SuppressWarnings("ALL")
public class Step {
	private int centerY = 300, centerX = 500, radius = 300;
	private String text = "";

	/**
	 * Get the y center of the circle
	 *
	 * @return the y center of the circle
	 */
	public int getCenterY() {
		return centerY;
	}

	/**
	 * Set the y center of the circle
	 *
	 * @param y the y center of the circle
	 */
	public void setCenterY(int y) {
		this.centerY = y;
	}

	/**
	 * Get the x center of the circle
	 *
	 * @return the x center of the circle
	 */
	public int getCenterX() {
		return centerX;
	}

	/**
	 * Set the x center of the circle
	 *
	 * @param x the x center of the circle
	 */
	public void setCenterX(int x) {
		this.centerX = x;
	}

	/**
	 * Get the radius of the circle
	 *
	 * @return the radius of the circle
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * Set the radius of the circle
	 *
	 * @param r the radius of the circle
	 */
	public void setRadius(int r) {
		this.radius = r;
	}

	/**
	 * Get the text of the tutorial step
	 *
	 * @return the text of the tutorial step
	 */
	public String getText() {
		return text;
	}

	/**
	 * Set the text of the tutorial step
	 *
	 * @param text the text of the tutorial step
	 */
	public void setText(String text) {
		this.text = text;
	}
}