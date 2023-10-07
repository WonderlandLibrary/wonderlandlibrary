package best.azura.client.util.other;

public class DelayUtil {

    private long start = 0;

    public void reset() {
        start = System.currentTimeMillis();
    }

    public long getPassedTime() {
        return System.currentTimeMillis() - start;
    }

    public boolean isOver(double millis) {
        return System.currentTimeMillis() - start >= millis;
    }

    public boolean isOver(long millis) {
        return System.currentTimeMillis() - start >= millis;
    }

    public boolean hasReached(long millis) {
        return isOver(millis);
    }

    public boolean hasReached(double millis) {
        return isOver(millis);
    }

    public boolean hasReached(long millis, boolean reset) {
        boolean b = isOver(millis);
        if (reset && b) reset();
        return b;
    }

    public boolean isElapsed(long millis) {
        return isOver(millis);
    }

    public boolean isElapsed(double millis) {
        return isOver(millis);
    }

}
