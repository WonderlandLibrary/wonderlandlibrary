package koks.api.util;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 11:31
 */
public class TimeHelper {

    private long ms;

    public boolean hasReached(long delay) {
        return System.currentTimeMillis() - ms >= delay;
    }

    public void reset() {
        ms = System.currentTimeMillis();
    }

}
