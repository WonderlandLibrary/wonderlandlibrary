// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import java.util.Iterator;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.pathfinding.PathNavigateGround;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.pathfinding.Path;
import net.minecraft.entity.EntityCreature;

public class EntityAIMoveThroughVillage extends EntityAIBase
{
    private final EntityCreature entity;
    private final double movementSpeed;
    private Path path;
    private VillageDoorInfo doorInfo;
    private final boolean isNocturnal;
    private final List<VillageDoorInfo> doorList;
    
    public EntityAIMoveThroughVillage(final EntityCreature entityIn, final double movementSpeedIn, final boolean isNocturnalIn) {
        this.doorList = (List<VillageDoorInfo>)Lists.newArrayList();
        this.entity = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.isNocturnal = isNocturnalIn;
        this.setMutexBits(1);
        if (!(entityIn.getNavigator() instanceof PathNavigateGround)) {
            throw new IllegalArgumentException("Unsupported mob for MoveThroughVillageGoal");
        }
    }
    
    @Override
    public boolean shouldExecute() {
        this.resizeDoorList();
        if (this.isNocturnal && this.entity.world.isDaytime()) {
            return false;
        }
        final Village village = this.entity.world.getVillageCollection().getNearestVillage(new BlockPos(this.entity), 0);
        if (village == null) {
            return false;
        }
        this.doorInfo = this.findNearestDoor(village);
        if (this.doorInfo == null) {
            return false;
        }
        final PathNavigateGround pathnavigateground = (PathNavigateGround)this.entity.getNavigator();
        final boolean flag = pathnavigateground.getEnterDoors();
        pathnavigateground.setBreakDoors(false);
        this.path = pathnavigateground.getPathToPos(this.doorInfo.getDoorBlockPos());
        pathnavigateground.setBreakDoors(flag);
        if (this.path != null) {
            return true;
        }
        final Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.entity, 10, 7, new Vec3d(this.doorInfo.getDoorBlockPos().getX(), this.doorInfo.getDoorBlockPos().getY(), this.doorInfo.getDoorBlockPos().getZ()));
        if (vec3d == null) {
            return false;
        }
        pathnavigateground.setBreakDoors(false);
        this.path = this.entity.getNavigator().getPathToXYZ(vec3d.x, vec3d.y, vec3d.z);
        pathnavigateground.setBreakDoors(flag);
        return this.path != null;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        if (this.entity.getNavigator().noPath()) {
            return false;
        }
        final float f = this.entity.width + 4.0f;
        return this.entity.getDistanceSq(this.doorInfo.getDoorBlockPos()) > f * f;
    }
    
    @Override
    public void startExecuting() {
        this.entity.getNavigator().setPath(this.path, this.movementSpeed);
    }
    
    @Override
    public void resetTask() {
        if (this.entity.getNavigator().noPath() || this.entity.getDistanceSq(this.doorInfo.getDoorBlockPos()) < 16.0) {
            this.doorList.add(this.doorInfo);
        }
    }
    
    private VillageDoorInfo findNearestDoor(final Village villageIn) {
        VillageDoorInfo villagedoorinfo = null;
        int i = Integer.MAX_VALUE;
        for (final VillageDoorInfo villagedoorinfo2 : villageIn.getVillageDoorInfoList()) {
            final int j = villagedoorinfo2.getDistanceSquared(MathHelper.floor(this.entity.posX), MathHelper.floor(this.entity.posY), MathHelper.floor(this.entity.posZ));
            if (j < i && !this.doesDoorListContain(villagedoorinfo2)) {
                villagedoorinfo = villagedoorinfo2;
                i = j;
            }
        }
        return villagedoorinfo;
    }
    
    private boolean doesDoorListContain(final VillageDoorInfo doorInfoIn) {
        for (final VillageDoorInfo villagedoorinfo : this.doorList) {
            if (doorInfoIn.getDoorBlockPos().equals(villagedoorinfo.getDoorBlockPos())) {
                return true;
            }
        }
        return false;
    }
    
    private void resizeDoorList() {
        if (this.doorList.size() > 15) {
            this.doorList.remove(0);
        }
    }
}
