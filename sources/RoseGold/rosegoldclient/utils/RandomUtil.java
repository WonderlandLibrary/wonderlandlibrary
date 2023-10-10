package rosegoldclient.utils;

import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class RandomUtil {
    private static Random rand;

    public static Vec3d randomVec() {
        return new Vec3d(RandomUtil.rand.nextDouble(), RandomUtil.rand.nextDouble(), RandomUtil.rand.nextDouble());
    }

    public static int randBetween(final int a, final int b) {
        return RandomUtil.rand.nextInt(b - a + 1) + a;
    }

    public static double randBetween(final double a, final double b) {
        return RandomUtil.rand.nextDouble() * (b - a) + a;
    }

    public static float randBetween(final float a, final float b) {
        return RandomUtil.rand.nextFloat() * (b - a) + a;
    }

    public static int nextInt(final int yep) {
        return RandomUtil.rand.nextInt(yep);
    }

    static {
        RandomUtil.rand = new Random();
    }
}
