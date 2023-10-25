package net.minecraft.world;

import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.border.WorldBorder;

class WorldServerMulti.1
implements IBorderListener {
    WorldServerMulti.1() {
    }

    public void onSizeChanged(WorldBorder border, double newSize) {
        WorldServerMulti.this.getWorldBorder().setTransition(newSize);
    }

    public void onTransitionStarted(WorldBorder border, double oldSize, double newSize, long time) {
        WorldServerMulti.this.getWorldBorder().setTransition(oldSize, newSize, time);
    }

    public void onCenterChanged(WorldBorder border, double x, double z) {
        WorldServerMulti.this.getWorldBorder().setCenter(x, z);
    }

    public void onWarningTimeChanged(WorldBorder border, int newTime) {
        WorldServerMulti.this.getWorldBorder().setWarningTime(newTime);
    }

    public void onWarningDistanceChanged(WorldBorder border, int newDistance) {
        WorldServerMulti.this.getWorldBorder().setWarningDistance(newDistance);
    }

    public void onDamageAmountChanged(WorldBorder border, double newAmount) {
        WorldServerMulti.this.getWorldBorder().setDamageAmount(newAmount);
    }

    public void onDamageBufferChanged(WorldBorder border, double newSize) {
        WorldServerMulti.this.getWorldBorder().setDamageBuffer(newSize);
    }
}
