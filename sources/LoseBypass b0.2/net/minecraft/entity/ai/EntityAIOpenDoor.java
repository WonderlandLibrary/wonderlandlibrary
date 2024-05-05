/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIDoorInteract;

public class EntityAIOpenDoor
extends EntityAIDoorInteract {
    boolean closeDoor;
    int closeDoorTemporisation;

    public EntityAIOpenDoor(EntityLiving entitylivingIn, boolean shouldClose) {
        super(entitylivingIn);
        this.theEntity = entitylivingIn;
        this.closeDoor = shouldClose;
    }

    @Override
    public boolean continueExecuting() {
        if (!this.closeDoor) return false;
        if (this.closeDoorTemporisation <= 0) return false;
        if (!super.continueExecuting()) return false;
        return true;
    }

    @Override
    public void startExecuting() {
        this.closeDoorTemporisation = 20;
        this.doorBlock.toggleDoor(this.theEntity.worldObj, this.doorPosition, true);
    }

    @Override
    public void resetTask() {
        if (!this.closeDoor) return;
        this.doorBlock.toggleDoor(this.theEntity.worldObj, this.doorPosition, false);
    }

    @Override
    public void updateTask() {
        --this.closeDoorTemporisation;
        super.updateTask();
    }
}

