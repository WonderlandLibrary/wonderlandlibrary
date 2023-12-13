package io.github.nevalackin.client.util.movement;

import net.minecraft.client.entity.EntityPlayerSP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class FrictionUtil {

    private static final double AIR_FRICTION = 0.98F;
    private static final double WATER_FRICTION = 0.89F;
    private static final double LAVA_FRICTION = 0.535F;
    public static final double BUNNY_DIV_FRICTION = 160.0 - MovementUtil.MIN_DIST;

    private static final double[] SPEEDS = new double[3];

    private FrictionUtil() {
    }

    public static double applyNCPFriction(final EntityPlayerSP player,
                                          final double moveSpeed,
                                          final double lastDist,
                                          final double baseMoveSpeedRef) {
        SPEEDS[0] = lastDist - (lastDist / BUNNY_DIV_FRICTION);
        SPEEDS[1] = lastDist - ((moveSpeed - lastDist) / 33.3D);
        double materialFriction = player.isInWater() ? WATER_FRICTION :
            player.isInLava() ? LAVA_FRICTION :
                AIR_FRICTION;
        SPEEDS[2] = lastDist - (baseMoveSpeedRef * (1.0D - materialFriction));

        Arrays.sort(SPEEDS);

        return SPEEDS[0];
    }

    public static double applyVanillaFriction(final EntityPlayerSP player,
                                              final double moveSpeed,
                                              final double lastDist,
                                              final double baseMoveSpeedRef) {
        return moveSpeed * 0.91F;
    }

    @FunctionalInterface
    public interface Friction {
        double applyFriction(final EntityPlayerSP player,
                             final double moveSpeed,
                             final double lastDist,
                             final double baseMoveSpeed);
    }

}
