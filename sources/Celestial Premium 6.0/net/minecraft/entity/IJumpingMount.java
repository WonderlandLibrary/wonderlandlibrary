/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity;

public interface IJumpingMount {
    public void setJumpPower(int var1);

    public boolean canJump();

    public void handleStartJump(int var1);

    public void handleStopJump();
}

