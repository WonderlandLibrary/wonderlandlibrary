// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import javax.annotation.Nullable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.EntityCreature;

public class EntityAIWander extends EntityAIBase
{
    protected final EntityCreature entity;
    protected double x;
    protected double y;
    protected double z;
    protected final double speed;
    protected int executionChance;
    protected boolean mustUpdate;
    
    public EntityAIWander(final EntityCreature creatureIn, final double speedIn) {
        this(creatureIn, speedIn, 120);
    }
    
    public EntityAIWander(final EntityCreature creatureIn, final double speedIn, final int chance) {
        this.entity = creatureIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        if (!this.mustUpdate) {
            if (this.entity.getIdleTime() >= 100) {
                return false;
            }
            if (this.entity.getRNG().nextInt(this.executionChance) != 0) {
                return false;
            }
        }
        final Vec3d vec3d = this.getPosition();
        if (vec3d == null) {
            return false;
        }
        this.x = vec3d.x;
        this.y = vec3d.y;
        this.z = vec3d.z;
        this.mustUpdate = false;
        return true;
    }
    
    @Nullable
    protected Vec3d getPosition() {
        return RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return !this.entity.getNavigator().noPath();
    }
    
    @Override
    public void startExecuting() {
        this.entity.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, this.speed);
    }
    
    public void makeUpdate() {
        this.mustUpdate = true;
    }
    
    public void setExecutionChance(final int newchance) {
        this.executionChance = newchance;
    }
}
