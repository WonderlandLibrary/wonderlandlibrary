// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathNavigateGround;
import com.google.common.collect.Sets;
import net.minecraft.item.Item;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityCreature;

public class EntityAITempt extends EntityAIBase
{
    private final EntityCreature temptedEntity;
    private final double speed;
    private double targetX;
    private double targetY;
    private double targetZ;
    private double pitch;
    private double yaw;
    private EntityPlayer temptingPlayer;
    private int delayTemptCounter;
    private boolean isRunning;
    private final Set<Item> temptItem;
    private final boolean scaredByPlayerMovement;
    
    public EntityAITempt(final EntityCreature temptedEntityIn, final double speedIn, final Item temptItemIn, final boolean scaredByPlayerMovementIn) {
        this(temptedEntityIn, speedIn, scaredByPlayerMovementIn, Sets.newHashSet((Object[])new Item[] { temptItemIn }));
    }
    
    public EntityAITempt(final EntityCreature temptedEntityIn, final double speedIn, final boolean scaredByPlayerMovementIn, final Set<Item> temptItemIn) {
        this.temptedEntity = temptedEntityIn;
        this.speed = speedIn;
        this.temptItem = temptItemIn;
        this.scaredByPlayerMovement = scaredByPlayerMovementIn;
        this.setMutexBits(3);
        if (!(temptedEntityIn.getNavigator() instanceof PathNavigateGround)) {
            throw new IllegalArgumentException("Unsupported mob type for TemptGoal");
        }
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.delayTemptCounter > 0) {
            --this.delayTemptCounter;
            return false;
        }
        this.temptingPlayer = this.temptedEntity.world.getClosestPlayerToEntity(this.temptedEntity, 10.0);
        return this.temptingPlayer != null && (this.isTempting(this.temptingPlayer.getHeldItemMainhand()) || this.isTempting(this.temptingPlayer.getHeldItemOffhand()));
    }
    
    protected boolean isTempting(final ItemStack stack) {
        return this.temptItem.contains(stack.getItem());
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        if (this.scaredByPlayerMovement) {
            if (this.temptedEntity.getDistanceSq(this.temptingPlayer) < 36.0) {
                if (this.temptingPlayer.getDistanceSq(this.targetX, this.targetY, this.targetZ) > 0.010000000000000002) {
                    return false;
                }
                if (Math.abs(this.temptingPlayer.rotationPitch - this.pitch) > 5.0 || Math.abs(this.temptingPlayer.rotationYaw - this.yaw) > 5.0) {
                    return false;
                }
            }
            else {
                this.targetX = this.temptingPlayer.posX;
                this.targetY = this.temptingPlayer.posY;
                this.targetZ = this.temptingPlayer.posZ;
            }
            this.pitch = this.temptingPlayer.rotationPitch;
            this.yaw = this.temptingPlayer.rotationYaw;
        }
        return this.shouldExecute();
    }
    
    @Override
    public void startExecuting() {
        this.targetX = this.temptingPlayer.posX;
        this.targetY = this.temptingPlayer.posY;
        this.targetZ = this.temptingPlayer.posZ;
        this.isRunning = true;
    }
    
    @Override
    public void resetTask() {
        this.temptingPlayer = null;
        this.temptedEntity.getNavigator().clearPath();
        this.delayTemptCounter = 100;
        this.isRunning = false;
    }
    
    @Override
    public void updateTask() {
        this.temptedEntity.getLookHelper().setLookPositionWithEntity(this.temptingPlayer, (float)(this.temptedEntity.getHorizontalFaceSpeed() + 20), (float)this.temptedEntity.getVerticalFaceSpeed());
        if (this.temptedEntity.getDistanceSq(this.temptingPlayer) < 6.25) {
            this.temptedEntity.getNavigator().clearPath();
        }
        else {
            this.temptedEntity.getNavigator().tryMoveToEntityLiving(this.temptingPlayer, this.speed);
        }
    }
    
    public boolean isRunning() {
        return this.isRunning;
    }
}
