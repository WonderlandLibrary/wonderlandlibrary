package com.canon.majik.api.utils.turok.util;


import com.canon.majik.api.utils.turok.hardware.mouse.TurokMouse;

/**
 * @author SrRina
 * @since 27/07/20 at 12:45pm
 */
public class TurokRect {
	public int x;
	public int y;

	public int width;
	public int height;

	public String tag;

	public TurokRect(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;

		this.width  = width;
		this.height = height;

		this.tag = "nontag";
	}

	public TurokRect(String tag, int x, int y, int width, int height) {
		this.x = x;
		this.y = y;

		this.width  = width;
		this.height = height;

		this.tag = "nontag";
	}

	public TurokRect(int x, int y) {
		this.x = x;
		this.y = y;

		this.width  = 0;
		this.height = 0;

		this.tag = "nontag";		
	}

	public TurokRect(String tag, int x, int y) {
		this.x = x;
		this.y = y;

		this.width  = 0;
		this.height = 0;

		this.tag = tag;		
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getTag() {
		return tag;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getDistance(TurokRect rect) {
		int calculatedX = this.x - rect.getX();
		int calculatedY = this.y - rect.getY();

		int calculatedW = (this.x + this.width) - (rect.getX() + rect.getWidth());
		int calculatedH = (this.y + this.height) - (rect.getY() + rect.getHeight());

		return TurokMath.sqrt(calculatedX * calculatedW + calculatedY * calculatedH);
	}

	public boolean collideWithMouse(TurokMouse mouse) {
		return mouse.getX() >= this.x && mouse.getX() <= (this.x + this.width) && mouse.getY() >= this.y && mouse.getY() <= (this.y + this.height);
	}

	public boolean collideWithRect(TurokRect rect) {
		return this.x <= (rect.getX() + rect.getWidth()) && (this.x + this.width) >= rect.getX() && this.y <= (rect.getY() + rect.getHeight()) && (this.y + this.height) >= rect.getY();
	}

	public static boolean collideRectWith(TurokRect rect, TurokMouse mouse) {
		return rect.collideWithMouse(mouse);
	}

	public static boolean collideRectWith(TurokRect rect, TurokRect rect1) {
		return rect.collideWithRect(rect1);
	}
}