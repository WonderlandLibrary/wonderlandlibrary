package me.aquavit.liquidsense.utils.render;

public final class Translate {

	private float x;
	private float y;

	public Translate(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void interpolate(float targetX, float targetY, double smoothing) {
		x = (float) AnimationUtils.animate(targetX, this.x, smoothing);
		y = (float) AnimationUtils.animate(targetY, this.y, smoothing);
	}

	public void translate(float targetX, float targetY, double speed) {
		x = AnimationUtils.lstransition(x, targetX, speed);
		y = AnimationUtils.lstransition(y, targetY, speed);
	}

	public void translate(float targetX, float targetY) {
		x = AnimationUtils.lstransition(x, targetX, 0.0);
		y = AnimationUtils.lstransition(y, targetY, 0.0);
	}

	public float getX() {
		return this.x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return this.y;
	}

	public void setY(float y) {
		this.y = y;
	}
}
