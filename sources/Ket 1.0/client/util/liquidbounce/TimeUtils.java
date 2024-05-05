package client.util.liquidbounce;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class TimeUtils {
    public int randomDelay(int minDelay, int maxDelay) {
        return RandomUtils.nextInt(minDelay, maxDelay + 1);
    }
}
