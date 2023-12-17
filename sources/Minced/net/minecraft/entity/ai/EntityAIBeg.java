// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.passive.EntityWolf;

public class EntityAIBeg extends EntityAIBase
{
    private final EntityWolf wolf;
    private EntityPlayer player;
    private final World world;
    private final float minPlayerDistance;
    private int timeoutCounter;
    
    public EntityAIBeg(final EntityWolf wolf, final float minDistance) {
        this.wolf = wolf;
        this.world = wolf.world;
        this.minPlayerDistance = minDistance;
        this.setMutexBits(2);
    }
    
    @Override
    public boolean shouldExecute() {
        this.player = this.world.getClosestPlayerToEntity(this.wolf, this.minPlayerDistance);
        return this.player != null && this.hasTemptationItemInHand(this.player);
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return this.player.isEntityAlive() && this.wolf.getDistanceSq(this.player) <= this.minPlayerDistance * this.minPlayerDistance && this.timeoutCounter > 0 && this.hasTemptationItemInHand(this.player);
    }
    
    @Override
    public void startExecuting() {
        this.wolf.setBegging(true);
        this.timeoutCounter = 40 + this.wolf.getRNG().nextInt(40);
    }
    
    @Override
    public void resetTask() {
        this.wolf.setBegging(false);
        this.player = null;
    }
    
    @Override
    public void updateTask() {
        this.wolf.getLookHelper().setLookPosition(this.player.posX, this.player.posY + this.player.getEyeHeight(), this.player.posZ, 10.0f, (float)this.wolf.getVerticalFaceSpeed());
        --this.timeoutCounter;
    }
    
    private boolean hasTemptationItemInHand(final EntityPlayer player) {
        for (final EnumHand enumhand : EnumHand.values()) {
            final ItemStack itemstack = player.getHeldItem(enumhand);
            if (this.wolf.isTamed() && itemstack.getItem() == Items.BONE) {
                return true;
            }
            if (this.wolf.isBreedingItem(itemstack)) {
                return true;
            }
        }
        return false;
    }
}
