/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIFollowOwner
extends EntityAIBase {
    World theWorld;
    private boolean field_75344_i;
    private EntityLivingBase theOwner;
    float minDist;
    private int field_75343_h;
    float maxDist;
    private PathNavigate petPathfinder;
    private EntityTameable thePet;
    private double followSpeed;

    @Override
    public boolean continueExecuting() {
        return !this.petPathfinder.noPath() && this.thePet.getDistanceSqToEntity(this.theOwner) > (double)(this.maxDist * this.maxDist) && !this.thePet.isSitting();
    }

    @Override
    public void updateTask() {
        this.thePet.getLookHelper().setLookPositionWithEntity(this.theOwner, 10.0f, this.thePet.getVerticalFaceSpeed());
        if (!this.thePet.isSitting() && --this.field_75343_h <= 0) {
            this.field_75343_h = 10;
            if (!this.petPathfinder.tryMoveToEntityLiving(this.theOwner, this.followSpeed) && !this.thePet.getLeashed() && this.thePet.getDistanceSqToEntity(this.theOwner) >= 144.0) {
                int n = MathHelper.floor_double(this.theOwner.posX) - 2;
                int n2 = MathHelper.floor_double(this.theOwner.posZ) - 2;
                int n3 = MathHelper.floor_double(this.theOwner.getEntityBoundingBox().minY);
                int n4 = 0;
                while (n4 <= 4) {
                    int n5 = 0;
                    while (n5 <= 4) {
                        if ((n4 < 1 || n5 < 1 || n4 > 3 || n5 > 3) && World.doesBlockHaveSolidTopSurface(this.theWorld, new BlockPos(n + n4, n3 - 1, n2 + n5)) && this.func_181065_a(new BlockPos(n + n4, n3, n2 + n5)) && this.func_181065_a(new BlockPos(n + n4, n3 + 1, n2 + n5))) {
                            this.thePet.setLocationAndAngles((float)(n + n4) + 0.5f, n3, (float)(n2 + n5) + 0.5f, this.thePet.rotationYaw, this.thePet.rotationPitch);
                            this.petPathfinder.clearPathEntity();
                            return;
                        }
                        ++n5;
                    }
                    ++n4;
                }
            }
        }
    }

    @Override
    public void startExecuting() {
        this.field_75343_h = 0;
        this.field_75344_i = ((PathNavigateGround)this.thePet.getNavigator()).getAvoidsWater();
        ((PathNavigateGround)this.thePet.getNavigator()).setAvoidsWater(false);
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase entityLivingBase = this.thePet.getOwner();
        if (entityLivingBase == null) {
            return false;
        }
        if (entityLivingBase instanceof EntityPlayer && ((EntityPlayer)entityLivingBase).isSpectator()) {
            return false;
        }
        if (this.thePet.isSitting()) {
            return false;
        }
        if (this.thePet.getDistanceSqToEntity(entityLivingBase) < (double)(this.minDist * this.minDist)) {
            return false;
        }
        this.theOwner = entityLivingBase;
        return true;
    }

    private boolean func_181065_a(BlockPos blockPos) {
        IBlockState iBlockState = this.theWorld.getBlockState(blockPos);
        Block block = iBlockState.getBlock();
        return block == Blocks.air ? true : !block.isFullCube();
    }

    public EntityAIFollowOwner(EntityTameable entityTameable, double d, float f, float f2) {
        this.thePet = entityTameable;
        this.theWorld = entityTameable.worldObj;
        this.followSpeed = d;
        this.petPathfinder = entityTameable.getNavigator();
        this.minDist = f;
        this.maxDist = f2;
        this.setMutexBits(3);
        if (!(entityTameable.getNavigator() instanceof PathNavigateGround)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    @Override
    public void resetTask() {
        this.theOwner = null;
        this.petPathfinder.clearPathEntity();
        ((PathNavigateGround)this.thePet.getNavigator()).setAvoidsWater(true);
    }
}

