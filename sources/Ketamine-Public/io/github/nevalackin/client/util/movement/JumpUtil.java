package io.github.nevalackin.client.util.movement;

import io.github.nevalackin.client.util.math.MathUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public final class JumpUtil {

    private JumpUtil() {
    }

    public static double getJumpHeight(final EntityPlayerSP player) {
        final double base = 0.42F;
        final PotionEffect effect = player.getActivePotionEffect(Potion.jump);
        return effect == null ? base : base + ((effect.getAmplifier() + 1) * 0.1F);
    }

    public static float getMinFallDist(final EntityPlayerSP player) {
        final float baseFallDist = 3.0F;
        final PotionEffect effect = player.getActivePotionEffect(Potion.jump);
        final int amp = effect != null ? effect.getAmplifier() + 1 : 0;
        return baseFallDist + amp;
    }

    public static double calculateJumpDistance(final double baseMoveSpeedRef,
                                               double[] velocity, // Re-use velocity to store velocity
                                               double lastDist, // Re-use lastDist to store the lastDist
                                               final MotionModificationFunc motionModificationFunc) {
        double posY = 0.0;

        double totalDistance = 0.0;

        int tick = 0;

        do {
            // Part of vanilla logic
            if (Math.abs(velocity[0]) < 0.005) velocity[0] = 0.0;
            if (Math.abs(velocity[1]) < 0.005) velocity[1] = 0.0;
            if (Math.abs(velocity[2]) < 0.005) velocity[2] = 0.0;
            // Run the motion (x/y/z) modification
            motionModificationFunc.runSimulation(velocity, baseMoveSpeedRef, lastDist,
                                                 MathUtil.round(posY - (int) posY, 0.001), tick);
            // Accumulate position
            posY += velocity[1];
            // Calculate dist travelled this tick
            final double dist = Math.sqrt(velocity[0] * velocity[0] + velocity[2] * velocity[2]);
            // Store last dist
            lastDist = dist;
            // Accumulate total distance
            totalDistance += dist;
            // Vanilla gravity
            velocity[1] -= 0.08;
            velocity[1] *= 0.98F;
            // Each run is equivalent to a tick passing
            tick++;
        } while (posY > 0.0); // When posY is <= 0.0 that represents that you have reached the ground

        return totalDistance;
    }
}
