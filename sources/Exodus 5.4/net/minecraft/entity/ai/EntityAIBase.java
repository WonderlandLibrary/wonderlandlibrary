/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

public abstract class EntityAIBase {
    private int mutexBits;

    public void setMutexBits(int n) {
        this.mutexBits = n;
    }

    public void resetTask() {
    }

    public abstract boolean shouldExecute();

    public boolean isInterruptible() {
        return true;
    }

    public void startExecuting() {
    }

    public void updateTask() {
    }

    public boolean continueExecuting() {
        return this.shouldExecute();
    }

    public int getMutexBits() {
        return this.mutexBits;
    }
}

