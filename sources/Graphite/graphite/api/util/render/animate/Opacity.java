package graphite.api.util.render.animate;

public class Opacity {
    private float opacity;
    private long lastMS;

    public Opacity(float opacity) {
        this.opacity = opacity;
        this.lastMS = System.currentTimeMillis();
    }

    public void interpolate(float targetOpacity, int speed) {
        long currentMS = System.currentTimeMillis();
        long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        this.opacity = AnimationUtil.calculateCompensation(this.opacity, targetOpacity, delta, speed);
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }
}