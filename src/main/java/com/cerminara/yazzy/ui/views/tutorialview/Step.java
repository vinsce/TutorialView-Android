package com.cerminara.yazzy.ui.views.tutorialview;

/**
 * Created by vinsce on 27/04/17 at 22.29.
 *
 * @author vinsce
 */

public class Step {
	private int centerY = 300, centerX = 500, radius = 300;
	private String text = "";

	public int getCenterY() {
		return centerY;
	}

	public void setCenterY(int y) {
		this.centerY = y;
	}

	public int getCenterX() {
		return centerX;
	}

	public void setCenterX(int x) {
		this.centerX = x;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int r) {
		this.radius = r;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}