// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.passive.EntityWolf;

public class EntityAIBeg extends EntityAIBase
{
    private EntityWolf theWolf;
    private EntityPlayer thePlayer;
    private World worldObject;
    private float minPlayerDistance;
    private int field_75384_e;
    private static final String __OBFID = "CL_00001576";
    
    public EntityAIBeg(final EntityWolf p_i1617_1_, final float p_i1617_2_) {
        this.theWolf = p_i1617_1_;
        this.worldObject = p_i1617_1_.worldObj;
        this.minPlayerDistance = p_i1617_2_;
        this.setMutexBits(2);
    }
    
    @Override
    public boolean shouldExecute() {
        this.thePlayer = this.worldObject.getClosestPlayerToEntity(this.theWolf, this.minPlayerDistance);
        return this.thePlayer != null && this.hasPlayerGotBoneInHand(this.thePlayer);
    }
    
    @Override
    public boolean continueExecuting() {
        return this.thePlayer.isEntityAlive() && this.theWolf.getDistanceSqToEntity(this.thePlayer) <= this.minPlayerDistance * this.minPlayerDistance && (this.field_75384_e > 0 && this.hasPlayerGotBoneInHand(this.thePlayer));
    }
    
    @Override
    public void startExecuting() {
        this.theWolf.func_70918_i(true);
        this.field_75384_e = 40 + this.theWolf.getRNG().nextInt(40);
    }
    
    @Override
    public void resetTask() {
        this.theWolf.func_70918_i(false);
        this.thePlayer = null;
    }
    
    @Override
    public void updateTask() {
        this.theWolf.getLookHelper().setLookPosition(this.thePlayer.posX, this.thePlayer.posY + this.thePlayer.getEyeHeight(), this.thePlayer.posZ, 10.0f, this.theWolf.getVerticalFaceSpeed());
        --this.field_75384_e;
    }
    
    private boolean hasPlayerGotBoneInHand(final EntityPlayer p_75382_1_) {
        final ItemStack var2 = p_75382_1_.inventory.getCurrentItem();
        return var2 != null && ((!this.theWolf.isTamed() && var2.getItem() == Items.bone) || this.theWolf.isBreedingItem(var2));
    }
}
