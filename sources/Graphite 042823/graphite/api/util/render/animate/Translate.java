package graphite.api.util.render.animate;

public class Translate {
	private float x, y;
	private long lastMS;

	public Translate(float x, float y) {
		this.x = x;
		this.y = y;
		this.lastMS = System.currentTimeMillis();
	}

	public void interpolate(float targetX, float targetY, float smoothing) {
		long currentMS = System.currentTimeMillis();
		long delta = currentMS - this.lastMS;
		this.lastMS = currentMS;
		int deltaX = (int) (Math.abs(targetX - this.x) * smoothing);
		int deltaY = (int) (Math.abs(targetY - this.y) * smoothing);
		this.x = AnimationUtil.calculateCompensation(this.x, targetX, delta, deltaX);
		this.y = AnimationUtil.calculateCompensation(this.y, targetY, delta, deltaY);
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