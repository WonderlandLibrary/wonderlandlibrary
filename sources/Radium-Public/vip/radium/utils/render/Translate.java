package vip.radium.utils.render;

public final class Translate {

    private double x, y;

    public Translate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void animate(double newX, double newY) {
        x = RenderingUtils.progressiveAnimation(x, newX, 1.0D);
        y = RenderingUtils.progressiveAnimation(y, newY, 0.5D);
    }

    public double getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
