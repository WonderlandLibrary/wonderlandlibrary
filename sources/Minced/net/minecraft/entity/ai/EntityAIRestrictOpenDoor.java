// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.village.Village;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.entity.EntityCreature;

public class EntityAIRestrictOpenDoor extends EntityAIBase
{
    private final EntityCreature entity;
    private VillageDoorInfo frontDoor;
    
    public EntityAIRestrictOpenDoor(final EntityCreature creatureIn) {
        this.entity = creatureIn;
        if (!(creatureIn.getNavigator() instanceof PathNavigateGround)) {
            throw new IllegalArgumentException("Unsupported mob type for RestrictOpenDoorGoal");
        }
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.entity.world.isDaytime()) {
            return false;
        }
        final BlockPos blockpos = new BlockPos(this.entity);
        final Village village = this.entity.world.getVillageCollection().getNearestVillage(blockpos, 16);
        if (village == null) {
            return false;
        }
        this.frontDoor = village.getNearestDoor(blockpos);
        return this.frontDoor != null && this.frontDoor.getDistanceToInsideBlockSq(blockpos) < 2.25;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return !this.entity.world.isDaytime() && !this.frontDoor.getIsDetachedFromVillageFlag() && this.frontDoor.isInsideSide(new BlockPos(this.entity));
    }
    
    @Override
    public void startExecuting() {
        ((PathNavigateGround)this.entity.getNavigator()).setBreakDoors(false);
        ((PathNavigateGround)this.entity.getNavigator()).setEnterDoors(false);
    }
    
    @Override
    public void resetTask() {
        ((PathNavigateGround)this.entity.getNavigator()).setBreakDoors(true);
        ((PathNavigateGround)this.entity.getNavigator()).setEnterDoors(true);
        this.frontDoor = null;
    }
    
    @Override
    public void updateTask() {
        this.frontDoor.incrementDoorOpeningRestrictionCounter();
    }
}
