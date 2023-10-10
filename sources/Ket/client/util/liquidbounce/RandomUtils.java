package client.util.liquidbounce;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class RandomUtils {
    public int nextInt() {
        return new Random().nextInt(Integer.MAX_VALUE);
    }
    public int nextInt(int startInclusive) {
        return Integer.MAX_VALUE - startInclusive <= 0 ? startInclusive : startInclusive + new Random().nextInt(Integer.MAX_VALUE - startInclusive);
    }
    public int nextInt(int startInclusive, int endExclusive) {
        return endExclusive - startInclusive <= 0 ? startInclusive : startInclusive + new Random().nextInt(endExclusive - startInclusive);
    }
}
