/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIDoorInteract;

public class EntityAIOpenDoor
extends EntityAIDoorInteract {
    boolean closeDoor;
    int closeDoorTemporisation;
    private static final String __OBFID = "CL_00001603";

    public EntityAIOpenDoor(EntityLiving p_i1644_1_, boolean p_i1644_2_) {
        super(p_i1644_1_);
        this.theEntity = p_i1644_1_;
        this.closeDoor = p_i1644_2_;
    }

    @Override
    public boolean continueExecuting() {
        return this.closeDoor && this.closeDoorTemporisation > 0 && super.continueExecuting();
    }

    @Override
    public void startExecuting() {
        this.closeDoorTemporisation = 20;
        this.doorBlock.func_176512_a(this.theEntity.worldObj, this.field_179507_b, true);
    }

    @Override
    public void resetTask() {
        if (this.closeDoor) {
            this.doorBlock.func_176512_a(this.theEntity.worldObj, this.field_179507_b, false);
        }
    }

    @Override
    public void updateTask() {
        --this.closeDoorTemporisation;
        super.updateTask();
    }
}

