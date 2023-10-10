package appu26j.utils;

public class Animation {
    private float value;
    private long previous;

    public Animation(final float value){
        this.value = value;
        this.previous = System.currentTimeMillis();
    }

    public void setAnimation(final float value, double speed){

        final long currentMS = System.currentTimeMillis();
        final long delta = currentMS - this.previous;
        this.previous = currentMS;

        double deltaValue = 0.0;

        if(speed > 28) {
            speed = 28;
        }

        if (speed != 0.0)
        {
            deltaValue = Math.abs(value - this.value) * 0.35f / (10.0 / speed);
        }

        this.value = calculateValue(value, this.value, deltaValue, delta);
    }

    public static float calculateValue(final float target, float current, final double speed, long delta) {
        final float diff = current - target;
        double add =  (delta * (speed / 50));
        if (diff > speed){
            if(current - add > target) {
                current -= add;
            }else {
                current = target;
            }
        }
        else if (diff < -speed) {
            if(current + add < target) {
                current += add;
            }else {
                current = target;
            }
        }
        else{
            current = target;
        }

        return current;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
