/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;

public class EntityJumpHelper {
    protected boolean isJumping;
    private EntityLiving entity;

    public void setJumping() {
        this.isJumping = true;
    }

    public EntityJumpHelper(EntityLiving entityLiving) {
        this.entity = entityLiving;
    }

    public void doJump() {
        this.entity.setJumping(this.isJumping);
        this.isJumping = false;
    }
}

