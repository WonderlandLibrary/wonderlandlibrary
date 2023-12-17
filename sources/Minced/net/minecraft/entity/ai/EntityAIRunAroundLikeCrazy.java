// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.passive.AbstractHorse;

public class EntityAIRunAroundLikeCrazy extends EntityAIBase
{
    private final AbstractHorse horseHost;
    private final double speed;
    private double targetX;
    private double targetY;
    private double targetZ;
    
    public EntityAIRunAroundLikeCrazy(final AbstractHorse horse, final double speedIn) {
        this.horseHost = horse;
        this.speed = speedIn;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.horseHost.isTame() || !this.horseHost.isBeingRidden()) {
            return false;
        }
        final Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.horseHost, 5, 4);
        if (vec3d == null) {
            return false;
        }
        this.targetX = vec3d.x;
        this.targetY = vec3d.y;
        this.targetZ = vec3d.z;
        return true;
    }
    
    @Override
    public void startExecuting() {
        this.horseHost.getNavigator().tryMoveToXYZ(this.targetX, this.targetY, this.targetZ, this.speed);
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return !this.horseHost.isTame() && !this.horseHost.getNavigator().noPath() && this.horseHost.isBeingRidden();
    }
    
    @Override
    public void updateTask() {
        if (!this.horseHost.isTame() && this.horseHost.getRNG().nextInt(50) == 0) {
            final Entity entity = this.horseHost.getPassengers().get(0);
            if (entity == null) {
                return;
            }
            if (entity instanceof EntityPlayer) {
                final int i = this.horseHost.getTemper();
                final int j = this.horseHost.getMaxTemper();
                if (j > 0 && this.horseHost.getRNG().nextInt(j) < i) {
                    this.horseHost.setTamedBy((EntityPlayer)entity);
                    return;
                }
                this.horseHost.increaseTemper(5);
            }
            this.horseHost.removePassengers();
            this.horseHost.makeMad();
            this.horseHost.world.setEntityState(this.horseHost, (byte)6);
        }
    }
}
