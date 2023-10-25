package wtf.evolution.helpers.render;

import net.minecraft.entity.player.*;
import net.minecraft.util.math.*;

public interface CapeHolder
{
    Simulation getSimulation();

    default void updateSimulation(final EntityPlayer abstractClientPlayer, final int partCount) {
        final Simulation simulation = this.getSimulation();
        boolean dirty = false;
        if (simulation.points.size() != partCount) {
            simulation.points.clear();
            simulation.sticks.clear();
            for (int i = 0; i < partCount; ++i) {
                final Simulation.Point point = new Simulation.Point();
                point.position.y = (float)(-i);
                point.locked = (i == 0);
                simulation.points.add(point);
                if (i > 0) {
                    simulation.sticks.add(new Simulation.Stick(simulation.points.get(i - 1), point, 1.0f));
                }
            }
            dirty = true;
        }
        if (dirty) {
            for (int i = 0; i < 10; ++i) {
                this.simulate(abstractClientPlayer);
            }
        }
    }

    default void simulate(final EntityPlayer abstractClientPlayer) {
        final Simulation simulation = this.getSimulation();
        if (simulation.points.isEmpty()) {
            return;
        }
        simulation.points.get(0).prevPosition.copy(simulation.points.get(0).position);
        final double d = abstractClientPlayer.chasingPosX - abstractClientPlayer.posX;
        final double m = abstractClientPlayer.chasingPosZ - abstractClientPlayer.posZ;
        final float n = abstractClientPlayer.prevRenderYawOffset + abstractClientPlayer.renderYawOffset - abstractClientPlayer.prevRenderYawOffset;
        final double o = Math.sin(n * 0.017453292f);
        final double p = -Math.cos(n * 0.017453292f);
        final float heightMul = 10;
        final double fallHack = MathHelper.clamp(simulation.points.get(0).position.y - abstractClientPlayer.posY * heightMul, 0.0, 1.0);
        final Simulation.Vector2 position = simulation.points.get(0).position;
        position.x += (float)(d * o + m * p + fallHack);
        simulation.points.get(0).position.y = (float)(abstractClientPlayer.posY * heightMul + (abstractClientPlayer.isSneaking() ? -4 : 0));
        simulation.simulate();
    }
}