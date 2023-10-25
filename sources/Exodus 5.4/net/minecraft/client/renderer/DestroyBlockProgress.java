/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer;

import net.minecraft.util.BlockPos;

public class DestroyBlockProgress {
    private int partialBlockProgress;
    private int createdAtCloudUpdateTick;
    private final BlockPos position;
    private final int miningPlayerEntId;

    public BlockPos getPosition() {
        return this.position;
    }

    public int getPartialBlockDamage() {
        return this.partialBlockProgress;
    }

    public void setPartialBlockDamage(int n) {
        if (n > 10) {
            n = 10;
        }
        this.partialBlockProgress = n;
    }

    public DestroyBlockProgress(int n, BlockPos blockPos) {
        this.miningPlayerEntId = n;
        this.position = blockPos;
    }

    public void setCloudUpdateTick(int n) {
        this.createdAtCloudUpdateTick = n;
    }

    public int getCreationCloudUpdateTick() {
        return this.createdAtCloudUpdateTick;
    }
}

