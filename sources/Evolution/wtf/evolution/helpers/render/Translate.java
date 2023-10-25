package wtf.evolution.helpers.render;



public final class Translate {
    private double x;
    private double y;

    public Translate(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public final void interpolate(double targetX, double targetY, double smoothing) {
        this.x = animate(targetX, this.x, smoothing);
        this.y = animate(targetY, this.y, smoothing);
    }

    public void animate(double newX, double newY) {
        this.x = animate(this.x, newX, 1.0);
        this.y = animate(this.y, newY, 1.0);
    }
    public double animate(final double target, double current, double speed) {
        final boolean larger = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        }
        else if (speed > 1.0) {
            speed = 1.0;
        }
        final double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1) {
            factor = 0.1;
        }
        if (larger) {
            current += factor;
        }
        else {
            current -= factor;
        }
        return current;
    }
    public double getX() {
        return Math.round(this.x);
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return Math.round(this.y);
    }

    public void setY(double y) {
        this.y = y;
    }
}