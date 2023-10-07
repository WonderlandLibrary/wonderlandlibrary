// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;

public class EntityJumpHelper
{
    private EntityLiving entity;
    protected boolean isJumping;
    private static final String __OBFID = "CL_00001571";
    
    public EntityJumpHelper(final EntityLiving p_i1612_1_) {
        this.entity = p_i1612_1_;
    }
    
    public void setJumping() {
        this.isJumping = true;
    }
    
    public void doJump() {
        this.entity.setJumping(this.isJumping);
        this.isJumping = false;
    }
}
