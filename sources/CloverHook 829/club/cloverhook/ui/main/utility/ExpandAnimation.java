package club.cloverhook.ui.main.utility;

/**
 * @author antja03
 */
public class ExpandAnimation {

    private float x;
    private float y;
    private long lastMS;

    public ExpandAnimation(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public void expand(float targetX, float targetY, float xSpeed, float ySpeed)
    {
        long currentMS = System.currentTimeMillis();
        long delta = currentMS - lastMS;
        if (delta > 60)
            delta = 16;
        lastMS = currentMS;

        int deltaX = (int) (Math.abs(targetX - x) * xSpeed);
        int deltaY = (int) (Math.abs(targetY - y) * ySpeed);
        x = calculateCompensation(targetX, x, delta, deltaX);
        y = calculateCompensation(targetY, y, delta, deltaY);
    }

    public float calculateCompensation(float target, float current, long delta, int speed) {
        float diff = current - target;
        if(delta < 1) {
            delta = 1;
        }
        if(delta > 1000) {
            delta = 16;
        }
        if(diff > speed) {
            double xD = (speed * delta / (1000/60) < 0.5 ? 0.5 : speed * delta / (1000/60));
            current -= xD;
            if(current < target) {
                current = target;
            }
        } else if(diff < -speed) {
            double xD = (speed * delta / (1000/60) < 0.5 ? 0.5 : speed * delta / (1000/60));
            current += xD;
            if(current > target) {
                current = target;
            }
        } else {
            current = target;
        }
        return current;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
