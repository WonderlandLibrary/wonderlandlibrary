package intent.AquaDev.aqua.utils;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {
    public static RandomUtil instance = new RandomUtil();

    public int nextInt(int origin, int bound) {
        if (origin == bound) {
            return origin;
        }
        return ThreadLocalRandom.current().nextInt(origin, bound);
    }

    public int nextInt(double origin, double bound) {
        if ((int)origin == (int)bound) {
            return (int)origin;
        }
        return ThreadLocalRandom.current().nextInt((int)origin, (int)bound);
    }

    public long nextLong(long origin, long bound) {
        if (origin == bound) {
            return origin;
        }
        return ThreadLocalRandom.current().nextLong(origin, bound);
    }

    public long nextLong(double origin, double bound) {
        if ((long)origin == (long)bound) {
            return (long)origin;
        }
        return ThreadLocalRandom.current().nextLong((long)origin, (long)bound);
    }

    public float nextFloat(double origin, double bound) {
        if (origin == bound) {
            return (float)origin;
        }
        return (float)ThreadLocalRandom.current().nextDouble((double)((float)origin), (double)((float)bound));
    }

    public float nextFloat(float origin, float bound) {
        if (origin == bound) {
            return origin;
        }
        return (float)ThreadLocalRandom.current().nextDouble((double)origin, (double)bound);
    }

    public double nextDouble(double origin, double bound) {
        if (origin == bound) {
            return origin;
        }
        return ThreadLocalRandom.current().nextDouble(origin, bound);
    }

    public double nextSecureInt(int origin, int bound) {
        if (origin == bound) {
            return origin;
        }
        SecureRandom secureRandom = new SecureRandom();
        int difference = bound - origin;
        return origin + secureRandom.nextInt(difference);
    }

    public double nextSecureInt(double origin, double bound) {
        if ((int)origin == (int)bound) {
            return origin;
        }
        SecureRandom secureRandom = new SecureRandom();
        int difference = (int)bound - (int)origin;
        return origin + (double)secureRandom.nextInt(difference);
    }

    public double nextSecureDouble(double origin, double bound) {
        if (origin == bound) {
            return origin;
        }
        SecureRandom secureRandom = new SecureRandom();
        double difference = bound - origin;
        return origin + secureRandom.nextDouble() * difference;
    }

    public float nextSecureFloat(double origin, double bound) {
        if (origin == bound) {
            return (float)origin;
        }
        SecureRandom secureRandom = new SecureRandom();
        float difference = (float)(bound - origin);
        return (float)(origin + (double)(secureRandom.nextFloat() * difference));
    }

    public double randomSin() {
        return Math.sin((double)instance.nextDouble(0.0, Math.PI * 2));
    }
}
