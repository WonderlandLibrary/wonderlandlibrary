package vestige.util.misc;

public class TimerUtil {

    private long lastTime;

    public TimerUtil() {
        reset();
    }

    public long getTimeElapsed() {
        return System.currentTimeMillis() - lastTime;
    }

    public void reset() {
        lastTime = System.currentTimeMillis();
    }

}
