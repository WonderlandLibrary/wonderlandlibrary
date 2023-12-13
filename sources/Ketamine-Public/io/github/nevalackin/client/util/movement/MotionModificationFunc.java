package io.github.nevalackin.client.util.movement;

public interface MotionModificationFunc {
    void runSimulation(final double[] motion,
                       final double baseMoveSpeed,
                       final double lastDist,
                       final double yDistFromGround,
                       final int nthTick);
}
